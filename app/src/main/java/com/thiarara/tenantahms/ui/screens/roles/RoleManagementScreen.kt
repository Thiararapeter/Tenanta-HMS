package com.thiarara.tenantahms.ui.screens.roles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.data.RoleManager
import com.thiarara.tenantahms.data.model.Role
import com.thiarara.tenantahms.data.model.SystemRoles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleManagementScreen(
    onNavigateBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf<Role?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf<Role?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Role Management") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, "Add Role")
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
            items(RoleManager.roles) { role ->
                RoleCard(
                    role = role,
                    onEdit = { 
                        if (role.id != SystemRoles.SUPER_ADMIN.id) {
                            selectedRole = role 
                        }
                    },
                    onDelete = { 
                        if (!role.isSystem && role.id != SystemRoles.SUPER_ADMIN.id) {
                            showDeleteConfirmation = role 
                        }
                    }
                )
            }
        }
    }

    // Add/Edit Dialog
    if (showAddDialog || selectedRole != null) {
        RoleDialog(
            role = selectedRole,
            onDismiss = {
                showAddDialog = false
                selectedRole = null
            },
            onSave = { role ->
                if (selectedRole != null) {
                    RoleManager.updateRole(role.copy(isSystem = selectedRole!!.isSystem))
                } else {
                    RoleManager.addRole(role)
                }
            }
        )
    }

    // Delete Confirmation
    showDeleteConfirmation?.let { role ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete Role") },
            text = { Text("Are you sure you want to delete the role '${role.name}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        RoleManager.deleteRole(role.id)
                        showDeleteConfirmation = null
                    }
                ) {
                    Text("Delete")
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
private fun RoleCard(
    role: Role,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val isSuperAdmin = role.id == SystemRoles.SUPER_ADMIN.id

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = if (!isSuperAdmin) onEdit else { {} }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = role.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = role.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (role.isSystem) {
                        Text(
                            text = if (isSuperAdmin) "Super Admin (Protected)" else "System Role",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSuperAdmin) 
                                MaterialTheme.colorScheme.error 
                            else 
                                MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Row {
                    // Only show edit button if not Super Admin
                    if (!isSuperAdmin) {
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, "Edit")
                        }
                        // Only show delete for non-system, non-Super Admin roles
                        if (!role.isSystem) {
                            IconButton(onClick = onDelete) {
                                Icon(Icons.Default.Delete, "Delete")
                            }
                        }
                    }
                }
            }

            // Show permissions
            Text(
                text = "Permissions:",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = if (isSuperAdmin) 
                    "Full System Access" 
                else 
                    role.permissions.joinToString(", ") { 
                        it.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
                    },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
} 