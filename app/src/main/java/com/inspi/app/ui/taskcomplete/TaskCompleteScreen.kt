package com.inspi.app.ui.taskcomplete

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.inspi.app.domain.models.HobbyType
import com.inspi.app.ui.common.MascotImage
import com.inspi.app.ui.common.MascotMood
import com.inspi.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TaskCompleteScreen(
    onSuccess: (Long) -> Unit,
    onBack: () -> Unit,
    viewModel: TaskCompleteViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val hobby by viewModel.hobby.collectAsStateWithLifecycle()
    val retakeTaskTitle by viewModel.retakeTaskTitle.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHost = remember { SnackbarHostState() }
    var showPhotoSourcePicker by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.submitImage(it) }
    }

    var capturedUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(TakePictureWithGrant()) { success ->
        if (success) capturedUri?.let { viewModel.submitImage(it) }
    }

    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            capturedUri = uri
            cameraLauncher.launch(uri)
        }
    }

    LaunchedEffect(key1 = state is TaskCompleteState.Success) {
        val successState = state as? TaskCompleteState.Success ?: return@LaunchedEffect

        delay(1200)

        onSuccess(successState.submissionId)
    }

    LaunchedEffect(state) {
        if (state is TaskCompleteState.Error) {
            snackbarHost.showSnackbar("Could not save image — please try again.")
            viewModel.resetError()
        }
    }

    // ── Боттом-шит выбора источника фото ─────────────────────────────────────
    if (showPhotoSourcePicker) {
        ModalBottomSheet(
            onDismissRequest = { showPhotoSourcePicker = false },
            containerColor = InspySurface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    "Add photo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = InspyOnBackground,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                // Камера
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(InspyHighlight)
                        .clickable {
                            showPhotoSourcePicker = false
                            if (cameraPermission.status == PermissionStatus.Granted) {
                                val uri = createImageUri(context)
                                capturedUri = uri
                                cameraLauncher.launch(uri)
                            } else {
                                cameraPermission.launchPermissionRequest()
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Icon(Icons.Outlined.CameraAlt, contentDescription = null, tint = InspyPrimary, modifier = Modifier.size(22.dp))
                    Text("Take a photo", fontSize = 15.sp, color = InspyOnBackground)
                }
                Spacer(Modifier.height(4.dp))
                // Галерея
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(InspyHighlight)
                        .clickable {
                            showPhotoSourcePicker = false
                            imagePicker.launch("image/*")
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Icon(Icons.Outlined.PhotoLibrary, contentDescription = null, tint = InspyPrimary, modifier = Modifier.size(22.dp))
                    Text("Choose from gallery", fontSize = 15.sp, color = InspyOnBackground)
                }
            }
        }
    }

    Scaffold(
        containerColor = InspyBackground,
        snackbarHost = { SnackbarHost(snackbarHost) },
        topBar = {
            TopAppBar(
                title = { Text("Complete Task") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = InspyBackground),
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            when (state) {
                is TaskCompleteState.Saving -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = InspyPrimary)
                        Spacer(Modifier.height(12.dp))
                        Text("Saving your work…", color = InspyOnBackground.copy(alpha = 0.6f))
                    }
                }
                is TaskCompleteState.Success -> {
                    SuccessContent(xp = (state as TaskCompleteState.Success).xpEarned)
                }
                else -> {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        MascotImage(
                            mood = if (retakeTaskTitle != null) MascotMood.NEUTRAL else MascotMood.HAPPY,
                            modifier = Modifier.size(120.dp),
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            if (retakeTaskTitle != null) "Retake: $retakeTaskTitle" else "Time to create!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = InspyOnBackground,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            if (retakeTaskTitle != null) "See how much you've improved since last time."
                            else "Take a photo or choose one from your gallery.",
                            fontSize = 16.sp,
                            color = InspyOnBackground.copy(alpha = 0.65f),
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(40.dp))

                        Button(
                            onClick = { showPhotoSourcePicker = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = InspyPrimary),
                        ) {
                            Text("Add Photo", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SuccessContent(xp: Int) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "mascot_scale",
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
        MascotImage(
            mood = MascotMood.HAPPY,
            modifier = Modifier.size(120.dp).scale(scale),
        )
        Spacer(Modifier.height(16.dp))
        Text("Awesome work!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = InspyOnBackground)
        Spacer(Modifier.height(8.dp))
        Text("+$xp XP earned!", fontSize = 20.sp, color = InspyPrimary, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Text("Streak updated ✓", fontSize = 14.sp, color = InspyAccent)
    }
}

// Adds explicit URI read/write grants — required from Android 18 onwards.
private class TakePictureWithGrant : ActivityResultContract<Uri, Boolean>() {
    override fun createIntent(context: Context, input: Uri): Intent =
        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, input)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            .also { it.clipData = android.content.ClipData.newRawUri(null, input) }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
        resultCode == Activity.RESULT_OK
}

private fun createImageUri(context: android.content.Context): Uri {
    val file = java.io.File(context.cacheDir, "cam_${System.currentTimeMillis()}.jpg")
    return androidx.core.content.FileProvider.getUriForFile(
        context, "${context.packageName}.fileprovider", file
    )
}