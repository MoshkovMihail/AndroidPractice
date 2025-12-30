package com.example.androidpractice.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    @StringRes val labelRes: Int,
    val iconRes: ImageVector
)
