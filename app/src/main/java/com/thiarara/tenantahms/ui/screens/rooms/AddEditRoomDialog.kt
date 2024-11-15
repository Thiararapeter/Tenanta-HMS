package com.thiarara.tenantahms.ui.screens.rooms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.data.PropertyDataManager
import com.thiarara.tenantahms.data.model.Room
import com.thiarara.tenantahms.data.model.RoomStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRoomDialog(
    room: Room? = null,
    propertyId: String? = null,
    onDismiss: () -> Unit,
    onSave: (Room) -> Unit
) {
    // Debug print at the start of the composable
    LaunchedEffect(Unit) {
        println("AddEditRoomDialog - Checking properties")
        PropertyDataManager.debugPrintProperties()
    }

    // Get latest lists with derivedStateOf to observe changes
    val properties by remember { derivedStateOf { PropertyDataManager.getAllProperties() } }
    val amenities by remember { derivedStateOf { PropertyDataManager.amenities } }
    val roomTypes by remember { derivedStateOf { PropertyDataManager.roomTypes } }
    
    // Initialize selectedPropertyId with propertyId parameter or room's propertyId
    var selectedPropertyId by remember { 
        mutableStateOf(propertyId ?: room?.propertyId ?: properties.firstOrNull()?.propertyId ?: "") 
    }

    // Update selectedPropertyId if properties change and current selection is invalid
    LaunchedEffect(properties) {
        if (selectedPropertyId.isNotBlank() && !properties.any { it.propertyId == selectedPropertyId }) {
            selectedPropertyId = properties.firstOrNull()?.propertyId ?: ""
        }
    }

    // Property selection dropdown state
    var expanded by remember { mutableStateOf(false) }

    var number by remember { mutableStateOf(room?.number ?: "") }
    var selectedType by remember(roomTypes) { 
        mutableStateOf(room?.type ?: roomTypes.firstOrNull() ?: "") 
    }
    var selectedStatus by remember { mutableStateOf(room?.status ?: RoomStatus.VACANT) }
    var monthlyRent by remember { mutableStateOf(room?.monthlyRent?.toString() ?: "") }
    var floor by remember { mutableStateOf(room?.floor?.toString() ?: "") }
    var description by remember { mutableStateOf(room?.description ?: "") }
    var selectedAmenities by remember(amenities) { 
        mutableStateOf(room?.amenities?.toSet() ?: setOf()) 
    }
    
    var numberError by remember { mutableStateOf<String?>(null) }
    var propertyError by remember { mutableStateOf<String?>(null) }
    var rentError by remember { mutableStateOf<String?>(null) }
    var floorError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        println("Properties count: ${properties.size}")
        properties.forEach { property ->
            println("Property: ${property.name}")
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (room == null) "Add Room" else "Edit Room") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Property Selection (only show if not editing and no propertyId provided)
                if (room == null && propertyId == null) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        val currentProperty = properties.find { it.propertyId == selectedPropertyId }
                        OutlinedTextField(
                            value = currentProperty?.name ?: "Select Property",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Property") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            isError = propertyError != null,
                            supportingText = if (currentProperty != null) {
                                {
                                    val currentRooms = PropertyDataManager.getRoomsForProperty(selectedPropertyId).size
                                    Text("${currentRooms}/${currentProperty.totalRooms} rooms used")
                                }
                            } else propertyError?.let { { Text(it) } }
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            properties.forEach { property ->
                                val currentRooms = PropertyDataManager.getRoomsForProperty(property.propertyId).size
                                val isAtCapacity = currentRooms >= property.totalRooms
                                
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(property.name)
                                                if (isAtCapacity) {
                                                    Text(
                                                        "(Full)",
                                                        color = MaterialTheme.colorScheme.error,
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                            }
                                            Text(
                                                "${currentRooms}/${property.totalRooms} rooms",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    },
                                    onClick = {
                                        if (!isAtCapacity) {
                                            selectedPropertyId = property.propertyId
                                            expanded = false
                                            propertyError = null
                                        }
                                    },
                                    enabled = !isAtCapacity
                                )
                            }
                        }
                    }
                }

                // Room Number with validation
                OutlinedTextField(
                    value = number,
                    onValueChange = { 
                        number = it
                        numberError = validateRoomNumber(
                            number = it,
                            propertyId = selectedPropertyId,
                            currentRoomId = room?.roomId
                        )
                    },
                    label = { Text("Room Number") },
                    isError = numberError != null,
                    supportingText = {
                        val currentProperty = properties.find { it.propertyId == selectedPropertyId }
                        if (currentProperty != null) {
                            val currentRooms = PropertyDataManager.getRoomsForProperty(selectedPropertyId).size
                            Text("${currentRooms}/${currentProperty.totalRooms} rooms used")
                        } else if (numberError != null) {
                            Text(numberError!!)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Room Type Selection with latest room types
                Text("Room Type")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(roomTypes) { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type) }
                        )
                    }
                }

                // Room Status
                Text("Status")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(RoomStatus.values()) { status ->
                        FilterChip(
                            selected = selectedStatus == status,
                            onClick = { selectedStatus = status },
                            label = { Text(status.displayName()) }
                        )
                    }
                }

                OutlinedTextField(
                    value = monthlyRent,
                    onValueChange = { 
                        monthlyRent = it
                        rentError = validateRent(it)
                    },
                    label = { Text("Monthly Rent (KES)") },
                    isError = rentError != null,
                    supportingText = rentError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = floor,
                    onValueChange = { 
                        floor = it
                        floorError = validateFloor(it)
                    },
                    label = { Text("Floor Number") },
                    isError = floorError != null,
                    supportingText = floorError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                // Amenities Selection with latest amenities
                Text("Amenities")
                LazyColumn(
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(amenities) { amenity ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(amenity.name)
                            Checkbox(
                                checked = selectedAmenities.contains(amenity.name),
                                onCheckedChange = { checked ->
                                    selectedAmenities = if (checked) {
                                        selectedAmenities + amenity.name
                                    } else {
                                        selectedAmenities - amenity.name
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedProperty = properties.find { it.propertyId == selectedPropertyId }
                    val errors = listOfNotNull(
                        validateRoomNumber(number, selectedPropertyId, room?.roomId),
                        validateRent(monthlyRent),
                        validateFloor(floor),
                        when {
                            selectedPropertyId.isBlank() -> "Please select a property"
                            selectedProperty != null && 
                            PropertyDataManager.getRoomsForProperty(selectedPropertyId).size >= selectedProperty.totalRooms &&
                            room?.propertyId != selectedPropertyId -> "Property has reached maximum room capacity"
                            else -> null
                        }
                    )
                    
                    if (errors.isEmpty()) {
                        onSave(
                            Room(
                                roomId = room?.roomId ?: "room_${System.currentTimeMillis()}",
                                propertyId = selectedPropertyId,
                                number = number,
                                type = selectedType,
                                status = selectedStatus,
                                monthlyRent = monthlyRent.toDoubleOrNull() ?: 0.0,
                                floor = floor.toIntOrNull() ?: 0,
                                amenities = selectedAmenities.toList(),
                                description = description
                            )
                        )
                    } else {
                        numberError = validateRoomNumber(number, selectedPropertyId, room?.roomId)
                        rentError = validateRent(monthlyRent)
                        floorError = validateFloor(floor)
                        propertyError = if (selectedPropertyId.isBlank()) "Please select a property" else null
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun validateRoomNumber(number: String, propertyId: String, currentRoomId: String? = null): String? {
    return when {
        number.isBlank() -> "Room number cannot be empty"
        number.length > 10 -> "Room number too long"
        !PropertyDataManager.isRoomNumberAvailable(propertyId, number, currentRoomId) -> 
            "Room number already exists in this property"
        else -> null
    }
}

private fun validateRent(rent: String): String? {
    return when {
        rent.isBlank() -> "Rent cannot be empty"
        rent.toDoubleOrNull() == null -> "Invalid rent amount"
        rent.toDouble() <= 0 -> "Rent must be greater than 0"
        else -> null
    }
}

private fun validateFloor(floor: String): String? {
    return when {
        floor.isBlank() -> "Floor number cannot be empty"
        floor.toIntOrNull() == null -> "Invalid floor number"
        else -> null
    }
} 