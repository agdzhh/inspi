package com.inspi.app.ui.taskcomplete

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.inspi.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TaskCompleteScreen(
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: TaskCompleteViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val hobby by viewModel.hobby.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHost = remember { SnackbarHostState() }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.submitImage(it) }
    }

    var capturedUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) capturedUri?.let { viewModel.submitImage(it) }
    }

    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            capturedUri = uri
            cameraLauncher.launch(uri)
        }
    }

    LaunchedEffect(state) {
        if (state is TaskCompleteState.Success) onSuccess()
    }

    LaunchedEffect(state) {
        if (state is TaskCompleteState.Error) {
            snackbarHost.showSnackbar("Could not save image — please try again.")
            viewModel.resetError()
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
                        Text("👾", fontSize = 72.sp)
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Time to create!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = InspyOnBackground,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            if (hobby == HobbyType.PHOTOGRAPHY) "Take a photo with your camera."
                            else "Upload a photo of your drawing.",
                            fontSize = 16.sp,
                            color = InspyOnBackground.copy(alpha = 0.65f),
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(40.dp))

                        if (hobby == HobbyType.PHOTOGRAPHY) {
                            Button(
                                onClick = {
                                    if (cameraPermission.status == PermissionStatus.Granted) {
                                        val uri = createImageUri(context)
                                        capturedUri = uri
                                        cameraLauncher.launch(uri)
                                    } else {
                                        cameraPermission.launchPermissionRequest()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = InspyPrimary),
                            ) {
                                Text("Open Camera", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            }
                        } else {
                            Button(
                                onClick = { imagePicker.launch("image/*") },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = InspyPrimary),
                            ) {
                                Text("Upload Drawing", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            }
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
        Text("🎉", fontSize = 80.sp, modifier = Modifier.scale(scale))
        Spacer(Modifier.height(16.dp))
        Text("Awesome work!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = InspyOnBackground)
        Spacer(Modifier.height(8.dp))
        Text("+$xp XP earned!", fontSize = 20.sp, color = InspyPrimary, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Text("Streak updated ✓", fontSize = 14.sp, color = InspyAccent)
    }
}

private fun createImageUri(context: android.content.Context): Uri {
    val file = java.io.File(context.cacheDir, "cam_${System.currentTimeMillis()}.jpg")
    return androidx.core.content.FileProvider.getUriForFile(
        context, "${context.packageName}.fileprovider", file
    )
}
