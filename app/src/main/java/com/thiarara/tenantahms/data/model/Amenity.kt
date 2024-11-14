package com.thiarara.tenantahms.data.model

data class Amenity(
    val name: String,
    val icon: String,
    val category: AmenityCategory
)

enum class AmenityCategory {
    BASIC_UTILITIES,
    SECURITY,
    RECREATION,
    CONVENIENCE,
    PARKING,
    OTHER;

    fun displayName(): String = when (this) {
        BASIC_UTILITIES -> "Basic Utilities"
        SECURITY -> "Security"
        RECREATION -> "Recreation"
        CONVENIENCE -> "Convenience"
        PARKING -> "Parking"
        OTHER -> "Other"
    }
} 