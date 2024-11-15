package com.thiarara.tenantahms.data.model

data class Property(
    val propertyId: String,
    val ownerId: String,
    val name: String,
    val type: String,
    val address: String,
    val totalRooms: Int,
    val vacantRooms: Int,
    val amenities: List<String>,
    val location: Location,
    val monthlyRent: Double,
    val description: String,
    val images: List<String> = emptyList()
)

data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val city: String,
    val country: String
) 