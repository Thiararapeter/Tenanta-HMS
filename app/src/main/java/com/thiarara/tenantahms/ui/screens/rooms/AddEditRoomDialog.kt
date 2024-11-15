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
import com.thiarara.tenantahms.data.model.RoomType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRoomDialog(
    room: Room? = null,
    propertyId: String? = null,
    onDismiss: () -> Unit,
    onSave: (Room) -> Unit
) {
    var number by remember { mutableStateOf(room?.number ?: "") }
    var selectedType by remember { mutableStateOf(room?.type ?: RoomType.SINGLE) }
    var selectedStatus by remember { mutableStateOf(room?.status ?: RoomStatus.VACANT) }
    var monthlyRent by remember { mutableStateOf(room?.monthlyRent?.toString() ?: "") }
    var floor by remember { mutableStateOf(room?.floor?.toString() ?: "") }
    var description by remember { mutableStateOf(room?.description ?: "") }
    var selectedAmenities by remember { mutableStateOf(room?.amenities?.toSet() ?: setOf()) }
    
    var numberError by remember { mutableStateOf<String?>(null) }
    var rentError by remember { mutableStateOf<String?>(null) }
    var floorError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (room == null) "Add Room" else "Edit Room") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = number,
                    onValueChange = { 
                        number = it
                        numberError = validateRoomNumber(it)
                    },
                    label = { Text("Room Number") },
                    isError = numberError != null,
                    supportingText = numberError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )

                // Room Type Selection
                Text("Room Type")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(RoomType.values()) { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type.displayName()) }
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

                // Amenities Selection
                Text("Amenities")
                LazyColumn(
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(PropertyDataManager.amenities) { amenity ->
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
                    val errors = listOfNotNull(
                        validateRoomNumber(number),
                        validateRent(monthlyRent),
                        validateFloor(floor)
                    )
                    
                    if (errors.isEmpty()) {
                        onSave(
                            Room(
                                roomId = room?.roomId ?: "room_${System.currentTimeMillis()}",
                                propertyId = propertyId ?: room?.propertyId ?: "",
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
                        numberError = validateRoomNumber(number)
                        rentError = validateRent(monthlyRent)
                        floorError = validateFloor(floor)
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

private fun validateRoomNumber(number: String): String? {
    return when {
        number.isBlank() -> "Room number cannot be empty"
        number.length > 10 -> "Room number too long"
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