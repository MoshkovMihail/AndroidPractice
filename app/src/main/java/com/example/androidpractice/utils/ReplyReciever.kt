package com.example.androidpractice.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.RemoteInput
import com.example.androidpractice.R

class ReplyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        val reply = remoteInput?.getCharSequence("reply_key")?.toString()

        reply?.let {
            MessagesDatabase.add(context.getString(R.string.reply_from_notification, it))

            val notificationId = intent.getIntExtra("notification_id", -1)
            if (notificationId != -1) {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(notificationId)
            }

            Toast.makeText(context, R.string.reply_saved, Toast.LENGTH_SHORT).show()
        }
    }
}