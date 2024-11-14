package com.thiarara.tenantahms.data.model

enum class PropertyType {
    APARTMENT,
    HOUSE,
    VILLA,
    COMMERCIAL,
    OFFICE,
    WAREHOUSE,
    OTHER;

    fun displayName(): String = when (this) {
        APARTMENT -> "Apartment"
        HOUSE -> "House"
        VILLA -> "Villa"
        COMMERCIAL -> "Commercial Space"
        OFFICE -> "Office Space"
        WAREHOUSE -> "Warehouse"
        OTHER -> "Other"
    }
} 