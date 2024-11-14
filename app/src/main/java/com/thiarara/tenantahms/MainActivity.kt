package com.thiarara.tenantahms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thiarara.tenantahms.navigation.BottomNavItem
import com.thiarara.tenantahms.ui.screens.dashboard.DashboardScreen
import com.thiarara.tenantahms.ui.screens.home.HomeScreen
import com.thiarara.tenantahms.ui.screens.profile.ProfileScreen
import com.thiarara.tenantahms.ui.screens.settings.SettingsScreen
import com.thiarara.tenantahms.ui.theme.TenantaHMSTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TenantaHMSTheme {
                val navController = rememberNavController()
                var selectedItem by remember { mutableStateOf(0) }
                val items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Dashboard,
                    BottomNavItem.Profile,
                    BottomNavItem.Settings
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = item.title) },
                                    label = { Text(item.title) },
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        navController.navigate(item.route)
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Dashboard.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(BottomNavItem.Home.route) {
                            HomeScreen()
                        }
                        composable(BottomNavItem.Dashboard.route) {
                            DashboardScreen()
                        }
                        composable(BottomNavItem.Profile.route) {
                            ProfileScreen()
                        }
                        composable(BottomNavItem.Settings.route) {
                            SettingsScreen()
                        }
                    }
                }
            }
        }
    }
}