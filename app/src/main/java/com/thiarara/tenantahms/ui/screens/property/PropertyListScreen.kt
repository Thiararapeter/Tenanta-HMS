package com.thiarara.tenantahms.ui.screens.property

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.thiarara.tenantahms.data.PropertyDataManager
import com.thiarara.tenantahms.data.model.Property
import com.thiarara.tenantahms.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyListScreen(
    onPropertyClick: (String) -> Unit,
    onAddProperty: () -> Unit,
    navController: NavController
) {
    // Debug print at the start of the composable
    LaunchedEffect(Unit) {
        println("PropertyListScreen - Checking properties")
        PropertyDataManager.debugPrintProperties()
    }

    val properties by remember { derivedStateOf { PropertyDataManager.getAllProperties() } }
    var searchQuery by remember { mutableStateOf("") }
    var selectedProperty by remember { mutableStateOf<Property?>(null) }
    var showPropertyMenu by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Properties") },
                actions = {
                    IconButton(onClick = onAddProperty) {
                        Icon(Icons.Default.Add, "Add Property")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProperty) {
                Icon(Icons.Default.Add, "Add Property")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { },
                active = false,
                onActiveChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search properties...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            ) { }
            
            // Property List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(properties) { property ->
                    PropertyCard(
                        property = property,
                        onClick = { 
                            selectedProperty = property
                            showPropertyMenu = true
                        }
                    )
                }
            }
        }

        // Property Management Menu
        if (showPropertyMenu && selectedProperty != null) {
            AlertDialog(
                onDismissRequest = { 
                    showPropertyMenu = false
                    selectedProperty = null
                },
                title = { Text("Manage ${selectedProperty?.name}") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ListItem(
                            headlineContent = { Text("View Details") },
                            leadingContent = { Icon(Icons.Default.Info, null) },
                            modifier = Modifier.clickable { 
                                onPropertyClick(selectedProperty!!.propertyId)
                                showPropertyMenu = false
                                selectedProperty = null
                            }
                        )
                        ListItem(
                            headlineContent = { Text("Manage Rooms") },
                            leadingContent = { Icon(Icons.Default.MeetingRoom, null) },
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.RoomsList.route + "?propertyId=${selectedProperty!!.propertyId}")
                                showPropertyMenu = false
                                selectedProperty = null
                            }
                        )
                        ListItem(
                            headlineContent = { Text("View Tenants") },
                            leadingContent = { Icon(Icons.Default.Group, null) },
                            modifier = Modifier.clickable {
                                // TODO: Navigate to property tenants
                                showPropertyMenu = false
                                selectedProperty = null
                            }
                        )
                        ListItem(
                            headlineContent = { Text("Financial Reports") },
                            leadingContent = { Icon(Icons.Default.AttachMoney, null) },
                            modifier = Modifier.clickable {
                                // TODO: Navigate to property financial reports
                                showPropertyMenu = false
                                selectedProperty = null
                            }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { 
                        showPropertyMenu = false
                        selectedProperty = null
                    }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PropertyCard(
    property: Property,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = property.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = property.type,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "${property.vacantRooms} vacant",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = property.location.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Property Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PropertyDetail(
                    icon = Icons.Default.Home,
                    label = "${property.totalRooms} Rooms"
                )
                PropertyDetail(
                    icon = Icons.Default.AttachMoney,
                    label = "KES ${property.monthlyRent}"
                )
                PropertyDetail(
                    icon = Icons.Default.Star,
                    label = "${property.amenities.size} Amenities"
                )
            }
            
            if (property.amenities.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                
                // Amenities
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    property.amenities.take(3).forEach { amenity ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Text(
                                text = amenity,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    if (property.amenities.size > 3) {
                        Text(
                            text = "+${property.amenities.size - 3} more",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PropertyDetail(
    icon: ImageVector,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
} 