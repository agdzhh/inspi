package com.inspi.app.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inspi.app.R
import com.inspi.app.ui.theme.InspyBackground
import com.inspi.app.ui.theme.InspyOnBackground
import com.inspi.app.ui.theme.InspyPrimary

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    onHobbyAlreadySelected: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val hobby by viewModel.hobby.collectAsStateWithLifecycle()

    // Navigate away if hobby already selected (cold-start routing)
    LaunchedEffect(Unit) {
        val value = hobby

        if (!value.isNullOrBlank()) {
            kotlinx.coroutines.delay(150)
            onHobbyAlreadySelected()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = InspyBackground,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Logo / app name
            Text(
                text = "Inspi",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = InspyPrimary,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Build your creative habit,\none day at a time.",
                fontSize = 18.sp,
                color = InspyOnBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
            )

            Spacer(Modifier.height(48.dp))

            Image(
                painter = painterResource(R.drawable.mascot_inspi),
                contentDescription = "Inspi mascot waving hello",
                modifier = Modifier.size(220.dp),
            )

            Spacer(Modifier.height(64.dp))

            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = InspyPrimary),
            ) {
                Text(
                    "Get Started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }
        }
    }
}
