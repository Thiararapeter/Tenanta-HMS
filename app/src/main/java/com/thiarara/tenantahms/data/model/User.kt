package com.thiarara.tenantahms.data.model

enum class UserRole {
    SUPER_ADMIN,
    PROPERTY_OWNER,
    PROPERTY_MANAGER,
    TENANT
}

data class User(
    val userId: String,
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    val role: UserRole,
    val profileImageUrl: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    // Additional fields based on role
    val managedPropertyIds: List<String> = emptyList(), // For property managers
    val ownedPropertyIds: List<String> = emptyList(),   // For property owners
    val tenantInfo: TenantInfo? = null                  // For tenants
)

data class TenantInfo(
    val propertyId: String,
    val roomId: String,
    val leaseStartDate: String,
    val leaseEndDate: String,
    val monthlyRent: Double,
    val securityDeposit: Double
) 