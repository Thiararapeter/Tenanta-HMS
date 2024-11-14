package com.thiarara.tenantahms.ui.screens.amenities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.data.model.Amenity
import com.thiarara.tenantahms.data.model.AmenityCategory
import com.thiarara.tenantahms.data.PropertyDataManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmenitiesScreen(
    onNavigateBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<AmenityCategory?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf<Amenity?>(null) }
    var editingAmenity by remember { mutableStateOf<Amenity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Amenities") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, "Add Amenity")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Category Filter
            ScrollableTabRow(
                selectedTabIndex = if (selectedCategory == null) 0 
                    else AmenityCategory.values().indexOf(selectedCategory) + 1,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    text = { Text("All") }
                )
                AmenityCategory.values().forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = { Text(category.displayName()) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filteredAmenities = if (selectedCategory != null) {
                    PropertyDataManager.amenities.filter { it.category == selectedCategory }
                } else PropertyDataManager.amenities

                items(filteredAmenities) { amenity ->
                    AmenityItem(
                        amenity = amenity,
                        onEdit = { editingAmenity = amenity },
                        onDelete = { showDeleteConfirmation = amenity }
                    )
                }
            }
        }
    }

    // Add/Edit Dialog
    if (showAddDialog || editingAmenity != null) {
        AddEditAmenityDialog(
            amenity = editingAmenity,
            onDismiss = { 
                showAddDialog = false
                editingAmenity = null
            },
            onSave = { name, icon, category ->
                val newAmenity = Amenity(name, icon, category)
                if (editingAmenity != null) {
                    PropertyDataManager.updateAmenity(editingAmenity!!, newAmenity)
                } else {
                    PropertyDataManager.addAmenity(newAmenity)
                }
                showAddDialog = false
                editingAmenity = null
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmation != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete Amenity") },
            text = { Text("Are you sure you want to delete ${showDeleteConfirmation?.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        PropertyDataManager.deleteAmenity(showDeleteConfirmation!!)
                        showDeleteConfirmation = null
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AmenityItem(
    amenity: Amenity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = getIconForName(amenity.icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = amenity.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditAmenityDialog(
    amenity: Amenity?,
    onDismiss: () -> Unit,
    onSave: (String, String, AmenityCategory) -> Unit
) {
    var name by remember { mutableStateOf(amenity?.name ?: "") }
    var selectedIcon by remember { mutableStateOf(amenity?.icon ?: availableIcons.first()) }
    var selectedCategory by remember { mutableStateOf(amenity?.category ?: AmenityCategory.OTHER) }
    var nameError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (amenity == null) "Add Amenity" else "Edit Amenity") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        nameError = validateAmenityName(it)
                    },
                    label = { Text("Amenity Name") },
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Category Selection
                Text("Category")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(AmenityCategory.values()) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.displayName()) }
                        )
                    }
                }
                
                // Icon Selection
                Text("Select Icon")
                LazyColumn(
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableIcons) { icon ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedIcon == icon,
                                onClick = { selectedIcon = icon }
                            )
                            Icon(
                                imageVector = getIconForName(icon),
                                contentDescription = null
                            )
                            Text(icon)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val error = validateAmenityName(name)
                    if (error == null) {
                        onSave(name, selectedIcon, selectedCategory)
                    } else {
                        nameError = error
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

private fun validateAmenityName(name: String): String? {
    return when {
        name.isBlank() -> "Name cannot be empty"
        name.length < 3 -> "Name must be at least 3 characters"
        name.length > 50 -> "Name must be less than 50 characters"
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

private val availableIcons = listOf(
    "wifi",
    "parking",
    "security",
    "water",
    "electricity",
    "gym",
    "pool",
    "cctv",
    "garbage",
    "elevator"
) 