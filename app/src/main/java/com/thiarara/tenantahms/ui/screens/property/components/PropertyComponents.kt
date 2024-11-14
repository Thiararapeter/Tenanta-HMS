package com.thiarara.tenantahms.ui.screens.property.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.data.model.Property
import com.thiarara.tenantahms.data.model.Location

@Composable
fun PropertyOverviewCard(property: Property) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Total Rooms: ${property.totalRooms}")
            Text(text = "Vacant Rooms: ${property.vacantRooms}")
            Text(text = "Monthly Rent: KES ${property.monthlyRent}")
        }
    }
}

@Composable
fun LocationCard(location: Location) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = location.address)
            Text(text = "${location.city}, ${location.country}")
        }
    }
}

@Composable
fun AmenitiesList(amenities: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            amenities.forEach { amenity ->
                Text(text = "• $amenity")
            }
        }
    }
}

@Composable
fun RoomsOverviewCard(property: Property) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Rooms Status",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Total Rooms: ${property.totalRooms}")
            Text(text = "Occupied: ${property.totalRooms - property.vacantRooms}")
            Text(text = "Vacant: ${property.vacantRooms}")
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
} 