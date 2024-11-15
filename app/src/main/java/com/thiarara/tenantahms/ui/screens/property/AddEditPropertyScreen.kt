package com.thiarara.tenantahms.ui.screens.property

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.thiarara.tenantahms.data.PropertyDataManager
import com.thiarara.tenantahms.data.model.Property
import com.thiarara.tenantahms.data.model.Location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPropertyScreen(
    propertyId: String? = null,
    onNavigateBack: () -> Unit,
    onSave: (Property) -> Unit
) {
    val property = propertyId?.let { PropertyDataManager.getProperty(it) }
    val isEditing = property != null

    // Initialize state with existing property values or defaults
    var name by remember { mutableStateOf(property?.name ?: "") }
    var type by remember { mutableStateOf(property?.type ?: "") }
    var address by remember { mutableStateOf(property?.address ?: "") }
    var city by remember { mutableStateOf(property?.location?.city ?: "") }
    var country by remember { mutableStateOf(property?.location?.country ?: "") }
    var totalRooms by remember { mutableStateOf(property?.totalRooms?.toString() ?: "") }
    var vacantRooms by remember { mutableStateOf(property?.vacantRooms?.toString() ?: "") }
    var monthlyRent by remember { mutableStateOf(property?.monthlyRent?.toString() ?: "") }
    var description by remember { mutableStateOf(property?.description ?: "") }
    var selectedAmenities by remember { mutableStateOf(property?.amenities?.toSet() ?: setOf()) }

    // Validation states
    var nameError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }
    var rentError by remember { mutableStateOf<String?>(null) }
    var roomsError by remember { mutableStateOf<String?>(null) }

    // Add expanded state for dropdown
    var typeExpanded by remember { mutableStateOf(false) }
    
    // Get property types list
    val propertyTypes = remember { PropertyDataManager.propertyTypes }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Property" else "Add Property") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Property Name
            OutlinedTextField(
                value = name,
                onValueChange = { 
                    name = it
                    nameError = null
                },
                label = { Text("Property Name") },
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            // Replace Property Type TextField with ExposedDropdownMenuBox
            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = it }
            ) {
                OutlinedTextField(
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Property Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false }
                ) {
                    propertyTypes.forEach { propertyType ->
                        DropdownMenuItem(
                            text = { Text(propertyType) },
                            onClick = {
                                type = propertyType
                                typeExpanded = false
                            }
                        )
                    }
                }
            }

            // Address
            OutlinedTextField(
                value = address,
                onValueChange = { 
                    address = it
                    addressError = null
                },
                label = { Text("Address") },
                isError = addressError != null,
                supportingText = addressError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            // City
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth()
            )

            // Country
            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("Country") },
                modifier = Modifier.fillMaxWidth()
            )

            // Total Rooms
            OutlinedTextField(
                value = totalRooms,
                onValueChange = { 
                    totalRooms = it
                    roomsError = null
                },
                label = { Text("Total Rooms") },
                isError = roomsError != null,
                supportingText = roomsError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Vacant Rooms
            OutlinedTextField(
                value = vacantRooms,
                onValueChange = { vacantRooms = it },
                label = { Text("Vacant Rooms") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Monthly Rent
            OutlinedTextField(
                value = monthlyRent,
                onValueChange = { 
                    monthlyRent = it
                    rentError = null
                },
                label = { Text("Monthly Rent (KES)") },
                isError = rentError != null,
                supportingText = rentError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Amenities
            Text(
                text = "Amenities",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(PropertyDataManager.amenities) { amenity ->
                    FilterChip(
                        selected = selectedAmenities.contains(amenity.name),
                        onClick = {
                            selectedAmenities = if (selectedAmenities.contains(amenity.name)) {
                                selectedAmenities - amenity.name
                            } else {
                                selectedAmenities + amenity.name
                            }
                        },
                        label = { Text(amenity.name) }
                    )
                }
            }

            // Save Button
            Button(
                onClick = {
                    val nameValidation = validateName(name)
                    val addressValidation = validateAddress(address)
                    val rentValidation = validateRent(monthlyRent)
                    val roomsValidation = validateRooms(totalRooms, vacantRooms)

                    if (nameValidation == null && addressValidation == null && 
                        rentValidation == null && roomsValidation == null) {
                        val newProperty = Property(
                            propertyId = propertyId ?: "property_${System.currentTimeMillis()}",
                            ownerId = property?.ownerId ?: "owner_1", // TODO: Get from auth
                            name = name,
                            type = type,
                            address = address,
                            totalRooms = totalRooms.toIntOrNull() ?: 0,
                            vacantRooms = vacantRooms.toIntOrNull() ?: 0,
                            amenities = selectedAmenities.toList(),
                            location = Location(
                                latitude = property?.location?.latitude ?: 0.0,
                                longitude = property?.location?.longitude ?: 0.0,
                                address = address,
                                city = city,
                                country = country
                            ),
                            monthlyRent = monthlyRent.toDoubleOrNull() ?: 0.0,
                            description = description
                        )
                        onSave(newProperty)
                    } else {
                        nameError = nameValidation
                        addressError = addressValidation
                        rentError = rentValidation
                        roomsError = roomsValidation
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Update Property" else "Add Property")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun validateName(name: String): String? {
    return when {
        name.isBlank() -> "Name cannot be empty"
        name.length < 3 -> "Name too short"
        name.length > 50 -> "Name too long"
        else -> null
    }
}

private fun validateAddress(address: String): String? {
    return when {
        address.isBlank() -> "Address cannot be empty"
        address.length < 5 -> "Address too short"
        address.length > 100 -> "Address too long"
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

private fun validateRooms(total: String, vacant: String): String? {
    val totalRooms = total.toIntOrNull()
    val vacantRooms = vacant.toIntOrNull()
    return when {
        totalRooms == null -> "Invalid total rooms"
        totalRooms <= 0 -> "Total rooms must be greater than 0"
        vacantRooms == null -> "Invalid vacant rooms"
        vacantRooms < 0 -> "Vacant rooms cannot be negative"
        vacantRooms > totalRooms -> "Vacant rooms cannot exceed total rooms"
        else -> null
    }
} 