package com.thiarara.tenantahms.data.model

data class Tenant(
    val tenantId: String,
    val name: String,
    val contactInfo: String,
    val email: String = "",
    val phoneNumber: String = "",
    val propertyId: String? = null,
    val roomId: String? = null,
    val emergencyContact: String = "",
    val moveInDate: String = "",
    val leaseEndDate: String = ""
) 