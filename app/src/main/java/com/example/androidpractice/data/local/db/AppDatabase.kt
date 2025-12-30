package com.example.androidpractice.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidpractice.data.local.dao.UserDao
import com.example.androidpractice.data.local.dao.MemeDao
import com.example.androidpractice.data.local.entity.MemeEntity
import com.example.androidpractice.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, MemeEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun memeDao(): MemeDao
}
