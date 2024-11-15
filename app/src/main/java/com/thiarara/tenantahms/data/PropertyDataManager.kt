package com.thiarara.tenantahms.data

import androidx.compose.runtime.mutableStateListOf
import com.thiarara.tenantahms.data.model.Amenity
import com.thiarara.tenantahms.data.model.Property
import com.thiarara.tenantahms.data.model.PropertyType
import com.thiarara.tenantahms.data.model.Room
import com.thiarara.tenantahms.data.model.RoomStatus
import com.thiarara.tenantahms.data.model.Location
import com.thiarara.tenantahms.data.sample.sampleAmenities
import com.thiarara.tenantahms.data.model.Tenant

object PropertyDataManager {
    private val _amenities = mutableStateListOf<Amenity>()
    val amenities: List<Amenity> get() = _amenities

    private val _propertyTypes = mutableStateListOf<String>()
    val propertyTypes: List<String> get() = _propertyTypes

    private val _roomTypes = mutableStateListOf<String>()
    val roomTypes: List<String> get() = _roomTypes

    private val _rooms = mutableStateListOf<Room>()
    val rooms: List<Room> get() = _rooms

    private val _properties = mutableStateListOf<Property>()
    val properties: List<Property> get() = _properties

    private val _roomCategories = mutableStateListOf<String>()
    val roomCategories: List<String> get() = _roomCategories

    private val _tenants = mutableStateListOf<Tenant>()
    val tenants: List<Tenant> get() = _tenants

    // Add this debug function
    fun debugPrintProperties() {
        println("Total properties: ${_properties.size}")
        _properties.forEach { property ->
            println("Property: ${property.name} (ID: ${property.propertyId})")
        }
    }

    init {
        // Initialize with default property types
        _propertyTypes.addAll(
            listOf(
                "Apartment",
                "House",
                "Villa",
                "Commercial",
                "Office",
                "Warehouse",
                "Other"
            )
        )
        
        // Initialize with default room types
        _roomTypes.addAll(
            listOf(
                "Single Room",
                "Double Room",
                "Studio",
                "En-suite",
                "Master Room",
                "Other"
            )
        )
        
        _amenities.addAll(sampleAmenities)
        
        // Initialize default room categories
        _roomCategories.addAll(
            listOf(
                "Wing A",
                "Wing B",
                "Block 1",
                "Block 2",
                "Main Building",
                "Annex"
            )
        )

        // Add sample properties
        _properties.addAll(
            listOf(
                Property(
                    propertyId = "prop_1",
                    ownerId = "owner_1",
                    name = "Sunshine Apartments",
                    type = "Apartment",
                    address = "123 Sun Street",
                    totalRooms = 10,
                    vacantRooms = 10,
                    amenities = listOf("WiFi", "Parking", "Security"),
                    location = Location(
                        latitude = -1.2921,
                        longitude = 36.8219,
                        address = "123 Sun Street",
                        city = "Nairobi",
                        country = "Kenya"
                    ),
                    monthlyRent = 25000.0,
                    description = "Modern apartment complex in the heart of the city"
                ),
                Property(
                    propertyId = "prop_2",
                    ownerId = "owner_1",
                    name = "Green Valley Estate",
                    type = "House",
                    address = "456 Valley Road",
                    totalRooms = 15,
                    vacantRooms = 15,
                    amenities = listOf("Swimming Pool", "Gym", "Security"),
                    location = Location(
                        latitude = -1.2974,
                        longitude = 36.8115,
                        address = "456 Valley Road",
                        city = "Nairobi",
                        country = "Kenya"
                    ),
                    monthlyRent = 35000.0,
                    description = "Luxurious gated community with modern amenities"
                ),
                Property(
                    propertyId = "prop_3",
                    ownerId = "owner_2",
                    name = "Blue Waters Apartments",
                    type = "Apartment",
                    address = "789 Lake View",
                    totalRooms = 20,
                    vacantRooms = 20,
                    amenities = listOf("WiFi", "Parking", "CCTV"),
                    location = Location(
                        latitude = -1.3028,
                        longitude = 36.8062,
                        address = "789 Lake View",
                        city = "Nairobi",
                        country = "Kenya"
                    ),
                    monthlyRent = 30000.0,
                    description = "Waterfront apartments with scenic views"
                )
            )
        )
        
        // Debug print after adding properties
        debugPrintProperties()
    }

    fun addPropertyType(name: String) {
        if (!_propertyTypes.contains(name)) {
            _propertyTypes.add(name)
        }
    }

    fun updatePropertyType(oldName: String, newName: String) {
        val index = _propertyTypes.indexOf(oldName)
        if (index != -1) {
            _propertyTypes[index] = newName
        }
    }

    fun deletePropertyType(name: String) {
        _propertyTypes.remove(name)
    }

    fun addAmenity(amenity: Amenity) {
        _amenities.add(amenity)
    }

    fun updateAmenity(oldAmenity: Amenity, newAmenity: Amenity) {
        val index = _amenities.indexOf(oldAmenity)
        if (index != -1) {
            _amenities[index] = newAmenity
        }
    }

    fun deleteAmenity(amenity: Amenity) {
        _amenities.remove(amenity)
    }

    fun getRoomsForProperty(propertyId: String): List<Room> {
        return _rooms.filter { it.propertyId == propertyId }
    }

    fun addRoom(room: Room) {
        _rooms.add(room)
    }

    fun updateRoom(oldRoom: Room, newRoom: Room) {
        val index = _rooms.indexOf(oldRoom)
        if (index != -1) {
            _rooms[index] = newRoom
        }
    }

    fun deleteRoom(room: Room) {
        _rooms.remove(room)
    }

    fun addRoomType(name: String) {
        if (!_roomTypes.contains(name)) {
            _roomTypes.add(name)
        }
    }

    fun updateRoomType(oldName: String, newName: String) {
        val index = _roomTypes.indexOf(oldName)
        if (index != -1) {
            _roomTypes[index] = newName
        }
    }

    fun deleteRoomType(name: String) {
        _roomTypes.remove(name)
    }

    fun addProperty(property: Property) {
        _properties.add(property)
        println("Added property: ${property.name}, Total properties: ${_properties.size}")
        debugPrintProperties() // Print current properties list
    }

    fun updateProperty(oldProperty: Property, newProperty: Property) {
        val index = _properties.indexOf(oldProperty)
        if (index != -1) {
            _properties[index] = newProperty
            println("Updated property: ${newProperty.name}")
            debugPrintProperties()
        }
    }

    fun deleteProperty(property: Property) {
        _properties.remove(property)
        println("Deleted property: ${property.name}, Remaining properties: ${_properties.size}")
        debugPrintProperties()
    }

    fun getProperty(propertyId: String): Property? {
        return _properties.find { it.propertyId == propertyId }.also {
            println("Getting property with ID: $propertyId, Found: ${it?.name}")
        }
    }

    fun addRoomCategory(name: String) {
        if (!_roomCategories.contains(name)) {
            _roomCategories.add(name)
        }
    }

    fun updateRoomCategory(oldName: String, newName: String) {
        val index = _roomCategories.indexOf(oldName)
        if (index != -1) {
            _roomCategories[index] = newName
        }
    }

    fun deleteRoomCategory(name: String) {
        _roomCategories.remove(name)
    }

    // Helper functions for room management
    fun getPropertyOccupancy(propertyId: String): Pair<Int, Int> {
        val property = getProperty(propertyId) ?: return Pair(0, 0)
        val occupiedRooms = _rooms.count { 
            it.propertyId == propertyId && it.status == RoomStatus.OCCUPIED 
        }
        return Pair(occupiedRooms, property.totalRooms)
    }

    fun isRoomNumberAvailable(propertyId: String, roomNumber: String, currentRoomId: String? = null): Boolean {
        return _rooms.none { 
            it.propertyId == propertyId && 
            it.number == roomNumber && 
            it.roomId != currentRoomId 
        }
    }

    fun hasAvailableCapacity(propertyId: String): Boolean {
        val property = getProperty(propertyId) ?: return false
        val currentRooms = _rooms.count { it.propertyId == propertyId }
        return currentRooms < property.totalRooms
    }

    // Add this function to help debug
    fun getAllProperties(): List<Property> {
        println("Getting all properties, count: ${_properties.size}")
        _properties.forEach { property ->
            println("Property: ${property.name} (ID: ${property.propertyId})")
        }
        return properties
    }

    fun addTenant(tenant: Tenant) {
        _tenants.add(tenant)
    }

    fun updateTenant(oldTenant: Tenant, newTenant: Tenant) {
        val index = _tenants.indexOf(oldTenant)
        if (index != -1) {
            _tenants[index] = newTenant
        }
    }

    fun deleteTenant(tenant: Tenant) {
        _tenants.remove(tenant)
    }

    fun assignTenantToRoom(tenantId: String, roomId: String) {
        val tenant = _tenants.find { it.tenantId == tenantId }
        if (tenant != null) {
            val updatedTenant = tenant.copy(roomId = roomId)
            updateTenant(tenant, updatedTenant)
        }
    }

    fun isRoomOccupied(roomId: String): Boolean {
        return tenants.any { it.roomId == roomId }
    }
} 