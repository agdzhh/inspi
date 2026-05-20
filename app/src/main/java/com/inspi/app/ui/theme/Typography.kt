package com.inspi.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Matching the website's typography feel:
// - Heavy bold for headings (like "Build better habits through play")
// - Comfortable body size with generous line height
val InspiTypography = Typography(
    displayLarge  = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, lineHeight = 36.sp),
    headlineLarge = TextStyle(fontWeight = FontWeight.Bold,      fontSize = 24.sp, lineHeight = 30.sp),
    headlineMedium= TextStyle(fontWeight = FontWeight.Bold,      fontSize = 20.sp, lineHeight = 26.sp),
    titleLarge    = TextStyle(fontWeight = FontWeight.SemiBold,  fontSize = 18.sp, lineHeight = 24.sp),
    titleMedium   = TextStyle(fontWeight = FontWeight.SemiBold,  fontSize = 16.sp, lineHeight = 22.sp),
    bodyLarge     = TextStyle(fontWeight = FontWeight.Normal,    fontSize = 15.sp, lineHeight = 22.sp),
    bodyMedium    = TextStyle(fontWeight = FontWeight.Normal,    fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall     = TextStyle(fontWeight = FontWeight.Normal,    fontSize = 12.sp, lineHeight = 18.sp),
    labelLarge    = TextStyle(fontWeight = FontWeight.SemiBold,  fontSize = 14.sp),
    labelMedium   = TextStyle(fontWeight = FontWeight.Medium,    fontSize = 12.sp),
    labelSmall    = TextStyle(fontWeight = FontWeight.Medium,    fontSize = 11.sp),
)
