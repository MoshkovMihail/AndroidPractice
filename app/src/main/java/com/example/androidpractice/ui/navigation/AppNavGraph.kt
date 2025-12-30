package com.example.androidpractice.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidpractice.data.local.dao.MemeDao
import com.example.androidpractice.data.local.dao.UserDao
import com.example.androidpractice.data.session.SessionPrefs
import com.example.androidpractice.ui.auth.AuthViewModel
import com.example.androidpractice.ui.auth.AuthViewModelFactory
import com.example.androidpractice.ui.auth.DeletedAccountScreen
import com.example.androidpractice.ui.auth.DeletedAccountViewModel
import com.example.androidpractice.ui.auth.DeletedAccountViewModelFactory
import com.example.androidpractice.ui.auth.LoginScreen
import com.example.androidpractice.ui.auth.RegisterScreen

@Composable
fun AppNavGraph(
    userDao: UserDao,
    memeDao: MemeDao,
    sessionPrefs: SessionPrefs
) {
    val navController = rememberNavController()

    val startDestination =
        if (sessionPrefs.getUserId() != SessionPrefs.NO_USER) Routes.MAIN else Routes.LOGIN

    val authVm: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(userDao, sessionPrefs)
    )

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            val state by authVm.state.collectAsState()
            LoginScreen(
                state = state,
                onLogin = { email, pass ->
                    authVm.login(
                        email = email,
                        password = pass,
                        onSuccess = {
                            navController.navigate(Routes.MAIN) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        },
                        onDeleted = { deletedUserId ->
                            navController.navigate("${Routes.DELETED_ACCOUNT}/$deletedUserId")
                        }
                    )
                },
                onGoRegister = { navController.navigate(Routes.REGISTER) },
                onDismissError = { authVm.clearError() }
            )
        }

        composable(Routes.REGISTER) {
            val state by authVm.state.collectAsState()
            RegisterScreen(
                state = state,
                onRegister = { name, email, pass, confirm ->
                    authVm.register(name, email, pass, confirm) {
                        navController.navigate(Routes.MAIN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onBackToLogin = { navController.popBackStack() },
                onDismissError = { authVm.clearError() }
            )
        }

        composable("${Routes.DELETED_ACCOUNT}/{userId}") { backStackEntry ->
            val deletedUserId = backStackEntry.arguments?.getString("userId")?.toLongOrNull() ?: -1L

            val vm: DeletedAccountViewModel = viewModel(
                factory = DeletedAccountViewModelFactory(deletedUserId, userDao, sessionPrefs)
            )
            val st by vm.state.collectAsState()

            DeletedAccountScreen(
                isLoading = st.isLoading,
                onRestore = {
                    vm.restore {
                        navController.navigate(Routes.MAIN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onDeleteForever = {
                    vm.deleteForever {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.DELETED_ACCOUNT) { inclusive = true }
                        }
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.MAIN) {
            val userId = sessionPrefs.getUserId()

            LaunchedEffect(userId) {
                if (userId != SessionPrefs.NO_USER) {
                    val u = userDao.getById(userId)
                    if (u == null || u.isDeleted) {
                        sessionPrefs.clear()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.MAIN) { inclusive = true }
                        }
                    }
                }
            }

            MainScaffold(
                userDao = userDao,
                memeDao = memeDao,
                sessionPrefs = sessionPrefs,
                onLogout = {
                    sessionPrefs.clear()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
            )
        }
    }
}
