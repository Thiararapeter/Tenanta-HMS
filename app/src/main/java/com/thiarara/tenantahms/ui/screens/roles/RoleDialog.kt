package com.thiarara.tenantahms.ui.screens.roles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.data.model.Role
import com.thiarara.tenantahms.data.model.UserPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleDialog(
    role: Role? = null,
    onDismiss: () -> Unit,
    onSave: (Role) -> Unit
) {
    var name by remember { mutableStateOf(role?.name ?: "") }
    var description by remember { mutableStateOf(role?.description ?: "") }
    var selectedPermissions by remember { 
        mutableStateOf(role?.permissions ?: emptySet()) 
    }
    
    // Validation states
    var nameError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }

    // Group permissions by category
    val permissionGroups = remember {
        mapOf(
            "Property Management" to listOf(
                UserPermission.MANAGE_PROPERTIES,
                UserPermission.VIEW_REPORTS,
                UserPermission.MANAGE_MANAGERS
            ),
            "Tenant Management" to listOf(
                UserPermission.MANAGE_TENANTS,
                UserPermission.VIEW_TENANTS,
                UserPermission.VIEW_LEASE
            ),
            "Maintenance" to listOf(
                UserPermission.HANDLE_MAINTENANCE,
                UserPermission.SUBMIT_MAINTENANCE
            ),
            "Financial" to listOf(
                UserPermission.MANAGE_PAYMENTS,
                UserPermission.VIEW_PAYMENTS
            )
        )
    }

    fun validateInputs(): Boolean {
        var isValid = true
        
        if (name.isBlank()) {
            nameError = "Role name is required"
            isValid = false
        } else {
            nameError = null
        }

        if (description.isBlank()) {
            descriptionError = "Description is required"
            isValid = false
        } else {
            descriptionError = null
        }

        return isValid
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (role == null) "Add Role" else "Edit Role") },
        text = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        nameError = null
                    },
                    label = { Text("Role Name") },
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { 
                        description = it
                        descriptionError = null
                    },
                    label = { Text("Description") },
                    isError = descriptionError != null,
                    supportingText = descriptionError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Permissions Section with Categories
                Text(
                    text = "Permissions",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                permissionGroups.forEach { (category, permissions) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            permissions.forEach { permission ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = permission.name
                                            .replace("_", " ")
                                            .lowercase()
                                            .replaceFirstChar { it.uppercase() },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 8.dp)
                                    )
                                    Checkbox(
                                        checked = permission in selectedPermissions,
                                        onCheckedChange = { checked ->
                                            selectedPermissions = if (checked) {
                                                selectedPermissions + permission
                                            } else {
                                                selectedPermissions - permission
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Quick select buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = { 
                            selectedPermissions = UserPermission.values().toSet()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Select All")
                    }
                    TextButton(
                        onClick = { selectedPermissions = emptySet() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear All")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (validateInputs()) {
                        val newRole = Role(
                            id = role?.id ?: "role_${System.currentTimeMillis()}",
                            name = name,
                            description = description,
                            permissions = selectedPermissions,
                            isSystem = false
                        )
                        onSave(newRole)
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