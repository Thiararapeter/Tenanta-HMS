package com.thiarara.tenantahms.data.model

data class Room(
    val roomId: String,
    val propertyId: String,
    val number: String,
    val type: String,
    val status: RoomStatus,
    val monthlyRent: Double,
    val floor: Int,
    val amenities: List<String>,
    val description: String = "",
    val images: List<String> = emptyList()
)

enum class RoomStatus {
    VACANT,
    OCCUPIED,
    MAINTENANCE,
    RESERVED;

    fun displayName(): String = when (this) {
        VACANT -> "Vacant"
        OCCUPIED -> "Occupied"
        MAINTENANCE -> "Under Maintenance"
        RESERVED -> "Reserved"
    }
} 