package com.thiarara.tenantahms.ui.screens.rooms

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.ui.components.ManagementCard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.MeetingRoom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsManagementScreen(
    onNavigateBack: () -> Unit,
    onNavigateToRooms: () -> Unit,
    onNavigateToRoomTypes: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Room Management") },
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
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Rooms Management Card
            ManagementCard(
                title = "Rooms",
                subtitle = "View and manage all rooms",
                icon = Icons.Default.MeetingRoom,
                onClick = onNavigateToRooms
            )

            // Room Types Management Card
            ManagementCard(
                title = "Room Types",
                subtitle = "Configure room types",
                icon = Icons.Default.Category,
                onClick = onNavigateToRoomTypes
            )
        }
    }
} 