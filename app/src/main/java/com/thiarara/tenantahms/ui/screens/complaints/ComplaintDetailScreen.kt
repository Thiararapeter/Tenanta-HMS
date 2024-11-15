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
import com.thiarara.tenantahms.data.model.ComplaintComment
import com.thiarara.tenantahms.ui.screens.complaints.components.ComplaintStatusChip
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintDetailScreen(
    complaintId: String,
    onNavigateBack: () -> Unit
) {
    val complaint = ComplaintManager.getComplaint(complaintId)
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var newComment by remember { mutableStateOf("") }
    
    if (complaint == null) {
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Complaint Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, "Edit")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, "Delete")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ComplaintHeader(complaint)
            }
            
            item {
                ComplaintDetails(complaint)
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Comments",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newComment,
                            onValueChange = { newComment = it },
                            label = { Text("Add a comment") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (newComment.isNotBlank()) {
                                    val comment = ComplaintComment(
                                        commentId = "comment_${System.currentTimeMillis()}",
                                        complaintId = complaintId,
                                        userId = "current_user_id", // TODO: Get actual user ID
                                        message = newComment,
                                        timestamp = LocalDateTime.now()
                                    )
                                    ComplaintManager.addComment(complaintId, comment)
                                    newComment = ""
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Add Comment")
                        }
                    }
                }
            }
            
            items(complaint.comments) { comment ->
                CommentCard(comment)
            }
        }
    }

    if (showEditDialog) {
        AddEditComplaintDialog(
            complaint = complaint,
            onDismiss = { showEditDialog = false },
            onSave = { updatedComplaint ->
                ComplaintManager.updateComplaint(updatedComplaint)
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Complaint") },
            text = { Text("Are you sure you want to delete this complaint?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        ComplaintManager.deleteComplaint(complaintId)
                        showDeleteDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ComplaintHeader(complaint: Complaint) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = complaint.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ComplaintStatusChip(status = complaint.status)
                Text(
                    text = "Priority: ${complaint.priority}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ComplaintDetails(complaint: Complaint) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Details",
                style = MaterialTheme.typography.titleMedium
            )
            Text(complaint.description)
            Divider()
            Text("Category: ${complaint.category}")
            Text("Created: ${complaint.createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}")
            Text("Last Updated: ${complaint.updatedAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}")
            complaint.resolvedAt?.let {
                Text("Resolved: ${it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}")
            }
        }
    }
}

@Composable
private fun CommentCard(comment: ComplaintComment) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "User ID: ${comment.userId}", // TODO: Get actual user name
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = comment.timestamp.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(comment.message)
        }
    }
} 