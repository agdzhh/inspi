package com.inspi.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.inspi.app.ui.navigation.InspiBottomBar
import com.inspi.app.ui.theme.*
import com.inspi.app.ui.common.MascotImage
import com.inspi.app.ui.common.MascotMood

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    onChangeHobby: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showChangeHobbyDialog by remember { mutableStateOf(false) }

    if (showChangeHobbyDialog) {
        AlertDialog(
            onDismissRequest = { showChangeHobbyDialog = false },
            title = { Text("Change hobby?") },
            text = { Text("Changing your hobby will reset your current streak. Are you sure?") },
            confirmButton = {
                TextButton(onClick = { onChangeHobby(); showChangeHobbyDialog = false }) {
                    Text("Change", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { showChangeHobbyDialog = false }) { Text("Cancel") } }
        )
    }

    Scaffold(
        containerColor = InspyBackground,
        bottomBar = { InspiBottomBar(navController) },
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = InspyBackground),
            )
        }
    ) { padding ->

        if (state.isLoading || state.profile == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = InspyPrimary)
            }
            return@Scaffold
        }

        val profile = state.profile!!
        var editingName by remember { mutableStateOf(false) }
        var nameInput by remember(profile.username) { mutableStateOf(profile.username) }
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {

            // ── Аватар + имя ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(InspyHighlight),
                        contentAlignment = Alignment.Center,
                    ) {
                        MascotImage(
                            mood = MascotMood.NEUTRAL,
                            modifier = Modifier.size(120.dp),
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = InspyPrimary,
                        modifier = Modifier.padding(2.dp),
                    ) {
                        Text(
                            "Lv ${profile.level}",
                            fontSize = 10.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (editingName) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it.take(20) },
                        modifier = Modifier
                            .widthIn(min = 120.dp, max = 240.dp)
                            .focusRequester(focusRequester),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            viewModel.updateUsername(nameInput)
                            editingName = false
                            focusManager.clearFocus()
                        }),
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.updateUsername(nameInput)
                                editingName = false
                                focusManager.clearFocus()
                            }) {
                                Icon(Icons.Outlined.Check, contentDescription = "Save", tint = InspyPrimary)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InspyPrimary,
                            unfocusedBorderColor = InspyOnBackground.copy(alpha = 0.2f),
                        ),
                        shape = RoundedCornerShape(12.dp),
                    )
                    LaunchedEffect(Unit) { focusRequester.requestFocus() }
                } else {
                    Row(
                        modifier = Modifier
                            .clickable { editingName = true }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            profile.username,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = InspyOnBackground,
                        )
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = "Edit name",
                            tint = InspyOnBackground.copy(alpha = 0.3f),
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Surface(shape = RoundedCornerShape(50.dp), color = InspyHighlight) {
                    Text(
                        profile.hobby.displayName,
                        fontSize = 12.sp,
                        color = InspyPrimary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                    )
                }
            }

            // ── XP прогресс ───────────────────────────────────────────────
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = InspySurface),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text("Level ${profile.level}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = InspyOnBackground)
                            Text("${profile.xpToNextLevel} XP to next level", fontSize = 12.sp, color = InspyOnBackground.copy(alpha = 0.5f))
                        }
                        Icon(Icons.Outlined.Star, contentDescription = null, tint = InspyPrimary, modifier = Modifier.size(28.dp))
                    }
                    Spacer(Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(InspyHighlight),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(profile.xpProgressFraction.coerceIn(0f, 1f))
                                .fillMaxHeight()
                                .background(InspyPrimary),
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // ── Стат-карточки — ОДИНАКОВАЯ высота через IntrinsicSize.Max ──
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                StatCard(
                    icon = Icons.Outlined.Whatshot,
                    iconTint = Color(0xFFE8804A),
                    value = "${profile.currentStreak}",
                    label = "Streak",
                    sub = "Best: ${profile.longestStreak}d",
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
                StatCard(
                    icon = Icons.Outlined.PhotoLibrary,
                    iconTint = InspyPrimary,
                    value = "${state.totalSubmissions}",
                    label = "Works",
                    sub = null,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                StatCard(
                    icon = Icons.Outlined.EmojiEvents,
                    iconTint = Color(0xFFC4922A),
                    value = "${profile.totalXp} XP",
                    label = "Total earned",
                    sub = null,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
                StatCard(
                    icon = Icons.Outlined.CalendarToday,
                    iconTint = Color(0xFF3A9E75),
                    value = "${state.totalSubmissions}",
                    label = "Quests done",
                    sub = null,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Настройки ─────────────────────────────────────────────────
            Text(
                "SETTINGS",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = InspyOnBackground.copy(alpha = 0.4f),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
            )

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = InspySurface),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(InspyHighlight),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Outlined.Notifications, contentDescription = null, tint = InspyPrimary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Daily reminders", modifier = Modifier.weight(1f), color = InspyOnBackground, fontSize = 15.sp)
                    Switch(
                        checked = state.notificationsOn,
                        onCheckedChange = { viewModel.setNotifications(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = InspyPrimary,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = InspyOnBackground.copy(alpha = 0.2f),
                        ),
                    )
                }

                HorizontalDivider(color = InspyCardBorder, thickness = 0.5.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showChangeHobbyDialog = true }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(InspyHighlight),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Outlined.SwapHoriz, contentDescription = null, tint = InspyPrimary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Change hobby", modifier = Modifier.weight(1f), color = InspyOnBackground, fontSize = 15.sp)
                    Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = InspyOnBackground.copy(alpha = 0.3f))
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    iconTint: Color,
    value: String,
    label: String,
    sub: String?,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
            Column {
                Spacer(Modifier.height(8.dp))
                Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = InspyOnBackground)
                Text(label, fontSize = 11.sp, color = InspyOnBackground.copy(alpha = 0.5f))
                // sub всегда занимает место — если null, рендерим пустую строку той же высоты
                Text(
                    sub ?: "",
                    fontSize = 11.sp,
                    color = InspyPrimary,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}