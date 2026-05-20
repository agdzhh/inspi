package com.inspi.app.ui.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.inspi.app.domain.models.CoachMessage
import com.inspi.app.domain.models.MessageRole
import com.inspi.app.ui.navigation.InspiBottomBar
import com.inspi.app.ui.theme.*

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

    // Scroll to bottom when new message arrives
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.scrollToItem(state.messages.size - 1)
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear conversation?") },
            text = { Text("This will delete all messages. You can't undo this.") },
            confirmButton = {
                TextButton(onClick = { viewModel.clearConversation(); showClearDialog = false }) {
                    Text("Clear", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Cancel") }
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
                    MessageBubble(msg)
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
private fun MessageBubble(message: CoachMessage) {
    val isAssistant = message.role == MessageRole.ASSISTANT

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
                Text("👾", fontSize = 18.sp)
            }
            Spacer(Modifier.width(8.dp))
        }

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
            Text("👾", fontSize = 18.sp)
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
