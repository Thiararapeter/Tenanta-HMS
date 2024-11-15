package com.thiarara.tenantahms.ui.screens.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.data.UserManager
import com.thiarara.tenantahms.data.model.User
import com.thiarara.tenantahms.data.model.UserRole
import com.thiarara.tenantahms.data.RoleManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    onNavigateBack: () -> Unit,
    onNavigateToRoles: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf<User?>(null) }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredUsers = remember(UserManager.users, selectedRole, searchQuery) {
        UserManager.users.filter { user ->
            (selectedRole == null || user.role == selectedRole) &&
            (searchQuery.isEmpty() || user.fullName.contains(searchQuery, ignoreCase = true) ||
             user.email.contains(searchQuery, ignoreCase = true))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Management") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToRoles) {
                        Icon(Icons.Default.AdminPanelSettings, "Manage Roles")
                    }
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.PersonAdd, "Add User")
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
            // Search and Filter Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search users...") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        singleLine = true
                    )

                    // Role Filter Chips in multiple rows
                    RoleFilterChips(
                        selectedRole = selectedRole,
                        onRoleSelected = { selectedRole = it }
                    )
                }
            }

            // Add Roles Section
            RolesSection(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                onManageRoles = onNavigateToRoles
            )

            // Users List
            if (filteredUsers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Group,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "No users found",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Add users to get started",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredUsers) { user ->
                        UserListItem(
                            user = user,
                            onEdit = { selectedUser = user },
                            onDelete = { showDeleteConfirmation = user }
                        )
                    }
                }
            }
        }
    }

    // Add/Edit Dialog
    if (showAddDialog || selectedUser != null) {
        UserDialog(
            user = selectedUser,
            onDismiss = {
                showAddDialog = false
                selectedUser = null
            },
            onSave = { user ->
                if (selectedUser != null) {
                    UserManager.updateUser(user)
                } else {
                    UserManager.addUser(user)
                }
            }
        )
    }

    // Delete Confirmation Dialog
    showDeleteConfirmation?.let { user ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete User") },
            text = { Text("Are you sure you want to delete ${user.fullName}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        UserManager.deleteUser(user.userId)
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
private fun UserListItem(
    user: User,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onEdit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = user.role.name,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Delete")
                }
            }
        }
    }
}

@Composable
private fun RolesSection(
    modifier: Modifier = Modifier,
    onManageRoles: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Available Roles",
                    style = MaterialTheme.typography.titleMedium
                )
                TextButton(onClick = onManageRoles) {
                    Text("Manage Roles")
                }
            }
            
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                RoleManager.roles.forEach { role ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = role.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (role.isSystem) {
                            Text(
                                text = "System",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoleFilterChips(
    selectedRole: UserRole?,
    onRoleSelected: (UserRole?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // First row with "All" and first two roles
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilterChip(
                selected = selectedRole == null,
                onClick = { onRoleSelected(null) },
                label = { Text("All") }
            )
            FilterChip(
                selected = selectedRole == UserRole.SUPER_ADMIN,
                onClick = { onRoleSelected(UserRole.SUPER_ADMIN) },
                label = { Text("SUPER_ADMIN") }
            )
            FilterChip(
                selected = selectedRole == UserRole.PROPERTY_OWNER,
                onClick = { onRoleSelected(UserRole.PROPERTY_OWNER) },
                label = { Text("PROPERTY_OWNER") }
            )
        }
        
        // Second row with remaining roles
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilterChip(
                selected = selectedRole == UserRole.PROPERTY_MANAGER,
                onClick = { onRoleSelected(UserRole.PROPERTY_MANAGER) },
                label = { Text("PROPERTY_MANAGER") }
            )
            FilterChip(
                selected = selectedRole == UserRole.TENANT,
                onClick = { onRoleSelected(UserRole.TENANT) },
                label = { Text("TENANT") }
            )
        }
    }
} 