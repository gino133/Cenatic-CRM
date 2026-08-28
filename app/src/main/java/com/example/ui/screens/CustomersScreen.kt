package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CustomerEntity
import com.example.data.model.CustomerStatus
import com.example.data.model.CustomerWithDetails
import com.example.ui.components.AdvancedCustomerFilterSheet
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.CustomerAvatar
import com.example.ui.components.StatusBadge
import com.example.ui.components.formatCurrencyVND
import com.example.ui.components.formatRelativeTime
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.viewmodel.CrmViewModel
import com.example.ui.viewmodel.CustomerSort

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CustomersScreen(
    viewModel: CrmViewModel,
    onCustomerClick: (Long) -> Unit,
    onEditCustomer: (CustomerEntity) -> Unit,
    onAddCorporateClick: () -> Unit,
    onAddIndividualClick: () -> Unit
) {
    val context = LocalContext.current
    val customers by viewModel.customersWithDetails.collectAsStateWithLifecycle()
    val customerTypes by viewModel.allCustomerTypes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.customerSearchQuery.collectAsStateWithLifecycle()
    val selectedStatus by viewModel.selectedStatusFilter.collectAsStateWithLifecycle()
    val selectedCustomType by viewModel.selectedCustomTypeFilter.collectAsStateWithLifecycle()
    val selectedTimeRange by viewModel.selectedTimeRangeFilter.collectAsStateWithLifecycle()
    val selectedValueRange by viewModel.selectedValueRangeFilter.collectAsStateWithLifecycle()
    val selectedSource by viewModel.selectedSourceFilter.collectAsStateWithLifecycle()
    val currentSort by viewModel.customerSort.collectAsStateWithLifecycle()
    val activeFilterCount by viewModel.activeCustomerFilterCount.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Tất cả, 1: Doanh nghiệp, 2: Cá nhân
    var showFilterSheet by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var customerToDelete by remember { mutableStateOf<CustomerEntity?>(null) }
    var showAddMenu by remember { mutableStateOf(false) }

    val filteredCustomers = remember(customers, selectedTab) {
        when (selectedTab) {
            1 -> customers.filter { it.customer.isCorporate }
            2 -> customers.filter { !it.customer.isCorporate }
            else -> customers
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FB))
                .testTag("customers_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Search Box and Filter Button Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setCustomerSearchQuery(it) },
                        placeholder = { Text("Tìm tên, công ty, email, SĐT...", fontSize = 14.sp, color = Color(0xFF94A3B8)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Tìm kiếm", tint = Color(0xFF64748B)) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setCustomerSearchQuery("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Xóa", tint = Color(0xFF64748B))
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF0F172A),
                            unfocusedTextColor = Color(0xFF0F172A),
                            cursorColor = ProfessionalPrimary,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = ProfessionalPrimary,
                            unfocusedBorderColor = Color(0xFFCBD5E1),
                            focusedPlaceholderColor = Color(0xFF94A3B8),
                            unfocusedPlaceholderColor = Color(0xFF94A3B8),
                            focusedLeadingIconColor = Color(0xFF64748B),
                            unfocusedLeadingIconColor = Color(0xFF64748B),
                            focusedTrailingIconColor = Color(0xFF64748B),
                            unfocusedTrailingIconColor = Color(0xFF64748B)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("search_customer_input")
                    )

                    // Filter Button with Badge
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (activeFilterCount > 0) Color(0xFFE0E7FF) else Color.White,
                        tonalElevation = 1.dp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { showFilterSheet = true }
                    ) {
                        Box(
                            modifier = Modifier.padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (activeFilterCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(containerColor = ProfessionalPrimary) {
                                            Text(activeFilterCount.toString(), color = Color.White)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "Bộ lọc nâng cao",
                                        tint = ProfessionalPrimary
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = "Bộ lọc",
                                    tint = Color(0xFF475569)
                                )
                            }
                        }
                    }
                }
            }

            // 2. Customer Type Segmented Tab Row (Tất cả / Doanh nghiệp / Cá nhân)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE2E8F0))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val tabs = listOf("Tất cả (${customers.size})", "Doanh nghiệp", "Cá nhân")
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color.White else Color.Transparent)
                                .clickable { selectedTab = index }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color(0xFF0F172A) else Color(0xFF64748B)
                            )
                        }
                    }
                }
            }

            // 3. Quick Status Chips & Sort Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedStatus == null && selectedCustomType == null,
                                onClick = {
                                    viewModel.setStatusFilter(null)
                                    viewModel.setCustomTypeFilter(null)
                                },
                                label = { Text("Tất cả", fontSize = 12.sp) },
                                shape = RoundedCornerShape(20.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFE2E8F0),
                                    selectedLabelColor = Color(0xFF0F172A),
                                    labelColor = Color(0xFF0F172A),
                                    containerColor = Color.White
                                )
                            )
                        }
                        if (customerTypes.isNotEmpty()) {
                            items(customerTypes, key = { it.id }) { type ->
                                val isSelected = selectedCustomType == type.name ||
                                        selectedCustomType == type.code ||
                                        selectedStatus?.name.equals(type.code, ignoreCase = true)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        if (isSelected) {
                                            viewModel.setStatusFilter(null)
                                            viewModel.setCustomTypeFilter(null)
                                        } else {
                                            val matchingStatus = CustomerStatus.entries.find {
                                                it.name.equals(type.code, ignoreCase = true)
                                            }
                                            if (matchingStatus != null) {
                                                viewModel.setStatusFilter(matchingStatus)
                                            } else {
                                                viewModel.setCustomTypeFilter(type.name)
                                            }
                                        }
                                    },
                                    label = { Text(type.name, fontSize = 12.sp) },
                                    shape = RoundedCornerShape(20.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFFE2E8F0),
                                        selectedLabelColor = Color(0xFF0F172A),
                                        labelColor = Color(0xFF0F172A),
                                        containerColor = Color.White
                                    )
                                )
                            }
                        } else {
                            items(CustomerStatus.entries) { status ->
                                FilterChip(
                                    selected = selectedStatus == status,
                                    onClick = { viewModel.setStatusFilter(if (selectedStatus == status) null else status) },
                                    label = { Text(status.label, fontSize = 12.sp) },
                                    shape = RoundedCornerShape(20.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFFE2E8F0),
                                        selectedLabelColor = Color(0xFF0F172A),
                                        labelColor = Color(0xFF0F172A),
                                        containerColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Box {
                        IconButton(
                            onClick = { showSortMenu = true },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            Icon(Icons.Default.Sort, contentDescription = "Sắp xếp", tint = Color(0xFF475569))
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            CustomerSort.entries.forEach { sortOption ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = sortOption.label,
                                            fontWeight = if (sortOption == currentSort) FontWeight.Bold else FontWeight.Normal,
                                            color = if (sortOption == currentSort) ProfessionalPrimary else Color(0xFF0F172A)
                                        )
                                    },
                                    onClick = {
                                        viewModel.setCustomerSort(sortOption)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 4. Customer List
            if (filteredCustomers.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = ProfessionalPrimary,
                                modifier = Modifier.size(52.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (searchQuery.isNotBlank() || activeFilterCount > 0) "Không tìm thấy khách hàng phù hợp" else "Chưa có khách hàng nào",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (searchQuery.isNotBlank() || activeFilterCount > 0) "Thử thay đổi từ khóa tìm kiếm hoặc bấm 'Bỏ lọc'" else "Nhấn nút thêm để tạo hồ sơ khách hàng doanh nghiệp hoặc cá nhân",
                                fontSize = 13.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            } else {
                items(filteredCustomers, key = { it.customer.id }) { item ->
                    CustomerCard(
                        item = item,
                        onClick = { onCustomerClick(item.customer.id) },
                        onEdit = { onEditCustomer(item.customer) },
                        onDelete = { customerToDelete = item.customer },
                        onCall = { phone ->
                            if (phone.isNotBlank()) {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.replace(" ", "")}"))
                                try { context.startActivity(intent) } catch (e: Exception) {
                                    Toast.makeText(context, "Không thể mở cuộc gọi", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        onSms = { phone ->
                            if (phone.isNotBlank()) {
                                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${phone.replace(" ", "")}"))
                                try { context.startActivity(intent) } catch (e: Exception) {
                                    Toast.makeText(context, "Không thể mở SMS", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        onEmail = { email ->
                            if (email.isNotBlank()) {
                                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
                                try { context.startActivity(intent) } catch (e: Exception) {
                                    Toast.makeText(context, "Không thể mở Email", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        // Advanced Filter Bottom Sheet
        if (showFilterSheet) {
            AdvancedCustomerFilterSheet(
                currentStatus = selectedStatus,
                currentTimeRange = selectedTimeRange,
                currentValueRange = selectedValueRange,
                currentSource = selectedSource,
                currentSort = currentSort,
                onDismiss = { showFilterSheet = false },
                onApply = { status, time, value, source, sort ->
                    viewModel.setStatusFilter(status)
                    viewModel.setTimeRangeFilter(time)
                    viewModel.setValueRangeFilter(value)
                    viewModel.setSourceFilter(source)
                    viewModel.setCustomerSort(sort)
                },
                onReset = {
                    viewModel.resetCustomerFilters()
                }
            )
        }

        // Delete Dialog
        customerToDelete?.let { cust ->
            ConfirmDeleteDialog(
                title = "Xóa khách hàng",
                message = "Bạn có chắc chắn muốn xóa khách hàng '${cust.name}'? Mọi cơ hội và dữ liệu liên quan sẽ bị xóa.",
                onConfirm = {
                    viewModel.deleteCustomer(cust.id)
                    customerToDelete = null
                },
                onDismiss = { customerToDelete = null }
            )
        }
    }
}

@Composable
fun CustomerCard(
    item: CustomerWithDetails,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCall: (String) -> Unit,
    onSms: (String) -> Unit,
    onEmail: (String) -> Unit
) {
    val cust = item.customer
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("customer_card_${cust.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header row: Avatar, Name, Corporate/Individual Tag, More Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomerAvatar(
                    name = cust.name,
                    avatarColorHex = cust.avatarColorHex,
                    size = 46.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = cust.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Corporate vs Individual icon tag
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (cust.isCorporate) Color(0xFFEFF6FF) else Color(0xFFECFDF5)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (cust.isCorporate) "🏢 DN" else "👤 CN",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (cust.isCorporate) ProfessionalPrimary else Color(0xFF059669)
                                )
                            }
                        }
                    }

                    if (cust.company.isNotBlank() || cust.position.isNotBlank()) {
                        Text(
                            text = listOf(cust.position, cust.company).filter { it.isNotBlank() }.joinToString(" • "),
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                StatusBadge(status = CustomerStatus.fromString(cust.status))

                Box {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Tùy chọn", tint = Color(0xFF64748B))
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Chỉnh sửa") },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                            onClick = {
                                menuExpanded = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Xóa khách hàng", color = Color(0xFFEF4444)) },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444)) },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            // Contact details (Phone, Email, Address)
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (cust.phone.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📞", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = cust.phone,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF334155)
                        )
                    }
                }
                if (cust.email.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("✉️", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = cust.email,
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (cust.address.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📍", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = cust.address,
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Action buttons (Call, SMS, Email)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (cust.phone.isNotBlank()) {
                    IconButton(
                        onClick = { onCall(cust.phone) },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Gọi điện", tint = ProfessionalPrimary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = { onSms(cust.phone) },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.Sms, contentDescription = "SMS", tint = Color(0xFF0EA5E9), modifier = Modifier.size(18.dp))
                    }
                }
                if (cust.email.isNotBlank()) {
                    IconButton(
                        onClick = { onEmail(cust.email) },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.Email, contentDescription = "Email", tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
