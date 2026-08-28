package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    var selectedTab by remember(initialTab) { mutableIntStateOf(initialTab) } // 0: Báo giá, 1: Tiến độ thực hiện
    val quotes by viewModel.quotes.collectAsStateWithLifecycle()
    val projects by viewModel.projects.collectAsStateWithLifecycle()

    var showAddStepDialogForProject by remember { mutableStateOf<Long?>(null) }
    var quoteForRevisionDialog by remember { mutableStateOf<QuoteItem?>(null) }
    var stepForWeightDialog by remember { mutableStateOf<Pair<Long, ProjectStep>?>(null) } // Pair(projectId, step)

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
                        Column {
                            Text(
                                text = "Phòng Kinh Doanh",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Báo giá & Tiến độ dự án",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
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
                Text(
                    text = "Báo giá & Tiến độ",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Segmented Tabs: [Báo giá] | [Tiến độ thực hiện]
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
                            fontSize = 14.sp,
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
                            text = "Tiến độ thực hiện (${projects.size})",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = if (selectedTab == 1) Color(0xFF0F172A) else Color(0xFF64748B)
                        )
                    }
                }
            }

            // Content Area
            if (selectedTab == 0) {
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
                        QuoteSectionHeader(title = "Bản nháp (Draft)", count = drafts.size)
                    }
                    if (drafts.isEmpty()) {
                        item {
                            EmptyQuoteNotice("Chưa có báo giá bản nháp. Nhấn nút + bên dưới để tạo mới.")
                        }
                    } else {
                        items(items = drafts, key = { it.id }) { quote: QuoteItem ->
                            QuoteCard(
                                quote = quote,
                                onEdit = { onEditQuote(quote) },
                                onSend = { viewModel.sendQuote(quote.id) },
                                onAccept = { viewModel.acceptQuote(quote.id) },
                                onAddRevision = { quoteForRevisionDialog = quote },
                                onDelete = { viewModel.deleteQuote(quote.id) },
                                onGoToProject = { selectedTab = 1 }
                            )
                        }
                    }

                    // SENT SECTION
                    item {
                        QuoteSectionHeader(title = "Đã gửi khách hàng (Sent)", count = sent.size)
                    }
                    if (sent.isEmpty()) {
                        item {
                            EmptyQuoteNotice("Chưa có báo giá nào đang gửi.")
                        }
                    } else {
                        items(items = sent, key = { it.id }) { quote: QuoteItem ->
                            QuoteCard(
                                quote = quote,
                                onEdit = { onEditQuote(quote) },
                                onSend = { viewModel.sendQuote(quote.id) },
                                onAccept = { viewModel.acceptQuote(quote.id) },
                                onAddRevision = { quoteForRevisionDialog = quote },
                                onDelete = { viewModel.deleteQuote(quote.id) },
                                onGoToProject = { selectedTab = 1 }
                            )
                        }
                    }

                    // ACCEPTED SECTION
                    item {
                        QuoteSectionHeader(title = "Đã chốt hợp đồng (Accepted)", count = accepted.size)
                    }
                    if (accepted.isEmpty()) {
                        item {
                            EmptyQuoteNotice("Chưa có báo giá đã chốt.")
                        }
                    } else {
                        items(items = accepted, key = { it.id }) { quote: QuoteItem ->
                            QuoteCard(
                                quote = quote,
                                onEdit = { onEditQuote(quote) },
                                onSend = { viewModel.sendQuote(quote.id) },
                                onAccept = { viewModel.acceptQuote(quote.id) },
                                onAddRevision = { quoteForRevisionDialog = quote },
                                onDelete = { viewModel.deleteQuote(quote.id) },
                                onGoToProject = { selectedTab = 1 }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(90.dp)) }
                }
            } else {
                // ==================== PROJECT PROGRESS (TIẾN ĐỘ DỰ ÁN) ====================
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Dự án đang theo dõi",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Đánh dấu bước hoàn thành và tùy chỉnh % tỷ trọng",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }

                    if (projects.isEmpty()) {
                        item {
                            EmptyQuoteNotice("Chưa có dự án nào. Báo giá sau khi Chốt hợp đồng sẽ tự động hiển thị ở đây.")
                        }
                    } else {
                        items(items = projects, key = { it.id }) { project: ProjectProgressItem ->
                            ProjectProgressCard(
                                project = project,
                                onToggleStep = { stepId -> viewModel.toggleProjectStep(project.id, stepId) },
                                onAddStep = { showAddStepDialogForProject = project.id },
                                onEditStepWeight = { step -> stepForWeightDialog = Pair(project.id, step) },
                                onDeleteStep = { stepId -> viewModel.deleteProjectStep(project.id, stepId) },
                                onDeleteProject = { viewModel.deleteProject(project.id) }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(90.dp)) }
                }
            }
        }

        // Floating Action Button to create Quote or Step
        FloatingActionButton(
            onClick = {
                if (selectedTab == 0) {
                    onCreateQuote()
                } else {
                    if (projects.isNotEmpty()) {
                        showAddStepDialogForProject = projects.first().id
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

    // Add Project Step Dialog
    if (showAddStepDialogForProject != null) {
        val projId = showAddStepDialogForProject!!
        var stepTitle by remember { mutableStateOf("") }
        var stepDeadline by remember { mutableStateOf("15/12/2023") }
        var customWeightStr by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddStepDialogForProject = null },
            containerColor = Color.White,
            title = { Text("Thêm Bước Thực Hiện", fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = stepTitle,
                        onValueChange = { stepTitle = it },
                        label = { Text("Tên bước thực hiện *") },
                        placeholder = { Text("Ví dụ: Nghiệm thu bàn giao") },
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = stepDeadline,
                        onValueChange = { stepDeadline = it },
                        label = { Text("Hạn chót / Deadline") },
                        placeholder = { Text("dd/MM/yyyy") },
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = customWeightStr,
                        onValueChange = { customWeightStr = it.filter { c -> c.isDigit() } },
                        label = { Text("% Trọng số tiến độ (Tùy chọn)") },
                        placeholder = { Text("Để trống = Tự chia đều") },
                        trailingIcon = { Text("%", fontWeight = FontWeight.Bold, color = Color(0xFF64748B)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "💡 Nếu không cài đặt %, hệ thống sẽ tự động phân bổ đều 100% cho tất cả các bước.",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (stepTitle.isNotBlank()) {
                            val weight = customWeightStr.toIntOrNull()
                            viewModel.addProjectStep(projId, stepTitle, stepDeadline, weight)
                        }
                        showAddStepDialogForProject = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
                ) {
                    Text("Thêm bước", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddStepDialogForProject = null }) {
                    Text("Hủy", color = Color(0xFF64748B))
                }
            }
        )
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

    // Step Custom Weight Editor Dialog
    if (stepForWeightDialog != null) {
        val (projId, step) = stepForWeightDialog!!
        var weightInput by remember { mutableStateOf(step.customWeightPercent?.toString() ?: "") }

        AlertDialog(
            onDismissRequest = { stepForWeightDialog = null },
            containerColor = Color.White,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Percent, contentDescription = null, tint = ProfessionalPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cài Đặt % Trọng Số Bước", fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Bước: ${step.title}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A)
                    )
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it.filter { c -> c.isDigit() } },
                        label = { Text("% Trọng số đóng góp") },
                        placeholder = { Text("Để trống = Tự động chia đều") },
                        trailingIcon = { Text("%", fontWeight = FontWeight.Bold, color = Color(0xFF64748B)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = crmTextFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Mẹo: Nếu xóa trắng, bước này sẽ dùng % phân bổ đều theo số lượng bước.",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val weight = weightInput.toIntOrNull()
                        viewModel.updateProjectStepWeight(projId, step.id, weight)
                        stepForWeightDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
                ) {
                    Text("Cập nhật %", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { stepForWeightDialog = null }) {
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
fun QuoteSectionHeader(title: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFE2E8F0))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = "$count",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF475569)
            )
        }
    }
}

@Composable
fun QuoteCard(
    quote: QuoteItem,
    onEdit: () -> Unit,
    onSend: () -> Unit,
    onAccept: () -> Unit,
    onAddRevision: () -> Unit,
    onDelete: () -> Unit,
    onGoToProject: () -> Unit
) {
    val formattedAmount = formatFullCurrencyVND(quote.amount)
    var showMenu by remember { mutableStateOf(false) }

    val (badgeBg, badgeText, badgeLabel) = when (quote.status.lowercase()) {
        "accepted" -> Triple(Color(0xFFDEF7EC), Color(0xFF047857), "Đã chốt (Accepted)")
        "sent" -> Triple(Color(0xFFE1EFFE), Color(0xFF1E429F), "Đã gửi (Sent)")
        else -> Triple(Color(0xFFF1F5F9), Color(0xFF475569), "Bản nháp (Draft)")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Quote Number, Badge, Options Menu
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
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(badgeBg)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badgeLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeText
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
                                    text = { Text("Gửi báo giá (Sent)") },
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
                                    text = { Text("Chốt hợp đồng (Accepted)") },
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

@Composable
fun ProjectProgressCard(
    project: ProjectProgressItem,
    onToggleStep: (Long) -> Unit,
    onAddStep: () -> Unit,
    onEditStepWeight: (ProjectStep) -> Unit,
    onDeleteStep: (Long) -> Unit,
    onDeleteProject: () -> Unit
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
                            text = project.statusType.label,
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

            // Warning note if any
            if (project.warningNote != null) {
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
                progress = { project.progressPercent / 100f },
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
                    ProjectStepItemView(
                        step = step,
                        defaultWeight = defaultStepWeight,
                        onToggle = { onToggleStep(step.id) },
                        onEditWeight = { onEditStepWeight(step) },
                        onDelete = { onDeleteStep(step.id) }
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
    onToggle: () -> Unit,
    onEditWeight: () -> Unit,
    onDelete: () -> Unit
) {
    val isCompleted = step.status == StepStatus.COMPLETED
    val weightLabel = if (step.customWeightPercent != null) "${step.customWeightPercent}%" else "$defaultWeight%"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isCompleted) Color(0xFFF8FAFC) else Color.White)
            .border(1.dp, if (isCompleted) Color(0xFFE2E8F0) else Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
            .clickable { onToggle() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Interactive Checkbox
        Checkbox(
            checked = isCompleted,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF10B981),
                uncheckedColor = Color(0xFF94A3B8)
            ),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = step.title,
                fontSize = 13.sp,
                fontWeight = if (step.isHighlighted || isCompleted) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isCompleted) Color(0xFF047857) else Color(0xFF0F172A)
            )
            Text(
                text = step.dateLabel,
                fontSize = 11.sp,
                color = if (step.status == StepStatus.OVERDUE) Color(0xFFE02424) else Color(0xFF64748B)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Weight pill badge with click to customize %
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (step.customWeightPercent != null) Color(0xFFEFF6FF) else Color(0xFFF1F5F9))
                .clickable { onEditWeight() }
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = weightLabel,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (step.customWeightPercent != null) ProfessionalPrimary else Color(0xFF64748B)
            )
        }
    }
}
