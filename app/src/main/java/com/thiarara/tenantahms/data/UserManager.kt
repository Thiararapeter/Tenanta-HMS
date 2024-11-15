package com.thiarara.tenantahms.data

import com.thiarara.tenantahms.data.model.User
import com.thiarara.tenantahms.data.model.UserRole
import com.thiarara.tenantahms.data.model.UserPermission
import com.thiarara.tenantahms.data.model.TenantInfo

object UserManager {
    private val _users = mutableListOf<User>().apply {
        // Add sample users
        add(User(
            userId = "user_1",
            email = "admin@tenanta.com",
            fullName = "John Admin",
            phoneNumber = "+254700000001",
            role = UserRole.SUPER_ADMIN
        ))
        
        add(User(
            userId = "user_2",
            email = "owner@tenanta.com",
            fullName = "Sarah Owner",
            phoneNumber = "+254700000002",
            role = UserRole.PROPERTY_OWNER,
            ownedPropertyIds = listOf("prop_1", "prop_2")
        ))
        
        add(User(
            userId = "user_3",
            email = "manager@tenanta.com",
            fullName = "Mike Manager",
            phoneNumber = "+254700000003",
            role = UserRole.PROPERTY_MANAGER,
            managedPropertyIds = listOf("prop_1")
        ))
        
        add(User(
            userId = "user_4",
            email = "tenant@tenanta.com",
            fullName = "Tom Tenant",
            phoneNumber = "+254700000004",
            role = UserRole.TENANT,
            tenantInfo = TenantInfo(
                propertyId = "prop_1",
                roomId = "room_1",
                leaseStartDate = "2024-01-01",
                leaseEndDate = "2024-12-31",
                monthlyRent = 15000.0,
                securityDeposit = 15000.0
            )
        ))
    }
    
    val users: List<User> get() = _users.toList()
    
    private var _currentUser: User? = null
    val currentUser: User? get() = _currentUser

    fun addUser(user: User) {
        _users.add(user)
    }

    fun updateUser(user: User) {
        val index = _users.indexOfFirst { it.userId == user.userId }
        if (index != -1) {
            _users[index] = user
        }
    }

    fun deleteUser(userId: String) {
        _users.removeAll { it.userId == userId }
    }

    fun getUserById(userId: String): User? {
        return _users.find { it.userId == userId }
    }

    fun getUsersByRole(role: UserRole): List<User> {
        return _users.filter { it.role == role }
    }

    fun setCurrentUser(user: User) {
        _currentUser = user
    }

    fun hasPermission(permission: UserPermission): Boolean {
        return when (_currentUser?.role) {
            UserRole.SUPER_ADMIN -> true
            UserRole.PROPERTY_OWNER -> permission in propertyOwnerPermissions
            UserRole.PROPERTY_MANAGER -> permission in propertyManagerPermissions
            UserRole.TENANT -> permission in tenantPermissions
            null -> false
        }
    }

    private val propertyOwnerPermissions = setOf(
        UserPermission.MANAGE_PROPERTIES,
        UserPermission.VIEW_REPORTS,
        UserPermission.MANAGE_MANAGERS,
        UserPermission.VIEW_TENANTS
    )

    private val propertyManagerPermissions = setOf(
        UserPermission.MANAGE_TENANTS,
        UserPermission.HANDLE_MAINTENANCE,
        UserPermission.MANAGE_PAYMENTS,
        UserPermission.VIEW_REPORTS
    )

    private val tenantPermissions = setOf(
        UserPermission.VIEW_LEASE,
        UserPermission.SUBMIT_MAINTENANCE,
        UserPermission.VIEW_PAYMENTS
    )
} 