package com.thiarara.tenantahms.ui.screens.settings

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var darkTheme by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }
    var emailNotifications by remember { mutableStateOf(true) }
    var smsNotifications by remember { mutableStateOf(false) }
    
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Appearance Section
        SettingsSection(title = "Appearance") {
            SettingsSwitchItem(
                title = "Dark Theme",
                description = "Enable dark theme for the app",
                icon = Icons.Default.DarkMode,
                checked = darkTheme,
                onCheckedChange = { darkTheme = it }
            )
        }
        
        // Notifications Section
        SettingsSection(title = "Notifications") {
            SettingsSwitchItem(
                title = "Enable Notifications",
                description = "Receive push notifications",
                icon = Icons.Default.Notifications,
                checked = notifications,
                onCheckedChange = { notifications = it }
            )
            SettingsSwitchItem(
                title = "Email Notifications",
                description = "Receive email updates",
                icon = Icons.Default.Email,
                checked = emailNotifications,
                onCheckedChange = { emailNotifications = it }
            )
            SettingsSwitchItem(
                title = "SMS Notifications",
                description = "Receive SMS alerts",
                icon = Icons.Default.Sms,
                checked = smsNotifications,
                onCheckedChange = { smsNotifications = it }
            )
        }
        
        // System Settings
        SettingsSection(title = "System") {
            SettingsClickableItem(
                title = "Language",
                description = "English (US)",
                icon = Icons.Default.Language,
                onClick = { /* TODO */ }
            )
            SettingsClickableItem(
                title = "Data Backup",
                description = "Manage your data backup",
                icon = Icons.Default.Backup,
                onClick = { /* TODO */ }
            )
            SettingsClickableItem(
                title = "Security",
                description = "App security settings",
                icon = Icons.Default.Security,
                onClick = { /* TODO */ }
            )
        }
        
        // About Section
        SettingsSection(title = "About") {
            SettingsClickableItem(
                title = "App Version",
                description = "1.0.0",
                icon = Icons.Default.Info,
                onClick = { /* TODO */ }
            )
            SettingsClickableItem(
                title = "Terms of Service",
                description = "Read our terms",
                icon = Icons.Default.Description,
                onClick = { /* TODO */ }
            )
            SettingsClickableItem(
                title = "Privacy Policy",
                description = "Read our privacy policy",
                icon = Icons.Default.PrivacyTip,
                onClick = { /* TODO */ }
            )
        }
        
        // Support Section
        SettingsSection(title = "Support") {
            SettingsClickableItem(
                title = "Help Center",
                description = "Get help with the app",
                icon = Icons.Default.Help,
                onClick = { /* TODO */ }
            )
            SettingsClickableItem(
                title = "Contact Support",
                description = "Reach out to our team",
                icon = Icons.Default.SupportAgent,
                onClick = { /* TODO */ }
            )
            SettingsClickableItem(
                title = "Report a Bug",
                description = "Help us improve",
                icon = Icons.Default.BugReport,
                onClick = { /* TODO */ }
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                content()
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    description: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsClickableItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
} 