package com.inspi.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Inspi "Pure Growth" Palette ──────────────────────────────────────────────
val InspyPrimary       = Color(0xFF7B6CC6)   // purple – CTA buttons, active tabs (slightly deeper)
val InspyAccent        = Color(0xFFB8D98D)   // lime-green – completed, progress bars
val InspyBackground    = Color(0xFFF0F7EC)   // soft mint background (matches website #F0F7EC)
val InspyOnBackground  = Color(0xFF1A2410)   // deep green-black – body text
val InspyHighlight     = Color(0xFFEDE9FF)   // lavender – cards, selected state
val InspySurface       = Color(0xFFFFFFFF)   // pure white cards (cleaner, like website)
val InspyOnSurface     = Color(0xFF1A2410)
val InspyOnPrimary     = Color(0xFFFFFFFF)

// Alternate shades
val InspyPurpleLight   = Color(0xFFB8ADEC)
val InspyGreenLight    = Color(0xFFDDEFB3)
val InspyCardBorder    = Color(0xFFE8F0E4)   // subtle border for cards

private val InspiColorScheme = lightColorScheme(
    primary            = InspyPrimary,
    onPrimary          = InspyOnPrimary,
    primaryContainer   = InspyHighlight,
    onPrimaryContainer = InspyOnBackground,
    secondary          = InspyAccent,
    onSecondary        = InspyOnBackground,
    background         = InspyBackground,
    onBackground       = InspyOnBackground,
    surface            = InspySurface,
    onSurface          = InspyOnSurface,
    surfaceVariant     = InspyHighlight,
    outline            = InspyCardBorder,
    error              = Color(0xFFB00020),
    onError            = Color.White,
)

@Composable
fun InspiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = InspiColorScheme,
        typography  = InspiTypography,
        content     = content,
    )
}
