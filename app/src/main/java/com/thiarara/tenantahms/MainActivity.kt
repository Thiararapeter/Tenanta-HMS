package com.thiarara.tenantahms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thiarara.tenantahms.navigation.BottomNavItem
import com.thiarara.tenantahms.navigation.Screen
import com.thiarara.tenantahms.ui.screens.*
import com.thiarara.tenantahms.ui.screens.dashboard.DashboardScreen
import com.thiarara.tenantahms.ui.screens.home.HomeScreen
import com.thiarara.tenantahms.ui.screens.profile.ProfileScreen
import com.thiarara.tenantahms.ui.screens.settings.SettingsScreen
import com.thiarara.tenantahms.ui.screens.property.*
import com.thiarara.tenantahms.ui.screens.amenities.AmenitiesScreen
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
                        // Bottom Navigation Routes
                        composable(BottomNavItem.Home.route) {
                            HomeScreen()
                        }
                        composable(BottomNavItem.Dashboard.route) {
                            DashboardScreen(navController = navController)
                        }
                        composable(BottomNavItem.Profile.route) {
                            ProfileScreen()
                        }
                        composable(BottomNavItem.Settings.route) {
                            SettingsScreen()
                        }

                        // Property Management Routes
                        composable(Screen.PropertyList.route) {
                            PropertyListScreen(
                                onPropertyClick = { propertyId ->
                                    navController.navigate(Screen.PropertyDetail.createRoute(propertyId))
                                },
                                onAddProperty = {
                                    navController.navigate(Screen.AddProperty.route)
                                }
                            )
                        }
                        composable(
                            Screen.PropertyDetail.route,
                            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: return@composable
                            PropertyDetailScreen(
                                propertyId = propertyId,
                                onNavigateBack = { navController.navigateUp() },
                                onEditProperty = { id ->
                                    navController.navigate(Screen.EditProperty.createRoute(id))
                                }
                            )
                        }
                        composable(Screen.AddProperty.route) {
                            AddEditPropertyScreen(
                                onNavigateBack = { navController.navigateUp() },
                                onSave = { property ->
                                    // TODO: Save property
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable(
                            Screen.EditProperty.route,
                            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: return@composable
                            AddEditPropertyScreen(
                                propertyId = propertyId,
                                onNavigateBack = { navController.navigateUp() },
                                onSave = { property ->
                                    // TODO: Update property
                                    navController.navigateUp()
                                }
                            )
                        }

                        // Amenities Route
                        composable(Screen.Amenities.route) {
                            AmenitiesScreen(
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }

                        // Property Types Route
                        composable(Screen.PropertyTypes.route) {
                            PropertyTypesScreen(
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }

                        // Rooms Route
                        composable(Screen.Rooms.route) {
                            // TODO: Implement RoomsScreen
                            Text("Rooms Screen - Coming Soon")
                        }

                        // Users Route
                        composable(Screen.Users.route) {
                            // TODO: Implement UsersScreen
                            Text("Users Screen - Coming Soon")
                        }

                        // Tenants Route
                        composable(Screen.Tenants.route) {
                            // TODO: Implement TenantsScreen
                            Text("Tenants Screen - Coming Soon")
                        }

                        // Payments Route
                        composable(Screen.Payments.route) {
                            // TODO: Implement PaymentsScreen
                            Text("Payments Screen - Coming Soon")
                        }

                        // Complaints Route
                        composable(Screen.Complaints.route) {
                            // TODO: Implement ComplaintsScreen
                            Text("Complaints Screen - Coming Soon")
                        }

                        // Reports Routes
                        composable(Screen.FinancialReports.route) {
                            // TODO: Implement FinancialReportsScreen
                            Text("Financial Reports Screen - Coming Soon")
                        }
                        composable(Screen.OccupancyReports.route) {
                            // TODO: Implement OccupancyReportsScreen
                            Text("Occupancy Reports Screen - Coming Soon")
                        }
                    }
                }
            }
        }
    }
}