package com.thiarara.tenantahms.ui.screens.rooms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.data.PropertyDataManager
import com.thiarara.tenantahms.data.model.Room
import com.thiarara.tenantahms.data.model.RoomStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsScreen(
    propertyId: String? = null,
    onNavigateBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingRoom by remember { mutableStateOf<Room?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf<Room?>(null) }
    var selectedStatus by remember { mutableStateOf<RoomStatus?>(null) }

    val rooms = if (propertyId != null) {
        PropertyDataManager.getRoomsForProperty(propertyId)
    } else {
        PropertyDataManager.rooms
    }

    val filteredRooms = if (selectedStatus != null) {
        rooms.filter { it.status == selectedStatus }
    } else rooms

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (propertyId != null) "Property Rooms" else "All Rooms") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, "Add Room")
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
            // Status Filter
            ScrollableTabRow(
                selectedTabIndex = if (selectedStatus == null) 0 
                    else RoomStatus.values().indexOf(selectedStatus) + 1,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    text = { Text("All") }
                )
                RoomStatus.values().forEach { status ->
                    Tab(
                        selected = selectedStatus == status,
                        onClick = { selectedStatus = status },
                        text = { Text(status.displayName()) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredRooms) { room ->
                    RoomItem(
                        room = room,
                        onEdit = { editingRoom = room },
                        onDelete = { showDeleteConfirmation = room }
                    )
                }
            }
        }
    }

    // Add/Edit Dialog
    if (showAddDialog || editingRoom != null) {
        AddEditRoomDialog(
            room = editingRoom,
            propertyId = propertyId,
            onDismiss = { 
                showAddDialog = false
                editingRoom = null
            },
            onSave = { newRoom ->
                if (editingRoom != null) {
                    PropertyDataManager.updateRoom(editingRoom!!, newRoom)
                } else {
                    PropertyDataManager.addRoom(newRoom)
                }
                showAddDialog = false
                editingRoom = null
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmation != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete Room") },
            text = { Text("Are you sure you want to delete Room ${showDeleteConfirmation?.number}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        PropertyDataManager.deleteRoom(showDeleteConfirmation!!)
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
private fun RoomItem(
    room: Room,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Room ${room.number}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = room.type,
                        style = MaterialTheme.typography.bodyMedium,
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
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (room.status) {
                            RoomStatus.VACANT -> MaterialTheme.colorScheme.primaryContainer
                            RoomStatus.OCCUPIED -> MaterialTheme.colorScheme.tertiaryContainer
                            RoomStatus.MAINTENANCE -> MaterialTheme.colorScheme.errorContainer
                            RoomStatus.RESERVED -> MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                ) {
                    Text(
                        text = room.status.displayName(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(
                    text = "KES ${room.monthlyRent}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
} 