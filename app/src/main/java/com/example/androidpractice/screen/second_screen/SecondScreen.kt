package com.example.androidpractice.screen.second_screen

import android.app.NotificationManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import com.example.androidpractice.utils.NotificationCounter
import com.example.androidpractice.utils.NotificationData
import com.example.androidpractice.R

@Composable
fun SecondScreen() {
    val context = LocalContext.current
    var notificationId by remember { mutableStateOf("") }
    var notificationText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.edit_notification),
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = notificationId,
            onValueChange = { notificationId = it },
            label = { Text(stringResource(R.string.notification_id_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = notificationText,
            onValueChange = { notificationText = it },
            label = { Text(stringResource(R.string.new_text_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val id = notificationId.toIntOrNull()
                if (id != null && updateNotification(context, id, notificationText)) {
                    Toast.makeText(context, context.getString(R.string.notification_updated), Toast.LENGTH_SHORT).show()
                    notificationText = ""
                } else {
                    Toast.makeText(context, context.getString(R.string.notification_not_found), Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.update_notification))
        }

        Button(
            onClick = {
                if (clearAllNotifications(context)) {
                    Toast.makeText(context, context.getString(R.string.all_notifications_cleared), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, context.getString(R.string.no_notifications), Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.clear_all))
        }
    }
}

private fun updateNotification(context: Context, id: Int, newText: String): Boolean {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val originalTitle = NotificationData.createdNotifications[id]
    if (originalTitle == null) {
        val activeNotifications = notificationManager.activeNotifications
        if (activeNotifications.none { it.id == id }) return false
    }

    val notification = NotificationCompat.Builder(context, "main_channel")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(originalTitle ?: context.getString(R.string.notification_title))
        .setContentText(newText)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    notificationManager.notify(id, notification)
    return true
}

private fun clearAllNotifications(context: Context): Boolean {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val hasNotifications = notificationManager.activeNotifications.isNotEmpty()

    notificationManager.cancelAll()
    NotificationData.createdNotifications.clear()
    NotificationCounter.reset()
    return hasNotifications
}
