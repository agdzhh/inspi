package com.inspi.app.ui.friends

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.inspi.app.domain.models.HobbyType
import com.inspi.app.domain.models.LeaderboardEntry
import com.inspi.app.ui.navigation.InspiBottomBar
import com.inspi.app.ui.theme.*
import com.inspi.app.ui.common.MascotImage
import com.inspi.app.ui.common.MascotMood
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(
    navController: NavController,
    onFriendClick: (String) -> Unit = {},
    viewModel: FriendsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val clipboard = LocalClipboardManager.current
    var codeCopied by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = InspyBackground,
        bottomBar = { InspiBottomBar(navController) },
        topBar = {
            TopAppBar(
                title = { Text("Friends & Leagues", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = InspyBackground),
            )
        }
    ) { padding ->

        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = InspyPrimary)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // ── My code card ─────────────────────────────────────────────────
            item {
                MyCodeCard(
                    code = state.myCode,
                    copied = codeCopied,
                    onCopy = {
                        clipboard.setText(AnnotatedString(state.myCode))
                        codeCopied = true
                    },
                )
            }

            // ── Add friend form ───────────────────────────────────────────────
            item {
                AddFriendCard(
                    showForm = state.showAddForm,
                    codeInput = state.addCodeInput,
                    usernameInput = state.addUsernameInput,
                    error = state.addError,
                    onToggle = { viewModel.toggleAddForm() },
                    onCodeChange = { viewModel.onCodeInput(it) },
                    onUsernameChange = { viewModel.onUsernameInput(it) },
                    onAdd = { viewModel.addFriend() },
                )
            }

            // ── Weekly leaderboard ────────────────────────────────────────────
            if (state.leaderboard.isNotEmpty()) {
                item {
                    Text(
                        "This week",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = InspyOnBackground.copy(alpha = 0.5f),
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }

                items(state.leaderboard, key = { it.code }) { entry ->
                    LeaderboardRow(
                        entry = entry,
                        onRemove = { viewModel.removeFriend(it) },
                        onProfileClick = { if (!entry.isMe) onFriendClick(entry.code) },
                    )
                }
            }

            // ── Empty friends hint ────────────────────────────────────────────
            if (state.friends.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = InspySurface),
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            MascotImage(
                                mood = MascotMood.SAD,
                                modifier = Modifier.size(120.dp),
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Share your code with a friend and add theirs to compete on the leaderboard.",
                                fontSize = 13.sp,
                                color = InspyOnBackground.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center,
                                lineHeight = 19.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MyCodeCard(code: String, copied: Boolean, onCopy: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspyHighlight),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Your friend code", fontSize = 12.sp, color = InspyPrimary, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    code,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 6.sp,
                    color = InspyPrimary,
                )
                OutlinedButton(
                    onClick = onCopy,
                    shape = RoundedCornerShape(50.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, InspyPrimary),
                ) {
                    Icon(
                        if (copied) Icons.Outlined.Check else Icons.Outlined.ContentCopy,
                        contentDescription = null,
                        tint = InspyPrimary,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        if (copied) "Copied!" else "Copy",
                        color = InspyPrimary,
                        fontSize = 13.sp,
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Share this code so friends can add you.",
                fontSize = 12.sp,
                color = InspyOnBackground.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
private fun AddFriendCard(
    showForm: Boolean,
    codeInput: String,
    usernameInput: String,
    error: String?,
    onToggle: () -> Unit,
    onCodeChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onAdd: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Add Friend", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = InspyOnBackground)
                IconButton(onClick = onToggle, modifier = Modifier.size(32.dp)) {
                    Icon(
                        if (showForm) Icons.Outlined.Close else Icons.Outlined.PersonAdd,
                        contentDescription = null,
                        tint = InspyPrimary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            AnimatedVisibility(
                visible = showForm,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = codeInput,
                        onValueChange = onCodeChange,
                        label = { Text("Friend's code") },
                        placeholder = { Text("e.g. XK9M2P") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Next,
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InspyPrimary,
                            unfocusedBorderColor = InspyOnBackground.copy(alpha = 0.2f),
                        ),
                    )

                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = usernameInput,
                        onValueChange = onUsernameChange,
                        label = { Text("Their username") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onAdd() }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InspyPrimary,
                            unfocusedBorderColor = InspyOnBackground.copy(alpha = 0.2f),
                        ),
                    )
                    if (error != null) {
                        Spacer(Modifier.height(6.dp))
                        Text(error, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onAdd,
                        enabled = codeInput.length == 6 && (usernameInput.isNotBlank() || codeInput == "INSPI1"),
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = InspyPrimary),
                    ) {
                        Text("Add", fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun LeaderboardRow(
    entry: LeaderboardEntry,
    onRemove: (String) -> Unit,
    onProfileClick: () -> Unit = {},
) {
    val bgColor = if (entry.isMe) InspyHighlight else InspySurface
    val rankEmoji = when (entry.rank) {
        1 -> "🥇"
        2 -> "🥈"
        3 -> "🥉"
        else -> "#${entry.rank}"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (!entry.isMe) Modifier.clickable { onProfileClick() }
                else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(if (entry.isMe) 2.dp else 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Rank
            Text(
                rankEmoji,
                fontSize = if (entry.rank <= 3) 22.sp else 15.sp,
                fontWeight = FontWeight.Bold,
                color = InspyOnBackground,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center,
            )

            // Avatar circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (entry.isMe) InspyPrimary.copy(alpha = 0.2f)
                        else InspyAccent.copy(alpha = 0.3f)
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (entry.avatarUrl.isNotEmpty()) {
                    AsyncImage(
                        model = entry.avatarUrl,
                        contentDescription = entry.username,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Text(
                        if (entry.hobby == HobbyType.PHOTOGRAPHY) "📸" else "🎨",
                        fontSize = 18.sp,
                    )
                }
            }

            // Name + streak
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        entry.username,
                        fontWeight = if (entry.isMe) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 15.sp,
                        color = InspyOnBackground,
                    )
                    if (entry.isMe) {
                        Surface(shape = RoundedCornerShape(50.dp), color = InspyPrimary.copy(alpha = 0.15f)) {
                            Text(
                                "you",
                                fontSize = 10.sp,
                                color = InspyPrimary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            )
                        }
                    }
                }
                Text(
                    "🔥 ${entry.currentStreak} streak",
                    fontSize = 12.sp,
                    color = InspyOnBackground.copy(alpha = 0.55f),
                )
            }

            // Weekly XP
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${entry.weeklyXp} XP",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (entry.isMe) InspyPrimary else InspyOnBackground,
                )
                Text("this week", fontSize = 11.sp, color = InspyOnBackground.copy(alpha = 0.4f))
            }

            // Remove button (friends only)
            if (!entry.isMe) {
                IconButton(
                    onClick = { onRemove(entry.code) },
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        Icons.Outlined.PersonRemove,
                        contentDescription = "Remove friend",
                        tint = InspyOnBackground.copy(alpha = 0.3f),
                        modifier = Modifier.size(18.dp),
                    )
                }
                Icon(
                    Icons.Outlined.ChevronRight,
                    contentDescription = "View profile",
                    tint = InspyOnBackground.copy(alpha = 0.25f),
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}