package com.example.androidpractice.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavDestination
import com.example.androidpractice.R

@Composable
fun BottomNavigationBar(
    currentDestination: NavDestination?,
    onNavigate: (String) -> Unit
) {
    val listItems = listOf(
        BottomNavigationBarItem(
            route = NavigationRoute.NotificationSettingsScreen.destination,
            label = stringResource(R.string.notification_settings),
            icon = Icons.Default.Settings
        ),

        BottomNavigationBarItem(
            route = NavigationRoute.NotificationEditScreen.destination,
            label = stringResource(R.string.edit_notifications),
            icon = Icons.Default.Edit
        ),

        BottomNavigationBarItem(
            route = NavigationRoute.UserMessagesScreen.destination,
            label = stringResource(R.string.notification_user_messages),
            icon = Icons.Default.AccountCircle
        )
    )

    NavigationBar {
        listItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()) },
                selected = currentDestination?.route == item.route,
                onClick = { onNavigate(item.route) }
            )
        }
    }
}

data class BottomNavigationBarItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)