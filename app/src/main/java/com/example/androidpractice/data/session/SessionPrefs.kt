package com.example.androidpractice.data.session

import android.content.Context

class SessionPrefs(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getUserId(): Long = prefs.getLong(KEY_USER_ID, NO_USER)

    fun setUserId(id: Long) {
        prefs.edit().putLong(KEY_USER_ID, id).apply()
    }

    fun clear() {
        prefs.edit().remove(KEY_USER_ID).apply()
    }

    companion object {
        private const val PREFS_NAME = "session_prefs"
        private const val KEY_USER_ID = "current_user_id"
        const val NO_USER = -1L
    }
}
