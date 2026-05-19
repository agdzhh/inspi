package com.inspi.app.ui.taskcomplete

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
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
    data class Success(val xpEarned: Int, val submissionId: Long) : TaskCompleteState()
    data class Error(val message: String) : TaskCompleteState()
}

@HiltViewModel
class TaskCompleteViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val submissionRepo: SubmissionRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow<TaskCompleteState>(TaskCompleteState.Idle)
    val state = _state.asStateFlow()

    private val _hobby = MutableStateFlow<HobbyType>(HobbyType.PHOTOGRAPHY)
    val hobby = _hobby.asStateFlow()

    private val retakeSubmissionId: Long = savedStateHandle.get<Long>("retakeSubmissionId") ?: -1L

    // Non-null when this is a retake session — used to display a hint in the UI
    private val _retakeTaskTitle = MutableStateFlow<String?>(null)
    val retakeTaskTitle = _retakeTaskTitle.asStateFlow()

    init {
        viewModelScope.launch {
            userRepo.observeProfile().firstOrNull()?.let {
                _hobby.value = it.hobby
            }
        }
        if (retakeSubmissionId > 0L) {
            viewModelScope.launch {
                _retakeTaskTitle.value = submissionRepo.getById(retakeSubmissionId)?.taskTitle
            }
        }
    }

    fun submitImage(uri: Uri) {
        viewModelScope.launch {
            _state.value = TaskCompleteState.Saving
            try {
                val profile = userRepo.getProfile() ?: throw Exception("No profile")
                val task = if (retakeSubmissionId > 0L && _retakeTaskTitle.value != null) {
                    DailyTask(_retakeTaskTitle.value!!, "", profile.hobby)
                } else {
                    TaskSelector.getTodayTask(profile.hobby)
                }
                val xp = XpCalculator.dailyTaskXp(profile.currentStreak)

                // Save image + thumbnail
                val (imagePath, thumbPath) = withContext(Dispatchers.IO) {
                    saveImageFromUri(uri, context)
                }

                val submissionId = submissionRepo.insert(
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
                _state.value = TaskCompleteState.Success(xp, submissionId)
            } catch (e: Exception) {
                _state.value = TaskCompleteState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetError() { _state.value = TaskCompleteState.Idle }

    private fun saveImageFromUri(uri: Uri, context: Context): Pair<String, String> {
        val submissionsDir = File(context.filesDir, "submissions").also { it.mkdirs() }
        val ts = System.currentTimeMillis()

        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: throw Exception("Cannot open image")
        if (bytes.size > 10 * 1024 * 1024) throw Exception("Image too large (max 10 MB)")

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            ?: throw Exception("Cannot decode image")

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
