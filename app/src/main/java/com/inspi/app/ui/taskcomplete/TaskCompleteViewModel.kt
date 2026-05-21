package com.inspi.app.ui.taskcomplete

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
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

    fun resetError() {
        _state.value = TaskCompleteState.Idle
    }

    // ─── Image saving with EXIF rotation fix ─────────────────────────────────

    private fun saveImageFromUri(uri: Uri, context: Context): Pair<String, String> {
        val submissionsDir = File(context.filesDir, "submissions").also { it.mkdirs() }
        val ts = System.currentTimeMillis()

        // Read bytes once — used for both EXIF reading and decoding
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: throw Exception("Cannot open image")
        if (bytes.size > 10 * 1024 * 1024) throw Exception("Image too large (max 10 MB)")

        // ── Step 1: Decode efficiently with inSampleSize ─────────────────────
        // First pass: read only dimensions
        val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opts)

        // We want to decode at full quality for the saved file, so sample = 1.
        // For very large images (>4000px), use inSampleSize = 2 to save memory
        // while still producing a crisp result after JPEG encoding at 85%.
        val sampleSize = if (opts.outWidth > 4000 || opts.outHeight > 4000) 2 else 1
        val decodeOpts = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val rawBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, decodeOpts)
            ?: throw Exception("Cannot decode image")

        // ── Step 2: Read EXIF orientation and rotate ─────────────────────────
        // ExifInterface can read from a byte stream (API 24+ / minSdk 26 ✓)
        val exif = bytes.inputStream().use { ExifInterface(it) }
        val rotatedBitmap = rawBitmap.applyExifRotation(exif)

        // ── Step 3: Save full-resolution image (JPEG 85) ─────────────────────
        val imageFile = File(submissionsDir, "img_$ts.jpg")
        FileOutputStream(imageFile).use { out ->
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
        }

        // ── Step 4: Save thumbnail at 800×800 px ─────────────────────────────
        // 800px is sufficient for any grid cell even on 3× density (≈ 266dp),
        // while being 8× smaller in file size than the full image.
        val thumbBitmap = rotatedBitmap.scaledToFit(maxDimension = 800)
        val thumbFile = File(submissionsDir, "thumb_$ts.jpg")
        FileOutputStream(thumbFile).use { out ->
            thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 82, out)
        }

        // Recycle intermediates (rotatedBitmap may be rawBitmap itself if no rotation needed)
        if (rotatedBitmap !== rawBitmap) rawBitmap.recycle()
        thumbBitmap.recycle()

        return imageFile.absolutePath to thumbFile.absolutePath
    }

    // ─── Bitmap helpers ───────────────────────────────────────────────────────

    /**
     * Reads the EXIF_TAG_ORIENTATION from the given ExifInterface and returns a
     * rotated/mirrored copy of the bitmap.  Returns the SAME object (no copy)
     * when no transform is needed so we avoid an unnecessary allocation.
     */
    private fun Bitmap.applyExifRotation(exif: ExifInterface): Bitmap {
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL,
        )
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90  -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL   -> matrix.postScale(1f, -1f)
            ExifInterface.ORIENTATION_TRANSPOSE -> { matrix.postRotate(90f); matrix.postScale(-1f, 1f) }
            ExifInterface.ORIENTATION_TRANSVERSE -> { matrix.postRotate(-90f); matrix.postScale(-1f, 1f) }
            else -> return this // ORIENTATION_NORMAL or UNDEFINED — no transform needed
        }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    /**
     * Scales the bitmap so neither dimension exceeds [maxDimension], preserving
     * aspect ratio.  Returns the same object if already small enough.
     */
    private fun Bitmap.scaledToFit(maxDimension: Int): Bitmap {
        val scale = minOf(maxDimension.toFloat() / width, maxDimension.toFloat() / height)
        return if (scale < 1f) {
            Bitmap.createScaledBitmap(
                this,
                (width * scale).toInt(),
                (height * scale).toInt(),
                true, // bilinear filter — critical for quality
            )
        } else this
    }
}