package com.thiarara.tenantahms.data.model

data class Room(
    val roomId: String,
    val propertyId: String,
    val number: String,
    val type: RoomType,
    val status: RoomStatus,
    val monthlyRent: Double,
    val floor: Int,
    val amenities: List<String>,
    val description: String = "",
    val images: List<String> = emptyList()
)

enum class RoomType {
    SINGLE,
    DOUBLE,
    STUDIO,
    ENSUITE,
    MASTER,
    OTHER;

    fun displayName(): String = when (this) {
        SINGLE -> "Single Room"
        DOUBLE -> "Double Room"
        STUDIO -> "Studio"
        ENSUITE -> "En-suite"
        MASTER -> "Master Room"
        OTHER -> "Other"
    }
}

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