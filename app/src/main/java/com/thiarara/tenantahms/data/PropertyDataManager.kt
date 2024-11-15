package com.thiarara.tenantahms.data

import androidx.compose.runtime.mutableStateListOf
import com.thiarara.tenantahms.data.model.Amenity
import com.thiarara.tenantahms.data.model.PropertyType
import com.thiarara.tenantahms.data.model.Room
import com.thiarara.tenantahms.data.sample.sampleAmenities

object PropertyDataManager {
    private val _amenities = mutableStateListOf<Amenity>()
    val amenities: List<Amenity> get() = _amenities

    private val _propertyTypes = mutableStateListOf<String>()
    val propertyTypes: List<String> get() = _propertyTypes

    private val _roomTypes = mutableStateListOf<String>()
    val roomTypes: List<String> get() = _roomTypes

    private val _rooms = mutableStateListOf<Room>()
    val rooms: List<Room> get() = _rooms

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
} 