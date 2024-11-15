package com.thiarara.tenantahms.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    
    object Dashboard : BottomNavItem(
        route = "dashboard",
        title = "Dashboard",
        icon = Icons.Default.Dashboard
    )
    
    object Profile : BottomNavItem(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )
    
    object Settings : BottomNavItem(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
} 