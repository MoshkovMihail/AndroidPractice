package com.example.androidpractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.room.Room
import com.example.androidpractice.data.local.dao.UserDao
import com.example.androidpractice.data.local.dao.MemeDao
import com.example.androidpractice.data.local.db.AppDatabase
import com.example.androidpractice.data.session.SessionPrefs
import com.example.androidpractice.ui.navigation.AppNavGraph

class MainActivity : ComponentActivity() {

    lateinit var database: AppDatabase
        private set

    lateinit var userDao: UserDao
        private set

    lateinit var memeDao: MemeDao
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_db"
        ).build()

        userDao = database.userDao()
        memeDao = database.memeDao()

        val sessionPrefs = SessionPrefs(applicationContext)

        setContent {
            MaterialTheme {
                AppNavGraph(
                    userDao = userDao,
                    memeDao = memeDao,
                    sessionPrefs = sessionPrefs
                )
            }
        }
    }
}
