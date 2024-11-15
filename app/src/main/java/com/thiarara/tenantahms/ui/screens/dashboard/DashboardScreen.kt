package com.thiarara.tenantahms.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.thiarara.tenantahms.data.sample.sampleProperties
import com.thiarara.tenantahms.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    
    // Calculate stats
    val totalProperties = sampleProperties.size
    val totalRooms = sampleProperties.sumOf { it.totalRooms }
    val totalVacantRooms = sampleProperties.sumOf { it.vacantRooms }
    val occupiedRooms = totalRooms - totalVacantRooms
    val totalTenants = occupiedRooms // Assuming 1 tenant per occupied room

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Welcome Section
        Text(
            text = "Welcome Admin",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Manage your properties and tenants",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Quick Stats Cards
        Text(
            text = "Overview",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    StatCard(
                        title = "Properties",
                        value = totalProperties.toString(),
                        subtitle = "+2 this month",
                        icon = Icons.Default.Home,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    StatCard(
                        title = "Tenants",
                        value = totalTenants.toString(),
                        subtitle = "+5 new tenants",
                        icon = Icons.Default.Person,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    StatCard(
                        title = "Vacant Rooms",
                        value = totalVacantRooms.toString(),
                        subtitle = "Available for rent",
                        icon = Icons.Default.MeetingRoom,
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    StatCard(
                        title = "Total Rooms",
                        value = totalRooms.toString(),
                        subtitle = "$occupiedRooms occupied",
                        icon = Icons.Default.Apartment,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Management Section
        Text(
            text = "Management",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Property Management Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ManagementCard(
                        title = "Properties",
                        subtitle = "Manage properties",
                        icon = Icons.Default.Home,
                        onClick = { onNavigate(Screen.PropertyList.route) }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    ManagementCard(
                        title = "Property Types",
                        subtitle = "Configure types",
                        icon = Icons.Default.Category,
                        onClick = { onNavigate(Screen.PropertyTypes.route) }
                    )
                }
            }
            
            // Room Management Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ManagementCard(
                        title = "Rooms",
                        subtitle = "Manage rooms",
                        icon = Icons.Default.MeetingRoom,
                        onClick = { onNavigate(Screen.Rooms.route) }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    ManagementCard(
                        title = "Amenities",
                        subtitle = "Manage amenities",
                        icon = Icons.Default.List,
                        onClick = { onNavigate(Screen.Amenities.route) }
                    )
                }
            }
            
            // User Management Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ManagementCard(
                        title = "Users",
                        subtitle = "Manage users",
                        icon = Icons.Default.Group,
                        onClick = { onNavigate(Screen.Users.route) }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    ManagementCard(
                        title = "Tenants",
                        subtitle = "Manage tenants",
                        icon = Icons.Default.Person,
                        onClick = { onNavigate(Screen.Tenants.route) }
                    )
                }
            }
            
            // Financial & Issues Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ManagementCard(
                        title = "Payments",
                        subtitle = "Track payments",
                        icon = Icons.Default.Payment,
                        onClick = { onNavigate(Screen.Payments.route) }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    ManagementCard(
                        title = "Complaints",
                        subtitle = "Handle issues",
                        icon = Icons.Default.Report,
                        onClick = { onNavigate(Screen.Complaints.route) }
                    )
                }
            }
        }
        
        // Reports Section
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Reports",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ManagementCard(
                    title = "Financial Reports",
                    subtitle = "View statements",
                    icon = Icons.Default.Assessment,
                    onClick = { onNavigate(Screen.FinancialReports.route) }
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                ManagementCard(
                    title = "Occupancy Reports",
                    subtitle = "View statistics",
                    icon = Icons.Default.PieChart,
                    onClick = { onNavigate(Screen.OccupancyReports.route) }
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManagementCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 