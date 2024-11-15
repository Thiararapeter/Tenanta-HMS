package com.thiarara.tenantahms.data

import com.thiarara.tenantahms.data.model.Role
import com.thiarara.tenantahms.data.model.SystemRoles
import com.thiarara.tenantahms.data.model.UserPermission

object RoleManager {
    private val _roles = mutableListOf<Role>().apply {
        // Add system roles
        add(SystemRoles.SUPER_ADMIN)
        add(SystemRoles.PROPERTY_OWNER)
        add(SystemRoles.PROPERTY_MANAGER)
        add(SystemRoles.TENANT)
    }
    
    val roles: List<Role> get() = _roles.toList()

    fun addRole(role: Role) {
        if (!_roles.any { it.name == role.name }) {
            _roles.add(role)
        }
    }

    fun updateRole(role: Role) {
        val index = _roles.indexOfFirst { it.id == role.id }
        if (index != -1 && !isSuperAdminRole(_roles[index])) {
            _roles[index] = role.copy(isSystem = _roles[index].isSystem)
        }
    }

    fun deleteRole(roleId: String) {
        val role = _roles.find { it.id == roleId }
        if (role != null && !role.isSystem && !isSuperAdminRole(role)) {
            _roles.remove(role)
        }
    }

    fun getRoleById(roleId: String): Role? {
        return _roles.find { it.id == roleId }
    }

    fun hasPermission(role: Role, permission: UserPermission): Boolean {
        return if (isSuperAdminRole(role)) {
            true // Super Admin has all permissions
        } else {
            permission in role.permissions
        }
    }

    private fun isSuperAdminRole(role: Role): Boolean {
        return role.id == SystemRoles.SUPER_ADMIN.id
    }
} 