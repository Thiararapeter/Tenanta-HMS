package com.thiarara.tenantahms.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thiarara.tenantahms.ui.screens.users.UsersScreen
import com.thiarara.tenantahms.ui.screens.dashboard.DashboardScreen
import com.thiarara.tenantahms.ui.screens.roles.RoleManagementScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PropertyList.route
    ) {
        composable(Screen.PropertyList.route) {
            // Your PropertyList screen composable
        }

        composable(Screen.Users.route) {
            UsersScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToRoles = { navController.navigate(Screen.Roles.route) }
            )
        }

        composable(Screen.Roles.route) {
            RoleManagementScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // Add other routes as needed
        composable(Screen.Tenants.route) {
            // Your Tenants screen composable
        }

        composable(Screen.Payments.route) {
            // Your Payments screen composable
        }

        composable(Screen.Complaints.route) {
            // Your Complaints screen composable
        }

        // Add other routes based on your Screen sealed class
    }
} 