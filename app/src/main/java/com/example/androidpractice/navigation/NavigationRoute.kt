package com.example.androidpractice.navigation

import com.example.androidpractice.navigation.Keys

sealed class NavigationRoute(val destination: String) {
    object NotificationSettingsScreen : NavigationRoute(Keys.NOTIFICATION_SETTINGS_SCREEN)
    object NotificationEditScreen : NavigationRoute(Keys.NOTIFICATION_EDIT_SCREEN)
    object UserMessagesScreen : NavigationRoute(Keys.USER_MESSAGES_SCREEN)
}