package com.thiarara.tenantahms.ui.screens.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thiarara.tenantahms.data.model.User
import com.thiarara.tenantahms.data.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDialog(
    user: User? = null,
    onDismiss: () -> Unit,
    onSave: (User) -> Unit
) {
    var fullName by remember { mutableStateOf(user?.fullName ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phoneNumber by remember { mutableStateOf(user?.phoneNumber ?: "") }
    var selectedRole by remember { mutableStateOf(user?.role ?: UserRole.PROPERTY_MANAGER) }
    var expanded by remember { mutableStateOf(false) }
    
    // Validation states
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }

    fun validateInputs(): Boolean {
        var isValid = true
        
        if (fullName.isBlank()) {
            fullNameError = "Name is required"
            isValid = false
        } else {
            fullNameError = null
        }

        if (email.isBlank()) {
            emailError = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
            isValid = false
        } else {
            emailError = null
        }

        if (phoneNumber.isBlank()) {
            phoneError = "Phone number is required"
            isValid = false
        } else if (!phoneNumber.matches(Regex("^[+]?[0-9]{10,13}$"))) {
            phoneError = "Invalid phone number format"
            isValid = false
        } else {
            phoneError = null
        }

        return isValid
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (user == null) "Add User" else "Edit User") },
        text = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { 
                        fullName = it
                        fullNameError = null
                    },
                    label = { Text("Full Name") },
                    isError = fullNameError != null,
                    supportingText = fullNameError?.let { { Text(it) } }
                )
                
                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        emailError = null
                    },
                    label = { Text("Email") },
                    isError = emailError != null,
                    supportingText = emailError?.let { { Text(it) } }
                )
                
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { 
                        phoneNumber = it
                        phoneError = null
                    },
                    label = { Text("Phone Number") },
                    isError = phoneError != null,
                    supportingText = phoneError?.let { { Text(it) } }
                )
                
                // Role dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedRole.name,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Role") },
                        modifier = Modifier.menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        UserRole.values().forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role.name) },
                                onClick = {
                                    selectedRole = role
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (validateInputs()) {
                        val newUser = User(
                            userId = user?.userId ?: "user_${System.currentTimeMillis()}",
                            fullName = fullName,
                            email = email,
                            phoneNumber = phoneNumber,
                            role = selectedRole
                        )
                        onSave(newUser)
                        onDismiss()
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 