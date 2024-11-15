package com.thiarara.tenantahms.ui.screens.property

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
import com.thiarara.tenantahms.data.PropertyDataManager
import com.thiarara.tenantahms.data.model.Property
import com.thiarara.tenantahms.ui.screens.property.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyDetailScreen(
    propertyId: String,
    onNavigateBack: () -> Unit,
    onEditProperty: (String) -> Unit,
    onDeleteProperty: (Property) -> Unit,
    onManageRooms: (String) -> Unit,
    onViewTenants: (String) -> Unit
) {
    val property = PropertyDataManager.getProperty(propertyId)
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (property == null) {
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(property.name) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditProperty(propertyId) }) {
                        Icon(Icons.Default.Edit, "Edit Property")
                    }
                    IconButton(onClick = { showDeleteConfirmation = true }) {
                        Icon(Icons.Default.Delete, "Delete Property")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp)
        ) {
            // Property Overview Card
            PropertyOverviewCard(property)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Location Section
            SectionTitle("Location")
            LocationCard(property.location)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Amenities Section
            SectionTitle("Amenities")
            AmenitiesList(property.amenities)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Rooms Section
            SectionTitle("Rooms Overview")
            RoomsOverviewCard(property)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Management Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { onManageRooms(propertyId) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.MeetingRoom, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Manage Rooms")
                }
                
                Button(
                    onClick = { onViewTenants(propertyId) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.Group, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Tenants")
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Property") },
            text = { 
                Text("Are you sure you want to delete ${property.name}? This action cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteProperty(property)
                        showDeleteConfirmation = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Add other composables (PropertyOverviewCard, LocationCard, etc.)... 