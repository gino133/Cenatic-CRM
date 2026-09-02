package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ContractItem
import com.example.data.model.ContractNamingRule
import com.example.data.model.ContractStatus
import com.example.data.model.ProjectProgressItem
import com.example.data.model.ProjectStatusType
import com.example.data.model.ProjectStep
import com.example.data.model.QuoteItem
import com.example.data.model.StepStatus
import com.example.ui.components.crmTextFieldColors
import com.example.ui.components.formatFullCurrencyVND
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.theme.ProfessionalPrimaryNavy
import com.example.ui.viewmodel.CrmViewModel

@Composable
fun QuotesAndProjectsScreen(
    viewModel: CrmViewModel,
    initialTab: Int = 0,
    onBack: (() -> Unit)? = null,
    onOpenProfile: () -> Unit = {},
    onCreateQuote: () -> Unit = {},
    onEditQuote: (QuoteItem) -> Unit = {}
) {
    var selectedTab by remember(initialTab) { mutableIntStateOf(initialTab) } // 0: Báo giá, 1: Hợp đồng, 2: Tiến độ thực hiện
    val quotes by viewModel.quotes.collectAsStateWithLifecycle()
    val contracts by viewModel.contracts.collectAsStateWithLifecycle()
    val contractNamingRule by viewModel.contractNamingRule.collectAsStateWithLifecycle()
    val projects by viewModel.projects.collectAsStateWithLifecycle()

    var quoteForRevisionDialog by remember { mutableStateOf<QuoteItem?>(null) }
    var stepForAddOrEditDialog by remember { mutableStateOf<Pair<Long, ProjectStep?>?>(null) } // Pair(projectId, stepOrNull)
    var projectSearchQuery by remember { mutableStateOf("") }
    var projectStatusFilter by remember { mutableStateOf("ALL") } // ALL, ON_TRACK, NEARING, DELAYED, COMPLETED

    val context = LocalContext.current
    val stepTitleSuggestions = remember(projects) {
        projects.flatMap { it.steps }.map { it.title.trim() }.filter { it.isNotBlank() }.distinct()
    }

    // Contract Workflow Dialog States
    var quoteForAcceptanceChoice by remember { mutableStateOf<QuoteItem?>(null) }
    var quoteForDetailDialog by remember { mutableStateOf<QuoteItem?>(null) }
    var contractForDetailDialog by remember { mutableStateOf<ContractItem?>(null) }
    var contractForAnnexDialog by remember { mutableStateOf<ContractItem?>(null) }
    var showNamingRuleDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FB))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Quay lại",
                                tint = Color(0xFF0F172A)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    } else {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onOpenProfile() }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(ProfessionalPrimaryNavy),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("A", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Báo giá, Hợp đồng & tiến độ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9))
                        .clickable { onOpenProfile() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Segmented Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Segmented Tabs: [Báo giá] | [Hợp đồng] | [Tiến độ thực hiện]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE2E8F0))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == 0) Color.White else Color.Transparent)
                            .clickable { selectedTab = 0 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Báo giá (${quotes.size})",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = if (selectedTab == 0) Color(0xFF0F172A) else Color(0xFF64748B)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == 1) Color.White else Color.Transparent)
                            .clickable { selectedTab = 1 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Hợp đồng (${contracts.size})",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = if (selectedTab == 1) Color(0xFF0F172A) else Color(0xFF64748B)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == 2) Color.White else Color.Transparent)
                            .clickable { selectedTab = 2 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tiến độ (${projects.size})",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = if (selectedTab == 2) Color(0xFF0F172A) else Color(0xFF64748B)
                        )
                    }
                }
            }

            // Content Area
            when (selectedTab) {
                0 -> {
                    // ==================== QUOTES (BÁO GIÁ) ====================
                    val drafts = quotes.filter { it.status.equals("Draft", ignoreCase = true) }
                    val sent = quotes.filter { it.status.equals("Sent", ignoreCase = true) }
                    val accepted = quotes.filter { it.status.equals("Accepted", ignoreCase = true) }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(4.dp)) }

                        // DRAFT SECTION
                        item {
                            QuoteSectionHeader(
                                title = "Bản nháp",
                                count = drafts.size,
                                titleColor = Color(0xFFD97706),
                                badgeBg = Color(0xFFFEF3C7),
                                badgeColor = Color(0xFFB45309)
                            )
                        }
                        if (drafts.isEmpty()) {
                            item {
                                EmptyQuoteNotice("Chưa có báo giá bản nháp. Nhấn nút + bên dưới để tạo mới.")
                            }
                        } else {
                            items(items = drafts, key = { it.id }) { quote: QuoteItem ->
                                QuoteCard(
                                    quote = quote,
                                    onOpenDetail = { quoteForDetailDialog = quote },
                                    onEdit = { onEditQuote(quote) },
                                    onSend = { viewModel.sendQuote(quote.id) },
                                    onAccept = { quoteForAcceptanceChoice = quote },
                                    onAddRevision = { quoteForRevisionDialog = quote },
                                    onDelete = { viewModel.deleteQuote(quote.id) },
                                    onGoToContract = { selectedTab = 1 },
                                    onGoToProject = { selectedTab = 2 }
                                )
                            }
                        }

                        // SENT SECTION
                        item {
                            QuoteSectionHeader(
                                title = "Đã gửi",
                                count = sent.size,
                                titleColor = Color(0xFF2563EB),
                                badgeBg = Color(0xFFEFF6FF),
                                badgeColor = Color(0xFF1D4ED8)
                            )
                        }
                        if (sent.isEmpty()) {
                            item {
                                EmptyQuoteNotice("Chưa có báo giá nào đang gửi.")
                            }
                        } else {
                            items(items = sent, key = { it.id }) { quote: QuoteItem ->
                                QuoteCard(
                                    quote = quote,
                                    onOpenDetail = { quoteForDetailDialog = quote },
                                    onEdit = { onEditQuote(quote) },
                                    onSend = { viewModel.sendQuote(quote.id) },
                                    onAccept = { quoteForAcceptanceChoice = quote },
                                    onAddRevision = { quoteForRevisionDialog = quote },
                                    onDelete = { viewModel.deleteQuote(quote.id) },
                                    onGoToContract = { selectedTab = 1 },
                                    onGoToProject = { selectedTab = 2 }
                                )
                            }
                        }

                        // ACCEPTED SECTION
                        item {
                            QuoteSectionHeader(
                                title = "Đã chốt",
                                count = accepted.size,
                                titleColor = Color(0xFF059669),
                                badgeBg = Color(0xFFDEF7EC),
                                badgeColor = Color(0xFF047857)
                            )
                        }
                        if (accepted.isEmpty()) {
                            item {
                                EmptyQuoteNotice("Chưa có báo giá đã chốt.")
                            }
                        } else {
                            items(items = accepted, key = { it.id }) { quote: QuoteItem ->
                                QuoteCard(
                                    quote = quote,
                                    onOpenDetail = { quoteForDetailDialog = quote },
                                    onEdit = { onEditQuote(quote) },
                                    onSend = { viewModel.sendQuote(quote.id) },
                                    onAccept = { quoteForAcceptanceChoice = quote },
                                    onAddRevision = { quoteForRevisionDialog = quote },
                                    onDelete = { viewModel.deleteQuote(quote.id) },
                                    onGoToContract = { selectedTab = 1 },
                                    onGoToProject = { selectedTab = 2 }
                                )
                            }
                        }

                        item { Spacer(modifier = Modifier.height(90.dp)) }
                    }
                }

                1 -> {
                    // ==================== CONTRACTS (HỢP ĐỒNG) ====================
                    ContractsListView(
                        contracts = contracts,
                        namingRule = contractNamingRule,
                        projects = projects,
                        onSignContract = { contract ->
                            viewModel.signContract(contract.id)
                            selectedTab = 2 // Move to progress after signing
                        },
                        onOpenContractDetail = { contractForDetailDialog = it },
                        onOpenAnnexDialog = { contractForAnnexDialog = it },
                        onOpenNamingRuleDialog = { showNamingRuleDialog = true },
                        onOpenCreateContract = { onCreateQuote() },
                        onDeleteContract = { viewModel.deleteContract(it) },
                        onGoToProject = { selectedTab = 2 }
                    )
                }

                else -> {
                    // ==================== PROJECT PROGRESS (TIẾN ĐỘ DỰ ÁN) ====================
                    val filteredProjects = remember(projects, projectSearchQuery, projectStatusFilter) {
                        projects.filter { proj ->
                            val matchesSearch = projectSearchQuery.isBlank() ||
                                    proj.title.contains(projectSearchQuery, ignoreCase = true) ||
                                    proj.customerName.contains(projectSearchQuery, ignoreCase = true)
                            val matchesStatus = when (projectStatusFilter) {
                                "ON_TRACK" -> proj.statusType == ProjectStatusType.ON_TRACK && proj.progressPercent < 100
                                "NEARING" -> proj.statusType == ProjectStatusType.NEARING && proj.progressPercent < 100
                                "DELAYED" -> proj.statusType == ProjectStatusType.DELAYED && proj.progressPercent < 100
                                "COMPLETED" -> proj.progressPercent == 100
                                else -> true
                            }
                            matchesSearch && matchesStatus
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Search bar
                            OutlinedTextField(
                                value = projectSearchQuery,
                                onValueChange = { projectSearchQuery = it },
                                placeholder = { Text("Tìm theo tên dự án, khách hàng...", fontSize = 13.sp) },
                                leadingIcon = {
                                    Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                                },
                                trailingIcon = {
                                    if (projectSearchQuery.isNotBlank()) {
                                        IconButton(onClick = { projectSearchQuery = "" }) {
                                            Icon(Icons.Default.Close, contentDescription = "Xóa", tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
                                        }
                                    }
                                },
                                singleLine = true,
                                colors = crmTextFieldColors(),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("search_project_input")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Status Filter Chips
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val filterOptions = listOf(
                                    "ALL" to "Tất cả (${projects.size})",
                                    "ON_TRACK" to "Đang đúng hạn",
                                    "NEARING" to "Gần đến hạn",
                                    "DELAYED" to "Chậm tiến độ",
                                    "COMPLETED" to "Hoàn thành (100%)"
                                )
                                items(filterOptions) { (key, label) ->
                                    val isSelected = projectStatusFilter == key
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (isSelected) ProfessionalPrimary else Color.White)
                                            .border(
                                                1.dp,
                                                if (isSelected) ProfessionalPrimary else Color(0xFFE2E8F0),
                                                RoundedCornerShape(20.dp)
                                            )
                                            .clickable { projectStatusFilter = key }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = label,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else Color(0xFF475569)
                                        )
                                    }
                                }
                            }
                        }

                        if (filteredProjects.isEmpty()) {
                            item {
                                EmptyQuoteNotice(
                                    if (projects.isEmpty()) "Chưa có dự án nào. Báo giá sau khi Chốt hợp đồng sẽ tự động hiển thị ở đây."
                                    else "Không tìm thấy dự án nào phù hợp với bộ lọc."
                                )
                            }
                        } else {
                            items(items = filteredProjects, key = { it.id }) { project: ProjectProgressItem ->
                                ProjectProgressCard(
                                    project = project,
                                    onToggleStep = { stepId ->
                                        if (project.isStepCompletable(stepId)) {
                                            viewModel.toggleProjectStep(project.id, stepId)
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Vui lòng hoàn thành các bước trước đó trước khi đánh dấu bước này!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    onAddStep = { stepForAddOrEditDialog = Pair(project.id, null) },
                                    onEditStep = { step -> stepForAddOrEditDialog = Pair(project.id, step) },
                                    onDeleteStep = { stepId -> viewModel.deleteProjectStep(project.id, stepId) },
                                    onDeleteProject = { viewModel.deleteProject(project.id) },
                                    onCantComplete = { reason ->
                                        Toast.makeText(context, reason, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }

                        item { Spacer(modifier = Modifier.height(90.dp)) }
                    }
                }
            }
        }

        // Floating Action Button to create Quote or Step
        FloatingActionButton(
            onClick = {
                if (selectedTab == 0 || selectedTab == 1) {
                    onCreateQuote()
                } else {
                    if (projects.isNotEmpty()) {
                        stepForAddOrEditDialog = Pair(projects.first().id, null)
                    } else {
                        onCreateQuote()
                    }
                }
            },
            containerColor = ProfessionalPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_quote_fab")
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }

    // Quote Detail & Export Dialog
    if (quoteForDetailDialog != null) {
        val q = quoteForDetailDialog!!
        QuoteDetailDialog(
            quote = q,
            onDismiss = { quoteForDetailDialog = null },
            onEdit = {
                quoteForDetailDialog = null
                onEditQuote(q)
            },
            onSend = {
                viewModel.sendQuote(q.id)
                quoteForDetailDialog = null
            },
            onAccept = {
                quoteForDetailDialog = null
                quoteForAcceptanceChoice = q
            },
            onGoToContract = {
                quoteForDetailDialog = null
                selectedTab = 1
            },
            onGoToProject = {
                quoteForDetailDialog = null
                selectedTab = 2
            }
        )
    }

    // Choice Dialog: "Ký hợp đồng" vs "Thực hiện theo báo giá"
    if (quoteForAcceptanceChoice != null) {
        val q = quoteForAcceptanceChoice!!
        QuoteAcceptanceChoiceDialog(
            quote = q,
            onSignContract = {
                val (_, createdContract) = viewModel.acceptQuoteWithOption(q.id, isSignContract = true)
                quoteForAcceptanceChoice = null
                selectedTab = 1 // Navigate to Contract Tab
                if (createdContract != null) {
                    contractForDetailDialog = createdContract
                }
            },
            onProceedByQuote = {
                viewModel.acceptQuoteWithOption(q.id, isSignContract = false)
                quoteForAcceptanceChoice = null
                selectedTab = 2 // Navigate directly to Progress Tab
            },
            onDismiss = { quoteForAcceptanceChoice = null }
        )
    }

    // Contract Detail & Signing Dialog
    if (contractForDetailDialog != null) {
        val c = contractForDetailDialog!!
        ContractDetailDialog(
            contract = c,
            namingRule = contractNamingRule,
            onSave = { updated ->
                // Update in ViewModel
                viewModel.updateContractTerms(
                    contractId = updated.id,
                    payment = updated.paymentTerms,
                    delivery = updated.deliveryTerms,
                    warranty = updated.warrantyTerms,
                    notes = updated.notes
                )
                contractForDetailDialog = null
            },
            onSign = { updated ->
                viewModel.updateContractTerms(
                    contractId = updated.id,
                    payment = updated.paymentTerms,
                    delivery = updated.deliveryTerms,
                    warranty = updated.warrantyTerms,
                    notes = updated.notes
                )
                viewModel.signContract(updated.id)
                contractForDetailDialog = null
                selectedTab = 2 // Move to progress tab
            },
            onDismiss = { contractForDetailDialog = null }
        )
    }

    // Contract Annex Dialog
    if (contractForAnnexDialog != null) {
        val c = contractForAnnexDialog!!
        ContractAnnexDialog(
            contract = c,
            namingRule = contractNamingRule,
            onSaveAnnex = { newAnnex ->
                viewModel.addContractAnnex(c.id, newAnnex)
                contractForAnnexDialog = null
            },
            onDismiss = { contractForAnnexDialog = null }
        )
    }

    // Contract Naming Rule Configuration Dialog
    if (showNamingRuleDialog) {
        ContractNamingRuleDialog(
            currentRule = contractNamingRule,
            existingContracts = contracts,
            onSaveRule = { newRule ->
                viewModel.updateContractNamingRule(newRule)
                showNamingRuleDialog = false
            },
            onDismiss = { showNamingRuleDialog = false }
        )
    }

    // Add or Edit Project Step Dialog with Auto-Suggestions & Weight Warnings
    if (stepForAddOrEditDialog != null) {
        val (projId, stepToEdit) = stepForAddOrEditDialog!!
        val targetProject = projects.find { it.id == projId }
        if (targetProject != null) {
            AddOrEditStepDialog(
                projectId = projId,
                existingStep = stepToEdit,
                project = targetProject,
                suggestions = stepTitleSuggestions,
                onSave = { title, deadline, weight ->
                    if (stepToEdit == null) {
                        viewModel.addProjectStep(projId, title, deadline, weight)
                        Toast.makeText(context, "Đã thêm bước thực hiện thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.updateProjectStep(projId, stepToEdit.id, title, deadline, weight)
                        Toast.makeText(context, "Đã cập nhật bước thực hiện", Toast.LENGTH_SHORT).show()
                    }
                    stepForAddOrEditDialog = null
                },
                onDelete = if (stepToEdit != null) {
                    {
                        viewModel.deleteProjectStep(projId, stepToEdit.id)
                        Toast.makeText(context, "Đã xóa bước thực hiện", Toast.LENGTH_SHORT).show()
                        stepForAddOrEditDialog = null
                    }
                } else null,
                onDismiss = { stepForAddOrEditDialog = null }
            )
        }
    }

    // Add Quote Revision Dialog (for Sent quotes)
    if (quoteForRevisionDialog != null) {
        val q = quoteForRevisionDialog!!
        var revisionNote by remember { mutableStateOf("") }
        var newAmountStr by remember { mutableStateOf(q.amount.toLong().toString()) }

        AlertDialog(
            onDismissRequest = { quoteForRevisionDialog = null },
            containerColor = Color.White,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, contentDescription = null, tint = ProfessionalPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Thêm Bản Cập Nhật v${q.version + 1}", fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Báo giá: ${q.quoteNumber} - ${q.title}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155)
                    )

                    OutlinedTextField(
                        value = newAmountStr,
                        onValueChange = { newAmountStr = it.filter { c -> c.isDigit() } },
                        label = { Text("Giá trị điều chỉnh (VNĐ)") },
                        trailingIcon = { Text("đ", fontWeight = FontWeight.Bold, color = Color(0xFF64748B)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = revisionNote,
                        onValueChange = { revisionNote = it },
                        label = { Text("Nội dung thay đổi / Ghi chú cập nhật *") },
                        placeholder = { Text("Ví dụ: Bổ sung thêm hạng mục bảo hành 12 tháng") },
                        minLines = 3,
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = newAmountStr.toDoubleOrNull() ?: q.amount
                        viewModel.addQuoteRevision(q.id, revisionNote.ifBlank { "Cập nhật giá trị và điều khoản" }, amount)
                        quoteForRevisionDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
                ) {
                    Text("Lưu Bản Cập Nhật", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { quoteForRevisionDialog = null }) {
                    Text("Hủy", color = Color(0xFF64748B))
                }
            }
        )
    }
}

@Composable
fun EmptyQuoteNotice(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                fontSize = 13.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
fun QuoteSectionHeader(
    title: String,
    count: Int,
    titleColor: Color = Color(0xFF334155),
    badgeBg: Color = Color(0xFFE2E8F0),
    badgeColor: Color = Color(0xFF475569)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 4.dp, height = 16.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(titleColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(badgeBg)
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = "$count",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = badgeColor
            )
        }
    }
}

@Composable
fun QuoteCard(
    quote: QuoteItem,
    onOpenDetail: () -> Unit,
    onEdit: () -> Unit,
    onSend: () -> Unit,
    onAccept: () -> Unit,
    onAddRevision: () -> Unit,
    onDelete: () -> Unit,
    onGoToContract: (() -> Unit)? = null,
    onGoToProject: () -> Unit
) {
    val formattedAmount = formatFullCurrencyVND(quote.amount)
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenDetail() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Quote Number & Options Menu (Status badge removed as requested)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = quote.quoteNumber,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569)
                    )
                    if (quote.version > 1) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFEFF6FF))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "v${quote.version}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ProfessionalPrimary
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
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
                                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = ProfessionalPrimary) },
                                text = { Text("Xem chi tiết & Xuất file") },
                                onClick = {
                                    showMenu = false
                                    onOpenDetail()
                                }
                            )
                            if (quote.status.equals("Draft", ignoreCase = true)) {
                                DropdownMenuItem(
                                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = ProfessionalPrimary) },
                                    text = { Text("Chỉnh sửa báo giá") },
                                    onClick = {
                                        showMenu = false
                                        onEdit()
                                    }
                                )
                                DropdownMenuItem(
                                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color(0xFF1E429F)) },
                                    text = { Text("Gửi báo giá") },
                                    onClick = {
                                        showMenu = false
                                        onSend()
                                    }
                                )
                            }
                            if (quote.status.equals("Sent", ignoreCase = true)) {
                                DropdownMenuItem(
                                    leadingIcon = { Icon(Icons.Default.History, contentDescription = null, tint = ProfessionalPrimary) },
                                    text = { Text("Thêm bản cập nhật") },
                                    onClick = {
                                        showMenu = false
                                        onAddRevision()
                                    }
                                )
                                DropdownMenuItem(
                                    leadingIcon = { Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF047857)) },
                                    text = { Text("Chốt đơn") },
                                    onClick = {
                                        showMenu = false
                                        onAccept()
                                    }
                                )
                            }
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE02424)) },
                                text = { Text("Xóa báo giá", color = Color(0xFFE02424)) },
                                onClick = {
                                    showMenu = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title & Customer
            Text(
                text = quote.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Text(
                text = "Khách hàng: ${quote.customerName}",
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )

            if (quote.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ghi chú: ${quote.notes}",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Amount & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedAmount,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProfessionalPrimary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = quote.dateStr,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // Quick Action Buttons based on Status
            Spacer(modifier = Modifier.height(12.dp))
            when (quote.status.lowercase()) {
                "draft" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onEdit,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Chỉnh sửa", fontSize = 12.sp)
                        }

                        Button(
                            onClick = onSend,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Gửi báo giá", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
                "sent" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onAddRevision,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Cập nhật", fontSize = 12.sp)
                        }

                        Button(
                            onClick = onAccept,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Chốt đơn", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
                "accepted" -> {
                    if (!quote.contractNumber.isNullOrBlank()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onGoToContract?.invoke() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = ProfessionalPrimary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Xem HĐ (${quote.contractNumber})", fontSize = 11.sp, color = ProfessionalPrimary)
                            }

                            Button(
                                onClick = onGoToProject,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Tiến độ", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF047857))
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF047857), modifier = Modifier.size(14.dp))
                            }
                        }
                    } else {
                        Button(
                            onClick = onGoToProject,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Đã chuyển qua tiến độ thực hiện", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF047857))
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF047857), modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectProgressCard(
    project: ProjectProgressItem,
    onToggleStep: (Long) -> Unit,
    onAddStep: () -> Unit,
    onEditStep: (ProjectStep) -> Unit,
    onDeleteStep: (Long) -> Unit,
    onDeleteProject: () -> Unit,
    onCantComplete: (String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val (statusBg, statusText) = when (project.statusType) {
        ProjectStatusType.ON_TRACK -> Pair(Color(0xFFDEF7EC), Color(0xFF047857))
        ProjectStatusType.NEARING -> Pair(Color(0xFFFEF3C7), Color(0xFFD97706))
        ProjectStatusType.DELAYED -> Pair(Color(0xFFFDE8E8), Color(0xFFE02424))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top: Title, Customer, Badge, Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "KH: ${project.customerName}",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(statusBg)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (project.progressPercent == 100) "Hoàn thành 100%" else project.statusType.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusText
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
                                leadingIcon = { Icon(Icons.Default.Add, contentDescription = null, tint = ProfessionalPrimary) },
                                text = { Text("Thêm bước thực hiện") },
                                onClick = {
                                    showMenu = false
                                    onAddStep()
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE02424)) },
                                text = { Text("Xóa dự án", color = Color(0xFFE02424)) },
                                onClick = {
                                    showMenu = false
                                    onDeleteProject()
                                }
                            )
                        }
                    }
                }
            }

            // Weight Exceeded Warning Banner
            if (project.isWeightExceeded) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEE2E2))
                        .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Cảnh báo: Tổng trọng số các bước là ${project.totalConfiguredWeight}% (vượt quá 100%)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626)
                    )
                }
            } else if (project.warningNote != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFDE8E8))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFE02424),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = project.warningNote,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE02424)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar & Percentage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tiến độ tổng thể",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "${project.progressPercent}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (project.progressPercent == 100) Color(0xFF047857) else ProfessionalPrimary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { (project.progressPercent.coerceIn(0, 100)) / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (project.progressPercent == 100) Color(0xFF10B981) else ProfessionalPrimary,
                trackColor = Color(0xFFE2E8F0)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Steps Checklist
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val totalSteps = project.steps.size
                val defaultStepWeight = if (totalSteps > 0) 100 / totalSteps else 0

                project.steps.forEach { step ->
                    val isCompletable = project.isStepCompletable(step.id)
                    ProjectStepItemView(
                        step = step,
                        defaultWeight = defaultStepWeight,
                        isCompletable = isCompletable,
                        isWeightExceeded = project.isWeightExceeded,
                        onToggle = { onToggleStep(step.id) },
                        onEdit = { onEditStep(step) },
                        onCantComplete = onCantComplete
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Add step button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onAddStep() }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = ProfessionalPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Thêm bước thực hiện",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ProfessionalPrimary
                )
            }
        }
    }
}

@Composable
fun ProjectStepItemView(
    step: ProjectStep,
    defaultWeight: Int,
    isCompletable: Boolean,
    isWeightExceeded: Boolean,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onCantComplete: (String) -> Unit
) {
    val isCompleted = step.status == StepStatus.COMPLETED
    val weightLabel = if (step.customWeightPercent != null) "${step.customWeightPercent}%" else "$defaultWeight%"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isCompleted) Color(0xFFF8FAFC) else Color.White)
            .border(
                1.dp,
                if (isWeightExceeded) Color(0xFFFCA5A5) else if (isCompleted) Color(0xFFE2E8F0) else Color(0xFFF1F5F9),
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Interactive Checkbox / Lock Icon if dependency not satisfied
        if (!isCompleted && !isCompletable) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFF1F5F9))
                    .clickable {
                        onCantComplete("Không thể hoàn thành! Vui lòng hoàn thành các bước trước đó trước.")
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Chưa thể hoàn thành",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(14.dp)
                )
            }
        } else {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF10B981),
                    uncheckedColor = Color(0xFF94A3B8)
                ),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onEdit() }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = step.title,
                    fontSize = 13.sp,
                    fontWeight = if (step.isHighlighted || isCompleted) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isCompleted) Color(0xFF047857) else if (!isCompletable && !isCompleted) Color(0xFF64748B) else Color(0xFF0F172A)
                )
                if (!isCompleted && !isCompletable) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(chờ bước trước)",
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
            Text(
                text = step.dateLabel,
                fontSize = 11.sp,
                color = if (step.status == StepStatus.OVERDUE) Color(0xFFE02424) else Color(0xFF64748B)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Weight pill badge with click to edit step
        val badgeBg = if (isWeightExceeded) Color(0xFFFEE2E2) else if (step.customWeightPercent != null) Color(0xFFEFF6FF) else Color(0xFFF1F5F9)
        val badgeTextColor = if (isWeightExceeded) Color(0xFFDC2626) else if (step.customWeightPercent != null) ProfessionalPrimary else Color(0xFF64748B)

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(badgeBg)
                .clickable { onEdit() }
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = weightLabel,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = badgeTextColor
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
            onClick = { onEdit() },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Sửa bước",
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddOrEditStepDialog(
    projectId: Long,
    existingStep: ProjectStep?,
    project: ProjectProgressItem,
    suggestions: List<String>,
    onSave: (title: String, deadline: String, weight: Int?) -> Unit,
    onDelete: (() -> Unit)?,
    onDismiss: () -> Unit
) {
    var stepTitle by remember { mutableStateOf(existingStep?.title ?: "") }
    var stepDeadline by remember { mutableStateOf(existingStep?.dateLabel ?: "15/12/2023") }
    var customWeightStr by remember { mutableStateOf(existingStep?.customWeightPercent?.toString() ?: "") }

    val parsedWeight = customWeightStr.toIntOrNull()
    val otherWeightsSum = project.steps.filter { it.id != existingStep?.id }.sumOf { it.customWeightPercent ?: 0 }
    val simulatedTotalWeight = otherWeightsSum + (parsedWeight ?: 0)
    val isExceeded = (parsedWeight != null && simulatedTotalWeight > 100) || (parsedWeight == null && otherWeightsSum > 100)

    val filteredSuggestions = remember(suggestions, stepTitle) {
        suggestions.filter { it.isNotBlank() && !it.equals(stepTitle, ignoreCase = true) }.take(6)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (existingStep != null) Icons.Default.Edit else Icons.Default.Add,
                    contentDescription = null,
                    tint = ProfessionalPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (existingStep != null) "Chỉnh Sửa Bước Tiến Độ" else "Thêm Bước Thực Hiện",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Title Field
                OutlinedTextField(
                    value = stepTitle,
                    onValueChange = { stepTitle = it },
                    label = { Text("Tên bước thực hiện *") },
                    placeholder = { Text("Ví dụ: Khảo sát hiện trạng, Thiết kế 3D...") },
                    colors = crmTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Suggestion chips from other orders
                if (filteredSuggestions.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Gợi ý từ các đơn hàng khác:", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                        }
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            filteredSuggestions.forEach { suggestion ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFEFF6FF))
                                        .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(12.dp))
                                        .clickable { stepTitle = suggestion }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "+ $suggestion",
                                        fontSize = 11.sp,
                                        color = ProfessionalPrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                // Deadline Field
                OutlinedTextField(
                    value = stepDeadline,
                    onValueChange = { stepDeadline = it },
                    label = { Text("Hạn chót / Deadline") },
                    placeholder = { Text("dd/MM/yyyy") },
                    colors = crmTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Custom Weight Field
                OutlinedTextField(
                    value = customWeightStr,
                    onValueChange = { customWeightStr = it.filter { c -> c.isDigit() } },
                    label = { Text("% Trọng số đóng góp (Tùy chọn)") },
                    placeholder = { Text("Để trống = Tự động chia đều") },
                    trailingIcon = { Text("%", fontWeight = FontWeight.Bold, color = if (isExceeded) Color(0xFFDC2626) else Color(0xFF64748B)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = crmTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Warning Banner if weight exceeds 100%
                if (isExceeded) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEE2E2))
                            .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Cảnh báo: Tổng trọng số sẽ là $simulatedTotalWeight%, vượt quá 100%!",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )
                    }
                } else {
                    Text(
                        text = "💡 Nếu không đặt %, hệ thống sẽ tự phân bổ đều cho các bước chưa có trọng số.",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                // Delete Button for Existing Step
                if (onDelete != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    TextButton(
                        onClick = onDelete,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE02424), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Xóa bước thực hiện này", color = Color(0xFFE02424), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (stepTitle.isNotBlank()) {
                        onSave(stepTitle, stepDeadline, parsedWeight)
                    }
                },
                enabled = stepTitle.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
            ) {
                Text(if (existingStep != null) "Lưu thay đổi" else "Thêm bước", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color(0xFF64748B))
            }
        }
    )
}
