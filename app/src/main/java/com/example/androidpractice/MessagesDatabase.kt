package com.example.androidpractice.utils

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
object MessagesDatabase {
    val messages = mutableStateListOf<String>()

    fun add(message: String) {
        messages.add("${getCurrentTime()} - $message")
    }

    private fun getCurrentTime(): String {
        return java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
            .format(java.util.Date())
    }
}