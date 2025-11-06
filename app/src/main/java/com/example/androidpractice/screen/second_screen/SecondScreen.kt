package com.example.androidpractice.screen.second_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidpractice.navigation.Keys
import com.example.androidpractice.R

import com.example.androidpractice.navigation.SharedData
import com.example.androidpractice.ui.theme.ThemeController



@Composable
fun SecondScreen(navController: NavController, data: SharedData) {
    val notes = data.notes.value
    val email = data.userEmail.value
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row {
            Text("User: $email", Modifier.weight(1f))
            ThemeSelector()
        }

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(notes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = note.title,
                        )
                        if (note.description.isNotEmpty()) {
                            Text(
                                text = note.description,
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                }
            }

        }

        Button(
            onClick = { navController.navigate(Keys.THIRD_SCREEN) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(context.getString(R.string.second_screen_button))
        }
    }
}

@Composable
fun ThemeSelector() {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Box {
        Button(onClick = { expanded = true }) {
            Text(context.getString(R.string.theme_colour_button))
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(context.getString(R.string.red_theme)) },
                onClick = {
                    ThemeController.currentColor = Color.Red
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(context.getString(R.string.green_theme)) },
                onClick = {
                    ThemeController.currentColor = Color.Green
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(context.getString(R.string.blue_theme)) },
                onClick = {
                    ThemeController.currentColor = Color.Blue
                    expanded = false
                }
            )
        }
    }
}