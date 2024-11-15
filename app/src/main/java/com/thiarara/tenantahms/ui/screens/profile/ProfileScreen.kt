package com.thiarara.tenantahms.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // User Name
                Text(
                    text = "John Doe",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                // Role
                Text(
                    text = "Administrator",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Quick Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStat(
                        icon = Icons.Default.Business,
                        value = "5",
                        label = "Properties"
                    )
                    ProfileStat(
                        icon = Icons.Default.Group,
                        value = "23",
                        label = "Tenants"
                    )
                    ProfileStat(
                        icon = Icons.Default.Star,
                        value = "4.8",
                        label = "Rating"
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Personal Information Section
        Text(
            text = "Personal Information",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        ProfileInfoCard(
            items = listOf(
                ProfileInfo("Email", "john.doe@example.com", Icons.Default.Email),
                ProfileInfo("Phone", "+254 712 345 678", Icons.Default.Phone),
                ProfileInfo("Location", "Nairobi, Kenya", Icons.Default.LocationOn),
                ProfileInfo("Joined", "January 2024", Icons.Default.CalendarToday)
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Account Settings
        Text(
            text = "Account Settings",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingsButton(
                title = "Edit Profile",
                icon = Icons.Default.Edit,
                onClick = { /* TODO */ }
            )
            SettingsButton(
                title = "Change Password",
                icon = Icons.Default.Lock,
                onClick = { /* TODO */ }
            )
            SettingsButton(
                title = "Notifications",
                icon = Icons.Default.Notifications,
                onClick = { /* TODO */ }
            )
            SettingsButton(
                title = "Privacy Settings",
                icon = Icons.Default.Security,
                onClick = { /* TODO */ }
            )
            SettingsButton(
                title = "Help & Support",
                icon = Icons.Default.Help,
                onClick = { /* TODO */ }
            )
            SettingsButton(
                title = "Logout",
                icon = Icons.Default.ExitToApp,
                onClick = { /* TODO */ },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            )
        }
    }
}

@Composable
private fun ProfileStat(
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

data class ProfileInfo(
    val label: String,
    val value: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileInfoCard(items: List<ProfileInfo>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items.forEach { info ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = info.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = info.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = info.value,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsButton(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
    )
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        onClick = onClick,
        colors = colors,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (title == "Logout") 
                        MaterialTheme.colorScheme.onErrorContainer 
                    else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (title == "Logout") 
                        MaterialTheme.colorScheme.onErrorContainer 
                    else MaterialTheme.colorScheme.onSurface
                )
            }
            if (title != "Logout") {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 