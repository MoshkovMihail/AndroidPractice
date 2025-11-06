package com.example.androidpractice.screen.first_screen

import android.widget.Toast
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidpractice.R
import com.example.androidpractice.utils.ValidationChecker
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.ui.Alignment
import com.example.androidpractice.navigation.Keys
import com.example.androidpractice.navigation.SharedData


@Composable
fun FirstScreen(navController: NavController, data: SharedData) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Column (modifier = Modifier.padding(20.dp)
        ){
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(context.getString(R.string.email_text_field)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(context.getString(R.string.password_text_field)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (showPassword)
                        Icons.Sharp.KeyboardArrowUp
                    else
                        Icons.Sharp.KeyboardArrowDown
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(image, null)
                    }
                }
            )
        }


        Button(
            onClick = {
                when {
                    email.isBlank() -> {
                        Toast.makeText(context, context.getString(R.string.empty_email), Toast.LENGTH_SHORT).show()
                    }

                    !ValidationChecker.isValidEmail(email) -> {
                        Toast.makeText(context, context.getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
                    }

                    password.isBlank() -> {
                        Toast.makeText(context, context.getString(R.string.empty_password), Toast.LENGTH_SHORT).show()
                    }

                    !ValidationChecker.isValidPassword(password) -> {
                        Toast.makeText(context, context.getString(R.string.to_low_password), Toast.LENGTH_SHORT).show()
                    }


                    else -> {data.userEmail.value = email
                    navController.navigate(Keys.SECOND_SCREEN)}
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter).imePadding()
        ) {
            Text(context.getString(R.string.first_screen_button))
        }

    }
}