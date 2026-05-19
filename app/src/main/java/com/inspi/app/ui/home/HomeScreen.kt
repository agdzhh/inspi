package com.inspi.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.inspi.app.ui.navigation.InspiBottomBar
import com.inspi.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onCompleteTask: () -> Unit,
    onRetakeTask: (Long) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.streakBroken) {
        if (state.streakBroken) viewModel.dismissStreakBroken()
    }

    Scaffold(
        containerColor = InspyBackground,
        bottomBar = { InspiBottomBar(navController) },
    ) { padding ->

        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = InspyPrimary)
            }
            return@Scaffold
        }

        val profile = state.profile
        if (profile == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Setting up your profile…")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            Text(
                "Hey, ${profile.username}! 👋",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = InspyOnBackground,
            )
            Spacer(Modifier.height(4.dp))
            Text(profile.hobby.displayName, fontSize = 14.sp, color = InspyPrimary, fontWeight = FontWeight.Medium)

            Spacer(Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StreakCard(streak = profile.currentStreak, modifier = Modifier.weight(1f))
                LevelCard(level = profile.level, fraction = profile.xpProgressFraction, xpToNext = profile.xpToNextLevel, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(20.dp))

            val task = state.todayTask
            if (task != null) {
                DailyTaskCard(
                    title = task.title,
                    description = task.description,
                    completed = state.taskCompletedToday,
                    onComplete = onCompleteTask,
                )
            } else {
                EmptyTaskCard()
            }

            Spacer(Modifier.height(16.dp))

            state.weeklyChallenge?.let { challenge ->
                WeeklyChallengeCard(
                    title = challenge.title,
                    description = challenge.description,
                    daysRemaining = challenge.daysRemaining,
                    isCompleted = challenge.isCompleted,
                )
            }

            state.retakeSubmission?.let { retake ->
                Spacer(Modifier.height(16.dp))
                RetakeCard(
                    taskTitle = retake.taskTitle,
                    onRetry = { onRetakeTask(retake.id) },
                )
            }

            state.weeklyInsight?.let { insight ->
                Spacer(Modifier.height(16.dp))
                WeeklyInsightCard(insight = insight)
            }
        }
    }
}

@Composable
private fun StreakCard(streak: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            // Whatshot is the flame/fire icon available in Material Icons Extended
            Icon(Icons.Outlined.Whatshot, contentDescription = "Streak", tint = Color(0xFFFF6B35), modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(4.dp))
            Text("$streak", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = InspyOnBackground)
            Text("day streak", fontSize = 12.sp, color = InspyOnBackground.copy(alpha = 0.6f))
        }
    }
}

@Composable
private fun LevelCard(level: Int, fraction: Float, xpToNext: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Star, contentDescription = "Level", tint = InspyPrimary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                Text("Level $level", fontWeight = FontWeight.Bold, color = InspyOnBackground)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { fraction },
                modifier = Modifier.fillMaxWidth(),
                color = InspyAccent,
                trackColor = InspyAccent.copy(alpha = 0.2f),
            )
            Spacer(Modifier.height(4.dp))
            Text("$xpToNext XP to next", fontSize = 11.sp, color = InspyOnBackground.copy(alpha = 0.55f))
        }
    }
}

@Composable
private fun DailyTaskCard(title: String, description: String, completed: Boolean, onComplete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (completed) InspyAccent.copy(alpha = 0.15f) else InspySurface),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Today's Task", fontSize = 12.sp, color = InspyPrimary, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = InspyOnBackground)
            Spacer(Modifier.height(6.dp))
            Text(description, fontSize = 14.sp, color = InspyOnBackground.copy(alpha = 0.7f), lineHeight = 20.sp)
            Spacer(Modifier.height(16.dp))

            if (completed) {
                Surface(shape = RoundedCornerShape(50.dp), color = InspyAccent, modifier = Modifier.fillMaxWidth()) {
                    Box(Modifier.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                        Text("✓ Done for today!", fontWeight = FontWeight.SemiBold, color = InspyOnBackground)
                    }
                }
            } else {
                Button(
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = InspyPrimary),
                ) {
                    Text("Complete Task", fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun WeeklyChallengeCard(title: String, description: String, daysRemaining: Int, isCompleted: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspyHighlight),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Weekly Challenge", fontSize = 12.sp, color = InspyPrimary, fontWeight = FontWeight.SemiBold)
                Surface(shape = RoundedCornerShape(50.dp), color = InspyPrimary.copy(alpha = 0.15f)) {
                    Text(
                        "$daysRemaining days left",
                        fontSize = 11.sp,
                        color = InspyPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = InspyOnBackground)
            Spacer(Modifier.height(4.dp))
            Text(description, fontSize = 13.sp, color = InspyOnBackground.copy(alpha = 0.65f), lineHeight = 19.sp)
            if (isCompleted) {
                Spacer(Modifier.height(8.dp))
                Text("✓ Completed!", color = InspyAccent, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun EmptyTaskCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
    ) {
        Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("👾", fontSize = 48.sp)
            Spacer(Modifier.height(12.dp))
            Text("Check back tomorrow!", fontWeight = FontWeight.SemiBold, color = InspyOnBackground, fontSize = 16.sp)
        }
    }
}

@Composable
private fun RetakeCard(taskTitle: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Improvement Challenge", fontSize = 12.sp, color = InspyAccent, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text("Redo: $taskTitle", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = InspyOnBackground)
            Spacer(Modifier.height(4.dp))
            Text(
                "You did this 3+ weeks ago. Try it again and see how much you've grown.",
                fontSize = 13.sp,
                color = InspyOnBackground.copy(alpha = 0.65f),
                lineHeight = 19.sp,
            )
            Spacer(Modifier.height(14.dp))
            OutlinedButton(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(50.dp),
            ) {
                Text("Try again", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun WeeklyInsightCard(insight: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspyHighlight),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Weekly Insight", fontSize = 12.sp, color = InspyPrimary, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(
                insight,
                fontSize = 14.sp,
                color = InspyOnBackground.copy(alpha = 0.85f),
                lineHeight = 21.sp,
            )
        }
    }
}
