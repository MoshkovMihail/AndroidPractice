package com.example.androidpractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidpractice.navigation.SharedData
import com.example.androidpractice.screen.first_screen.FirstScreen
import com.example.androidpractice.screen.second_screen.SecondScreen
import com.example.androidpractice.screen.third_screen.ThirdScreen
import com.example.androidpractice.ui.theme.AppTheme
import com.example.arthas.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val sharedViewModel: SharedData = viewModel()

                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(navController = navController, data = sharedViewModel)
                }
            }
        }
    }
}