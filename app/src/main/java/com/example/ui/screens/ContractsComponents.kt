package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AnnexItem
import com.example.data.model.AnnexPosition
import com.example.data.model.ContractAnnex
import com.example.data.model.ContractItem
import com.example.data.model.ContractNamingRule
import com.example.data.model.ContractStatus
import com.example.data.model.ProjectProgressItem
import com.example.data.model.QuoteItem
import com.example.ui.components.crmTextFieldColors
import com.example.ui.components.formatFullCurrencyVND
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.theme.ProfessionalPrimaryNavy
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Helper to determine contract status linked to project progress
fun getContractProgressStatus(
    contract: ContractItem,
    projects: List<ProjectProgressItem>
): Triple<String, Color, Color> {
    if (contract.status == ContractStatus.DRAFT) {
        return Triple("Dự thảo", Color(0xFF475569), Color(0xFFF1F5F9))
    }
    val matchingProject = projects.find {
        (contract.quoteId != null && it.quoteId == contract.quoteId) ||
                (it.title.equals(contract.title, ignoreCase = true) && it.customerName.equals(contract.customerName, ignoreCase = true))
    }
    return if (matchingProject != null) {
        if (matchingProject.progressPercent >= 100) {
            Triple("Đã thanh lý", Color(0xFF0E7490), Color(0xFFCFFAFE))
        } else {
            Triple("Đang thực hiện (${matchingProject.progressPercent}%)", Color(0xFF1E429F), Color(0xFFE1EFFE))
        }
    } else {
        if (contract.status == ContractStatus.COMPLETED) {
            Triple("Đã thanh lý", Color(0xFF0E7490), Color(0xFFCFFAFE))
        } else {
            Triple("Đang thực hiện", Color(0xFF1E429F), Color(0xFFE1EFFE))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContractsListView(
    contracts: List<ContractItem>,
    namingRule: ContractNamingRule,
    projects: List<ProjectProgressItem> = emptyList(),
    onSignContract: (ContractItem) -> Unit,
    onOpenContractDetail: (ContractItem) -> Unit,
    onOpenAnnexDialog: (ContractItem) -> Unit,
    onOpenNamingRuleDialog: () -> Unit,
    onOpenCreateContract: () -> Unit,
    onDeleteContract: (Long) -> Unit,
    onGoToProject: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf("ALL") } // ALL, IN_PROGRESS, COMPLETED, DRAFT, ANNEX

    val filteredContracts = remember(contracts, searchQuery, statusFilter, projects) {
        contracts.filter { contract ->
            val matchQuery = searchQuery.isBlank() ||
                    contract.contractNumber.contains(searchQuery, ignoreCase = true) ||
                    contract.customerName.contains(searchQuery, ignoreCase = true) ||
                    contract.title.contains(searchQuery, ignoreCase = true)

            val (statusText, _, _) = getContractProgressStatus(contract, projects)
            val isCompleted = statusText.startsWith("Đã thanh lý")
            val isInProgress = statusText.startsWith("Đang thực hiện")
            val isDraft = contract.status == ContractStatus.DRAFT

            val matchFilter = when (statusFilter) {
                "IN_PROGRESS" -> isInProgress
                "COMPLETED" -> isCompleted
                "DRAFT" -> isDraft
                "ANNEX" -> contract.annexes.isNotEmpty()
                else -> true
            }

            matchQuery && matchFilter
        }
    }

    var contractForExportDialog by remember { mutableStateOf<ContractItem?>(null) }

    val inProgressCount = remember(contracts, projects) {
        contracts.count { contract ->
            val (statusText, _, _) = getContractProgressStatus(contract, projects)
            statusText.startsWith("Đang thực hiện")
        }
    }
    val completedCount = remember(contracts, projects) {
        contracts.count { contract ->
            val (statusText, _, _) = getContractProgressStatus(contract, projects)
            statusText.startsWith("Đã thanh lý")
        }
    }
    val draftCount = remember(contracts) { contracts.count { it.status == ContractStatus.DRAFT } }
    val annexCount = remember(contracts) { contracts.count { it.annexes.isNotEmpty() } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Action Toolbar & Search
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        Text(
                            text = "Danh sách Hợp đồng",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Theo dõi ký kết, tiến độ & phụ lục bổ sung",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    OutlinedButton(
                        onClick = onOpenNamingRuleDialog,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_contract_naming_rule")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = ProfessionalPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Quy tắc số HĐ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ProfessionalPrimary,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Tìm theo số HĐ, khách hàng, tên dự án...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    colors = crmTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Filter Chips Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        label = "Tất cả (${contracts.size})",
                        isSelected = statusFilter == "ALL",
                        onClick = { statusFilter = "ALL" }
                    )
                    FilterChip(
                        label = "Đang thực hiện ($inProgressCount)",
                        isSelected = statusFilter == "IN_PROGRESS",
                        onClick = { statusFilter = "IN_PROGRESS" }
                    )
                    FilterChip(
                        label = "Đã thanh lý ($completedCount)",
                        isSelected = statusFilter == "COMPLETED",
                        onClick = { statusFilter = "COMPLETED" }
                    )
                    FilterChip(
                        label = "Dự thảo ($draftCount)",
                        isSelected = statusFilter == "DRAFT",
                        onClick = { statusFilter = "DRAFT" }
                    )
                    FilterChip(
                        label = "Có phụ lục ($annexCount)",
                        isSelected = statusFilter == "ANNEX",
                        onClick = { statusFilter = "ANNEX" }
                    )
                }
            }
        }

        if (filteredContracts.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = Color(0xFFCBD5E1),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (searchQuery.isNotBlank() || statusFilter != "ALL") "Không tìm thấy hợp đồng phù hợp" else "Chưa có hợp đồng nào",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Khi bạn chốt báo giá và chọn 'Ký hợp đồng', hợp đồng mới sẽ tự động được tạo tại đây.",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(items = filteredContracts, key = { it.id }) { contract ->
                val displayStatus = getContractProgressStatus(contract, projects)
                ContractCard(
                    contract = contract,
                    displayStatus = displayStatus,
                    onSign = { onSignContract(contract) },
                    onOpenDetail = { onOpenContractDetail(contract) },
                    onAddAnnex = { onOpenAnnexDialog(contract) },
                    onExport = { contractForExportDialog = contract },
                    onDelete = { onDeleteContract(contract.id) },
                    onGoToProject = onGoToProject
                )
            }
        }

        item { Spacer(modifier = Modifier.height(90.dp)) }
    }

    if (contractForExportDialog != null) {
        ContractExportDialog(
            contract = contractForExportDialog!!,
            onDismiss = { contractForExportDialog = null }
        )
    }
}

@Composable
fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) ProfessionalPrimaryNavy else Color.White)
            .border(1.dp, if (isSelected) ProfessionalPrimaryNavy else Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF475569)
        )
    }
}

@Composable
fun ContractCard(
    contract: ContractItem,
    displayStatus: Triple<String, Color, Color>? = null,
    onSign: () -> Unit,
    onOpenDetail: () -> Unit,
    onAddAnnex: () -> Unit,
    onExport: () -> Unit = {},
    onDelete: () -> Unit,
    onGoToProject: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    val formattedTotal = formatFullCurrencyVND(contract.currentAmount)
    val formattedOriginal = formatFullCurrencyVND(contract.originalAmount)
    val annexDelta = contract.totalAnnexAdjustment

    val statusBadgeBg = displayStatus?.third ?: Color(android.graphics.Color.parseColor(contract.status.badgeBgHex))
    val statusBadgeText = displayStatus?.second ?: Color(android.graphics.Color.parseColor(contract.status.badgeColorHex))
    val statusBadgeLabel = displayStatus?.first ?: contract.status.label

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Contract Number + Status + Options Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = contract.contractNumber,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ProfessionalPrimaryNavy
                        )
                    }

                    if (contract.annexes.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${contract.annexes.size} phụ lục",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(statusBadgeBg)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = statusBadgeLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusBadgeText
                        )
                    }

                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = ProfessionalPrimary) },
                                text = { Text("Xem & Sửa điều khoản") },
                                onClick = {
                                    showMenu = false
                                    onOpenDetail()
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.FileDownload, contentDescription = null, tint = Color(0xFF2563EB)) },
                                text = { Text("Xuất file Word / PDF") },
                                onClick = {
                                    showMenu = false
                                    onExport()
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.NoteAdd, contentDescription = null, tint = Color(0xFF047857)) },
                                text = { Text("Ký thêm phụ lục") },
                                onClick = {
                                    showMenu = false
                                    onAddAnnex()
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE02424)) },
                                text = { Text("Xóa hợp đồng", color = Color(0xFFE02424)) },
                                onClick = {
                                    showMenu = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title & Customer
            Text(
                text = contract.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Business, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = contract.customerName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF475569)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Financial & Date Summary Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Giá trị Hợp đồng:", fontSize = 12.sp, color = Color(0xFF64748B))
                        Text(
                            text = formattedTotal,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ProfessionalPrimaryNavy
                        )
                    }

                    if (contract.annexes.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("• Gốc: $formattedOriginal", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            val deltaSign = if (annexDelta >= 0) "+${formatFullCurrencyVND(annexDelta)}" else formatFullCurrencyVND(annexDelta)
                            val deltaColor = if (annexDelta >= 0) Color(0xFF047857) else Color(0xFFE02424)
                            Text("• Phụ lục: $deltaSign", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = deltaColor)
                        }
                    }

                    HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 0.8.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ngày ký: ${contract.signedDate.ifBlank { "Chưa ký" }}", fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                        Text(
                            text = "Xem điều khoản ▾",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ProfessionalPrimary,
                            modifier = Modifier.clickable { isExpanded = !isExpanded }
                        )
                    }
                }
            }

            // Expanded Terms Snapshot
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TermSnapshotRow(title = "Thanh toán", content = contract.paymentTerms)
                    TermSnapshotRow(title = "Tiến độ", content = contract.deliveryTerms)
                    TermSnapshotRow(title = "Bảo hành", content = contract.warrantyTerms)

                    if (contract.annexes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Danh sách Phụ lục:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        contract.annexes.forEach { annex ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("• ${annex.annexNumber}: ${annex.title}", fontSize = 11.sp, color = Color(0xFF475569), modifier = Modifier.weight(1f))
                                val changeText = if (annex.netChange >= 0) "+${formatFullCurrencyVND(annex.netChange)}" else formatFullCurrencyVND(annex.netChange)
                                Text(changeText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (annex.netChange >= 0) Color(0xFF047857) else Color(0xFFE02424))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            if (contract.status == ContractStatus.DRAFT) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onOpenDetail,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sửa điều khoản", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onSign,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Handshake, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ký hợp đồng", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onAddAnnex,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.NoteAdd, contentDescription = null, modifier = Modifier.size(14.dp), tint = ProfessionalPrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Ký phụ lục", fontSize = 12.sp, color = ProfessionalPrimary)
                    }

                    Button(
                        onClick = onGoToProject,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Xem tiến độ", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ProfessionalPrimaryNavy)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = ProfessionalPrimaryNavy, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TermSnapshotRow(title: String, content: String) {
    Column {
        Text(
            text = "$title:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155)
        )
        Text(
            text = content.lineSequence().firstOrNull() ?: content,
            fontSize = 11.sp,
            color = Color(0xFF64748B),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Dialog when user accepts a Quote: Chooses "Ký hợp đồng" or "Thực hiện theo báo giá"
 */
@Composable
fun QuoteAcceptanceChoiceDialog(
    quote: QuoteItem,
    onSignContract: () -> Unit,
    onProceedByQuote: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDEF7EC)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = Color(0xFF047857),
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Chốt Báo Giá Thành Công!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Báo giá ${quote.quoteNumber} (${formatFullCurrencyVND(quote.amount)})\nKhách hàng: ${quote.customerName}",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Lựa chọn phương thức triển khai dự án:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Option 1: Ký hợp đồng (Recommended)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.5.dp, Color(0xFF2563EB), RoundedCornerShape(14.dp))
                        .clickable { onSignContract() },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF2563EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Ký Hợp Đồng",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFDBEAFE))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Khuyên dùng", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E40AF))
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Tự động gán số HĐ, điều khoản thanh toán, bảo hành & quản lý phụ lục.",
                                fontSize = 11.sp,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Option 2: Thực hiện theo báo giá
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                        .clickable { onProceedByQuote() },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF64748B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Assignment, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Thực Hiện Theo Báo Giá",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Bỏ qua lập hợp đồng, chuyển trực tiếp đến theo dõi tiến độ dự án.",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Để sau / Đóng", color = Color(0xFF64748B), fontSize = 13.sp)
                }
            }
        }
    }
}

/**
 * Dialog for Contract Details, Terms and Signing
 */
@Composable
fun ContractDetailDialog(
    contract: ContractItem,
    namingRule: ContractNamingRule,
    onSave: (ContractItem) -> Unit,
    onSign: (ContractItem) -> Unit,
    onDismiss: () -> Unit
) {
    var contractNumber by remember { mutableStateOf(contract.contractNumber) }
    var title by remember { mutableStateOf(contract.title) }
    var paymentTerms by remember { mutableStateOf(contract.paymentTerms) }
    var deliveryTerms by remember { mutableStateOf(contract.deliveryTerms) }
    var warrantyTerms by remember { mutableStateOf(contract.warrantyTerms) }
    var notes by remember { mutableStateOf(contract.notes) }
    var signedDate by remember { mutableStateOf(contract.signedDate.ifBlank { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()) }) }
    var showExportDialog by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .heightIn(max = 680.dp)
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFEFF6FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = ProfessionalPrimary)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (contract.status == ContractStatus.DRAFT) "Soạn Thảo & Ký Hợp Đồng" else "Chi Tiết Hợp Đồng",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Khách hàng: ${contract.customerName}",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { showExportDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(Icons.Default.FileDownload, contentDescription = null, tint = ProfessionalPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Xuất Word/PDF", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ProfessionalPrimary)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF94A3B8))
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE2E8F0))

                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Contract Number
                    OutlinedTextField(
                        value = contractNumber,
                        onValueChange = { contractNumber = it },
                        label = { Text("Số Hợp Đồng *") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = ProfessionalPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Tên / Nội dung Hợp Đồng *") },
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Financial Summary Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Giá trị Hợp đồng:", fontSize = 12.sp, color = Color(0xFF64748B))
                                Text(
                                    text = formatFullCurrencyVND(contract.currentAmount),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ProfessionalPrimaryNavy
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Ngày lập/ký:", fontSize = 12.sp, color = Color(0xFF64748B))
                                Text(
                                    text = signedDate,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0F172A)
                                )
                            }
                        }
                    }

                    // 1. Payment Terms (Điều khoản thanh toán)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "1. ĐIỀU KHOẢN THANH TOÁN",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF334155)
                            )
                            Text(
                                text = "Chuẩn 3 đợt",
                                fontSize = 11.sp,
                                color = ProfessionalPrimary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable {
                                    paymentTerms = "• Đợt 1: Tạm ứng 30% ngay sau khi ký hợp đồng.\n• Đợt 2: Thanh toán 40% khi hoàn thành 50% khối lượng công việc.\n• Đợt 3: Quyết toán 30% còn lại trong vòng 07 ngày kể từ khi ký biên bản nghiệm thu."
                                }
                            )
                        }
                        OutlinedTextField(
                            value = paymentTerms,
                            onValueChange = { paymentTerms = it },
                            minLines = 3,
                            colors = crmTextFieldColors(),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // 2. Delivery Terms (Tiến độ giao hàng / Triển khai)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "2. TIẾN ĐỘ GIAO HÀNG / TRIỂN KHAI",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF334155)
                            )
                            Text(
                                text = "Chuẩn 30 ngày",
                                fontSize = 11.sp,
                                color = ProfessionalPrimary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable {
                                    deliveryTerms = "• Thời gian triển khai & bàn giao: Trong vòng 30 ngày làm việc kể từ ngày nhận đủ tiền tạm ứng đợt 1.\n• Địa điểm bàn giao: Trực tiếp tại trụ sở Bên Mua hoặc qua hệ thống số."
                                }
                            )
                        }
                        OutlinedTextField(
                            value = deliveryTerms,
                            onValueChange = { deliveryTerms = it },
                            minLines = 3,
                            colors = crmTextFieldColors(),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // 3. Warranty Terms (Chính sách bảo hành)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "3. CHÍNH SÁCH BẢO HÀNH & HỖ TRỢ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF334155)
                            )
                            Text(
                                text = "Chuẩn 12 tháng",
                                fontSize = 11.sp,
                                color = ProfessionalPrimary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable {
                                    warrantyTerms = "• Thời gian bảo hành: 12 tháng kể từ ngày ký biên bản nghiệm thu.\n• Hỗ trợ kỹ thuật và phản hồi khắc phục sự cố trong vòng 04 giờ làm việc."
                                }
                            )
                        }
                        OutlinedTextField(
                            value = warrantyTerms,
                            onValueChange = { warrantyTerms = it },
                            minLines = 3,
                            colors = crmTextFieldColors(),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Notes
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Ghi chú bổ sung (Tùy chọn)") },
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE2E8F0))

                // Bottom Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val updated = contract.copy(
                                contractNumber = contractNumber.trim(),
                                title = title.trim(),
                                paymentTerms = paymentTerms.trim(),
                                deliveryTerms = deliveryTerms.trim(),
                                warrantyTerms = warrantyTerms.trim(),
                                notes = notes.trim(),
                                signedDate = signedDate
                            )
                            onSave(updated)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Lưu nháp", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            val updated = contract.copy(
                                contractNumber = contractNumber.trim(),
                                title = title.trim(),
                                paymentTerms = paymentTerms.trim(),
                                deliveryTerms = deliveryTerms.trim(),
                                warrantyTerms = warrantyTerms.trim(),
                                notes = notes.trim(),
                                signedDate = signedDate,
                                status = ContractStatus.SIGNED
                            )
                            onSign(updated)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (contract.status == ContractStatus.DRAFT) "Ký hợp đồng & Tiến độ" else "Cập nhật & Tiến độ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    if (showExportDialog) {
        ContractExportDialog(
            contract = contract.copy(
                contractNumber = contractNumber.trim(),
                title = title.trim(),
                paymentTerms = paymentTerms.trim(),
                deliveryTerms = deliveryTerms.trim(),
                warrantyTerms = warrantyTerms.trim(),
                notes = notes.trim(),
                signedDate = signedDate
            ),
            onDismiss = { showExportDialog = false }
        )
    }
}

/**
 * Dialog for Annex & Quotation Amendment (Phụ lục hợp đồng & Bổ sung báo giá với 2 bảng Tăng/Giảm)
 */
@Composable
fun ContractAnnexDialog(
    contract: ContractItem,
    namingRule: ContractNamingRule,
    onSaveAnnex: (ContractAnnex) -> Unit,
    onDismiss: () -> Unit
) {
    val defaultAnnexNumber = remember(contract) { namingRule.generateNextAnnexNumber(contract) }
    var annexNumber by remember { mutableStateOf(defaultAnnexNumber) }
    var title by remember { mutableStateOf("Phụ lục 0${contract.annexes.size + 1}: Điều chỉnh & bổ sung phạm vi công việc") }
    val todayStr = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()) }

    // Lists of increase & decrease items
    val increaseItems = remember { mutableStateListOf<AnnexItem>() }
    val decreaseItems = remember { mutableStateListOf<AnnexItem>() }

    // Forms for adding item
    var incName by remember { mutableStateOf("") }
    var incQtyStr by remember { mutableStateOf("1") }
    var incUnit by remember { mutableStateOf("Gói") }
    var incPriceStr by remember { mutableStateOf("") }

    var decName by remember { mutableStateOf("") }
    var decQtyStr by remember { mutableStateOf("1") }
    var decUnit by remember { mutableStateOf("Gói") }
    var decPriceStr by remember { mutableStateOf("") }

    // Additional terms
    var annexPaymentTerms by remember { mutableStateOf("Thanh toán 100% giá trị phụ lục trong đợt tiếp theo của Hợp đồng chính.") }
    var annexDeliveryTerms by remember { mutableStateOf("Tiến độ thực hiện điều chỉnh bổ sung 15 ngày làm việc.") }
    var annexWarrantyTerms by remember { mutableStateOf("Bảo hành đồng bộ theo thời hạn của Hợp đồng chính.") }
    var notes by remember { mutableStateOf("") }

    val totalIncrease by remember { derivedStateOf { increaseItems.sumOf { it.totalPrice } } }
    val totalDecrease by remember { derivedStateOf { decreaseItems.sumOf { it.totalPrice } } }
    val netChange by remember { derivedStateOf { totalIncrease - totalDecrease } }
    val newContractAmount by remember { derivedStateOf { contract.currentAmount + netChange } }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = 720.dp)
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF3C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.NoteAdd, contentDescription = null, tint = Color(0xFFB45309))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Ký Phụ Lục Hợp Đồng",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "HĐ: ${contract.contractNumber} - ${contract.customerName}",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF94A3B8))
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFE2E8F0))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Annex Number & Title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = annexNumber,
                            onValueChange = { annexNumber = it },
                            label = { Text("Số Phụ Lục *") },
                            colors = crmTextFieldColors(),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = todayStr,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ngày lập") },
                            colors = crmTextFieldColors(),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.width(110.dp)
                        )
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Tiêu đề Phụ Lục *") },
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // ==========================================
                    // 1. BẢNG TĂNG SẢN PHẨM / GIÁ TRỊ (MÀU XANH / GREEN)
                    // ==========================================
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF86EFAC))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color(0xFF047857), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "BẢNG TĂNG (Bổ sung sản phẩm, số lượng, đơn giá)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857)
                                    )
                                }
                                Text(
                                    text = "+${formatFullCurrencyVND(totalIncrease)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // List of added increase items
                            if (increaseItems.isNotEmpty()) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    increaseItems.forEachIndexed { index, item ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color.White)
                                                .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 6.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(item.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                                                Text(
                                                    "${item.quantity} ${item.unit} × ${formatFullCurrencyVND(item.unitPrice)}",
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF64748B)
                                                )
                                            }
                                            Text(
                                                "+${formatFullCurrencyVND(item.totalPrice)}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF047857)
                                            )
                                            IconButton(
                                                onClick = { increaseItems.removeAt(index) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = "Xóa", tint = Color(0xFFE02424), modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            // Form to add increase item
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                OutlinedTextField(
                                    value = incName,
                                    onValueChange = { incName = it },
                                    placeholder = { Text("Tên sản phẩm / hạng mục tăng...", fontSize = 11.sp) },
                                    colors = crmTextFieldColors(),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedTextField(
                                        value = incQtyStr,
                                        onValueChange = { incQtyStr = it.filter { c -> c.isDigit() || c == '.' } },
                                        placeholder = { Text("SL", fontSize = 11.sp) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = crmTextFieldColors(),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.width(60.dp)
                                    )
                                    OutlinedTextField(
                                        value = incUnit,
                                        onValueChange = { incUnit = it },
                                        placeholder = { Text("ĐVT", fontSize = 11.sp) },
                                        colors = crmTextFieldColors(),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.width(65.dp)
                                    )
                                    OutlinedTextField(
                                        value = incPriceStr,
                                        onValueChange = { incPriceStr = it.filter { c -> c.isDigit() } },
                                        placeholder = { Text("Đơn giá (VNĐ)", fontSize = 11.sp) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = crmTextFieldColors(),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Button(
                                    onClick = {
                                        val qty = incQtyStr.toDoubleOrNull() ?: 1.0
                                        val price = incPriceStr.toDoubleOrNull() ?: 0.0
                                        if (incName.isNotBlank() && price > 0) {
                                            increaseItems.add(
                                                AnnexItem(
                                                    name = incName.trim(),
                                                    quantity = qty,
                                                    unit = incUnit.trim().ifBlank { "Gói" },
                                                    unitPrice = price
                                                )
                                            )
                                            incName = ""
                                            incPriceStr = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Thêm Hạng Mục Tăng", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }

                    // ==========================================
                    // 2. BẢNG GIẢM SẢN PHẨM / GIÁ TRỊ (MÀU ĐỎ / RED)
                    // ==========================================
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFCA5A5))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = Color(0xFFE02424), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "BẢNG GIẢM (Cắt giảm, chiết khấu, trừ hạng mục)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFE02424)
                                    )
                                }
                                Text(
                                    text = "-${formatFullCurrencyVND(totalDecrease)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE02424)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // List of added decrease items
                            if (decreaseItems.isNotEmpty()) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    decreaseItems.forEachIndexed { index, item ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color.White)
                                                .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 6.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(item.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                                                Text(
                                                    "${item.quantity} ${item.unit} × ${formatFullCurrencyVND(item.unitPrice)}",
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF64748B)
                                                )
                                            }
                                            Text(
                                                "-${formatFullCurrencyVND(item.totalPrice)}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFE02424)
                                            )
                                            IconButton(
                                                onClick = { decreaseItems.removeAt(index) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = "Xóa", tint = Color(0xFFE02424), modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            // Form to add decrease item
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                OutlinedTextField(
                                    value = decName,
                                    onValueChange = { decName = it },
                                    placeholder = { Text("Tên sản phẩm / hạng mục giảm...", fontSize = 11.sp) },
                                    colors = crmTextFieldColors(),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedTextField(
                                        value = decQtyStr,
                                        onValueChange = { decQtyStr = it.filter { c -> c.isDigit() || c == '.' } },
                                        placeholder = { Text("SL", fontSize = 11.sp) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = crmTextFieldColors(),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.width(60.dp)
                                    )
                                    OutlinedTextField(
                                        value = decUnit,
                                        onValueChange = { decUnit = it },
                                        placeholder = { Text("ĐVT", fontSize = 11.sp) },
                                        colors = crmTextFieldColors(),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.width(65.dp)
                                    )
                                    OutlinedTextField(
                                        value = decPriceStr,
                                        onValueChange = { decPriceStr = it.filter { c -> c.isDigit() } },
                                        placeholder = { Text("Đơn giá giảm (VNĐ)", fontSize = 11.sp) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = crmTextFieldColors(),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Button(
                                    onClick = {
                                        val qty = decQtyStr.toDoubleOrNull() ?: 1.0
                                        val price = decPriceStr.toDoubleOrNull() ?: 0.0
                                        if (decName.isNotBlank() && price > 0) {
                                            decreaseItems.add(
                                                AnnexItem(
                                                    name = decName.trim(),
                                                    quantity = qty,
                                                    unit = decUnit.trim().ifBlank { "Gói" },
                                                    unitPrice = price
                                                )
                                            )
                                            decName = ""
                                            decPriceStr = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE02424)),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Thêm Hạng Mục Giảm", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }

                    // Financial Summary Breakdown Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Giá trị HĐ hiện tại:", fontSize = 12.sp, color = Color(0xFF64748B))
                                Text(formatFullCurrencyVND(contract.currentAmount), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("(+) Tổng giá trị TĂNG:", fontSize = 12.sp, color = Color(0xFF047857))
                                Text("+${formatFullCurrencyVND(totalIncrease)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("(-) Tổng giá trị GIẢM:", fontSize = 12.sp, color = Color(0xFFE02424))
                                Text("-${formatFullCurrencyVND(totalDecrease)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE02424))
                            }
                            HorizontalDivider(color = Color(0xFFE2E8F0))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("GIÁ TRỊ HĐ MỚI SAU PHỤ LỤC:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ProfessionalPrimaryNavy)
                                Text(
                                    text = formatFullCurrencyVND(newContractAmount),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ProfessionalPrimaryNavy
                                )
                            }
                        }
                    }

                    // Additional Terms for Annex
                    OutlinedTextField(
                        value = annexPaymentTerms,
                        onValueChange = { annexPaymentTerms = it },
                        label = { Text("Điều khoản thanh toán phụ lục") },
                        minLines = 2,
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = annexDeliveryTerms,
                        onValueChange = { annexDeliveryTerms = it },
                        label = { Text("Tiến độ giao hàng / bàn giao phụ lục") },
                        minLines = 2,
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = annexWarrantyTerms,
                        onValueChange = { annexWarrantyTerms = it },
                        label = { Text("Bảo hành phụ lục") },
                        minLines = 2,
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Ghi chú phụ lục (Tùy chọn)") },
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFE2E8F0))

                // Bottom Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Hủy", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            val newAnnex = ContractAnnex(
                                id = System.currentTimeMillis(),
                                contractId = contract.id,
                                annexNumber = annexNumber.trim(),
                                title = title.trim(),
                                dateStr = todayStr,
                                notes = notes.trim(),
                                increaseItems = increaseItems.toList(),
                                decreaseItems = decreaseItems.toList(),
                                paymentTerms = annexPaymentTerms.trim(),
                                deliveryTerms = annexDeliveryTerms.trim(),
                                warrantyTerms = annexWarrantyTerms.trim(),
                                isSigned = true
                            )
                            onSaveAnnex(newAnnex)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Lưu & Ký Phụ Lục", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * Dialog for configuring Contract & Annex Naming Rules (Prefix + Sequence + Suffix + Monthly Reset)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContractNamingRuleDialog(
    currentRule: ContractNamingRule,
    existingContracts: List<ContractItem>,
    onSaveRule: (ContractNamingRule) -> Unit,
    onDismiss: () -> Unit
) {
    var prefix by remember { mutableStateOf(currentRule.prefix) }
    var suffix by remember { mutableStateOf(currentRule.suffix) }
    var digitsCount by remember { mutableIntStateOf(currentRule.digitsCount) }
    var resetMonthly by remember { mutableStateOf(currentRule.resetMonthly) }
    var monthFormatPattern by remember { mutableStateOf(currentRule.monthFormatPattern) }
    var monthInPrefix by remember { mutableStateOf(currentRule.monthFormatInPrefix) }
    var monthInSuffix by remember { mutableStateOf(currentRule.monthFormatInSuffix) }
    var annexPosition by remember { mutableStateOf(currentRule.annexPosition) }
    var annexPrefix by remember { mutableStateOf(currentRule.annexPrefix) }
    var annexSeparator by remember { mutableStateOf(currentRule.annexSeparator) }
    var annexSuffix by remember { mutableStateOf(currentRule.annexSuffix) }

    val liveRule = remember(prefix, suffix, digitsCount, resetMonthly, monthFormatPattern, monthInPrefix, monthInSuffix, annexPosition, annexPrefix, annexSeparator, annexSuffix) {
        ContractNamingRule(
            prefix = prefix,
            suffix = suffix,
            digitsCount = digitsCount,
            resetMonthly = resetMonthly,
            monthFormatPattern = monthFormatPattern,
            monthFormatInPrefix = monthInPrefix,
            monthFormatInSuffix = monthInSuffix,
            annexPosition = annexPosition,
            annexPrefix = annexPrefix,
            annexSeparator = annexSeparator,
            annexSuffix = annexSuffix
        )
    }

    val previewContractNum = remember(liveRule, existingContracts) {
        liveRule.generateNextContractNumber(existingContracts)
    }

    val sampleContract = remember(previewContractNum) {
        ContractItem(contractNumber = previewContractNum)
    }
    val previewAnnexNum = remember(liveRule, sampleContract) {
        liveRule.generateNextAnnexNumber(sampleContract)
    }

    val evaluatedDateSample = remember(liveRule, monthFormatPattern) {
        liveRule.formatDateWithPattern(monthFormatPattern)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFEFF6FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, tint = ProfessionalPrimary)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Quy Tắc Đặt Số Hợp Đồng",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Tiền tố + Số thứ tự + Hậu tố & Phụ lục",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF94A3B8))
                    }
                }

                HorizontalDivider(color = Color(0xFFE2E8F0))

                // Live Preview Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("XEM TRƯỚC SỐ HỢP ĐỒNG TIẾP THEO:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        Text(
                            text = previewContractNum,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = ProfessionalPrimaryNavy
                        )
                        HorizontalDivider(color = Color(0xFFE2E8F0))
                        Text("XEM TRƯỚC SỐ PHỤ LỤC TIẾP THEO:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        Text(
                            text = previewAnnexNum,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF047857)
                        )
                    }
                }

                // 1. Prefix (Tiền tố)
                OutlinedTextField(
                    value = prefix,
                    onValueChange = { prefix = it },
                    label = { Text("Tiền tố hợp đồng (Tùy chọn)") },
                    placeholder = { Text("Ví dụ: HĐ-, CTY-") },
                    colors = crmTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // 2. Digits count (Số thứ tự)
                Column {
                    Text(
                        text = "Số thứ tự",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(2 to "01", 3 to "001", 4 to "0001", 5 to "00001").forEach { (digits, sample) ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (digitsCount == digits) ProfessionalPrimaryNavy else Color(0xFFF1F5F9))
                                    .clickable { digitsCount = digits }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = sample,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (digitsCount == digits) Color.White else Color(0xFF475569)
                                )
                            }
                        }
                    }
                }

                // 3. Suffix (Hậu tố)
                OutlinedTextField(
                    value = suffix,
                    onValueChange = { suffix = it },
                    label = { Text("Hậu tố hợp đồng (Tùy chọn)") },
                    placeholder = { Text("Ví dụ: /2026, /CRM") },
                    colors = crmTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // 4. Monthly Reset Switch & Custom Format
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Reset số hợp đồng theo tháng",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Tự động reset số thứ tự về 001 mỗi tháng mới",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                            Switch(
                                checked = resetMonthly,
                                onCheckedChange = { resetMonthly = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ProfessionalPrimary)
                            )
                        }

                        if (resetMonthly) {
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = Color(0xFFE2E8F0))
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Định dạng tháng năm (tùy chỉnh):",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF334155)
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                value = monthFormatPattern,
                                onValueChange = { monthFormatPattern = it },
                                placeholder = { Text("Ví dụ: yyyyMM, yy/MM, yyyy-MM, MM/yy") },
                                colors = crmTextFieldColors(),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Preset chips
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("yyyyMM", "yyyy-MM", "yy/MM", "MM/yy", "MMyyyy", "yyyy").forEach { pattern ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (monthFormatPattern == pattern) Color(0xFFDBEAFE) else Color(0xFFE2E8F0))
                                            .clickable { monthFormatPattern = pattern }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = pattern,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (monthFormatPattern == pattern) Color(0xFF1E40AF) else Color(0xFF475569)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Hiển thị thực tế: \"$evaluatedDateSample\"",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF047857)
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = monthInPrefix,
                                    onCheckedChange = {
                                        monthInPrefix = it
                                        if (it) monthInSuffix = false
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = ProfessionalPrimary)
                                )
                                Text("Chèn vào Tiền tố", fontSize = 12.sp, color = Color(0xFF334155))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = monthInSuffix,
                                    onCheckedChange = {
                                        monthInSuffix = it
                                        if (it) monthInPrefix = false
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = ProfessionalPrimary)
                                )
                                Text("Chèn vào Hậu tố", fontSize = 12.sp, color = Color(0xFF334155))
                            }
                        }
                    }
                }

                // 5. Annex Rule Configuration (Prefix / Suffix positioning)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Quy tắc đặt số Phụ lục hợp đồng",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Text(
                            text = "Vị trí chèn mã Phụ lục:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (annexPosition == AnnexPosition.PREFIX) Color(0xFFDBEAFE) else Color(0xFFF1F5F9))
                                    .border(1.dp, if (annexPosition == AnnexPosition.PREFIX) Color(0xFF2563EB) else Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                                    .clickable { annexPosition = AnnexPosition.PREFIX }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Tiền tố (Mã PL trước)",
                                    fontSize = 12.sp,
                                    fontWeight = if (annexPosition == AnnexPosition.PREFIX) FontWeight.Bold else FontWeight.Medium,
                                    color = if (annexPosition == AnnexPosition.PREFIX) Color(0xFF1E40AF) else Color(0xFF475569)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (annexPosition == AnnexPosition.SUFFIX) Color(0xFFDBEAFE) else Color(0xFFF1F5F9))
                                    .border(1.dp, if (annexPosition == AnnexPosition.SUFFIX) Color(0xFF2563EB) else Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                                    .clickable { annexPosition = AnnexPosition.SUFFIX }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Hậu tố (Mã PL sau)",
                                    fontSize = 12.sp,
                                    fontWeight = if (annexPosition == AnnexPosition.SUFFIX) FontWeight.Bold else FontWeight.Medium,
                                    color = if (annexPosition == AnnexPosition.SUFFIX) Color(0xFF1E40AF) else Color(0xFF475569)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = annexPrefix,
                                onValueChange = { annexPrefix = it },
                                label = { Text("Ký hiệu PL") },
                                placeholder = { Text("PL") },
                                colors = crmTextFieldColors(),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = annexSeparator,
                                onValueChange = { annexSeparator = it },
                                label = { Text("Phân cách") },
                                placeholder = { Text("/") },
                                colors = crmTextFieldColors(),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Bottom Save
                Button(
                    onClick = {
                        onSaveRule(liveRule)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Lưu Cài Đặt Quy Tắc", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

/**
 * Dialog for Exporting Contract to Word (.docx) or PDF (.pdf)
 */
@Composable
fun ContractExportDialog(
    contract: ContractItem,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedFormat by remember { mutableStateOf("WORD") } // "WORD" or "PDF"
    val formattedTotal = formatFullCurrencyVND(contract.currentAmount)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFEFF6FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.FileDownload, contentDescription = null, tint = ProfessionalPrimary)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Xuất Hợp Đồng",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Tùy chọn xuất ra file Word hoặc PDF",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF94A3B8))
                    }
                }

                HorizontalDivider(color = Color(0xFFE2E8F0))

                // Format Selector (Word vs PDF)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Option Word
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                1.5.dp,
                                if (selectedFormat == "WORD") Color(0xFF2563EB) else Color(0xFFE2E8F0),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedFormat = "WORD" },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedFormat == "WORD") Color(0xFFEFF6FF) else Color(0xFFF8FAFC)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Article,
                                contentDescription = null,
                                tint = if (selectedFormat == "WORD") Color(0xFF2563EB) else Color(0xFF64748B),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "File Word (.docx)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedFormat == "WORD") Color(0xFF1E40AF) else Color(0xFF334155)
                            )
                            Text(
                                text = "Dễ chỉnh sửa, mẫu chuẩn",
                                fontSize = 10.sp,
                                color = Color(0xFF64748B),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Option PDF
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                1.5.dp,
                                if (selectedFormat == "PDF") Color(0xFFDC2626) else Color(0xFFE2E8F0),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedFormat = "PDF" },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedFormat == "PDF") Color(0xFFFEF2F2) else Color(0xFFF8FAFC)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                tint = if (selectedFormat == "PDF") Color(0xFFDC2626) else Color(0xFF64748B),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "File PDF (.pdf)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedFormat == "PDF") Color(0xFF991B1B) else Color(0xFF334155)
                            )
                            Text(
                                text = "Chống sửa, in ấn ngay",
                                fontSize = 10.sp,
                                color = Color(0xFF64748B),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Contract Document Preview Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "NỘI DUNG VĂN BẢN XUẤT:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569)
                        )

                        Text(
                            text = "CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM\nĐộc lập - Tự do - Hạnh phúc",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        HorizontalDivider(color = Color(0xFFE2E8F0))

                        Text(
                            text = "HỢP ĐỒNG KINH TẾ\nSố: ${contract.contractNumber}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ProfessionalPrimaryNavy,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "• Tên gói thầu / Dự án: ${contract.title}\n" +
                                    "• Khách hàng (Bên B): ${contract.customerName}\n" +
                                    "• Giá trị hợp đồng: $formattedTotal\n" +
                                    "• Ngày ký: ${contract.signedDate.ifBlank { "Mới lập" }}\n" +
                                    "• Số phụ lục kèm theo: ${contract.annexes.size} phụ lục\n" +
                                    "• Điều khoản TT: ${contract.paymentTerms.take(60)}...\n" +
                                    "• Điều khoản BH: ${contract.warrantyTerms.take(60)}...",
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            lineHeight = 16.sp
                        )
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val formatName = if (selectedFormat == "WORD") "Word (.docx)" else "PDF (.pdf)"
                            Toast.makeText(context, "Đang chia sẻ hợp đồng ${contract.contractNumber} dạng $formatName...", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Chia sẻ", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            val formatName = if (selectedFormat == "WORD") "Word (.docx)" else "PDF (.pdf)"
                            Toast.makeText(context, "Đã xuất thành công hợp đồng ${contract.contractNumber} ra file $formatName!", Toast.LENGTH_LONG).show()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedFormat == "WORD") Color(0xFF2563EB) else Color(0xFFDC2626)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Icon(Icons.Default.FileDownload, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (selectedFormat == "WORD") "Tải file Word" else "Tải file PDF",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
