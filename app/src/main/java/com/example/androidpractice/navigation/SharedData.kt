package com.example.androidpractice.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.androidpractice.models.Note

class SharedData : ViewModel() {
    val userEmail = mutableStateOf("")
    val notes = mutableStateOf(listOf<Note>())
}
