package com.cikup.sumopod.sample.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cikup.sumopod.sample.di.ClientProvider
import com.cikup.sumopod.sample.screen.chat.ChatScreen
import com.cikup.sumopod.sample.screen.chat.ChatViewModel
import com.cikup.sumopod.sample.screen.models.ModelsScreen
import com.cikup.sumopod.sample.screen.models.ModelsViewModel
import com.cikup.sumopod.sample.screen.settings.SettingsScreen
import com.cikup.sumopod.sample.screen.settings.SettingsViewModel

enum class Screen(val route: String, val label: String) {
    Chat("chat", "Chat"),
    Models("models", "Models"),
    Settings("settings", "Settings"),
}

@Composable
fun AppNavigation(
    settingsViewModel: SettingsViewModel,
    clientProvider: ClientProvider,
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    val chatViewModel = remember { ChatViewModel(clientProvider) }
    val modelsViewModel = remember { ModelsViewModel(clientProvider) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                Screen.entries.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { },
                        label = { Text(screen.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Chat.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.Chat.route) {
                ChatScreen(chatViewModel)
            }
            composable(Screen.Models.route) {
                ModelsScreen(modelsViewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(settingsViewModel)
            }
        }
    }
}
