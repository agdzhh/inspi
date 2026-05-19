package com.inspi.app.ui.taskcomplete

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspi.app.data.repository.SubmissionRepository
import com.inspi.app.data.repository.UserRepository
import com.inspi.app.domain.models.*
import com.inspi.app.utils.TaskSelector
import com.inspi.app.utils.XpCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

sealed class TaskCompleteState {
    object Idle : TaskCompleteState()
    object Saving : TaskCompleteState()
    data class Success(val xpEarned: Int) : TaskCompleteState()
    data class Error(val message: String) : TaskCompleteState()
}

@HiltViewModel
class TaskCompleteViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val submissionRepo: SubmissionRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow<TaskCompleteState>(TaskCompleteState.Idle)
    val state = _state.asStateFlow()

    private val _hobby = MutableStateFlow<HobbyType>(HobbyType.PHOTOGRAPHY)
    val hobby = _hobby.asStateFlow()

    init {
        viewModelScope.launch {
            userRepo.observeProfile().firstOrNull()?.let {
                _hobby.value = it.hobby
            }
        }
    }

    fun submitImage(uri: Uri) {
        viewModelScope.launch {
            _state.value = TaskCompleteState.Saving
            try {
                val profile = userRepo.getProfile() ?: throw Exception("No profile")
                val task = TaskSelector.getTodayTask(profile.hobby)
                val xp = XpCalculator.dailyTaskXp(profile.currentStreak)

                // Save image + thumbnail
                val (imagePath, thumbPath) = withContext(Dispatchers.IO) {
                    saveImageFromUri(uri, context)
                }

                submissionRepo.insert(
                    Submission(
                        id = 0,
                        imagePath = imagePath,
                        thumbnailPath = thumbPath,
                        taskTitle = task.title,
                        createdAt = System.currentTimeMillis(),
                        hobbyType = profile.hobby,
                        xpEarned = xp,
                    )
                )
                userRepo.recordTaskCompletion(xp)
                _state.value = TaskCompleteState.Success(xp)
            } catch (e: Exception) {
                _state.value = TaskCompleteState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetError() { _state.value = TaskCompleteState.Idle }

    private fun saveImageFromUri(uri: Uri, context: Context): Pair<String, String> {
        val submissionsDir = File(context.filesDir, "submissions").also { it.mkdirs() }
        val ts = System.currentTimeMillis()

        val stream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Cannot open image")
        val bitmap = BitmapFactory.decodeStream(stream)
            ?: throw Exception("Cannot decode image")
        stream.close()

        // Check size (10 MB limit)
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()
            ?: throw Exception("Cannot read image bytes")
        if (bytes.size > 10 * 1024 * 1024) throw Exception("Image too large (max 10 MB)")

        // Full image at quality 85
        val imageFile = File(submissionsDir, "img_$ts.jpg")
        FileOutputStream(imageFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
        }

        // Thumbnail at max 300×300
        val ratio = minOf(300f / bitmap.width, 300f / bitmap.height)
        val thumbBitmap = if (ratio < 1f) {
            Bitmap.createScaledBitmap(bitmap, (bitmap.width * ratio).toInt(), (bitmap.height * ratio).toInt(), true)
        } else bitmap

        val thumbFile = File(submissionsDir, "thumb_$ts.jpg")
        FileOutputStream(thumbFile).use { out ->
            thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out)
        }

        return imageFile.absolutePath to thumbFile.absolutePath
    }
}
