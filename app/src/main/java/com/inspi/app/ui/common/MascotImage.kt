package com.inspi.app.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.inspi.app.R

// Удобная обёртка — используй вместо Text("👾") везде в коде
@Composable
fun MascotImage(
    mood: MascotMood,
    modifier: Modifier = Modifier,
) {
    androidx.compose.foundation.layout.Box(modifier = modifier) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = mood.resId),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
        )
    }
}

enum class MascotMood(@DrawableRes val resId: Int) {
    HAPPY(R.drawable.ic_mascot_happy),      // руки вверх, язык — успех, квест выполнен
    NEUTRAL(R.drawable.ic_mascot_neutral),  // машет рукой — приветствие, Coach аватар
    SAD(R.drawable.ic_mascot_sad),          // грустный — пустая галерея, ошибки
}
