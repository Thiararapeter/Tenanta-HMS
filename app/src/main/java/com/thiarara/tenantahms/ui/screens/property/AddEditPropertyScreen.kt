package com.thiarara.tenantahms.ui.screens.property

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.data.model.Property
import com.thiarara.tenantahms.data.model.Location
import com.thiarara.tenantahms.data.sample.sampleProperties
import com.thiarara.tenantahms.data.sample.sampleAmenities
import com.thiarara.tenantahms.data.model.PropertyType
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextOverflow
import com.thiarara.tenantahms.data.PropertyDataManager

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AddEditPropertyScreen(
    propertyId: String? = null,
    onNavigateBack: () -> Unit,
    onSave: (Property) -> Unit,
    onDelete: ((String) -> Unit)? = null
) {
    val existingProperty = propertyId?.let { id ->
        sampleProperties.find { it.propertyId == id }
    }

    var name by remember { mutableStateOf(existingProperty?.name ?: "") }
    var address by remember { mutableStateOf(existingProperty?.address ?: "") }
    var city by remember { mutableStateOf(existingProperty?.location?.city ?: "") }
    var country by remember { mutableStateOf(existingProperty?.location?.country ?: "Kenya") }
    var totalRooms by remember { mutableStateOf(existingProperty?.totalRooms?.toString() ?: "") }
    var vacantRooms by remember { mutableStateOf(existingProperty?.vacantRooms?.toString() ?: "") }
    var monthlyRent by remember { mutableStateOf(existingProperty?.monthlyRent?.toString() ?: "") }
    var description by remember { mutableStateOf(existingProperty?.description ?: "") }
    var selectedAmenities by remember { mutableStateOf(existingProperty?.amenities?.toSet() ?: setOf()) }
    var selectedPropertyType by remember { 
        mutableStateOf(existingProperty?.type ?: PropertyDataManager.propertyTypes.first()) 
    }

    // Validation states
    var nameError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }
    var rentError by remember { mutableStateOf<String?>(null) }
    var roomsError by remember { mutableStateOf<String?>(null) }
    
    // Dialog states
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPreviewDialog by remember { mutableStateOf(false) }
    
    // Preview validation state
    var previewValidationErrors by remember { mutableStateOf<List<String>>(emptyList()) }
    
    val isEditMode = propertyId != null
    val scrollState = rememberScrollState()

    fun validatePreview(
        name: String,
        address: String,
        city: String,
        country: String,
        totalRooms: String,
        vacantRooms: String,
        monthlyRent: String,
        selectedAmenities: Set<String>
    ): List<String> {
        val errors = mutableListOf<String>()
        
        if (name.isBlank()) errors.add("Property name is required")
        if (name.length < 3) errors.add("Property name is too short")
        if (address.isBlank()) errors.add("Address is required")
        if (city.isBlank()) errors.add("City is required")
        if (country.isBlank()) errors.add("Country is required")
        
        totalRooms.toIntOrNull()?.let {
            if (it <= 0) errors.add("Total rooms must be greater than 0")
        } ?: errors.add("Invalid total rooms value")
        
        vacantRooms.toIntOrNull()?.let {
            if (it < 0) errors.add("Vacant rooms cannot be negative")
            if (totalRooms.toIntOrNull()?.let { total -> it > total } == true) {
                errors.add("Vacant rooms cannot exceed total rooms")
            }
        } ?: errors.add("Invalid vacant rooms value")
        
        monthlyRent.toDoubleOrNull()?.let {
            if (it <= 0) errors.add("Monthly rent must be greater than 0")
        } ?: errors.add("Invalid monthly rent value")
        
        if (selectedAmenities.isEmpty()) errors.add("At least one amenity should be selected")
        
        return errors
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Property" else "Add Property") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (isEditMode) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, "Delete Property")
                        }
                    }
                    IconButton(onClick = { showPreviewDialog = true }) {
                        Icon(Icons.Default.Preview, "Preview Property")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Basic Information Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Basic Information",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    // Property Type Selection
                    Text(
                        text = "Property Type",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(PropertyDataManager.propertyTypes) { type ->
                            FilterChip(
                                selected = selectedPropertyType == type,
                                onClick = { selectedPropertyType = type },
                                label = { Text(type) }
                            )
                        }
                    }
                    
                    OutlinedTextField(
                        value = name,
                        onValueChange = { 
                            name = it
                            nameError = validatePropertyName(it)
                        },
                        label = { Text("Property Name") },
                        isError = nameError != null,
                        supportingText = nameError?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            }
            
            // Location Information
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    OutlinedTextField(
                        value = address,
                        onValueChange = { 
                            address = it
                            addressError = validateAddress(it)
                        },
                        label = { Text("Address") },
                        isError = addressError != null,
                        supportingText = addressError?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = country,
                        onValueChange = { country = it },
                        label = { Text("Country") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Property Details
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Property Details",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    OutlinedTextField(
                        value = totalRooms,
                        onValueChange = { 
                            totalRooms = it
                            roomsError = validateRooms(it)
                        },
                        label = { Text("Total Rooms") },
                        isError = roomsError != null,
                        supportingText = roomsError?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = vacantRooms,
                        onValueChange = { vacantRooms = it },
                        label = { Text("Vacant Rooms") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
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
                }
            }
            
            // Amenities Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Amenities",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    // Group amenities by category
                    PropertyDataManager.amenities.groupBy { it.category }.forEach { (category, amenities) ->
                        Text(
                            text = category.displayName(),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        
                        amenities.forEach { amenity ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = getIconForName(amenity.icon),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(amenity.name)
                                }
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
            }
            
            // Save Button
            Button(
                onClick = {
                    // Validate all fields
                    val nameValidation = validatePropertyName(name)
                    val addressValidation = validateAddress(address)
                    val rentValidation = validateRent(monthlyRent)
                    val roomsValidation = validateRooms(totalRooms)
                    
                    if (nameValidation == null && addressValidation == null && 
                        rentValidation == null && roomsValidation == null) {
                        val property = Property(
                            propertyId = propertyId ?: "new_id",
                            ownerId = "current_owner_id",
                            name = name,
                            type = selectedPropertyType,
                            address = address,
                            totalRooms = totalRooms.toIntOrNull() ?: 0,
                            vacantRooms = vacantRooms.toIntOrNull() ?: 0,
                            amenities = selectedAmenities.toList(),
                            location = Location(
                                latitude = 0.0,
                                longitude = 0.0,
                                address = address,
                                city = city,
                                country = country
                            ),
                            monthlyRent = monthlyRent.toDoubleOrNull() ?: 0.0,
                            description = description
                        )
                        onSave(property)
                    } else {
                        nameError = nameValidation
                        addressError = addressValidation
                        rentError = rentValidation
                        roomsError = roomsValidation
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isEditMode) "Update Property" else "Add Property")
            }
        }

        // Delete Confirmation Dialog with Animation
        if (showDeleteDialog && propertyId != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Property") },
                text = { 
                    Text("Are you sure you want to delete this property? This action cannot be undone.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDelete?.invoke(propertyId)
                            showDeleteDialog = false
                            onNavigateBack()
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Preview Dialog with Validation
        if (showPreviewDialog) {
            AlertDialog(
                onDismissRequest = { showPreviewDialog = false },
                title = { Text("Property Preview") },
                text = {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Validation Summary
                        val errors = validatePreview(
                            name = name,
                            address = address,
                            city = city,
                            country = country,
                            totalRooms = totalRooms,
                            vacantRooms = vacantRooms,
                            monthlyRent = monthlyRent,
                            selectedAmenities = selectedAmenities
                        )
                        
                        if (errors.isNotEmpty()) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Validation Errors",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    errors.forEach { error ->
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.Error,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                            Text(
                                                text = error,
                                                color = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Preview Sections with Animation
                        PreviewSection("Basic Information") {
                            Text("Name: $name")
                            Text("Description: $description")
                        }
                        
                        PreviewSection("Location") {
                            Text("Address: $address")
                            Text("City: $city")
                            Text("Country: $country")
                        }
                        
                        PreviewSection("Property Details") {
                            Text("Total Rooms: $totalRooms")
                            Text("Vacant Rooms: $vacantRooms")
                            Text("Monthly Rent: KES $monthlyRent")
                        }
                        
                        PreviewSection("Amenities") {
                            if (selectedAmenities.isEmpty()) {
                                Text("No amenities selected")
                            } else {
                                selectedAmenities.forEach { amenity ->
                                    Text("• $amenity")
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = { showPreviewDialog = false }
                        ) {
                            Text("Close")
                        }
                        if (validatePreview(name, address, city, country, totalRooms, vacantRooms, monthlyRent, selectedAmenities).isEmpty()) {
                            Button(
                                onClick = {
                                    showPreviewDialog = false
                                    // Trigger save action
                                }
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun PreviewSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                content()
            }
        }
    }
}

private fun validatePropertyName(name: String): String? {
    return when {
        name.isBlank() -> "Name cannot be empty"
        name.length < 3 -> "Name must be at least 3 characters"
        name.length > 50 -> "Name must be less than 50 characters"
        else -> null
    }
}

private fun validateAddress(address: String): String? {
    return when {
        address.isBlank() -> "Address cannot be empty"
        address.length < 5 -> "Address must be at least 5 characters"
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

private fun validateRooms(rooms: String): String? {
    return when {
        rooms.isBlank() -> "Number of rooms cannot be empty"
        rooms.toIntOrNull() == null -> "Invalid number of rooms"
        rooms.toInt() <= 0 -> "Number of rooms must be greater than 0"
        else -> null
    }
}

private fun getIconForName(name: String) = when (name) {
    "wifi" -> Icons.Default.Wifi
    "parking" -> Icons.Default.LocalParking
    "security" -> Icons.Default.Security
    "water" -> Icons.Default.Water
    "electricity" -> Icons.Default.ElectricBolt
    "gym" -> Icons.Default.FitnessCenter
    "pool" -> Icons.Default.Pool
    "cctv" -> Icons.Default.Videocam
    "garbage" -> Icons.Default.Delete
    "elevator" -> Icons.Default.Elevator
    else -> Icons.Default.Star
} 