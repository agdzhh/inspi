package com.inspi.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Inspi "Pure Growth" Palette ──────────────────────────────────────────────
val InspyPrimary       = Color(0xFF8272D6)   // purple  – CTA buttons, active tabs
val InspyAccent        = Color(0xFFC0DD97)   // lime    – completed tasks, progress
val InspyBackground    = Color(0xFFF6FAF1)   // off-white background
val InspyOnBackground  = Color(0xFF1A2410)   // deep green-black – body text
val InspyHighlight     = Color(0xFFEDE9FF)   // lavender – selected / hover state
val InspySurface       = Color(0xFFF6FAF1)   // card surface
val InspyOnSurface     = Color(0xFF1A2410)
val InspyOnPrimary     = Color(0xFFFFFFFF)

// Alternate shades (mascot colors)
val InspyPurpleLight   = Color(0xFFB8ADEC)
val InspyGreenLight    = Color(0xFFDDEFB3)

private val InspiColorScheme = lightColorScheme(
    primary          = InspyPrimary,
    onPrimary        = InspyOnPrimary,
    primaryContainer = InspyHighlight,
    onPrimaryContainer = InspyOnBackground,
    secondary        = InspyAccent,
    onSecondary      = InspyOnBackground,
    background       = InspyBackground,
    onBackground     = InspyOnBackground,
    surface          = InspySurface,
    onSurface        = InspyOnSurface,
    surfaceVariant   = InspyHighlight,
    error            = Color(0xFFB00020),
    onError          = Color.White,
)

@Composable
fun InspiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = InspiColorScheme,
        typography  = InspiTypography,
        content     = content,
    )
}
