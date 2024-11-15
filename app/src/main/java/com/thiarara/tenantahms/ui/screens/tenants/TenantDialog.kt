package com.thiarara.tenantahms.ui.screens.tenants

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.thiarara.tenantahms.data.PropertyDataManager
import com.thiarara.tenantahms.data.model.Tenant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
private fun SectionHeader(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Divider(
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDialog(
    tenant: Tenant? = null,
    onDismiss: () -> Unit,
    onSave: (Tenant) -> Unit
) {
    var name by remember { mutableStateOf(tenant?.name ?: "") }
    var email by remember { mutableStateOf(tenant?.email ?: "") }
    var phoneNumber by remember { mutableStateOf(tenant?.phoneNumber ?: "") }
    var contactInfo by remember { mutableStateOf(tenant?.contactInfo ?: "") }
    var emergencyContact by remember { mutableStateOf(tenant?.emergencyContact ?: "") }
    
    var selectedPropertyId by remember { mutableStateOf(tenant?.propertyId) }
    var selectedRoomId by remember { mutableStateOf(tenant?.roomId) }
    
    // Lease type and duration states
    var leaseType by remember { mutableStateOf(if (tenant?.leaseEndDate?.isEmpty() == true) "untilNotice" else "duration") }
    var durationMonths by remember { mutableStateOf(12) }
    
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }

    // Dropdown expansion states
    var isPropertyDropdownExpanded by remember { mutableStateOf(false) }
    var isRoomDropdownExpanded by remember { mutableStateOf(false) }
    
    // Date picker states
    var showMoveInDatePicker by remember { mutableStateOf(false) }
    var showLeaseEndDatePicker by remember { mutableStateOf(false) }
    
    // Date formatter
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    
    // Parse existing dates or use current date
    var selectedMoveInDate by remember {
        mutableStateOf(
            try {
                if (tenant?.moveInDate?.isNotEmpty() == true) {
                    LocalDate.parse(tenant.moveInDate, dateFormatter)
                } else {
                    LocalDate.now()
                }
            } catch (e: Exception) {
                LocalDate.now()
            }
        )
    }
    
    var selectedLeaseEndDate by remember {
        mutableStateOf(
            try {
                if (tenant?.leaseEndDate?.isNotEmpty() == true) {
                    LocalDate.parse(tenant.leaseEndDate, dateFormatter)
                } else {
                    LocalDate.now().plusMonths(12)
                }
            } catch (e: Exception) {
                LocalDate.now().plusMonths(12)
            }
        )
    }

    // Get available properties
    val properties = PropertyDataManager.getAllProperties()
    
    val selectedProperty = properties.find { it.propertyId == selectedPropertyId }
    
    val availableRooms = selectedPropertyId?.let { propId ->
        PropertyDataManager.getRoomsForProperty(propId)
            .filter { room -> room.roomId == tenant?.roomId || !PropertyDataManager.isRoomOccupied(room.roomId) }
    } ?: emptyList()

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Property Assignment Section First
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SectionHeader("Property Assignment")
                    
                    // Property Selection
                    OutlinedTextField(
                        value = selectedProperty?.let { 
                            "${it.name} (${it.vacantRooms} vacant rooms)"
                        } ?: "Select Property",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Property") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isPropertyDropdownExpanded = true },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ),
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select property",
                                modifier = Modifier.clickable {
                                    isPropertyDropdownExpanded = true
                                }
                            )
                        }
                    )

                    // Room Selection
                    if (selectedPropertyId != null) {
                        OutlinedTextField(
                            value = availableRooms.find { it.roomId == selectedRoomId }?.let {
                                "Room ${it.number} (${it.type})"
                            } ?: "Select Room",
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Room") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isRoomDropdownExpanded = true },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            ),
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Select room",
                                    modifier = Modifier.clickable {
                                        isRoomDropdownExpanded = true
                                    }
                                )
                            }
                        )
                    }
                }

                // Lease Duration Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SectionHeader("Lease Duration")
                    
                    // Move-in Date
                    OutlinedTextField(
                        value = selectedMoveInDate.format(dateFormatter),
                        onValueChange = { },
                        label = { Text("Move-in Date") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Default.CalendarMonth,
                                contentDescription = "Select date",
                                modifier = Modifier.clickable { showMoveInDatePicker = true }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showMoveInDatePicker = true }
                    )

                    // Lease Type Selection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = leaseType == "duration",
                            onClick = { leaseType = "duration" },
                            label = { Text("Fixed Duration") }
                        )
                        FilterChip(
                            selected = leaseType == "untilNotice",
                            onClick = { leaseType = "untilNotice" },
                            label = { Text("Until Notice") }
                        )
                    }

                    // Show duration options only if fixed duration is selected
                    if (leaseType == "duration") {
                        OutlinedTextField(
                            value = selectedLeaseEndDate.format(dateFormatter),
                            onValueChange = { },
                            label = { Text("Lease End Date") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    Icons.Default.CalendarMonth,
                                    contentDescription = "Select date",
                                    modifier = Modifier.clickable { showLeaseEndDatePicker = true }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showLeaseEndDatePicker = true }
                        )
                    }
                }

                // Personal Information Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SectionHeader("Personal Information")
                    
                    OutlinedTextField(
                        value = name,
                        onValueChange = { 
                            name = it
                            nameError = null
                        },
                        label = { Text("Name") },
                        isError = nameError != null,
                        supportingText = nameError?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { 
                            email = it
                            emailError = null
                        },
                        label = { Text("Email") },
                        isError = emailError != null,
                        supportingText = emailError?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { 
                            phoneNumber = it
                            phoneError = null
                        },
                        label = { Text("Phone Number") },
                        isError = phoneError != null,
                        supportingText = phoneError?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = emergencyContact,
                        onValueChange = { emergencyContact = it },
                        label = { Text("Emergency Contact") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        confirmButton = {
            TextButton(onClick = {
                when {
                    name.isBlank() -> nameError = "Name cannot be empty"
                    email.isBlank() -> emailError = "Email cannot be empty"
                    phoneNumber.isBlank() -> phoneError = "Phone number cannot be empty"
                    selectedPropertyId == null -> nameError = "Please select a property"
                    selectedRoomId == null -> nameError = "Please select a room"
                    else -> {
                        onSave(
                            Tenant(
                                tenantId = tenant?.tenantId ?: "tenant_${System.currentTimeMillis()}",
                                name = name,
                                email = email,
                                phoneNumber = phoneNumber,
                                contactInfo = contactInfo,
                                emergencyContact = emergencyContact,
                                propertyId = selectedPropertyId,
                                roomId = selectedRoomId,
                                moveInDate = selectedMoveInDate.format(dateFormatter),
                                leaseEndDate = if (leaseType == "untilNotice") "" else selectedLeaseEndDate.format(dateFormatter)
                            )
                        )
                    }
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    // Property Selection Dialog
    if (isPropertyDropdownExpanded) {
        val currentProperties = PropertyDataManager.getAllProperties()
        AlertDialog(
            onDismissRequest = { isPropertyDropdownExpanded = false },
            title = { Text("Select Property") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    currentProperties.forEach { property ->
                        ListItem(
                            headlineContent = { Text(property.name) },
                            supportingContent = { 
                                Text("${property.vacantRooms} vacant rooms of ${property.totalRooms}")
                            },
                            modifier = Modifier
                                .clickable {
                                    selectedPropertyId = property.propertyId
                                    selectedRoomId = null
                                    isPropertyDropdownExpanded = false
                                }
                                .fillMaxWidth()
                        )
                        if (currentProperties.last() != property) {
                            Divider()
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    // Room Selection Dialog
    if (isRoomDropdownExpanded && selectedPropertyId != null) {
        AlertDialog(
            onDismissRequest = { isRoomDropdownExpanded = false },
            title = { Text("Select Room") },
            text = {
                if (availableRooms.isEmpty()) {
                    Text(
                        "No available rooms",
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Column {
                        availableRooms.forEach { room ->
                            ListItem(
                                headlineContent = { Text("Room ${room.number}") },
                                supportingContent = { Text("Type: ${room.type}") },
                                modifier = Modifier
                                    .clickable {
                                        selectedRoomId = room.roomId
                                        isRoomDropdownExpanded = false
                                    }
                                    .fillMaxWidth()
                            )
                            if (availableRooms.last() != room) {
                                Divider()
                            }
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    // Move-in Date Picker Dialog
    if (showMoveInDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showMoveInDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showMoveInDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showMoveInDatePicker = false
                }) {
                    Text("Cancel")
                }
            }
        ) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedMoveInDate.toEpochDay() * 24 * 60 * 60 * 1000
            )
            
            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let { millis ->
                    selectedMoveInDate = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                }
            }
            
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }

    // Lease End Date Picker Dialog
    if (showLeaseEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showLeaseEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showLeaseEndDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showLeaseEndDatePicker = false
                }) {
                    Text("Cancel")
                }
            }
        ) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedLeaseEndDate.toEpochDay() * 24 * 60 * 60 * 1000
            )
            
            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let { millis ->
                    selectedLeaseEndDate = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                }
            }
            
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }
} 