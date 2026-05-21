package com.inspi.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
                .padding(horizontal = 20.dp, vertical = 28.dp),
        ) {
            // Header
            Text(
                "Good morning",
                fontSize = 13.sp,
                color = InspyOnBackground.copy(alpha = 0.5f),
                fontWeight = FontWeight.Medium,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "Hey, ${profile.username}! 👋",
                style = MaterialTheme.typography.headlineLarge,
                color = InspyOnBackground,
            )
            Spacer(Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = InspyHighlight,
            ) {
                Text(
                    profile.hobby.displayName,
                    fontSize = 12.sp,
                    color = InspyPrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Streak + Level — ОДИНАКОВАЯ высота через IntrinsicSize.Max ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),  // ключевое: обе карточки тянутся до высоты наибольшей
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StreakCard(streak = profile.currentStreak, modifier = Modifier.weight(1f).fillMaxHeight())
                LevelCard(
                    level = profile.level,
                    fraction = profile.xpProgressFraction,
                    xpToNext = profile.xpToNextLevel,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
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
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                Icons.Outlined.Whatshot,
                contentDescription = "Streak",
                tint = Color(0xFFFF6B35),
                modifier = Modifier.size(28.dp),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "$streak",
                style = MaterialTheme.typography.headlineLarge,
                color = InspyOnBackground,
            )
            Text(
                "day streak",
                style = MaterialTheme.typography.bodySmall,
                color = InspyOnBackground.copy(alpha = 0.55f),
            )
        }
    }
}

@Composable
private fun LevelCard(level: Int, fraction: Float, xpToNext: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Star, contentDescription = "Level", tint = InspyPrimary, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    "Level $level",
                    style = MaterialTheme.typography.titleMedium,
                    color = InspyOnBackground,
                )
            }
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(InspyAccent.copy(alpha = 0.2f)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .background(InspyAccent),
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "$xpToNext XP to next",
                style = MaterialTheme.typography.labelSmall,
                color = InspyOnBackground.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
private fun DailyTaskCard(title: String, description: String, completed: Boolean, onComplete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFB3A8E8), Color(0xFFCBE0A8))
                        ),
                        RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp),
            ) {
                Column {
                    Text(
                        "Today's quest",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = InspyOnBackground.copy(alpha = 0.7f),
                    lineHeight = 22.sp,
                )
                Spacer(Modifier.height(16.dp))

                if (completed) {
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = InspyAccent,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Box(Modifier.padding(vertical = 13.dp), contentAlignment = Alignment.Center) {
                            Text(
                                "✓  Done for today!",
                                style = MaterialTheme.typography.labelLarge,
                                color = InspyOnBackground,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = onComplete,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = InspyPrimary),
                    ) {
                        Text(
                            "Complete Task",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                        )
                    }
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
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    "Weekly Challenge",
                    style = MaterialTheme.typography.labelMedium,
                    color = InspyPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Surface(shape = RoundedCornerShape(50.dp), color = InspyPrimary.copy(alpha = 0.12f)) {
                    Text(
                        "$daysRemaining days left",
                        style = MaterialTheme.typography.labelSmall,
                        color = InspyPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, color = InspyOnBackground, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall, color = InspyOnBackground.copy(alpha = 0.65f), lineHeight = 18.sp)
            if (isCompleted) {
                Spacer(Modifier.height(8.dp))
                Text("✓ Completed!", color = Color(0xFF5A8A3A), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun EmptyTaskCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MascotImage(
                mood = MascotMood.HAPPY,
                modifier = Modifier.size(120.dp),
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Check back tomorrow!",
                style = MaterialTheme.typography.titleMedium,
                color = InspyOnBackground,
            )
        }
    }
}

@Composable
private fun RetakeCard(taskTitle: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Improvement Challenge",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF5A8A3A),
            )
            Spacer(Modifier.height(8.dp))
            Text("Redo: $taskTitle", style = MaterialTheme.typography.titleMedium, color = InspyOnBackground)
            Spacer(Modifier.height(4.dp))
            Text(
                "You did this 3+ weeks ago. Try it again and see how much you've grown.",
                style = MaterialTheme.typography.bodySmall,
                color = InspyOnBackground.copy(alpha = 0.65f),
                lineHeight = 18.sp,
            )
            Spacer(Modifier.height(14.dp))
            OutlinedButton(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth().height(46.dp),
                shape = RoundedCornerShape(50.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, InspyPrimary),
            ) {
                Text("Try again", style = MaterialTheme.typography.labelLarge, color = InspyPrimary)
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
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Weekly Insight", style = MaterialTheme.typography.labelMedium, color = InspyPrimary, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(insight, style = MaterialTheme.typography.bodyMedium, color = InspyOnBackground.copy(alpha = 0.85f), lineHeight = 22.sp)
        }
    }
}