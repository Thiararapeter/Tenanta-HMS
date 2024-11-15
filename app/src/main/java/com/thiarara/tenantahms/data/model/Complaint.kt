package com.thiarara.tenantahms.data.model

import java.time.LocalDateTime

enum class ComplaintStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
}

enum class ComplaintPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}

enum class ComplaintCategory {
    MAINTENANCE,
    NOISE,
    SECURITY,
    CLEANLINESS,
    UTILITIES,
    NEIGHBOR_DISPUTE,
    OTHER
}

data class Complaint(
    val complaintId: String,
    val title: String,
    val description: String,
    val category: ComplaintCategory,
    val priority: ComplaintPriority,
    val status: ComplaintStatus,
    val submittedBy: String, // Tenant ID
    val propertyId: String,
    val roomId: String?,
    val assignedTo: String?, // Staff ID
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val resolvedAt: LocalDateTime?,
    val attachments: List<String> = emptyList(),
    val comments: List<ComplaintComment> = emptyList()
)

data class ComplaintComment(
    val commentId: String,
    val complaintId: String,
    val userId: String,
    val message: String,
    val timestamp: LocalDateTime
) 