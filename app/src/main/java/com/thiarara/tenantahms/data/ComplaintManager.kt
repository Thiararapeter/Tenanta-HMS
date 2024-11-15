package com.thiarara.tenantahms.data

import com.thiarara.tenantahms.data.model.Complaint
import com.thiarara.tenantahms.data.model.ComplaintComment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

object ComplaintManager {
    private val _complaints = MutableStateFlow<List<Complaint>>(emptyList())
    val complaints: StateFlow<List<Complaint>> = _complaints.asStateFlow()

    fun addComplaint(complaint: Complaint) {
        val currentList = _complaints.value.toMutableList()
        currentList.add(complaint)
        _complaints.value = currentList
    }

    fun updateComplaint(complaint: Complaint) {
        val currentList = _complaints.value.toMutableList()
        val index = currentList.indexOfFirst { it.complaintId == complaint.complaintId }
        if (index != -1) {
            currentList[index] = complaint
            _complaints.value = currentList
        }
    }

    fun deleteComplaint(complaintId: String) {
        val currentList = _complaints.value.toMutableList()
        currentList.removeAll { it.complaintId == complaintId }
        _complaints.value = currentList
    }

    fun getComplaint(complaintId: String): Complaint? {
        return _complaints.value.find { it.complaintId == complaintId }
    }

    fun addComment(complaintId: String, comment: ComplaintComment) {
        val complaint = getComplaint(complaintId) ?: return
        val updatedComplaints = complaint.copy(
            comments = complaint.comments + comment,
            updatedAt = LocalDateTime.now()
        )
        updateComplaint(updatedComplaints)
    }
} 