package com.example.androidpractice.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.androidpractice.R
import com.example.androidpractice.data.local.dao.MemeDao
import com.example.androidpractice.data.local.dao.UserDao
import com.example.androidpractice.data.memes.MemeRepository
import com.example.androidpractice.data.session.SessionPrefs
import com.example.androidpractice.ui.memes.AddMemeScreen
import com.example.androidpractice.ui.memes.MemeListScreen
import com.example.androidpractice.ui.memes.MemeListViewModel
import com.example.androidpractice.ui.memes.MemeListViewModelFactory
import com.example.androidpractice.ui.profile.ProfileScreen
import com.example.androidpractice.ui.profile.ProfileViewModel
import com.example.androidpractice.ui.profile.ProfileViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    userDao: UserDao,
    memeDao: MemeDao,
    sessionPrefs: SessionPrefs,
    onLogout: () -> Unit
) {
    val innerNav = rememberNavController()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val memeRepository = remember { MemeRepository(memeDao) }

    val items = listOf(
        BottomNavItem(Routes.MEMES, R.string.memes, Icons.Default.Star),
        BottomNavItem(Routes.ADD_MEME, R.string.add_meme, Icons.Default.AddCircle),
        BottomNavItem(Routes.PROFILE, R.string.profile, Icons.Default.AccountCircle)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by innerNav.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    val selected = if (item.route == Routes.ADD_MEME) {
                        false // Add = action, not a tab
                    } else {
                        currentDestination?.hierarchy?.any { it.route == item.route } == true
                    }

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (item.route == Routes.ADD_MEME) {
                                innerNav.navigate(Routes.ADD_MEME)
                                return@NavigationBarItem
                            }

                            innerNav.navigate(item.route) {
                                popUpTo(innerNav.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(stringResource(item.labelRes)) },
                        icon = { Icon(imageVector = item.iconRes, contentDescription = null) }
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = innerNav,
            startDestination = Routes.MEMES,
            modifier = Modifier.padding(padding)
        ) {

            composable(Routes.MEMES) {
                val userId = sessionPrefs.getUserId()

                val vm: MemeListViewModel = viewModel(
                    factory = MemeListViewModelFactory(userId, memeRepository)
                )

                LaunchedEffect(userId) {
                    vm.seedIfEmpty(
                        packageName = context.packageName,
                        resolveTitle = { id -> context.getString(id) }
                    )
                }

                val ui by vm.ui.collectAsState()
                val list by vm.memes.collectAsState()

                MemeListScreen(
                    ui = ui,
                    items = list,
                    onOpenSort = vm::setSort,
                    onToggleFavorite = { memeId, current ->
                        scope.launch { memeRepository.toggleFavorite(memeId, current) }
                    }
                )
            }

            composable(Routes.PROFILE) {
                val userId = sessionPrefs.getUserId()

                val vm: ProfileViewModel = viewModel(
                    factory = ProfileViewModelFactory(
                        userId = userId,
                        userDao = userDao,
                        memeDao = memeDao
                    )
                )

                val state by vm.state.collectAsState()

                ProfileScreen(
                    state = state,
                    memesCount = state.memesCount,
                    favoritesCount = state.favoritesCount,
                    onLogout = onLogout,
                    onDeleteAccount = {
                        scope.launch {
                            vm.deleteAccount {
                                onLogout()
                            }
                        }
                    }
                )
            }

            composable(Routes.ADD_MEME) {
                val userId = sessionPrefs.getUserId()

                AddMemeScreen(
                    onSave = { title, imageUri, notes ->
                        scope.launch {
                            memeRepository.addMeme(
                                userId = userId,
                                title = title,
                                imageUri = imageUri,
                                notes = notes
                            )
                            innerNav.popBackStack()
                        }
                    },
                    onCancel = { innerNav.popBackStack() }
                )
            }
        }
    }
}
