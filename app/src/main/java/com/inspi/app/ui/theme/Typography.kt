package com.inspi.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val InspiTypography = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.Bold,   fontSize = 28.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    bodyLarge    = TextStyle(fontWeight = FontWeight.Normal,  fontSize = 16.sp),
    bodySmall    = TextStyle(fontWeight = FontWeight.Normal,  fontSize = 12.sp),
    labelSmall   = TextStyle(fontWeight = FontWeight.Medium,  fontSize = 11.sp),
)
