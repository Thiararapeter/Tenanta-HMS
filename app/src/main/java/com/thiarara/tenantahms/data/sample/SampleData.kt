package com.thiarara.tenantahms.data.sample

import com.thiarara.tenantahms.data.model.Property
import com.thiarara.tenantahms.data.model.Location
import com.thiarara.tenantahms.data.model.Amenity
import com.thiarara.tenantahms.data.model.AmenityCategory
import com.thiarara.tenantahms.data.model.PropertyType

val sampleProperties = listOf(
    Property(
        propertyId = "1",
        ownerId = "owner1",
        name = "Sunrise Apartments",
        type = "Apartment",
        address = "123 Main St",
        totalRooms = 20,
        vacantRooms = 5,
        amenities = listOf("WiFi", "Parking", "Security", "Water", "Gym"),
        location = Location(
            latitude = 0.0,
            longitude = 0.0,
            address = "123 Main St, Nairobi",
            city = "Nairobi",
            country = "Kenya"
        ),
        monthlyRent = 25000.0,
        description = "Modern apartment complex with great amenities"
    )
    // Add more sample properties as needed
)

val sampleAmenities = listOf(
    Amenity("WiFi", "wifi", AmenityCategory.BASIC_UTILITIES),
    Amenity("Parking", "parking", AmenityCategory.PARKING),
    Amenity("Security", "security", AmenityCategory.SECURITY),
    Amenity("Water", "water", AmenityCategory.BASIC_UTILITIES),
    Amenity("Electricity", "electricity", AmenityCategory.BASIC_UTILITIES),
    Amenity("Gym", "gym", AmenityCategory.RECREATION),
    Amenity("Swimming Pool", "pool", AmenityCategory.RECREATION),
    Amenity("CCTV", "cctv", AmenityCategory.SECURITY),
    Amenity("Garbage Collection", "garbage", AmenityCategory.CONVENIENCE),
    Amenity("Elevator", "elevator", AmenityCategory.CONVENIENCE)
) 