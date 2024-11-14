package com.thiarara.tenantahms.ui.screens.property

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.data.PropertyDataManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyTypesScreen(
    onNavigateBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingType by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Property Types") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, "Add Property Type")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(PropertyDataManager.propertyTypes) { type ->
                PropertyTypeItem(
                    type = type,
                    onEdit = { editingType = type },
                    onDelete = { showDeleteConfirmation = type }
                )
            }
        }
    }

    // Add/Edit Dialog
    if (showAddDialog || editingType != null) {
        PropertyTypeDialog(
            propertyType = editingType,
            onDismiss = {
                showAddDialog = false
                editingType = null
            },
            onSave = { name ->
                if (editingType != null) {
                    PropertyDataManager.updatePropertyType(editingType!!, name)
                } else {
                    PropertyDataManager.addPropertyType(name)
                }
                showAddDialog = false
                editingType = null
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmation != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete Property Type") },
            text = {
                Text("Are you sure you want to delete $showDeleteConfirmation? " +
                     "This will affect all properties using this type.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        PropertyDataManager.deletePropertyType(showDeleteConfirmation!!)
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
private fun PropertyTypeItem(
    type: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = type,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Used in X properties", // TODO: Add actual count
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun PropertyTypeDialog(
    propertyType: String? = null,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var name by remember { mutableStateOf(propertyType ?: "") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (propertyType == null) "Add Property Type" else "Edit Property Type") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        error = validatePropertyTypeName(it)
                    },
                    label = { Text("Type Name") },
                    isError = error != null,
                    supportingText = error?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (error == null && name.isNotBlank()) {
                        onSave(name)
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

private fun validatePropertyTypeName(name: String): String? {
    return when {
        name.isBlank() -> "Name cannot be empty"
        name.length < 3 -> "Name must be at least 3 characters"
        name.length > 30 -> "Name must be less than 30 characters"
        else -> null
    }
} 