package com.example.arthas.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidpractice.navigation.Keys
import com.example.androidpractice.navigation.SharedData
import com.example.androidpractice.screen.first_screen.FirstScreen
import com.example.androidpractice.screen.second_screen.SecondScreen
import com.example.androidpractice.screen.third_screen.ThirdScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    data: SharedData
) {
    NavHost(navController = navController, startDestination = Keys.FIRST_SCREEN) {
        composable(Keys.FIRST_SCREEN) {
            FirstScreen(navController = navController, data = data)
        }
        composable(Keys.SECOND_SCREEN) {
            SecondScreen(navController = navController, data = data)
        }
        composable(Keys.THIRD_SCREEN) {
            ThirdScreen(navController = navController, data = data)
        }
    }
}