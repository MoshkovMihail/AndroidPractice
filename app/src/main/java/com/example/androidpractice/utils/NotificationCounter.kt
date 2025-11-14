package com.example.androidpractice.utils

object NotificationCounter {
    private var currentId = 1
    fun getNextId(): Int = currentId++
    fun reset() { currentId = 1 }
}