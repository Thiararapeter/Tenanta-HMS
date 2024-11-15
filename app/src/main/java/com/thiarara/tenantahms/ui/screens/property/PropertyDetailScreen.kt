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
import com.thiarara.tenantahms.data.sample.sampleProperties
import com.thiarara.tenantahms.ui.screens.property.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyDetailScreen(
    propertyId: String,
    onNavigateBack: () -> Unit,
    onEditProperty: (String) -> Unit
) {
    val property = sampleProperties.first { it.propertyId == propertyId }
    val scrollState = rememberScrollState()

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
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
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
            
            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { /* TODO: Navigate to Rooms Management */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.MeetingRoom, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Manage Rooms")
                }
                
                Button(
                    onClick = { /* TODO: Navigate to Tenants List */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Group, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Tenants")
                }
            }
        }
    }
}

// Add other composables (PropertyOverviewCard, LocationCard, etc.)... 