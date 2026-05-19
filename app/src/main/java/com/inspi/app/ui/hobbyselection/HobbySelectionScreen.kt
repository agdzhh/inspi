package com.inspi.app.ui.hobbyselection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inspi.app.domain.models.HobbyType
import com.inspi.app.ui.theme.*

@Composable
fun HobbySelectionScreen(
    onContinue: () -> Unit,
    viewModel: HobbySelectionViewModel = hiltViewModel(),
) {
    val selected by viewModel.selected.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize(), color = InspyBackground) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(32.dp))

            Text(
                "What's your hobby?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = InspyOnBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Pick what you'd like to practice daily.",
                fontSize = 16.sp,
                color = InspyOnBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(40.dp))

            HobbyCard(
                title = "Photography",
                description = "Capture the world around you. Improve your eye for light, composition, and storytelling.",
                icon = Icons.Outlined.PhotoCamera,
                isSelected = selected == HobbyType.PHOTOGRAPHY,
                onClick = { viewModel.select(HobbyType.PHOTOGRAPHY) },
            )

            Spacer(Modifier.height(16.dp))

            HobbyCard(
                title = "Drawing",
                description = "Build your drawing skills with daily sketches and observation exercises.",
                icon = Icons.Outlined.Brush,
                isSelected = selected == HobbyType.DRAWING,
                onClick = { viewModel.select(HobbyType.DRAWING) },
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { viewModel.confirm(onContinue) },
                enabled = selected != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = InspyPrimary),
            ) {
                Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun HobbyCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val border = if (isSelected)
        BorderStroke(2.dp, InspyPrimary)
    else
        BorderStroke(1.dp, InspyOnBackground.copy(alpha = 0.12f))

    val bg = if (isSelected) InspyHighlight else InspySurface

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = border,
        colors = CardDefaults.cardColors(containerColor = bg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = InspyPrimary,
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = InspyOnBackground)
                Spacer(Modifier.height(4.dp))
                Text(description, fontSize = 14.sp, color = InspyOnBackground.copy(alpha = 0.6f), lineHeight = 20.sp)
            }
        }
    }
}
