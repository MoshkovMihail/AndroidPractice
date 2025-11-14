package com.example.androidpractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.androidpractice.navigation.BottomNavigationBar
import com.example.androidpractice.navigation.NavigationRoute
import com.example.androidpractice.screen.first_screen.FirstScreen
import com.example.androidpractice.screen.second_screen.SecondScreen
import com.example.androidpractice.screen.third_screen.ThirdScreen
import com.example.androidpractice.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface {
                    val navController = rememberNavController()
                    val currentBackStack = navController.currentBackStackEntryAsState().value
                    val currentDestination = currentBackStack?.destination


                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                currentDestination = currentDestination,
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = NavigationRoute.NotificationSettingsScreen.destination,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            composable(route = NavigationRoute.NotificationSettingsScreen.destination) {
                                FirstScreen()
                            }
                            composable(route = NavigationRoute.NotificationEditScreen.destination) {
                                SecondScreen()
                            }
                            composable(route = NavigationRoute.UserMessagesScreen.destination) {
                                ThirdScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}