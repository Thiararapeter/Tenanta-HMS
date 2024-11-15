package com.thiarara.tenantahms.ui.screens.complaints.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.thiarara.tenantahms.data.model.ComplaintStatus

@Composable
fun ComplaintStatusChip(status: ComplaintStatus) {
    val (color, text) = when (status) {
        ComplaintStatus.OPEN -> MaterialTheme.colorScheme.error to "Open"
        ComplaintStatus.IN_PROGRESS -> MaterialTheme.colorScheme.tertiary to "In Progress"
        ComplaintStatus.RESOLVED -> MaterialTheme.colorScheme.primary to "Resolved"
        ComplaintStatus.CLOSED -> MaterialTheme.colorScheme.secondary to "Closed"
    }
    
    SuggestionChip(
        onClick = { },
        label = { Text(text) },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = color.copy(alpha = 0.1f),
            labelColor = color
        )
    )
} 