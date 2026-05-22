package com.inspi.app.ui.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.net.Uri
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.inspi.app.domain.models.CoachMessage
import com.inspi.app.domain.models.MessageRole
import com.inspi.app.ui.common.MascotMood
import com.inspi.app.ui.navigation.InspiBottomBar
import com.inspi.app.ui.theme.*
import com.inspi.app.ui.common.MascotImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachScreen(
    navController: NavController,
    viewModel: CoachViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var showClearDialog by remember { mutableStateOf(false) }
    var fullscreenImageUri by remember { mutableStateOf<String?>(null) }

    // Fullscreen photo viewer
    fullscreenImageUri?.let { uri ->
        FullscreenImageViewer(
            uri = uri,
            onDismiss = { fullscreenImageUri = null },
        )
    }

    // Scroll to bottom when new message arrives
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.scrollToItem(state.messages.size - 1)
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            containerColor = InspySurface,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    "Clear conversation?",
                    fontWeight = FontWeight.Bold,
                    color = InspyOnBackground,
                )
            },
            text = {
                Text(
                    "This will delete all messages. You can't undo this.",
                    color = InspyOnBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.clearConversation(); showClearDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = InspyPrimary),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Clear", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = InspyOnBackground.copy(alpha = 0.6f)),
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        containerColor = InspyBackground,
        bottomBar = { InspiBottomBar(navController) },
        topBar = {
            TopAppBar(
                title = { Text("AI Coach", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = InspyBackground),
                actions = {
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Clear conversation")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            // Messages list
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(state.messages, key = { it.id }) { msg ->
                    MessageBubble(msg, onPhotoClick = { fullscreenImageUri = it })
                }

                if (state.isTyping) {
                    item { TypingIndicator() }
                }

                state.error?.let { err ->
                    item {
                        ErrorInline(
                            message = err,
                            onRetry = { viewModel.retryLastMessage(); viewModel.dismissError() },
                            onDismiss = { viewModel.dismissError() },
                        )
                    }
                }
            }

            // Input bar
            HorizontalDivider(color = InspyOnBackground.copy(alpha = 0.08f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask your coach…", fontSize = 14.sp) },
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InspyPrimary,
                        unfocusedBorderColor = InspyOnBackground.copy(alpha = 0.2f),
                    ),
                )
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank() && !state.isTyping) {
                            val msg = inputText
                            inputText = ""
                            viewModel.sendMessage(msg)
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(InspyPrimary, CircleShape),
                ) {
                    Icon(Icons.Outlined.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: CoachMessage, onPhotoClick: (String) -> Unit = {}) {
    val isAssistant = message.role == MessageRole.ASSISTANT
    val isPhoto = message.content.startsWith("[img]")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isAssistant) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Bottom,
    ) {
        if (isAssistant) {
            // Coach avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(InspyPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
            ) {
                MascotImage(
                    mood = MascotMood.NEUTRAL,
                    modifier = Modifier.size(120.dp),
                )
            }
            Spacer(Modifier.width(8.dp))
        }

        if (isPhoto) {
            val imagePath = message.content.removePrefix("[img]")
            Card(
                shape = RoundedCornerShape(
                    topStart = 16.dp, topEnd = 16.dp,
                    bottomStart = 16.dp, bottomEnd = 4.dp,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .widthIn(max = 220.dp)
                    .clickable { onPhotoClick(imagePath) },
            ) {
                AsyncImage(
                    model = Uri.parse(imagePath),
                    contentDescription = "Submitted photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(210.dp)
                        .aspectRatio(4f / 3f),
                )
            }
        } else {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp, topEnd = 16.dp,
                    bottomStart = if (isAssistant) 4.dp else 16.dp,
                    bottomEnd = if (isAssistant) 16.dp else 4.dp,
                ),
                color = if (isAssistant) InspySurface else InspyPrimary,
                modifier = Modifier.widthIn(max = 280.dp),
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp, 10.dp),
                    fontSize = 15.sp,
                    color = if (isAssistant) InspyOnBackground else Color.White,
                    lineHeight = 21.sp,
                )
            }
        }
    }
}

@Composable
private fun TypingIndicator() {
    Row(verticalAlignment = Alignment.Bottom) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(InspyPrimary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center,
        ) {
            MascotImage(mood = MascotMood.NEUTRAL, modifier = Modifier.matchParentSize())
        }
        Spacer(Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp),
            color = InspySurface,
        ) {
            Row(modifier = Modifier.padding(16.dp, 12.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(3) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(InspyOnBackground.copy(alpha = 0.4f)))
                }
            }
        }
    }
}

@Composable
private fun ErrorInline(message: String, onRetry: () -> Unit, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(message, modifier = Modifier.weight(1f), fontSize = 13.sp, color = MaterialTheme.colorScheme.onErrorContainer)
            TextButton(onClick = onRetry) { Text("Retry") }
        }
    }
}

@Composable
private fun FullscreenImageViewer(uri: String, onDismiss: () -> Unit) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        // Ограничиваем панорамирование при зуме
        val maxOffset = 1000f * (scale - 1f) / scale
        offset = Offset(
            x = (offset.x + panChange.x).coerceIn(-maxOffset, maxOffset),
            y = (offset.y + panChange.y).coerceIn(-maxOffset, maxOffset),
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 5f)
                        val maxOffset = 1000f * (scale - 1f) / scale
                        offset = Offset(
                            x = (offset.x + pan.x).coerceIn(-maxOffset, maxOffset),
                            y = (offset.y + pan.y).coerceIn(-maxOffset, maxOffset),
                        )
                    }
                },
        ) {
            AsyncImage(
                model = Uri.parse(uri),
                contentDescription = "Full screen photo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .transformable(state = transformState)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y,
                    ),
            )

            // Кнопка закрытия
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape),
            ) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp),
                )
            }

            // Подсказка "щипок для зума" — исчезает через секунду
            var showHint by remember { mutableStateOf(true) }
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(1500)
                showHint = false
            }
            if (showHint) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(50.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text("Pinch to zoom", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                }
            }
        }
    }
}