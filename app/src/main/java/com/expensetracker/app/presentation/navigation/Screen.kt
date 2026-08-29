package com.expensetracker.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object History : Screen("history")
    data object Statistics : Screen("statistics")
    data object Settings : Screen("settings")
    data object AddTransaction : Screen("add_transaction")
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Dashboard, "Home", Icons.Filled.Home),
    BottomNavItem(Screen.History, "History", Icons.Filled.History),
    BottomNavItem(Screen.Statistics, "Stats", Icons.Filled.BarChart),
    BottomNavItem(Screen.Settings, "Settings", Icons.Filled.Settings)
)
