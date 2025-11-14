package com.example.androidpractice.screen.first_screen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.androidpractice.MainActivity
import com.example.androidpractice.utils.NotificationCounter
import com.example.androidpractice.utils.NotificationData
import com.example.androidpractice.utils.ReplyReceiver
import com.example.androidpractice.R
@Composable
fun FirstScreen() {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var isExpandable by remember { mutableStateOf(false) }
    var openApp by remember { mutableStateOf(false) }
    var addReply by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf(context.getString(R.string.priority_medium)) }

    // Автоматически выключаем isExpandable если нет текста
    LaunchedEffect(text) {
        if (text.isBlank()) {
            isExpandable = false
        }
    }

    val priorities = listOf(
        stringResource(R.string.priority_min),
        stringResource(R.string.priority_low),
        stringResource(R.string.priority_medium),
        stringResource(R.string.priority_high),
        stringResource(R.string.priority_urgent)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.notification_settings),
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.title_hint)) },
            isError = title.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        if (title.isBlank()) {
            Text(
                text = stringResource(R.string.title_error),
                color = MaterialTheme.colorScheme.error
            )
        }

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(stringResource(R.string.text_hint)) },
            modifier = Modifier.fillMaxWidth()
        )


        Text(
            text = stringResource(R.string.priority_label),
            modifier = Modifier.padding(top = 8.dp)
        )
        DropdownMenuBox(selectedPriority, priorities) { selectedPriority = it }


        SwitchWithLabel(
            label = stringResource(R.string.expand_label),
            checked = isExpandable,
            onCheckedChange = { isExpandable = it },
            enabled = text.isNotBlank()
        )

        SwitchWithLabel(
            label = stringResource(R.string.open_app_label),
            checked = openApp,
            onCheckedChange = { openApp = it }
        )

        SwitchWithLabel(
            label = stringResource(R.string.add_reply_label),
            checked = addReply,
            onCheckedChange = { addReply = it }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (title.isBlank()) {
                    Toast.makeText(context, context.getString(R.string.enter_title), Toast.LENGTH_SHORT).show()
                } else {
                    val notificationId = createNotification(context, title, text, selectedPriority, isExpandable, openApp, addReply)
                    Toast.makeText(
                        context,
                        context.getString(R.string.notification_created, notificationId),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.create_notification))
        }
    }
}

@Composable
fun DropdownMenuBox(selected: String, items: List<String>, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selected)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SwitchWithLabel(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, enabled: Boolean = true) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
    }
}

private fun createNotification(
    context: Context,
    title: String,
    text: String,
    priority: String,
    isExpandable: Boolean,
    openApp: Boolean,
    addReply: Boolean
): Int {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "main_channel",
            context.getString(R.string.channel_name),
            getImportance(context, priority)
        )
        notificationManager.createNotificationChannel(channel)
    }

    val notificationId = NotificationCounter.getNextId()

    NotificationData.createdNotifications[notificationId] = title

    val builder = NotificationCompat.Builder(context, "main_channel")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(title)
        .setContentText(if (text.isBlank()) context.getString(R.string.default_text) else text)
        .setPriority(getCompatPriority(context, priority))

    if (isExpandable && text.isNotBlank()) {
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(text))
    }

    if (openApp) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("title", title)
            putExtra("text", text)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pendingIntent)
    }

    if (addReply) {
        val replyIntent = Intent(context, ReplyReceiver::class.java).apply {
            putExtra("notification_id", notificationId)
        }
        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val remoteInput = RemoteInput.Builder("reply_key")
            .setLabel(context.getString(R.string.reply_label))
            .build()

        val action = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send,
            context.getString(R.string.reply_button),
            replyPendingIntent
        ).addRemoteInput(remoteInput)
            .setAllowGeneratedReplies(true)
            .build()

        builder.addAction(action)
    }

    notificationManager.notify(notificationId, builder.build())
    return notificationId
}
private fun getImportance(context: Context, priority: String): Int {
    return when (priority) {
        context.getString(R.string.priority_min) -> NotificationManager.IMPORTANCE_MIN
        context.getString(R.string.priority_low) -> NotificationManager.IMPORTANCE_LOW
        context.getString(R.string.priority_high) -> NotificationManager.IMPORTANCE_HIGH
        context.getString(R.string.priority_urgent) -> NotificationManager.IMPORTANCE_HIGH
        else -> NotificationManager.IMPORTANCE_DEFAULT
    }
}

private fun getCompatPriority(context: Context, priority: String): Int {
    return when (priority) {
        context.getString(R.string.priority_min) -> NotificationCompat.PRIORITY_MIN
        context.getString(R.string.priority_low) -> NotificationCompat.PRIORITY_LOW
        context.getString(R.string.priority_high) -> NotificationCompat.PRIORITY_HIGH
        context.getString(R.string.priority_urgent) -> NotificationCompat.PRIORITY_MAX
        else -> NotificationCompat.PRIORITY_DEFAULT
    }
}