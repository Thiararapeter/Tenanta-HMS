package com.thiarara.tenantahms.data

import androidx.compose.runtime.mutableStateListOf
import com.thiarara.tenantahms.data.model.Amenity
import com.thiarara.tenantahms.data.model.PropertyType
import com.thiarara.tenantahms.data.sample.sampleAmenities

object PropertyDataManager {
    private val _amenities = mutableStateListOf<Amenity>()
    val amenities: List<Amenity> get() = _amenities

    private val _propertyTypes = mutableStateListOf<String>()
    val propertyTypes: List<String> get() = _propertyTypes

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
} 