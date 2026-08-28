package com.example.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import com.example.util.NotificationHelper
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CustomerEntity
import com.example.data.model.CustomerStatus
import com.example.data.model.DealEntity
import com.example.data.model.DealStage
import com.example.data.model.InteractionEntity
import com.example.data.model.InteractionType
import com.example.data.model.TaskEntity
import com.example.data.model.TaskPriority
import com.example.data.model.TaskType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

val AvatarColors = listOf(
    "#2563EB", "#7C3AED", "#059669", "#EA580C", "#0891B2", "#E11D48", "#4F46E5", "#D97706"
)

// Add/Edit Customer Dialog
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditCustomerDialog(
    customer: CustomerEntity? = null,
    onDismiss: () -> Unit,
    onSave: (CustomerEntity) -> Unit
) {
    var name by remember { mutableStateOf(customer?.name ?: "") }
    var company by remember { mutableStateOf(customer?.company ?: "") }
    var position by remember { mutableStateOf(customer?.position ?: "") }
    var phone by remember { mutableStateOf(customer?.phone ?: "") }
    var email by remember { mutableStateOf(customer?.email ?: "") }
    var address by remember { mutableStateOf(customer?.address ?: "") }
    var status by remember { mutableStateOf(customer?.let { CustomerStatus.fromString(it.status) } ?: CustomerStatus.LEAD) }
    var source by remember { mutableStateOf(customer?.source ?: "Giới thiệu") }
    var tags by remember { mutableStateOf(customer?.tags ?: "") }
    var estimatedValueStr by remember { mutableStateOf(if (customer != null && customer.estimatedValue > 0) customer.estimatedValue.toLong().toString() else "") }
    var notes by remember { mutableStateOf(customer?.notes ?: "") }
    var selectedColor by remember { mutableStateOf(customer?.avatarColorHex ?: AvatarColors.first()) }

    var isError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (customer == null) "Thêm Khách Hàng Mới" else "Chỉnh Sửa Thông Tin Khách Hàng",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Quản lý chi tiết hồ sơ liên hệ, phân loại và lịch sử khách hàng",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Avatar color selection
                Text(
                    text = "Màu sắc đại diện:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AvatarColors.forEach { hex ->
                        val parsed = try { Color(android.graphics.Color.parseColor(hex)) } catch (e: Exception) { Color.Blue }
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(parsed)
                                .clickable { selectedColor = hex },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedColor == hex) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (it.isNotBlank()) isError = false
                    },
                    label = { Text("Họ và tên khách hàng *") },
                    placeholder = { Text("Ví dụ: Nguyễn Hoàng Nam") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    isError = isError && name.isBlank(),
                    supportingText = if (isError && name.isBlank()) { { Text("Vui lòng nhập tên khách hàng") } } else null,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_customer_name")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Company and Position
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = company,
                        onValueChange = { company = it },
                        label = { Text("Công ty / Tổ chức") },
                        placeholder = { Text("Ví dụ: VinaTech JSC") },
                        leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = position,
                        onValueChange = { position = it },
                        label = { Text("Chức vụ") },
                        placeholder = { Text("Ví dụ: Giám đốc") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Phone & Email
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Số điện thoại") },
                        placeholder = { Text("0988 123 456") },
                        leadingIcon = { Icon(Icons.Default.Call, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        placeholder = { Text("contact@example.com") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Address
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Địa chỉ trụ sở / Chi nhánh") },
                    placeholder = { Text("Số nhà, đường, quận/huyện, thành phố...") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Customer Classification / Status Chips
                Text(
                    text = "Phân loại khách hàng:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CustomerStatus.entries.forEach { s ->
                        FilterChip(
                            selected = status == s,
                            onClick = { status = s },
                            label = { Text(s.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Source Selection
                Text(
                    text = "Nguồn khách hàng:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Giới thiệu", "Website", "Sự kiện", "Hotline", "Mạng xã hội", "Khác").forEach { src ->
                        FilterChip(
                            selected = source == src,
                            onClick = { source = src },
                            label = { Text(src) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Estimated Value & Tags
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = estimatedValueStr,
                        onValueChange = { estimatedValueStr = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Giá trị dự kiến (VNĐ)") },
                        placeholder = { Text("Ví dụ: 150000000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1.2f)
                    )
                    OutlinedTextField(
                        value = tags,
                        onValueChange = { tags = it },
                        label = { Text("Tags / Nhãn") },
                        placeholder = { Text("VIP, ERP...") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Personal Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Ghi chú cá nhân / Đặc điểm khách hàng") },
                    placeholder = { Text("Sở thích, lưu ý phong cách làm việc, yêu cầu bảo mật...") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                isError = true
                                return@Button
                            }
                            val estVal = estimatedValueStr.toDoubleOrNull() ?: 0.0
                            val newCust = (customer ?: CustomerEntity(name = name)).copy(
                                name = name.trim(),
                                company = company.trim(),
                                position = position.trim(),
                                phone = phone.trim(),
                                email = email.trim(),
                                address = address.trim(),
                                status = status.name,
                                source = source.trim(),
                                tags = tags.trim(),
                                estimatedValue = estVal,
                                notes = notes.trim(),
                                avatarColorHex = selectedColor,
                                updatedAt = System.currentTimeMillis()
                            )
                            onSave(newCust)
                        },
                        modifier = Modifier.testTag("save_customer_button")
                    ) {
                        Text(if (customer == null) "Thêm Khách Hàng" else "Lưu Thay Đổi")
                    }
                }
            }
        }
    }
}

// Add/Edit Deal Dialog
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditDealDialog(
    customers: List<CustomerEntity>,
    preselectedCustomerId: Long? = null,
    deal: DealEntity? = null,
    onDismiss: () -> Unit,
    onSave: (DealEntity) -> Unit
) {
    var title by remember { mutableStateOf(deal?.title ?: "") }
    var selectedCustomerId by remember { mutableStateOf(deal?.customerId ?: preselectedCustomerId ?: customers.firstOrNull()?.id ?: 0L) }
    var valueStr by remember { mutableStateOf(deal?.value?.toLong()?.toString() ?: "") }
    var stage by remember { mutableStateOf(deal?.let { DealStage.fromString(it.stage) } ?: DealStage.LEAD) }
    var probability by remember { mutableStateOf(deal?.probability ?: 20) }
    var notes by remember { mutableStateOf(deal?.notes ?: "") }

    var expandedDropdown by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (deal == null) "Tạo Cơ Hội Kinh Doanh (Deal)" else "Chỉnh Sửa Cơ Hội",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Theo dõi doanh số và tiến độ cơ hội bán hàng",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) isError = false
                    },
                    label = { Text("Tên cơ hội bán hàng *") },
                    placeholder = { Text("Ví dụ: Triển khai phần mềm ERP") },
                    isError = isError && title.isBlank(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_deal_title")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Customer Selection Dropdown
                val selectedCust = customers.find { it.id == selectedCustomerId }
                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCust?.let { "${it.name} - ${it.company.ifBlank { "Cá nhân" }}" } ?: "Chọn khách hàng",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Khách hàng *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        customers.forEach { cust ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(cust.name, fontWeight = FontWeight.Bold)
                                        if (cust.company.isNotBlank()) {
                                            Text(cust.company, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                },
                                onClick = {
                                    selectedCustomerId = cust.id
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Value and Probability
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = valueStr,
                        onValueChange = { valueStr = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Giá trị cơ hội (VNĐ) *") },
                        placeholder = { Text("150000000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("input_deal_value")
                    )
                    OutlinedTextField(
                        value = probability.toString(),
                        onValueChange = {
                            val v = it.filter { ch -> ch.isDigit() }.toIntOrNull() ?: 0
                            probability = v.coerceIn(0, 100)
                        },
                        label = { Text("Tỉ lệ (%)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stage Selector
                Text(
                    text = "Giai đoạn bán hàng:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    DealStage.entries.forEach { s ->
                        FilterChip(
                            selected = stage == s,
                            onClick = {
                                stage = s
                                if (deal == null) probability = s.defaultProbability
                            },
                            label = { Text(s.label) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Ghi chú đàm phán / Điều kiện thanh toán") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isBlank() || selectedCustomerId == 0L) {
                                isError = true
                                return@Button
                            }
                            val valDouble = valueStr.toDoubleOrNull() ?: 0.0
                            val newDeal = (deal ?: DealEntity(
                                customerId = selectedCustomerId,
                                title = title,
                                value = valDouble
                            )).copy(
                                customerId = selectedCustomerId,
                                title = title.trim(),
                                value = valDouble,
                                stage = stage.name,
                                probability = probability,
                                notes = notes.trim()
                            )
                            onSave(newDeal)
                        },
                        modifier = Modifier.testTag("save_deal_button")
                    ) {
                        Text(if (deal == null) "Tạo cơ hội" else "Lưu thay đổi")
                    }
                }
            }
        }
    }
}

// Log Interaction Dialog
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LogInteractionDialog(
    customerId: Long,
    customerName: String,
    onDismiss: () -> Unit,
    onSave: (InteractionEntity) -> Unit
) {
    val context = LocalContext.current
    var type by remember { mutableStateOf(InteractionType.CALL) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var outcome by remember { mutableStateOf("") }

    // Appointment Reminder options
    var enableReminder by remember { mutableStateOf(false) }
    var reminderMinutesBefore by remember { mutableIntStateOf(15) }
    val now = System.currentTimeMillis()
    val dateTimeFormatter = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    var appointmentTime by remember { mutableStateOf(now + 2 * 3600 * 1000L) }
    var appointmentTimeText by remember { mutableStateOf(dateTimeFormatter.format(Date(appointmentTime))) }

    var isError by remember { mutableStateOf(false) }

    fun showDateTimePicker() {
        val cal = Calendar.getInstance().apply { timeInMillis = appointmentTime }
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        val newCal = Calendar.getInstance().apply {
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, 0)
                        }
                        appointmentTime = newCal.timeInMillis
                        appointmentTimeText = dateTimeFormatter.format(Date(appointmentTime))
                    },
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Ghi Nhật Ký Tương Tác",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Khách hàng: $customerName",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Interaction Type chips
                Text(
                    text = "Hình thức tương tác:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    InteractionType.entries.forEach { t ->
                        FilterChip(
                            selected = type == t,
                            onClick = {
                                type = t
                                if (title.isBlank()) {
                                    title = when(t) {
                                        InteractionType.CALL -> "Cuộc gọi trao đổi nhu cầu"
                                        InteractionType.MEETING -> "Họp trao đổi trực tiếp"
                                        InteractionType.EMAIL -> "Gửi email báo giá và tài liệu"
                                        InteractionType.MESSAGE -> "Nhắn tin chăm sóc khách hàng"
                                        InteractionType.NOTE -> "Ghi chú tư vấn kỹ thuật"
                                        InteractionType.CONTRACT -> "Ký kết và bàn giao hợp đồng"
                                    }
                                }
                                if (t == InteractionType.MEETING || t == InteractionType.CALL) {
                                    enableReminder = true
                                }
                            },
                            label = { Text(t.label) },
                            leadingIcon = {
                                Icon(
                                    imageVector = getInteractionIcon(t),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) isError = false
                    },
                    label = { Text("Tiêu đề hoạt động *") },
                    placeholder = { Text("Ví dụ: Gọi trao đổi báo giá phần mềm") },
                    isError = isError && title.isBlank(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_interaction_title")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Content
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Nội dung chi tiết cuộc trao đổi") },
                    placeholder = { Text("Tóm tắt ý kiến khách hàng, các thắc mắc...") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Outcome
                OutlinedTextField(
                    value = outcome,
                    onValueChange = { outcome = it },
                    label = { Text("Kết quả & Bước tiếp theo") },
                    placeholder = { Text("Ví dụ: Khách đồng ý nhận demo thứ 4 tuần tới") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Appointment Reminder Card
                Surface(
                    color = if (enableReminder) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = if (enableReminder) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = if (enableReminder) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Nhắc nhở lịch hẹn đẩy",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Nhận thông báo push trước khi đến giờ họp",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Switch(
                                checked = enableReminder,
                                onCheckedChange = { enableReminder = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }

                        if (enableReminder) {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = appointmentTimeText,
                                onValueChange = { input ->
                                    appointmentTimeText = input
                                    try {
                                        val parsed = dateTimeFormatter.parse(input)
                                        if (parsed != null) appointmentTime = parsed.time
                                    } catch (e: Exception) {}
                                },
                                label = { Text("Thời gian lịch họp") },
                                trailingIcon = {
                                    IconButton(onClick = { showDateTimePicker() }) {
                                        Icon(Icons.Default.CalendarMonth, contentDescription = "Chọn ngày giờ")
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Thông báo trước:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(
                                    15 to "Trước 15p",
                                    30 to "Trước 30p",
                                    60 to "Trước 1h",
                                    1440 to "Trước 1 ngày"
                                ).forEach { (mins, label) ->
                                    FilterChip(
                                        selected = reminderMinutesBefore == mins,
                                        onClick = { reminderMinutesBefore = mins },
                                        label = { Text(label, style = MaterialTheme.typography.labelSmall) }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                isError = true
                                return@Button
                            }
                            val interaction = InteractionEntity(
                                customerId = customerId,
                                type = type.name,
                                title = title.trim(),
                                content = content.trim(),
                                outcome = outcome.trim(),
                                date = System.currentTimeMillis()
                            )
                            if (enableReminder) {
                                NotificationHelper.showAppointmentNotification(
                                    context = context,
                                    title = title.trim(),
                                    message = "Khách hàng $customerName (Nhắc trước $reminderMinutesBefore phút)",
                                    timeInfo = dateTimeFormatter.format(Date(appointmentTime)),
                                    notificationId = (System.currentTimeMillis() % 100000).toInt()
                                )
                            }
                            onSave(interaction)
                        },
                        modifier = Modifier.testTag("save_interaction_button")
                    ) {
                        Text("Lưu nhật ký")
                    }
                }
            }
        }
    }
}

// Add/Edit Task & Calendar Event Dialog
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskDialog(
    customers: List<CustomerEntity>,
    preselectedCustomerId: Long? = null,
    task: TaskEntity? = null,
    isCreatingEvent: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (TaskEntity) -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var selectedCustomerId by remember { mutableStateOf(task?.customerId ?: preselectedCustomerId) }
    var priority by remember { mutableStateOf(task?.let { TaskPriority.fromString(it.priority) } ?: TaskPriority.MEDIUM) }
    var taskType by remember {
        mutableStateOf(
            task?.let { TaskType.fromString(it.taskType) }
                ?: if (isCreatingEvent) TaskType.MEETING else TaskType.FOLLOW_UP
        )
    }
    var location by remember { mutableStateOf(task?.location ?: "") }

    // Due Date Selection with manual input & picker
    val now = System.currentTimeMillis()
    val oneHour = 3600 * 1000L
    val oneDay = 24 * 3600 * 1000L

    var selectedDueDate by remember {
        mutableStateOf(task?.dueDate ?: (now + if (isCreatingEvent) 2 * oneHour else oneDay))
    }

    val dateTimeFormatter = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    var timeManualText by remember { mutableStateOf(dateTimeFormatter.format(Date(selectedDueDate))) }
    var isTimeParseError by remember { mutableStateOf(false) }

    fun showDateTimePicker() {
        val cal = Calendar.getInstance().apply { timeInMillis = selectedDueDate }
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        val newCal = Calendar.getInstance().apply {
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, 0)
                        }
                        selectedDueDate = newCal.timeInMillis
                        timeManualText = dateTimeFormatter.format(Date(selectedDueDate))
                        isTimeParseError = false
                    },
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    var expandedDropdown by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (task != null) "Chỉnh Sửa Công Việc" else "Tạo Công Việc",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Lên lịch cuộc hẹn, cuộc gọi hoặc công việc chăm sóc khách hàng",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) isError = false
                    },
                    label = { Text("Tiêu đề công việc *") },
                    placeholder = {
                        Text(if (taskType.isCalendarEvent) "Ví dụ: Họp demo giải pháp CRM EcoMart" else "Ví dụ: Gọi lại tư vấn báo giá")
                    },
                    isError = isError && title.isBlank(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_task_title")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Customer Selection Dropdown
                val selectedCust = customers.find { it.id == selectedCustomerId }
                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCust?.let { "${it.name} (${it.company.ifBlank { "Cá nhân" }})" } ?: "Không gắn khách hàng cụ thể",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Khách hàng liên kết") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Không gắn khách hàng") },
                            onClick = {
                                selectedCustomerId = null
                                expandedDropdown = false
                            }
                        )
                        customers.forEach { cust ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(cust.name, fontWeight = FontWeight.Bold)
                                        if (cust.company.isNotBlank()) {
                                            Text(cust.company, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                },
                                onClick = {
                                    selectedCustomerId = cust.id
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Task / Event Type selection
                Text(
                    text = "Loại công việc:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TaskType.entries.forEach { t ->
                        FilterChip(
                            selected = taskType == t,
                            onClick = {
                                taskType = t
                                if (title.isBlank()) {
                                    title = when (t) {
                                        TaskType.MEETING -> "Cuộc họp trao đổi dự án"
                                        TaskType.CALL -> "Cuộc gọi tư vấn giải pháp"
                                        TaskType.DEMO -> "Demo trực tuyến tính năng mới"
                                        TaskType.CALL_BACK -> "Gọi lại chăm sóc khách hàng"
                                        TaskType.SEND_PROPOSAL -> "Gửi bảng báo giá chi tiết"
                                        TaskType.FOLLOW_UP -> "Theo dõi tiến độ sau bán hàng"
                                        TaskType.CONTRACT -> "Ký kết hợp đồng & bàn giao"
                                    }
                                }
                            },
                            label = { Text(t.label) },
                            leadingIcon = {
                                Icon(
                                    imageVector = getTaskTypeIcon(t),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Date & Time (Editable & Picker)
                Text(
                    text = "Thời gian:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = timeManualText,
                    onValueChange = { input ->
                        timeManualText = input
                        try {
                            val parsed = dateTimeFormatter.parse(input)
                            if (parsed != null) {
                                selectedDueDate = parsed.time
                                isTimeParseError = false
                            }
                        } catch (e: Exception) {
                            isTimeParseError = true
                        }
                    },
                    label = { Text("Thời gian thực hiện (dd/MM/yyyy HH:mm)") },
                    placeholder = { Text("Ví dụ: 28/08/2026 14:30") },
                    isError = isTimeParseError,
                    supportingText = if (isTimeParseError) {
                        { Text("Định dạng chưa đúng: dd/MM/yyyy HH:mm", color = MaterialTheme.colorScheme.error) }
                    } else null,
                    trailingIcon = {
                        IconButton(onClick = { showDateTimePicker() }) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Hiện lịch chọn ngày giờ",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_task_datetime")
                )

                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        "Hôm nay (+2h)" to (now + 2 * oneHour),
                        "Ngày mai" to (now + oneDay),
                        "3 ngày tới" to (now + 3 * oneDay),
                        "1 tuần tới" to (now + 7 * oneDay)
                    ).forEach { (label, timestamp) ->
                        FilterChip(
                            selected = isSameDay(selectedDueDate, timestamp),
                            onClick = {
                                selectedDueDate = timestamp
                                timeManualText = dateTimeFormatter.format(Date(timestamp))
                                isTimeParseError = false
                            },
                            label = { Text(label, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Location / Meeting Link
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Địa điểm / Link họp / Kênh liên hệ") },
                    placeholder = { Text("Ví dụ: Google Meet, Trụ sở Keangnam, SĐT...") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Priority
                Text(
                    text = "Mức độ ưu tiên:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TaskPriority.entries.forEach { p ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(p.label) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Nội dung chi tiết cần chuẩn bị") },
                    placeholder = { Text("Tài liệu mang theo, câu hỏi cần giải đáp, mục tiêu cuộc gặp...") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                isError = true
                                return@Button
                            }
                            val newTask = (task ?: TaskEntity(
                                title = title,
                                dueDate = selectedDueDate
                            )).copy(
                                customerId = selectedCustomerId,
                                title = title.trim(),
                                description = description.trim(),
                                dueDate = selectedDueDate,
                                priority = priority.name,
                                taskType = taskType.name,
                                location = location.trim()
                            )
                            onSave(newTask)
                        },
                        modifier = Modifier.testTag("save_task_button")
                    ) {
                        Text(if (task == null) "Tạo Công Việc" else "Lưu Thay Đổi")
                    }
                }
            }
        }
    }
}

// Dialog for reporting task execution outcome & rating (5 levels)
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TaskOutcomeReportDialog(
    task: TaskEntity,
    onDismiss: () -> Unit,
    onSave: (rating: Int, report: String) -> Unit
) {
    var rating by remember { mutableIntStateOf(if (task.resultRating > 0) task.resultRating else 4) }
    var report by remember { mutableStateOf(task.resultSummary) }

    val ratingLevels = listOf(
        1 to Pair("Kém / Thất bại", "🔴"),
        2 to Pair("Dưới kỳ vọng", "🟡"),
        3 to Pair("Đạt yêu cầu", "🟢"),
        4 to Pair("Tốt / Thành công", "🔵"),
        5 to Pair("Xuất sắc / Vượt kỳ vọng", "🌟")
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Báo Cáo Kết Quả Thực Hiện",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Công việc: ${task.title}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 5-Level Rating Selector
                Text(
                    text = "Mức độ thành công (5 mức):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Star Rating Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        IconButton(
                            onClick = { rating = i },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = if (i <= rating) Icons.Default.Star else Icons.Outlined.StarOutline,
                                contentDescription = "Mức $i",
                                tint = if (i <= rating) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                // Selected level text badge
                val currentLevel = ratingLevels.find { it.first == rating }?.second
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${currentLevel?.second} Mức $rating: ${currentLevel?.first}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quick level selection chips
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ratingLevels.forEach { (lvl, pair) ->
                        FilterChip(
                            selected = rating == lvl,
                            onClick = { rating = lvl },
                            label = { Text("${pair.second} Mức $lvl") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Outcome Report details
                OutlinedTextField(
                    value = report,
                    onValueChange = { report = it },
                    label = { Text("Báo cáo kết quả chi tiết") },
                    placeholder = { Text("Ví dụ: Khách hàng đồng ý ký hợp đồng gói 6 tháng, thanh toán đợt 1 vào tuần sau...") },
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth().testTag("input_outcome_report")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick template buttons
                Text(
                    text = "Gợi ý nhanh kết quả:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf(
                        "Đã chốt hợp đồng thành công",
                        "Khách hàng hẹn gặp lại đợt tới",
                        "Đã gửi báo giá và tài liệu demo",
                        "Khách từ chối do vượt ngân sách"
                    ).forEach { quickText ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.clickable {
                                report = if (report.isBlank()) quickText else "$report\n$quickText"
                            }
                        ) {
                            Text(
                                text = quickText,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Đóng")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSave(rating, report.trim())
                        },
                        modifier = Modifier.testTag("save_outcome_button")
                    ) {
                        Text("Lưu Báo Cáo")
                    }
                }
            }
        }
    }
}

// Fast stage change dialog
@Composable
fun AdvanceDealStageDialog(
    currentStage: DealStage,
    onDismiss: () -> Unit,
    onSelectStage: (DealStage) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Chuyển Giai Đoạn Bán Hàng", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Chọn giai đoạn tiếp theo cho cơ hội này:")
                DealStage.entries.forEach { stage ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (stage == currentStage) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectStage(stage) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stage.label,
                                fontWeight = if (stage == currentStage) FontWeight.Bold else FontWeight.Normal,
                                color = if (stage == currentStage) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            DealStageBadge(stage = stage)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng")
            }
        }
    )
}

// Confirmation Dialog
@Composable
fun ConfirmDeleteDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Xóa vĩnh viễn")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
