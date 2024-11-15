package com.thiarara.tenantahms.data.model

data class Role(
    val id: String = "role_${System.currentTimeMillis()}",
    val name: String,
    val description: String,
    val permissions: Set<UserPermission>,
    val isSystem: Boolean = false, // To distinguish between system and custom roles
    val createdAt: Long = System.currentTimeMillis()
)

// Predefined system roles
object SystemRoles {
    val SUPER_ADMIN = Role(
        id = "role_super_admin",
        name = "Super Admin",
        description = "Full system access",
        permissions = UserPermission.values().toSet(),
        isSystem = true
    )

    val PROPERTY_OWNER = Role(
        id = "role_property_owner",
        name = "Property Owner",
        description = "Property owner with management rights",
        permissions = setOf(
            UserPermission.MANAGE_PROPERTIES,
            UserPermission.VIEW_REPORTS,
            UserPermission.MANAGE_MANAGERS,
            UserPermission.VIEW_TENANTS
        ),
        isSystem = true
    )

    val PROPERTY_MANAGER = Role(
        id = "role_property_manager",
        name = "Property Manager",
        description = "Property manager with operational rights",
        permissions = setOf(
            UserPermission.MANAGE_TENANTS,
            UserPermission.HANDLE_MAINTENANCE,
            UserPermission.MANAGE_PAYMENTS,
            UserPermission.VIEW_REPORTS
        ),
        isSystem = true
    )

    val TENANT = Role(
        id = "role_tenant",
        name = "Tenant",
        description = "Tenant with basic access rights",
        permissions = setOf(
            UserPermission.VIEW_LEASE,
            UserPermission.SUBMIT_MAINTENANCE,
            UserPermission.VIEW_PAYMENTS
        ),
        isSystem = true
    )
} 