package com.example.androidpractice.screen.third_screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidpractice.models.Note
import com.example.androidpractice.navigation.SharedData
import com.google.android.material.R

@Composable
fun ThirdScreen(navController: NavController,  data: SharedData) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title *") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                when {
                    title.isEmpty() -> {
                        Toast.makeText(context, context.getString(com.example.androidpractice.R.string.empty_title), Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        val newNote = Note(title, description)
                        data.notes.value = data.notes.value + newNote
                        navController.popBackStack()
                    }

                }


            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = context.getString(com.example.androidpractice.R.string.third_string_button))
        }
    }
}