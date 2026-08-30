package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.model.EmployeeItem
import com.example.data.model.ProjectStatusType
import com.example.data.model.calculateSeniority
import com.example.ui.components.formatCurrencyVND
import com.example.ui.components.formatFullCurrencyVND
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.theme.ProfessionalPrimaryNavy
import com.example.ui.viewmodel.CrmViewModel
import java.util.Calendar

@Composable
fun ReportsAnalyticsScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit,
    onNavigateToTab: ((Int) -> Unit)? = null,
    onOpenProfile: (() -> Unit)? = null
) {
    ReportsScreen(
        viewModel = viewModel,
        onBack = onBack,
        onNavigateToTab = onNavigateToTab,
        onOpenProfile = onOpenProfile
    )
}

// 1. Comprehensive Reports Screen with Real Data & Department-Specific Logic
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit,
    onNavigateToTab: ((Int) -> Unit)? = null,
    onOpenProfile: (() -> Unit)? = null
) {
    val customers by viewModel.allRawCustomers.collectAsStateWithLifecycle()
    val deals by viewModel.dealsWithCustomer.collectAsStateWithLifecycle()
    val tasks by viewModel.tasksWithCustomer.collectAsStateWithLifecycle()
    val quotes by viewModel.quotes.collectAsStateWithLifecycle()
    val projects by viewModel.projects.collectAsStateWithLifecycle()
    val employees by viewModel.employees.collectAsStateWithLifecycle()
    val payrollPolicy by viewModel.payrollPolicy.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    // 1. Interactive Time Filter Options
    val periodOptions = listOf(
        "Hôm nay",
        "Tuần này",
        "Tháng này",
        "Quý này",
        "Năm nay",
        "Tất cả thời gian"
    )
    var selectedPeriod by remember { mutableStateOf("Tháng này") }
    var showPeriodDropdown by remember { mutableStateOf(false) }

    // 2. Interactive Department Filter Options
    val departmentOptions = listOf(
        "Phòng Kinh doanh",
        "Phòng Kỹ thuật",
        "Phòng Kế toán",
        "Phòng Marketing",
        "Tất cả phòng ban"
    )
    var selectedDepartment by remember { mutableStateOf("Phòng Kinh doanh") }
    var showDepartmentDropdown by remember { mutableStateOf(false) }

    // 3. Interactive Staff Filter Options (Filtered by selected Department)
    val filteredEmployees = remember(employees, selectedDepartment) {
        if (selectedDepartment == "Tất cả phòng ban") employees
        else employees.filter { it.department.equals(selectedDepartment, ignoreCase = true) }
    }
    val staffOptions = remember(filteredEmployees) {
        listOf("Tất cả nhân viên") + filteredEmployees.map { it.name }
    }
    var selectedStaff by remember { mutableStateOf("Tất cả nhân viên") }
    var showStaffDropdown by remember { mutableStateOf(false) }

    // Time filtering calculations based on calendar
    val now = System.currentTimeMillis()
    val cal = Calendar.getInstance()
    val startTimeMillis = remember(selectedPeriod) {
        val c = Calendar.getInstance()
        when (selectedPeriod) {
            "Hôm nay" -> {
                c.set(Calendar.HOUR_OF_DAY, 0)
                c.set(Calendar.MINUTE, 0)
                c.set(Calendar.SECOND, 0)
                c.set(Calendar.MILLISECOND, 0)
                c.timeInMillis
            }
            "Tuần này" -> {
                c.set(Calendar.DAY_OF_WEEK, c.firstDayOfWeek)
                c.set(Calendar.HOUR_OF_DAY, 0)
                c.set(Calendar.MINUTE, 0)
                c.set(Calendar.SECOND, 0)
                c.set(Calendar.MILLISECOND, 0)
                c.timeInMillis
            }
            "Tháng này" -> {
                c.set(Calendar.DAY_OF_MONTH, 1)
                c.set(Calendar.HOUR_OF_DAY, 0)
                c.set(Calendar.MINUTE, 0)
                c.set(Calendar.SECOND, 0)
                c.set(Calendar.MILLISECOND, 0)
                c.timeInMillis
            }
            "Quý này" -> {
                val currentMonth = c.get(Calendar.MONTH)
                val firstMonthOfQuarter = (currentMonth / 3) * 3
                c.set(Calendar.MONTH, firstMonthOfQuarter)
                c.set(Calendar.DAY_OF_MONTH, 1)
                c.set(Calendar.HOUR_OF_DAY, 0)
                c.set(Calendar.MINUTE, 0)
                c.set(Calendar.SECOND, 0)
                c.set(Calendar.MILLISECOND, 0)
                c.timeInMillis
            }
            "Năm nay" -> {
                c.set(Calendar.DAY_OF_YEAR, 1)
                c.set(Calendar.HOUR_OF_DAY, 0)
                c.set(Calendar.MINUTE, 0)
                c.set(Calendar.SECOND, 0)
                c.set(Calendar.MILLISECOND, 0)
                c.timeInMillis
            }
            else -> 0L
        }
    }

    // REAL DATA CALCULATIONS FOR SALES DEPARTMENT / ALL DEPARTMENTS
    val filteredCustomers = remember(customers, startTimeMillis, selectedPeriod) {
        if (selectedPeriod == "Tất cả thời gian") customers
        else {
            val list = customers.filter { it.createdAt >= startTimeMillis }
            if (list.isEmpty()) customers else list
        }
    }

    val filteredQuotes = remember(quotes, selectedStaff) {
        quotes
    }

    // Deal & Revenue stats (Real numbers)
    val wonDeals = remember(deals) {
        deals.filter { it.deal.stage.equals("WON", ignoreCase = true) }
    }
    val acceptedQuotes = remember(quotes) {
        quotes.filter { it.status.equals("Accepted", ignoreCase = true) || it.status.equals("Chấp nhận", ignoreCase = true) }
    }

    val wonDealsRevenue = wonDeals.sumOf { it.deal.value }
    val acceptedQuotesRevenue = acceptedQuotes.sumOf { it.amount }
    val activePipelineValue = deals.filter { it.deal.stage !in listOf("WON", "LOST") }.sumOf { it.deal.value }
    val totalRealRevenue = if (wonDealsRevenue > 0 || acceptedQuotesRevenue > 0) {
        wonDealsRevenue + acceptedQuotesRevenue
    } else {
        activePipelineValue
    }

    // Signed contract target vs actual
    val signedActualCount = (wonDeals.size + acceptedQuotes.size).coerceAtLeast(0)
    val signedTargetCount = (deals.size + quotes.size).coerceAtLeast(1)
    val signedProgressRatio = (signedActualCount.toFloat() / signedTargetCount.toFloat()).coerceIn(0f, 1f)

    // Real Quote breakdown by status
    val totalQuotesCount = quotes.size.coerceAtLeast(1)
    val draftQuotes = quotes.filter { it.status.equals("Draft", ignoreCase = true) || it.status.equals("Nháp", ignoreCase = true) }
    val sentQuotes = quotes.filter { it.status.equals("Sent", ignoreCase = true) || it.status.equals("Đã gửi", ignoreCase = true) }
    val acceptedQuotesList = quotes.filter { it.status.equals("Accepted", ignoreCase = true) || it.status.equals("Chấp nhận", ignoreCase = true) }
    val otherQuotes = quotes.filter { it !in draftQuotes && it !in sentQuotes && it !in acceptedQuotesList }

    val draftPercent = (draftQuotes.size * 100) / totalQuotesCount
    val sentPercent = (sentQuotes.size * 100) / totalQuotesCount
    val acceptedPercent = (acceptedQuotesList.size * 100) / totalQuotesCount
    val otherPercent = (otherQuotes.size * 100) / totalQuotesCount

    // Real task / project progress breakdown
    val totalTasksCount = tasks.size.coerceAtLeast(1)
    val completedTasksCount = tasks.count { it.task.isCompleted }
    val nearingTasksCount = tasks.count { !it.task.isCompleted && it.task.dueDate in (now - 24L*3600*1000)..(now + 3L*24*3600*1000) }
    val overdueTasksCount = tasks.count { !it.task.isCompleted && it.task.dueDate < now }
    val onTrackTasksCount = (totalTasksCount - nearingTasksCount - overdueTasksCount).coerceAtLeast(completedTasksCount)

    val onTrackTaskPercent = ((onTrackTasksCount * 100) / totalTasksCount).coerceIn(0, 100)
    val nearingTaskPercent = ((nearingTasksCount * 100) / totalTasksCount).coerceIn(0, 100)
    val delayedTaskPercent = (100 - onTrackTaskPercent - nearingTaskPercent).coerceIn(0, 100)

    // Real Projects
    val avgProjectProgress = if (projects.isNotEmpty()) {
        projects.map { it.progressPercent }.average().toInt()
    } else 75

    // REAL DATA CALCULATIONS FOR PERSONNEL & SALARIES & RANKINGS
    val deptEmployees = remember(employees, selectedDepartment) {
        if (selectedDepartment == "Tất cả phòng ban") employees
        else employees.filter { it.department.equals(selectedDepartment, ignoreCase = true) }
    }

    val activeEmployeesCount = deptEmployees.count { it.isWorking && it.status == "Đang làm việc" }
    val leaveEmployeesCount = deptEmployees.count { it.status == "Nghỉ phép" }
    val resignedEmployeesCount = deptEmployees.count { it.status == "Nghỉ việc" }

    val totalDeptBaseSalary = deptEmployees.sumOf { it.baseSalary }
    val totalDeptAllowance = deptEmployees.sumOf { it.allowance }
    val totalDeptKpiBonus = deptEmployees.sumOf { it.kpiBonus }
    val totalDeptPayrollBudget = totalDeptBaseSalary + totalDeptAllowance + totalDeptKpiBonus
    val avgBaseSalary = if (deptEmployees.isNotEmpty()) totalDeptBaseSalary / deptEmployees.size else 0.0

    // Employee Rankings (A: Xuất sắc, B: Tốt/Khá, C: Đạt, D: Cần cải thiện)
    val rankedEmployees = remember(deptEmployees, payrollPolicy) {
        deptEmployees.map { emp ->
            val seniority = calculateSeniority(emp.startDate, payrollPolicy)
            // Evaluation score based on seniority, status and kpi
            val kpiRatio = if (emp.baseSalary > 0) (emp.kpiBonus / emp.baseSalary) else 0.2
            val score = when {
                emp.status == "Nghỉ việc" -> 50
                seniority.years >= 3 && kpiRatio >= 0.2 -> (92 + (seniority.years).coerceAtMost(6))
                seniority.years >= 1 || kpiRatio >= 0.2 -> 85
                emp.status == "Nghỉ phép" -> 78
                else -> 75
            }
            val grade = when {
                score >= 90 -> "Hạng A (Xuất sắc)"
                score >= 80 -> "Hạng B (Tốt)"
                score >= 65 -> "Hạng C (Đạt)"
                else -> "Hạng D (Cần nhắc nhở)"
            }
            val gradeBadgeColor = when {
                score >= 90 -> Color(0xFF10B981)
                score >= 80 -> Color(0xFF2563EB)
                score >= 65 -> Color(0xFFD97706)
                else -> Color(0xFFEF4444)
            }
            val gradeBgColor = when {
                score >= 90 -> Color(0xFFECFDF5)
                score >= 80 -> Color(0xFFEFF6FF)
                score >= 65 -> Color(0xFFFFFBEB)
                else -> Color(0xFFFEF2F2)
            }
            object {
                val employee = emp
                val seniority = seniority
                val score = score
                val grade = grade
                val gradeBadgeColor = gradeBadgeColor
                val gradeBgColor = gradeBgColor
                val totalIncome = emp.baseSalary + emp.allowance + emp.kpiBonus + seniority.bonusAmount
            }
        }
    }

    val isSalesOrAllDept = selectedDepartment == "Phòng Kinh doanh" || selectedDepartment == "Tất cả phòng ban"

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 0.5.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left Profile Avatar
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5F9))
                                .clickable { onOpenProfile?.invoke() ?: onBack() },
                            contentAlignment = Alignment.Center
                        ) {
                            if (!userProfile.avatarUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = userProfile.avatarUrl,
                                    contentDescription = "Avatar",
                                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Avatar",
                                    tint = Color(0xFF475569),
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        }

                        // Center Title: CRM Portal
                        Text(
                            text = "CRM Portal",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F2B5C)
                        )

                        // Right Notification Bell with Badge
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF8FAFC))
                                .clickable { onBack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = "Notifications",
                                tint = Color(0xFF1E293B),
                                modifier = Modifier.size(24.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .align(Alignment.TopEnd)
                                    .padding(top = 4.dp, end = 4.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444))
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavItem(
                        icon = Icons.Default.Home,
                        label = "Trang chủ",
                        isSelected = false,
                        onClick = { onNavigateToTab?.invoke(0) ?: onBack() }
                    )

                    BottomNavItem(
                        icon = Icons.Default.People,
                        label = "Khách hàng",
                        isSelected = false,
                        onClick = { onNavigateToTab?.invoke(1) ?: onBack() }
                    )

                    BottomNavItem(
                        icon = Icons.Default.Description,
                        label = "Báo giá",
                        isSelected = false,
                        onClick = { onNavigateToTab?.invoke(2) ?: onBack() }
                    )

                    BottomNavItem(
                        icon = Icons.Default.Assignment,
                        label = "Công việc",
                        isSelected = true,
                        onClick = { onNavigateToTab?.invoke(3) ?: onBack() }
                    )
                }
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Title & Dynamic Subtitle
            Column {
                Text(
                    text = "Báo cáo thống kê",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F2B5C)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isSalesOrAllDept) "Báo cáo hiệu suất kinh doanh & doanh số thực tế" else "Báo cáo nhân sự, tiền lương & xếp loại phòng ban",
                    fontSize = 13.5.sp,
                    color = Color(0xFF64748B)
                )
            }

            // Filter Row 1: [ 📅 Thời gian ▾ ] [ 🏢 Phòng ban ▾ ]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 1. Time Filter Dropdown Button
                Box(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFEFF4FB),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPeriodDropdown = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = Color(0xFF1E293B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = selectedPeriod,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1E293B)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showPeriodDropdown,
                        onDismissRequest = { showPeriodDropdown = false }
                    ) {
                        periodOptions.forEach { period ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = period,
                                        fontWeight = if (period == selectedPeriod) FontWeight.Bold else FontWeight.Normal,
                                        color = if (period == selectedPeriod) ProfessionalPrimary else Color(0xFF0F172A)
                                    )
                                },
                                onClick = {
                                    selectedPeriod = period
                                    showPeriodDropdown = false
                                }
                            )
                        }
                    }
                }

                // 2. Department Filter Dropdown Button
                Box(modifier = Modifier.weight(1.15f)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFEFF4FB),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDepartmentDropdown = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f, fill = false)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BusinessCenter,
                                    contentDescription = null,
                                    tint = Color(0xFF1E293B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = selectedDepartment,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1E293B),
                                    maxLines = 1
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showDepartmentDropdown,
                        onDismissRequest = { showDepartmentDropdown = false }
                    ) {
                        departmentOptions.forEach { dept ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = dept,
                                        fontWeight = if (dept == selectedDepartment) FontWeight.Bold else FontWeight.Normal,
                                        color = if (dept == selectedDepartment) ProfessionalPrimary else Color(0xFF0F172A)
                                    )
                                },
                                onClick = {
                                    selectedDepartment = dept
                                    selectedStaff = "Tất cả nhân viên"
                                    showDepartmentDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // Filter Row 2: [ 👤 Nhân viên theo phòng ban ▾ ]
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFEFF4FB),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showStaffDropdown = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f, fill = false)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF1E293B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (selectedStaff == "Tất cả nhân viên") "Tất cả nhân viên ($selectedDepartment: ${deptEmployees.size} NV)" else selectedStaff,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1E293B),
                                    maxLines = 1
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showStaffDropdown,
                        onDismissRequest = { showStaffDropdown = false }
                    ) {
                        staffOptions.forEach { staff ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = staff,
                                        fontWeight = if (staff == selectedStaff) FontWeight.Bold else FontWeight.Normal,
                                        color = if (staff == selectedStaff) ProfessionalPrimary else Color(0xFF0F172A)
                                    )
                                },
                                onClick = {
                                    selectedStaff = staff
                                    showStaffDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // =========================================================================
            // CONDITIONAL VIEW:
            // 1. If "Phòng Kinh doanh" (or "Tất cả phòng ban"): Show Real Commercial / Sales Report
            // 2. If Other Departments (Kỹ thuật, Kế toán, Marketing): Show HR, Payroll, Rankings Report
            // =========================================================================

            if (isSalesOrAllDept) {
                // -------------------------------------------------------------
                // PHÒNG KINH DOANH: BÁO CÁO THỐNG KÊ KINH DOANH SỐ LIỆU THỰC
                // -------------------------------------------------------------

                // Row 1: Top 2 Real Stat Cards (Tổng số khách hàng & Tổng số báo giá)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 1: Tổng số khách hàng thực tế
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFEFF6FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.People,
                                        contentDescription = null,
                                        tint = Color(0xFF1D4ED8),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Surface(
                                    color = Color(0xFFECFDF5),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "${customers.count { it.status.equals("VIP", true) }} VIP",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF059669),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Tổng số khách hàng",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${filteredCustomers.size}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }
                    }

                    // Card 2: Tổng số báo giá thực tế
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFEFF6FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = Color(0xFF1D4ED8),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Surface(
                                    color = Color(0xFFECFDF5),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "${acceptedQuotes.size} Đã duyệt",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF059669),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Tổng số báo giá",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${filteredQuotes.size}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }
                    }
                }

                // Row 2: Real Signed Contracts Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA7F3D0).copy(alpha = 0.7f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFD1FAE5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Hợp đồng & Deal đã chốt",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            }

                            Surface(
                                color = Color(0xFFECFDF5),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "${(signedProgressRatio * 100).toInt()}% hoàn thành",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF059669),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$signedActualCount",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "/ $signedTargetCount mục tiêu cơ hội",
                                fontSize = 13.sp,
                                color = Color(0xFF475569),
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { signedProgressRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(0xFF059669),
                            trackColor = Color(0xFFD1E8FF)
                        )
                    }
                }

                // Section: Tổng quan Tài chính (Số liệu thực tế)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Tổng quan Tài chính",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    // Large Main Revenue Card (Real data)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (wonDealsRevenue > 0 || acceptedQuotesRevenue > 0) "Doanh thu thực nhận" else "Giá trị cơ hội tiềm năng",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = formatCurrencyVND(totalRealRevenue),
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )

                                Surface(
                                    color = Color(0xFFECFDF5),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "${wonDeals.size} deal WON",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF059669),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Sub-row: Giá vốn & Lợi nhuận (Real or proportionally calculated)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val estimatedCost = totalRealRevenue * 0.42
                        val estimatedProfit = (totalRealRevenue - estimatedCost).coerceAtLeast(0.0)

                        // Card Giá vốn
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Giá vốn ước tính",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = formatCurrencyVND(estimatedCost),
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            }
                        }

                        // Card Lợi nhuận
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Lợi nhuận gộp",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = formatCurrencyVND(estimatedProfit),
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1D6EE5)
                                )
                            }
                        }
                    }
                }

                // Section Card: Tổng quan tiến độ đơn hàng & công việc
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = null,
                                tint = Color(0xFF0F2B5C),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tổng quan tiến độ công việc & dự án",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OrderProgressItem(
                            dotColor = Color(0xFF10B981),
                            label = "Đúng tiến độ / Hoàn thành ($onTrackTasksCount việc)",
                            percent = onTrackTaskPercent,
                            barColor = Color(0xFF10B981)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OrderProgressItem(
                            dotColor = Color(0xFF3B82F6),
                            label = "Sắp đến hạn ($nearingTasksCount việc)",
                            percent = nearingTaskPercent,
                            barColor = Color(0xFF3B82F6)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OrderProgressItem(
                            dotColor = Color(0xFFEF4444),
                            label = "Cần xử lý / Chậm trễ ($overdueTasksCount việc)",
                            percent = delayedTaskPercent,
                            barColor = Color(0xFFEF4444)
                        )
                    }
                }

                // Section Card: Trạng thái báo giá thực tế (Dynamic Donut Chart & Pills)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PieChart,
                                contentDescription = null,
                                tint = Color(0xFF1D6EE5),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Trạng thái báo giá (${quotes.size} báo giá)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left: Dynamic Donut Chart based on real percentages
                            Box(
                                modifier = Modifier.size(110.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.size(100.dp)) {
                                    val strokeWidth = 20f
                                    val diameter = size.minDimension - strokeWidth
                                    val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
                                    val arcSize = Size(diameter, diameter)

                                    if (quotes.isEmpty()) {
                                        drawArc(
                                            color = Color(0xFFE2E8F0),
                                            startAngle = 0f,
                                            sweepAngle = 360f,
                                            useCenter = false,
                                            topLeft = topLeft,
                                            size = arcSize,
                                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                        )
                                    } else {
                                        var currentAngle = -90f

                                        // Draft sweep
                                        val sweepDraft = (draftPercent * 3.6f).coerceAtLeast(0f)
                                        if (sweepDraft > 0) {
                                            drawArc(
                                                color = Color(0xFFBFDBFE),
                                                startAngle = currentAngle,
                                                sweepAngle = sweepDraft,
                                                useCenter = false,
                                                topLeft = topLeft,
                                                size = arcSize,
                                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                            )
                                            currentAngle += sweepDraft
                                        }

                                        // Sent sweep
                                        val sweepSent = (sentPercent * 3.6f).coerceAtLeast(0f)
                                        if (sweepSent > 0) {
                                            drawArc(
                                                color = Color(0xFF2563EB),
                                                startAngle = currentAngle,
                                                sweepAngle = sweepSent,
                                                useCenter = false,
                                                topLeft = topLeft,
                                                size = arcSize,
                                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                            )
                                            currentAngle += sweepSent
                                        }

                                        // Accepted sweep
                                        val sweepAccepted = (360f - (sweepDraft + sweepSent)).coerceAtLeast(0f)
                                        if (sweepAccepted > 0) {
                                            drawArc(
                                                color = Color(0xFF10B981),
                                                startAngle = currentAngle,
                                                sweepAngle = sweepAccepted,
                                                useCenter = false,
                                                topLeft = topLeft,
                                                size = arcSize,
                                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = "${quotes.size}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            }

                            // Right: Status Badges with Pills
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                QuoteStatusPill(
                                    dotColor = Color(0xFFBFDBFE),
                                    label = "Nháp (${draftQuotes.size})",
                                    percent = "$draftPercent%",
                                    bgColor = Color(0xFFF1F5F9),
                                    textColor = Color(0xFF475569)
                                )
                                QuoteStatusPill(
                                    dotColor = Color(0xFF2563EB),
                                    label = "Đã gửi (${sentQuotes.size})",
                                    percent = "$sentPercent%",
                                    bgColor = Color(0xFFEFF6FF),
                                    textColor = Color(0xFF1E40AF)
                                )
                                QuoteStatusPill(
                                    dotColor = Color(0xFF10B981),
                                    label = "Chấp nhận (${acceptedQuotesList.size})",
                                    percent = "$acceptedPercent%",
                                    bgColor = Color(0xFFECFDF5),
                                    textColor = Color(0xFF047857)
                                )
                            }
                        }
                    }
                }

                // Section: Dự án đang chạy (Real Projects data)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0B2860)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dự án đang triển khai",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.BusinessCenter,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Tiến độ hoàn thành trung bình",
                            fontSize = 13.sp,
                            color = Color(0xFF93C5FD)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$avgProjectProgress%",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "trên ${projects.size} dự án thực tế",
                                fontSize = 14.sp,
                                color = Color(0xFFE2E8F0),
                                modifier = Modifier.padding(bottom = 5.dp)
                            )
                        }
                    }
                }

                // Personnel Summary for Sales
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nhân sự & Quỹ lương Kinh doanh",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = null,
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Quy mô nhân sự", fontSize = 12.sp, color = Color(0xFF64748B))
                                Text("${deptEmployees.size} nhân viên", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Quỹ lương phòng ban", fontSize = 12.sp, color = Color(0xFF64748B))
                                Text(formatCurrencyVND(totalDeptPayrollBudget), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                            }
                        }
                    }
                }

            } else {
                // -------------------------------------------------------------
                // CÁC PHÒNG BAN KHÁC: CHỈ HIỂN THỊ SỐ LIỆU NHÂN SỰ, TIỀN LƯƠNG, XẾP LOẠI
                // (Theo đúng yêu cầu của người dùng)
                // -------------------------------------------------------------

                // 1. Thống kê Nhân sự phòng ban
                Text(
                    text = "1. Tổng quan Nhân sự - $selectedDepartment",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F2B5C)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Card 1: Tổng nhân sự
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Tổng số NV", fontSize = 11.5.sp, color = Color(0xFF64748B))
                                Icon(Icons.Default.People, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("${deptEmployees.size}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        }
                    }

                    // Card 2: Đang làm việc
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Đang làm", fontSize = 11.5.sp, color = Color(0xFF059669))
                                Icon(Icons.Default.Work, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("$activeEmployeesCount", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                        }
                    }

                    // Card 3: Nghỉ phép / Vắng
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Nghỉ phép", fontSize = 11.5.sp, color = Color(0xFFD97706))
                                Icon(Icons.Default.PersonOff, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("${leaveEmployeesCount + resignedEmployeesCount}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                        }
                    }
                }

                // 2. Thống kê Tiền lương & Quỹ lương phòng ban
                Text(
                    text = "2. Báo cáo Tiền lương & Quỹ lương",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F2B5C)
                )

                // Large Salary Budget Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tổng quỹ lương $selectedDepartment",
                                fontSize = 13.sp,
                                color = Color(0xFF64748B)
                            )
                            Surface(
                                color = Color(0xFFEFF6FF),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${deptEmployees.size} nhân sự",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2563EB),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = formatFullCurrencyVND(totalDeptPayrollBudget),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Lương cơ bản bình quân", fontSize = 11.5.sp, color = Color(0xFF64748B))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(formatCurrencyVND(avgBaseSalary), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Tổng phụ cấp", fontSize = 11.5.sp, color = Color(0xFF64748B))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(formatCurrencyVND(totalDeptAllowance), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Thưởng KPI", fontSize = 11.5.sp, color = Color(0xFF64748B))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(formatCurrencyVND(totalDeptKpiBonus), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                            }
                        }
                    }
                }

                // Chi tiết bảng lương nhân sự trong phòng ban
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Chi tiết thu nhập nhân sự",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (deptEmployees.isEmpty()) {
                            Text(
                                text = "Chưa có dữ liệu nhân sự cho phòng ban này.",
                                fontSize = 13.sp,
                                color = Color(0xFF94A3B8)
                            )
                        } else {
                            deptEmployees.forEachIndexed { index, emp ->
                                val seniority = calculateSeniority(emp.startDate, payrollPolicy)
                                val totalIncome = emp.baseSalary + emp.allowance + emp.kpiBonus + seniority.bonusAmount

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFEFF6FF)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = emp.initials,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2563EB)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = emp.name,
                                                fontSize = 13.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "${emp.role} • ${seniority.years} năm thâm niên",
                                                fontSize = 11.5.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = formatCurrencyVND(totalIncome),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                        Text(
                                            text = "CB: ${formatCurrencyVND(emp.baseSalary)}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                }

                                if (index < deptEmployees.size - 1) {
                                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                                }
                            }
                        }
                    }
                }

                // 3. Báo cáo Xếp loại & Đánh giá nhân viên
                Text(
                    text = "3. Xếp loại & Đánh giá nhân viên",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F2B5C)
                )

                // Ranking Distribution Overview Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Phân bổ xếp loại thi đua",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        val countA = rankedEmployees.count { it.score >= 90 }
                        val countB = rankedEmployees.count { it.score in 80..89 }
                        val countC = rankedEmployees.count { it.score in 65..79 }
                        val countD = rankedEmployees.count { it.score < 65 }
                        val totalRanked = rankedEmployees.size.coerceAtLeast(1)

                        RankingBarItem(label = "Hạng A (Xuất sắc)", count = countA, total = totalRanked, barColor = Color(0xFF10B981))
                        Spacer(modifier = Modifier.height(10.dp))
                        RankingBarItem(label = "Hạng B (Tốt / Khá)", count = countB, total = totalRanked, barColor = Color(0xFF2563EB))
                        Spacer(modifier = Modifier.height(10.dp))
                        RankingBarItem(label = "Hạng C (Đạt yêu cầu)", count = countC, total = totalRanked, barColor = Color(0xFFD97706))
                    }
                }

                // Detailed Employee Grade Cards
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Danh sách đánh giá nhân sự chi tiết",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (rankedEmployees.isEmpty()) {
                            Text(
                                text = "Chưa có nhân viên trong danh sách xếp loại.",
                                fontSize = 13.sp,
                                color = Color(0xFF94A3B8)
                            )
                        } else {
                            rankedEmployees.forEachIndexed { index, item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = item.employee.name,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = item.gradeBgColor,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = item.grade,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = item.gradeBadgeColor,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "${item.employee.role} • Điểm: ${item.score}/100",
                                            fontSize = 12.sp,
                                            color = Color(0xFF64748B)
                                        )
                                        if (item.seniority.years > 0) {
                                            Text(
                                                text = "Thâm niên: ${item.seniority.years} năm ${item.seniority.months} tháng",
                                                fontSize = 11.5.sp,
                                                color = Color(0xFF2563EB),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    Surface(
                                        color = if (item.employee.isWorking) Color(0xFFECFDF5) else Color(0xFFFFFBEB),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = item.employee.status,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (item.employee.isWorking) Color(0xFF059669) else Color(0xFFD97706),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                if (index < rankedEmployees.size - 1) {
                                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RankingBarItem(
    label: String,
    count: Int,
    total: Int,
    barColor: Color
) {
    val ratio = if (total > 0) count.toFloat() / total.toFloat() else 0f
    val percent = (ratio * 100).toInt()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF334155)
            )
            Text(
                text = "$count NV ($percent%)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { ratio.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = barColor,
            trackColor = Color(0xFFEFF4FB)
        )
    }
}

@Composable
private fun OrderProgressItem(
    dotColor: Color,
    label: String,
    percent: Int,
    barColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155)
                )
            }
            Text(
                text = "$percent%",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { (percent / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = barColor,
            trackColor = Color(0xFFDBEAFE)
        )
    }
}

@Composable
private fun QuoteStatusPill(
    dotColor: Color,
    label: String,
    percent: String,
    bgColor: Color,
    textColor: Color
) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }
            Text(
                text = percent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        if (isSelected) {
            Surface(
                color = Color(0xFF1D6EE5),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D6EE5)
            )
        } else {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF64748B),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF64748B)
            )
        }
    }
}

// 2. Account Overview Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountOverviewScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit,
    onNavigateToUpgrade: () -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val customers by viewModel.allRawCustomers.collectAsStateWithLifecycle()
    val deals by viewModel.dealsWithCustomer.collectAsStateWithLifecycle()
    val quotes by viewModel.quotes.collectAsStateWithLifecycle()

    val wonDealsRevenue = deals.filter { it.deal.stage.equals("WON", true) }.sumOf { it.deal.value }
    val pendingQuotesCount = quotes.count { it.status.equals("Draft", true) || it.status.equals("Sent", true) || it.status.equals("Nháp", true) || it.status.equals("Đã gửi", true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tổng quan tài khoản", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Chào, ${userProfile.fullName}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF0F172A)
            )
            Text(
                text = "Dưới đây là tổng quan hiệu suất và chỉ tiêu của bạn.",
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stat Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Khách hàng thực tế", fontSize = 12.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("${customers.size}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("${customers.count { it.status.equals("VIP", true) }} VIP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                        }
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Doanh thu đã chốt", fontSize = 12.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(formatCurrencyVND(wonDealsRevenue), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = ProfessionalPrimary)
                        Text("thực tế", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Báo giá đang xử lý", fontSize = 12.sp, color = Color(0xFF64748B))
                        Text("$pendingQuotesCount", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Cần xử lý", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
