package com.thiarara.tenantahms.ui.screens.complaints

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.data.ComplaintManager
import com.thiarara.tenantahms.data.model.Complaint
import com.thiarara.tenantahms.ui.screens.complaints.components.ComplaintStatusChip
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintsScreen(
    onNavigateBack: () -> Unit,
    onComplaintClick: (String) -> Unit
) {
    val complaints by ComplaintManager.complaints.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Complaints") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, "Add Complaint")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(complaints) { complaint ->
                ComplaintCard(
                    complaint = complaint,
                    onClick = { onComplaintClick(complaint.complaintId) }
                )
            }
        }
    }

    if (showAddDialog) {
        AddEditComplaintDialog(
            onDismiss = { showAddDialog = false },
            onSave = { complaint ->
                ComplaintManager.addComplaint(complaint)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComplaintCard(
    complaint: Complaint,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
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
                Text(
                    text = complaint.title,
                    style = MaterialTheme.typography.titleMedium
                )
                ComplaintStatusChip(status = complaint.status)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = complaint.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Priority: ${complaint.priority}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = complaint.createdAt.format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    ),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
} 