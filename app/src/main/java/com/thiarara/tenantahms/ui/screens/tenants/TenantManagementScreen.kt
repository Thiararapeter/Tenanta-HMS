package com.thiarara.tenantahms.ui.screens.tenants

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
import com.thiarara.tenantahms.data.model.Tenant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantManagementScreen(
    onNavigateBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTenant by remember { mutableStateOf<Tenant?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf<Tenant?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tenants") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, "Add Tenant")
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
            items(PropertyDataManager.tenants) { tenant ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = tenant.name, style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Room: ${tenant.roomId ?: "Unassigned"}", style = MaterialTheme.typography.bodySmall)
                        }
                        Row {
                            IconButton(onClick = { editingTenant = tenant }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { showDeleteConfirmation = tenant }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }

    // Add/Edit Dialog
    if (showAddDialog || editingTenant != null) {
        TenantDialog(
            tenant = editingTenant,
            onDismiss = {
                showAddDialog = false
                editingTenant = null
            },
            onSave = { tenant ->
                if (editingTenant != null) {
                    PropertyDataManager.updateTenant(editingTenant!!, tenant)
                } else {
                    PropertyDataManager.addTenant(tenant)
                }
                showAddDialog = false
                editingTenant = null
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmation != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete Tenant") },
            text = { Text("Are you sure you want to delete ${showDeleteConfirmation?.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        PropertyDataManager.deleteTenant(showDeleteConfirmation!!)
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