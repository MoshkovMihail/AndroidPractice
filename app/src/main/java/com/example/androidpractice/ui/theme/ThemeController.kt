package com.example.androidpractice.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

object ThemeController {
    var currentColor by mutableStateOf(Color.Blue)
}


@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = ThemeController.currentColor,
        secondary = ThemeController.currentColor,
        background = ThemeController.currentColor.copy(alpha = 0.1f)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}