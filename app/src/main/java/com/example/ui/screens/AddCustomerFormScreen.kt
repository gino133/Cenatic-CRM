package com.example.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CustomerEntity
import com.example.data.model.CustomerStatus
import com.example.data.model.CustomerTypeEntity
import com.example.ui.viewmodel.CrmViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomerFormScreen(
    isCorporate: Boolean = true,
    existingCustomer: CustomerEntity? = null,
    viewModel: CrmViewModel? = null,
    onBack: () -> Unit,
    onSaveCustomer: (CustomerEntity) -> Unit,
    onNavigateToTab: ((Int) -> Unit)? = null,
    onOpenCustomerTypesSettings: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var isCorporateMode by remember(existingCustomer, isCorporate) {
        mutableStateOf(existingCustomer?.isCorporate ?: isCorporate)
    }

    val defaultCustomerTypes = remember {
        listOf(
            CustomerTypeEntity(id = 1, name = "Tiềm Năng", code = "LEAD", colorHex = "#2563EB", description = "Khách hàng mới tiếp cận, đang tìm hiểu"),
            CustomerTypeEntity(id = 2, name = "Khách hàng VIP", code = "VIP", colorHex = "#8B5CF6", description = "Khách hàng ưu tiên cao, doanh số lớn"),
            CustomerTypeEntity(id = 3, name = "Khách hàng Vãng Lai", code = "CASUAL", colorHex = "#F59E0B", description = "Khách hàng mua hàng lẻ, chưa thường xuyên"),
            CustomerTypeEntity(id = 4, name = "Khách hàng", code = "CUSTOMER", colorHex = "#10B981", description = "Khách hàng chuẩn đã phát sinh giao dịch"),
            CustomerTypeEntity(id = 5, name = "Đối tác", code = "PARTNER", colorHex = "#06B6D4", description = "Đối tác chiến lược hoặc nhà phân phối"),
            CustomerTypeEntity(id = 6, name = "Ngưng liên hệ", code = "INACTIVE", colorHex = "#94A3B8", description = "Tạm ngừng tiếp cận hoặc chăm sóc")
        )
    }

    val dbCustomerTypes = viewModel?.allCustomerTypes?.collectAsStateWithLifecycle(emptyList())?.value ?: emptyList()
    val availableCustomerTypes = remember(dbCustomerTypes) {
        if (dbCustomerTypes.isNotEmpty()) dbCustomerTypes else defaultCustomerTypes
    }

    var selectedCustomerType by remember(existingCustomer, availableCustomerTypes) {
        val initial = if (existingCustomer != null) {
            availableCustomerTypes.find {
                it.code.equals(existingCustomer.status, ignoreCase = true) ||
                it.name.equals(existingCustomer.status, ignoreCase = true) ||
                it.name.equals(existingCustomer.tags, ignoreCase = true)
            } ?: CustomerTypeEntity(
                name = CustomerStatus.fromString(existingCustomer.status).label,
                code = existingCustomer.status,
                colorHex = CustomerStatus.fromString(existingCustomer.status).colorHex
            )
        } else {
            availableCustomerTypes.firstOrNull() ?: defaultCustomerTypes.first()
        }
        mutableStateOf(initial)
    }

    // Helper to extract company birthday from notes if saved previously
    val initialCompanyDob = remember(existingCustomer) {
        extractCompanyDob(existingCustomer?.notes)
    }

    // Corporate fields pre-populated if editing
    var companyName by remember(existingCustomer) {
        mutableStateOf(
            existingCustomer?.company?.ifBlank {
                if (existingCustomer.isCorporate) existingCustomer.name else ""
            } ?: ""
        )
    }
    var taxCode by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.taxCode ?: "")
    }
    var taxAddress by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.taxAddress ?: "")
    }
    var companyDob by remember(existingCustomer) {
        mutableStateOf(initialCompanyDob)
    }
    var sameAddressAsTax by remember(existingCustomer) {
        mutableStateOf(
            existingCustomer != null &&
            existingCustomer.taxAddress.isNotBlank() &&
            existingCustomer.taxAddress == existingCustomer.address
        )
    }
    var operatingAddress by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.address ?: "")
    }
    var contactPersonName by remember(existingCustomer) {
        mutableStateOf(
            if (existingCustomer != null) {
                existingCustomer.contactPerson.ifBlank {
                    if (existingCustomer.isCorporate) existingCustomer.name else ""
                }
            } else ""
        )
    }
    var contactPosition by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.position ?: "")
    }
    var contactPhone by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.phone ?: "")
    }
    var contactEmail by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.email ?: "")
    }
    var contactDob by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.dob ?: "")
    }
    var customerLocation by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.address ?: "")
    }
    var sameAsOperatingAddress by remember(existingCustomer) {
        mutableStateOf(false)
    }

    // Individual fields pre-populated if editing
    var individualFullName by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.name ?: "")
    }
    var individualPhone by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.phone ?: "")
    }
    var individualEmail by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.email ?: "")
    }
    var individualDob by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.dob ?: "")
    }
    var individualAddress by remember(existingCustomer) {
        mutableStateOf(existingCustomer?.address ?: "")
    }

    // Date picker dialog helper
    fun showDatePicker(currentDateStr: String, onDateSelected: (String) -> Unit) {
        val calendar = parseDateOrDefault(currentDateStr)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDay = String.format("%02d", selectedDay)
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                onDateSelected("$formattedDay/$formattedMonth/$selectedYear")
            },
            year,
            month,
            day
        ).show()
    }

    val primaryBlue = Color(0xFF0057D9)
    val navyHeader = Color(0xFF0A2540)
    val darkText = Color(0xFF0F172A)
    val slateBorder = Color(0xFFCBD5E1)

    val isEditing = existingCustomer != null

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Kinetic Enterprise",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = navyHeader
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = navyHeader,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* User Account Profile */ }) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "Account",
                            tint = navyHeader,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 4.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigateToTab?.invoke(0) ?: onBack() },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Trang chủ") },
                    label = { Text("Trang chủ", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color(0xFF475569),
                        unselectedTextColor = Color(0xFF475569)
                    )
                )

                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already in Customers */ },
                    icon = { Icon(Icons.Filled.People, contentDescription = "Khách hàng") },
                    label = { Text("Khách hàng", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = primaryBlue,
                        selectedIconColor = Color.White,
                        selectedTextColor = primaryBlue
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigateToTab?.invoke(2) ?: onBack() },
                    icon = { Icon(Icons.Outlined.Description, contentDescription = "Báo giá") },
                    label = { Text("Báo giá", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color(0xFF475569),
                        unselectedTextColor = Color(0xFF475569)
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigateToTab?.invoke(3) ?: onBack() },
                    icon = { Icon(Icons.Outlined.Checklist, contentDescription = "Công việc") },
                    label = { Text("Công việc", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color(0xFF475569),
                        unselectedTextColor = Color(0xFF475569)
                    )
                )
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Screen Title
            Text(
                text = if (isEditing) "Chỉnh sửa thông tin khách hàng" else "Thông tin khách hàng",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = darkText
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Pill Tab Switcher: [ Công ty ] [ Cá nhân ]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE2E8F0))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isCorporateMode) Color.White else Color.Transparent)
                        .border(
                            width = if (isCorporateMode) 1.dp else 0.dp,
                            color = if (isCorporateMode) Color(0xFFCBD5E1) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (!isCorporateMode) {
                                isCorporateMode = true
                                // Xóa dữ liệu khách hàng cá nhân khi chuyển sang công ty
                                individualFullName = ""
                                individualPhone = ""
                                individualEmail = ""
                                individualDob = ""
                                individualAddress = ""
                            }
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Công ty",
                        fontWeight = if (isCorporateMode) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp,
                        color = if (isCorporateMode) Color(0xFF0F172A) else Color(0xFF475569)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (!isCorporateMode) Color.White else Color.Transparent)
                        .border(
                            width = if (!isCorporateMode) 1.dp else 0.dp,
                            color = if (!isCorporateMode) Color(0xFFCBD5E1) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (isCorporateMode) {
                                isCorporateMode = false
                                // Xóa dữ liệu khách hàng công ty khi chuyển sang cá nhân
                                companyName = ""
                                taxCode = ""
                                taxAddress = ""
                                companyDob = ""
                                sameAddressAsTax = false
                                operatingAddress = ""
                                contactPersonName = ""
                                contactPosition = ""
                                contactPhone = ""
                                contactEmail = ""
                                contactDob = ""
                                customerLocation = ""
                                sameAsOperatingAddress = false
                            }
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cá nhân",
                        fontWeight = if (!isCorporateMode) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp,
                        color = if (!isCorporateMode) Color(0xFF0F172A) else Color(0xFF475569)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==================== LOẠI KHÁCH HÀNG SECTION (DROPDOWN) ====================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, slateBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LOẠI KHÁCH HÀNG",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            letterSpacing = 0.5.sp
                        )
                        if (onOpenCustomerTypesSettings != null) {
                            Text(
                                text = "Tùy chỉnh trong Cài đặt",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryBlue,
                                modifier = Modifier
                                    .clickable { onOpenCustomerTypesSettings() }
                                    .padding(4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    var dropdownExpanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = dropdownExpanded,
                        onExpandedChange = { dropdownExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val currentColor = try {
                            Color(android.graphics.Color.parseColor(selectedCustomerType.colorHex))
                        } catch (_: Exception) {
                            primaryBlue
                        }

                        OutlinedTextField(
                            value = selectedCustomerType.name,
                            onValueChange = {},
                            readOnly = true,
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 12.dp, end = 4.dp)
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(currentColor)
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("customer_type_dropdown"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true
                        )

                        ExposedDropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false },
                            modifier = Modifier
                                .background(Color.White)
                                .widthIn(min = 280.dp)
                        ) {
                            availableCustomerTypes.forEach { type ->
                                val isSelected = selectedCustomerType.id == type.id ||
                                        (selectedCustomerType.code.isNotBlank() && selectedCustomerType.code.equals(type.code, ignoreCase = true)) ||
                                        selectedCustomerType.name.equals(type.name, ignoreCase = true)
                                val itemColor = try {
                                    Color(android.graphics.Color.parseColor(type.colorHex))
                                } catch (_: Exception) {
                                    primaryBlue
                                }

                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(12.dp)
                                                    .clip(CircleShape)
                                                    .background(itemColor)
                                            )
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = type.name,
                                                    fontSize = 14.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = Color(0xFF0F172A)
                                                )
                                                if (type.description.isNotBlank()) {
                                                    Text(
                                                        text = type.description,
                                                        fontSize = 11.sp,
                                                        color = Color(0xFF64748B),
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                            }
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Đã chọn",
                                                    tint = primaryBlue,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        selectedCustomerType = type
                                        dropdownExpanded = false
                                    },
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }

                            if (onOpenCustomerTypesSettings != null) {
                                HorizontalDivider(color = Color(0xFFE2E8F0))
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Settings,
                                                contentDescription = null,
                                                tint = primaryBlue,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = "Quản lý phân loại khách hàng...",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = primaryBlue
                                            )
                                        }
                                    },
                                    onClick = {
                                        dropdownExpanded = false
                                        onOpenCustomerTypesSettings()
                                    },
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isCorporateMode) {
                // ==================== 1. KHÁCH HÀNG CÔNG TY (CORPORATE) ====================

                // Section 1: THÔNG TIN DOANH NGHIỆP
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, slateBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "THÔNG TIN DOANH NGHIỆP",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Tên Công ty *
                        LabelWithAsterisk("Tên Công ty")
                        OutlinedTextField(
                            value = companyName,
                            onValueChange = { companyName = it },
                            placeholder = { Text("Nhập tên công ty", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("corp_company_name_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Mã số thuế *
                        LabelWithAsterisk("Mã số thuế")
                        OutlinedTextField(
                            value = taxCode,
                            onValueChange = { taxCode = it },
                            placeholder = { Text("Nhập mã số thuế", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("corp_tax_code_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Ngày sinh nhật công ty / Ngày thành lập
                        Text(
                            text = "Ngày sinh nhật công ty",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = darkText,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = companyDob,
                            onValueChange = { companyDob = it },
                            placeholder = { Text("dd/mm/yyyy", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            trailingIcon = {
                                IconButton(onClick = {
                                    showDatePicker(companyDob) { selected ->
                                        companyDob = selected
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = "Chọn ngày sinh nhật công ty",
                                        tint = primaryBlue,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("corp_dob_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Địa chỉ thuế
                        Text(
                            text = "Địa chỉ thuế",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = darkText,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = taxAddress,
                            onValueChange = {
                                taxAddress = it
                                if (sameAddressAsTax) {
                                    operatingAddress = it
                                    if (sameAsOperatingAddress) {
                                        customerLocation = it
                                    }
                                }
                            },
                            placeholder = { Text("Nhập địa chỉ đăng ký thuế", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("corp_tax_address_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Checkbox: Địa chỉ hoạt động trùng với địa chỉ thuế
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    sameAddressAsTax = !sameAddressAsTax
                                    if (sameAddressAsTax) {
                                        operatingAddress = taxAddress
                                        if (sameAsOperatingAddress) {
                                            customerLocation = taxAddress
                                        }
                                    }
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = sameAddressAsTax,
                                onCheckedChange = { isChecked ->
                                    sameAddressAsTax = isChecked
                                    if (isChecked) {
                                        operatingAddress = taxAddress
                                        if (sameAsOperatingAddress) {
                                            customerLocation = taxAddress
                                        }
                                    }
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = primaryBlue,
                                    uncheckedColor = Color(0xFF64748B)
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Địa chỉ hoạt động trùng với địa chỉ thuế",
                                fontSize = 13.sp,
                                color = Color(0xFF334155)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Địa chỉ hoạt động
                        Text(
                            text = "Địa chỉ hoạt động",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = darkText,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = operatingAddress,
                            onValueChange = {
                                if (!sameAddressAsTax) {
                                    operatingAddress = it
                                    if (sameAsOperatingAddress) {
                                        customerLocation = it
                                    }
                                }
                            },
                            readOnly = sameAddressAsTax,
                            placeholder = { Text("Nhập địa chỉ hoạt động", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("corp_operating_address_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = if (sameAddressAsTax) customReadOnlyFieldColors() else customFieldColors(),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section 2: THÔNG TIN LIÊN HỆ
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, slateBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "THÔNG TIN LIÊN HỆ",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                letterSpacing = 0.5.sp
                            )

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { /* Add new contact */ }
                                    .padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddCircleOutline,
                                    contentDescription = "Thêm người",
                                    tint = Color(0xFF0F172A),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Thêm người",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0F172A)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Người liên hệ *
                        LabelWithAsterisk("Người liên hệ")
                        OutlinedTextField(
                            value = contactPersonName,
                            onValueChange = { contactPersonName = it },
                            placeholder = { Text("Họ và tên người liên hệ", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("corp_contact_name_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Chức vụ
                        Text(
                            text = "Chức vụ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = darkText,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = contactPosition,
                            onValueChange = { contactPosition = it },
                            placeholder = { Text("Nhập chức vụ (ví dụ: Giám đốc, Trưởng phòng...)", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("corp_contact_position_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Số điện thoại *
                        LabelWithAsterisk("Số điện thoại")
                        OutlinedTextField(
                            value = contactPhone,
                            onValueChange = { contactPhone = it },
                            placeholder = { Text("Nhập số điện thoại", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("corp_contact_phone_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Email
                        Text(
                            text = "Email",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = darkText,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = contactEmail,
                            onValueChange = { contactEmail = it },
                            placeholder = { Text("Nhập địa chỉ email", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("corp_contact_email_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Ngày sinh nhật
                        Text(
                            text = "Ngày sinh nhật",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = darkText,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = contactDob,
                            onValueChange = { contactDob = it },
                            placeholder = { Text("dd/mm/yyyy", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            trailingIcon = {
                                IconButton(onClick = {
                                    showDatePicker(contactDob) { selected ->
                                        contactDob = selected
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = "Chọn ngày sinh",
                                        tint = primaryBlue,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("corp_contact_dob_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Địa chỉ Khách hàng
                        Text(
                            text = "Địa chỉ Khách hàng",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = darkText,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = customerLocation,
                            onValueChange = {
                                if (!sameAsOperatingAddress) {
                                    customerLocation = it
                                }
                            },
                            readOnly = sameAsOperatingAddress,
                            placeholder = { Text("Nhập địa chỉ khách hàng", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("corp_contact_location_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = if (sameAsOperatingAddress) customReadOnlyFieldColors() else customFieldColors(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Checkbox: Trùng với địa chỉ hoạt động
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    sameAsOperatingAddress = !sameAsOperatingAddress
                                    if (sameAsOperatingAddress) {
                                        customerLocation = operatingAddress
                                    }
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = sameAsOperatingAddress,
                                onCheckedChange = { isChecked ->
                                    sameAsOperatingAddress = isChecked
                                    if (isChecked) {
                                        customerLocation = operatingAddress
                                    }
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = primaryBlue,
                                    uncheckedColor = Color(0xFF64748B)
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Trùng với địa chỉ hoạt động",
                                fontSize = 13.sp,
                                color = Color(0xFF334155)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bottom Action Button
                Button(
                    onClick = {
                        val cust = (existingCustomer ?: CustomerEntity(name = "")).copy(
                            name = contactPersonName.ifBlank { companyName },
                            company = companyName,
                            position = contactPosition,
                            phone = contactPhone,
                            email = contactEmail,
                            address = operatingAddress.ifBlank { taxAddress },
                            isCorporate = true,
                            taxCode = taxCode,
                            taxAddress = taxAddress,
                            contactPerson = contactPersonName,
                            dob = contactDob,
                            notes = buildString {
                                if (companyDob.isNotBlank()) {
                                    append("Ngày sinh nhật công ty: $companyDob\n")
                                }
                                val cleanNotes = existingCustomer?.notes?.lines()
                                    ?.filterNot { it.startsWith("Ngày sinh nhật công ty:", ignoreCase = true) }
                                    ?.joinToString("\n") ?: ""
                                if (cleanNotes.isNotBlank()) {
                                    append(cleanNotes)
                                }
                            }.trim(),
                            status = selectedCustomerType.code.ifBlank { selectedCustomerType.name },
                            tags = selectedCustomerType.name
                        )
                        onSaveCustomer(cust)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_corp_customer_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                ) {
                    Text(
                        text = if (isEditing) "Cập Nhật Thông Tin" else "Lưu Thông Tin",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

            } else {
                // ==================== 2. KHÁCH HÀNG CÁ NHÂN (INDIVIDUAL) ====================
                // Giống thông tin liên hệ của khách hàng công ty:
                // - Bỏ chọn "Thêm người"
                // - Bỏ ô chọn "Trùng với địa chỉ hoạt động"
                // - Ngày sinh nhật có lịch để chọn

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, slateBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "THÔNG TIN LIÊN HỆ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Người liên hệ / Họ và tên *
                        LabelWithAsterisk("Người liên hệ")
                        OutlinedTextField(
                            value = individualFullName,
                            onValueChange = { individualFullName = it },
                            placeholder = { Text("Họ và tên người liên hệ", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("indiv_name_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Số điện thoại *
                        LabelWithAsterisk("Số điện thoại")
                        OutlinedTextField(
                            value = individualPhone,
                            onValueChange = { individualPhone = it },
                            placeholder = { Text("Nhập số điện thoại", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("indiv_phone_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Email
                        Text(
                            text = "Email",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = darkText,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = individualEmail,
                            onValueChange = { individualEmail = it },
                            placeholder = { Text("Nhập địa chỉ email", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("indiv_email_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Ngày sinh nhật (Có lịch để chọn)
                        Text(
                            text = "Ngày sinh nhật",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = darkText,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = individualDob,
                            onValueChange = { individualDob = it },
                            placeholder = { Text("dd/mm/yyyy", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            trailingIcon = {
                                IconButton(onClick = {
                                    showDatePicker(individualDob) { selected ->
                                        individualDob = selected
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = "Chọn ngày sinh",
                                        tint = primaryBlue,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("indiv_dob_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Địa chỉ Khách hàng
                        Text(
                            text = "Địa chỉ Khách hàng",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = darkText,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = individualAddress,
                            onValueChange = { individualAddress = it },
                            placeholder = { Text("Nhập địa chỉ khách hàng", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("indiv_address_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors(),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bottom Action Button
                Button(
                    onClick = {
                        val cust = (existingCustomer ?: CustomerEntity(name = "")).copy(
                            name = individualFullName,
                            company = "",
                            position = "",
                            phone = individualPhone,
                            email = individualEmail,
                            dob = individualDob,
                            address = individualAddress,
                            isCorporate = false,
                            taxCode = "",
                            taxAddress = "",
                            contactPerson = individualFullName,
                            status = selectedCustomerType.code.ifBlank { selectedCustomerType.name },
                            tags = selectedCustomerType.name
                        )
                        onSaveCustomer(cust)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_indiv_customer_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                ) {
                    Text(
                        text = if (isEditing) "Cập Nhật Thông Tin" else "Lưu Thông Tin",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CustomerTypeItem(
    label: String,
    status: CustomerStatus,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeBorderColor = Color(0xFF0F172A)
    val activeBgColor = Color(0xFFE2E8F0)
    val inactiveBorderColor = Color(0xFFCBD5E1)
    val inactiveBgColor = Color(0xFFF8FAFC)

    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) activeBgColor else inactiveBgColor,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) activeBorderColor else inactiveBorderColor
        ),
        modifier = modifier.testTag("type_option_${status.name.lowercase()}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircleOutline,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF0F172A) else Color(0xFF94A3B8),
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = Color(0xFF0F172A),
                maxLines = 1
            )
        }
    }
}

// Helper to extract company birthday from notes if saved previously
private fun extractCompanyDob(notes: String?): String {
    if (notes.isNullOrBlank()) return ""
    val prefix = "Ngày sinh nhật công ty:"
    val line = notes.lines().firstOrNull { it.startsWith(prefix, ignoreCase = true) } ?: return ""
    return line.substringAfter(":").trim()
}

// Helper to parse date or default to current date
private fun parseDateOrDefault(dateStr: String): Calendar {
    val cal = Calendar.getInstance()
    if (dateStr.isNotBlank()) {
        try {
            val parts = dateStr.split("/", "-")
            if (parts.size == 3) {
                val d = parts[0].trim().toIntOrNull() ?: 1
                val m = (parts[1].trim().toIntOrNull() ?: 1) - 1
                val y = parts[2].trim().toIntOrNull() ?: cal.get(Calendar.YEAR)
                cal.set(y, m, d)
                return cal
            }
        } catch (_: Exception) {
            // fallback
        }
    }
    return cal
}

@Composable
fun LabelWithAsterisk(label: String) {
    Text(
        text = buildAnnotatedString {
            append(label)
            append(" ")
            withStyle(SpanStyle(color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)) {
                append("*")
            }
        },
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF0F172A),
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
fun FormFieldLabel(label: String) {
    Text(
        text = label,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF0F172A),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    )
}

@Composable
fun customFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color(0xFF0F172A),
    unfocusedTextColor = Color(0xFF0F172A),
    focusedBorderColor = Color(0xFF0057D9),
    unfocusedBorderColor = Color(0xFFCBD5E1),
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = Color(0xFF0057D9),
    focusedPlaceholderColor = Color(0xFF94A3B8),
    unfocusedPlaceholderColor = Color(0xFF94A3B8)
)

@Composable
fun customReadOnlyFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color(0xFF334155),
    unfocusedTextColor = Color(0xFF334155),
    focusedBorderColor = Color(0xFFCBD5E1),
    unfocusedBorderColor = Color(0xFFE2E8F0),
    focusedContainerColor = Color(0xFFF1F5F9),
    unfocusedContainerColor = Color(0xFFF1F5F9),
    cursorColor = Color(0xFF0057D9),
    focusedPlaceholderColor = Color(0xFF94A3B8),
    unfocusedPlaceholderColor = Color(0xFF94A3B8)
)
