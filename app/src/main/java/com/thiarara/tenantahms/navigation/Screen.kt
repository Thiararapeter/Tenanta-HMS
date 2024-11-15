package com.thiarara.tenantahms.navigation

sealed class Screen(val route: String) {
    object PropertyList : Screen("property_list")
    object PropertyDetail : Screen("property_detail/{propertyId}") {
        fun createRoute(propertyId: String) = "property_detail/$propertyId"
    }
    object AddProperty : Screen("add_property")
    object EditProperty : Screen("edit_property/{propertyId}") {
        fun createRoute(propertyId: String) = "edit_property/$propertyId"
    }
    object Amenities : Screen("amenities")
    object PropertyTypes : Screen("property_types")
    object Rooms : Screen("rooms")
    object RoomTypes : Screen("room_types")
    object Users : Screen("users")
    object Tenants : Screen("tenants")
    object Payments : Screen("payments")
    object Complaints : Screen("complaints")
    object FinancialReports : Screen("financial_reports")
    object OccupancyReports : Screen("occupancy_reports")
    object RoomsList : Screen("rooms_list")
    object Roles : Screen("roles")
} 