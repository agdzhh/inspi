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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.inspi.app.ui.navigation.InspiBottomBar
import com.inspi.app.ui.theme.*

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
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(InspyPrimary.copy(alpha = 0.15f))
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center,
            ) {
                Text("👾", fontSize = 48.sp)
            }

            Spacer(Modifier.height(12.dp))

            if (editingName) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it.take(20) },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .widthIn(min = 120.dp, max = 240.dp)
                        .focusRequester(focusRequester),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
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
                        .align(Alignment.CenterHorizontally)
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
            Text(
                profile.hobby.displayName,
                fontSize = 14.sp,
                color = InspyPrimary,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Spacer(Modifier.height(24.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StatCard("Level", "${profile.level}", modifier = Modifier.weight(1f))
                StatCard("Total XP", "${profile.totalXp}", modifier = Modifier.weight(1f))
                StatCard("Works", "${state.totalSubmissions}", modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StatCard("🔥 Streak", "${profile.currentStreak} days", modifier = Modifier.weight(1f))
                StatCard("Best", "${profile.longestStreak} days", modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))

            // Settings section
            SectionHeader("Settings")

            Spacer(Modifier.height(8.dp))

            // Notifications toggle
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = InspySurface),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Notifications, contentDescription = null, tint = InspyPrimary)
                    Spacer(Modifier.width(12.dp))
                    Text("Daily reminders", modifier = Modifier.weight(1f), color = InspyOnBackground)
                    Switch(
                        checked = state.notificationsOn,
                        onCheckedChange = { viewModel.setNotifications(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = InspyPrimary, checkedTrackColor = InspyPrimary.copy(alpha = 0.3f)),
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Change hobby
            Card(
                onClick = { showChangeHobbyDialog = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = InspySurface),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.SwapHoriz, contentDescription = null, tint = InspyPrimary)
                    Spacer(Modifier.width(12.dp))
                    Text("Change hobby", modifier = Modifier.weight(1f), color = InspyOnBackground)
                    Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = InspyOnBackground.copy(alpha = 0.3f))
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = InspyOnBackground)
            Text(label, fontSize = 11.sp, color = InspyOnBackground.copy(alpha = 0.55f))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = InspyOnBackground.copy(alpha = 0.5f))
}
