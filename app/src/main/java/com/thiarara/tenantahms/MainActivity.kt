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
import com.thiarara.tenantahms.ui.screens.rooms.*
import com.thiarara.tenantahms.data.PropertyDataManager
import com.thiarara.tenantahms.data.model.Property
import com.thiarara.tenantahms.ui.screens.tenants.TenantManagementScreen
import com.thiarara.tenantahms.ui.screens.users.UsersScreen
import com.thiarara.tenantahms.ui.screens.roles.RoleManagementScreen
import com.thiarara.tenantahms.ui.screens.complaints.ComplaintsScreen
import com.thiarara.tenantahms.ui.screens.complaints.ComplaintDetailScreen

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
                        startDestination = Screen.PropertyList.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Bottom Navigation Routes
                        composable(BottomNavItem.Home.route) {
                            HomeScreen()
                        }
                        composable(BottomNavItem.Dashboard.route) {
                            DashboardScreen(
                                onNavigate = { route -> navController.navigate(route) }
                            )
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
                                },
                                navController = navController
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
                                },
                                onDeleteProperty = { property ->
                                    PropertyDataManager.deleteProperty(property)
                                    navController.navigateUp()
                                },
                                onManageRooms = { id ->
                                    navController.navigate(Screen.RoomsList.route + "?propertyId=$id")
                                },
                                onViewTenants = { _ ->
                                    // TODO: Implement navigation to tenants screen
                                    // navController.navigate(Screen.PropertyTenants.createRoute(id))
                                }
                            )
                        }
                        composable(Screen.AddProperty.route) {
                            AddEditPropertyScreen(
                                onNavigateBack = { navController.navigateUp() },
                                onSave = { property ->
                                    PropertyDataManager.addProperty(property)
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable(
                            Screen.EditProperty.route,
                            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: return@composable
                            val property = PropertyDataManager.getProperty(propertyId)
                            if (property != null) {
                                AddEditPropertyScreen(
                                    propertyId = propertyId,
                                    onNavigateBack = { navController.navigateUp() },
                                    onSave = { updatedProperty ->
                                        PropertyDataManager.updateProperty(property, updatedProperty)
                                        navController.navigateUp()
                                    }
                                )
                            }
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

                        // Rooms Management Routes
                        composable(Screen.Rooms.route) {
                            RoomsManagementScreen(
                                onNavigateBack = { navController.navigateUp() },
                                onNavigateToRooms = { navController.navigate(Screen.RoomsList.route) },
                                onNavigateToRoomTypes = { navController.navigate(Screen.RoomTypes.route) }
                            )
                        }

                        composable(
                            route = Screen.RoomsList.route + "?propertyId={propertyId}",
                            arguments = listOf(
                                navArgument("propertyId") {
                                    type = NavType.StringType
                                    nullable = true
                                    defaultValue = null
                                }
                            )
                        ) { backStackEntry ->
                            val propertyId = backStackEntry.arguments?.getString("propertyId")
                            RoomsScreen(
                                propertyId = propertyId,
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }

                        composable(Screen.RoomTypes.route) {
                            RoomTypesScreen(
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }

                        // Users Route
                        composable(Screen.Users.route) {
                            UsersScreen(
                                onNavigateBack = { navController.navigateUp() },
                                onNavigateToRoles = { navController.navigate(Screen.Roles.route) }
                            )
                        }

                        // Tenants Route
                        composable(Screen.Tenants.route) {
                            TenantManagementScreen(
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }

                        // Payments Route
                        composable(Screen.Payments.route) {
                            // TODO: Implement PaymentsScreen
                            Text("Payments Screen - Coming Soon")
                        }

                        // Complaints Route
                        composable(Screen.Complaints.route) {
                            ComplaintsScreen(
                                onNavigateBack = { navController.navigateUp() },
                                onComplaintClick = { complaintId ->
                                    navController.navigate("${Screen.ComplaintDetails.route}/$complaintId")
                                }
                            )
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

                        // Roles Route
                        composable(Screen.Roles.route) {
                            RoleManagementScreen(
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }

                        // Complaint Details Route
                        composable(
                            route = "${Screen.ComplaintDetails.route}/{complaintId}",
                            arguments = listOf(navArgument("complaintId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val complaintId = backStackEntry.arguments?.getString("complaintId") ?: return@composable
                            ComplaintDetailScreen(
                                complaintId = complaintId,
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}