package com.thiarara.tenantahms.ui.screens.complaints

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.data.model.*
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditComplaintDialog(
    complaint: Complaint? = null,
    onDismiss: () -> Unit,
    onSave: (Complaint) -> Unit
) {
    var title by remember { mutableStateOf(complaint?.title ?: "") }
    var description by remember { mutableStateOf(complaint?.description ?: "") }
    var selectedCategory by remember { 
        mutableStateOf(complaint?.category ?: ComplaintCategory.MAINTENANCE) 
    }
    var selectedPriority by remember { 
        mutableStateOf(complaint?.priority ?: ComplaintPriority.MEDIUM) 
    }
    
    // Validation states
    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    
    // Dropdown states
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showPriorityDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (complaint == null) "New Complaint" else "Edit Complaint") },
        text = {
            Column(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title Field
                OutlinedTextField(
                    value = title,
                    onValueChange = { 
                        title = it
                        titleError = validateTitle(it)
                    },
                    label = { Text("Title") },
                    isError = titleError != null,
                    supportingText = titleError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )

                // Description Field
                OutlinedTextField(
                    value = description,
                    onValueChange = { 
                        description = it
                        descriptionError = validateDescription(it)
                    },
                    label = { Text("Description") },
                    isError = descriptionError != null,
                    supportingText = descriptionError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = showCategoryDropdown,
                    onExpandedChange = { showCategoryDropdown = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory.name.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryDropdown) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false }
                    ) {
                        ComplaintCategory.values().forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                onClick = {
                                    selectedCategory = category
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }

                // Priority Dropdown
                ExposedDropdownMenuBox(
                    expanded = showPriorityDropdown,
                    onExpandedChange = { showPriorityDropdown = it }
                ) {
                    OutlinedTextField(
                        value = selectedPriority.name.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Priority") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showPriorityDropdown) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = showPriorityDropdown,
                        onDismissRequest = { showPriorityDropdown = false }
                    ) {
                        ComplaintPriority.values().forEach { priority ->
                            DropdownMenuItem(
                                text = { Text(priority.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                onClick = {
                                    selectedPriority = priority
                                    showPriorityDropdown = false
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
                    if (validateInputs(title, description)) {
                        val newComplaint = complaint?.copy(
                            title = title,
                            description = description,
                            category = selectedCategory,
                            priority = selectedPriority,
                            updatedAt = LocalDateTime.now()
                        ) ?: Complaint(
                            complaintId = "complaint_${System.currentTimeMillis()}",
                            title = title,
                            description = description,
                            category = selectedCategory,
                            priority = selectedPriority,
                            status = ComplaintStatus.OPEN,
                            submittedBy = "current_user_id", // TODO: Get actual user ID
                            propertyId = "property_id", // TODO: Get actual property ID
                            roomId = null,
                            assignedTo = null,
                            createdAt = LocalDateTime.now(),
                            updatedAt = LocalDateTime.now(),
                            resolvedAt = null
                        )
                        onSave(newComplaint)
                        onDismiss()
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

private fun validateTitle(title: String): String? {
    return when {
        title.isBlank() -> "Title cannot be empty"
        title.length < 5 -> "Title too short"
        title.length > 100 -> "Title too long"
        else -> null
    }
}

private fun validateDescription(description: String): String? {
    return when {
        description.isBlank() -> "Description cannot be empty"
        description.length < 10 -> "Description too short"
        description.length > 500 -> "Description too long"
        else -> null
    }
}

private fun validateInputs(title: String, description: String): Boolean {
    return validateTitle(title) == null && validateDescription(description) == null
} 