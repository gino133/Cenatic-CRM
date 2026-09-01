package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AccountTier
import com.example.data.model.DealWithCustomer
import com.example.data.model.EmployeeItem
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.viewmodel.CrmViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// =========================================================================
// 1. MÀN HÌNH DOANH THU ĐÃ CHỐT - DÀNH CHO TÀI KHOẢN MIỄN PHÍ (FREE)
// =========================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreeRevenueScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit,
    onNavigateToUpgrade: () -> Unit
) {
    val deals by viewModel.dealsWithCustomer.collectAsStateWithLifecycle()
    val wonDeals = deals.filter { it.deal.stage.equals("WON", true) }

    val currentMonth = remember { Calendar.getInstance().get(Calendar.MONTH) + 1 }
    var selectedMonth by remember { mutableIntStateOf(currentMonth) }

    val dealsInSelectedMonth = remember(wonDeals, selectedMonth) {
        wonDeals.filter { item ->
            val cal = Calendar.getInstance()
            if (item.deal.expectedCloseDate > 0) {
                cal.timeInMillis = item.deal.expectedCloseDate
                (cal.get(Calendar.MONTH) + 1) == selectedMonth
            } else {
                (item.deal.id.toInt() % 12 + 1) == selectedMonth || wonDeals.size <= 3
            }
        }
    }

    val totalRevenueMonth = dealsInSelectedMonth.sumOf { it.deal.value }
    val avgDealValue = if (dealsInSelectedMonth.isNotEmpty()) totalRevenueMonth / dealsInSelectedMonth.size else 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Doanh thu trong tháng", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFF1F5F9)
                            ) {
                                Text(
                                    text = "FREE",
                                    color = Color(0xFF64748B),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text("Số liệu doanh thu đã chốt theo tháng", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Bộ lọc Tháng
            item {
                Text(
                    text = "CHỌN THÁNG XEM DOANH THU (NĂM 2026)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items((1..12).toList()) { m ->
                        val isSelected = m == selectedMonth
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedMonth = m },
                            label = {
                                Text(
                                    text = "Tháng $m",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) ProfessionalPrimary else Color(0xFF475569)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFEFF6FF),
                                containerColor = Color.White
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) ProfessionalPrimary else Color(0xFFE2E8F0)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            // Card Tổng Doanh Thu Tháng
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Tổng doanh thu Tháng $selectedMonth/2026",
                                    fontSize = 13.sp,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = formatCurrencyVND(totalRevenueMonth),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEFF6FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = ProfessionalPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Số deal đã chốt", fontSize = 12.sp, color = Color(0xFF64748B))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${dealsInSelectedMonth.size} hợp đồng",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Giá trị trung bình/deal", fontSize = 12.sp, color = Color(0xFF64748B))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = formatCurrencyVND(avgDealValue),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF059669)
                                )
                            }
                        }
                    }
                }
            }

            // Danh sách các deal trong tháng
            item {
                Text(
                    text = "DANH SÁCH DEAL CHỐT THÀNH CÔNG (THÁNG $selectedMonth)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    letterSpacing = 0.5.sp
                )
            }

            if (dealsInSelectedMonth.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Chưa có deal nào chốt trong Tháng $selectedMonth",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "Các deal được đánh dấu 'Đã chốt (WON)' sẽ hiển thị tại đây.",
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(dealsInSelectedMonth) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFECFDF5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF059669),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.deal.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF0F172A),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Khách hàng: ${item.customerName}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = formatCurrencyVND(item.deal.value),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF047857)
                                )
                                Text(
                                    text = "Đã chốt",
                                    fontSize = 11.sp,
                                    color = Color(0xFF059669),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Banner Nâng Cấp VIP
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToUpgrade() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFBEB)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFFDE68A))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF3C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Mở khóa Báo cáo Doanh thu VIP",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF92400E)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Xem theo Tuần/Tháng/Quý/Năm, báo cáo chi tiết từng khách hàng & đơn hàng.",
                                fontSize = 12.sp,
                                color = Color(0xFFB45309),
                                lineHeight = 16.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 2. MÀN HÌNH DOANH THU ĐÃ CHỐT - DÀNH CHO TÀI KHOẢN VIP (VÀ BUSINESS)
// =========================================================================
enum class VipRevenueTimeFilter(val label: String) {
    WEEK("Theo Tuần"),
    MONTH("Theo Tháng"),
    QUARTER("Theo Quý"),
    YEAR("Theo Năm")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VipRevenueScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit,
    onNavigateToQuotes: () -> Unit = {},
    onNavigateToCustomers: () -> Unit = {}
) {
    val deals by viewModel.dealsWithCustomer.collectAsStateWithLifecycle()
    val wonDeals = deals.filter { it.deal.stage.equals("WON", true) }
    val allCustomers by viewModel.allRawCustomers.collectAsStateWithLifecycle()

    var timeFilter by remember { mutableStateOf(VipRevenueTimeFilter.MONTH) }
    var selectedPeriodIndex by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }
    var selectedReportTab by remember { mutableIntStateOf(0) } // 0: Tổng quan & Khách hàng, 1: Chi tiết Đơn hàng

    val totalWonRevenue = wonDeals.sumOf { it.deal.value }

    // Nhóm doanh thu theo khách hàng
    val customerRevenueMap = remember(wonDeals, allCustomers) {
        val map = mutableMapOf<Long, Double>()
        wonDeals.forEach { item ->
            val current = map.getOrDefault(item.deal.customerId, 0.0)
            map[item.deal.customerId] = current + item.deal.value
        }
        allCustomers.map { cust ->
            val rev = map[cust.id] ?: 0.0
            Triple(cust, rev, wonDeals.count { it.deal.customerId == cust.id })
        }.sortedByDescending { it.second }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Báo cáo Doanh thu VIP", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFFEF3C7)
                            ) {
                                Text(
                                    text = "VIP PRO",
                                    color = Color(0xFFD97706),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text("Phân tích đa chiều: Tuần, Tháng, Quý, Năm", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Bộ lọc Thời gian: Tuần / Tháng / Quý / Năm
            item {
                Column {
                    Text(
                        text = "BỘ LỌC CHU KỲ DOANH THU",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VipRevenueTimeFilter.entries.forEach { filter ->
                            val isSelected = timeFilter == filter
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        timeFilter = filter
                                        selectedPeriodIndex = 1
                                    },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(0xFFFEF3C7) else Color.White,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) Color(0xFFD97706) else Color(0xFFE2E8F0)
                                )
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = filter.label,
                                        fontSize = 12.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color(0xFF92400E) else Color(0xFF475569)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Lựa chọn con theo chu kỳ
                    when (timeFilter) {
                        VipRevenueTimeFilter.WEEK -> {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(listOf("Tuần này", "Tuần trước", "2 tuần trước", "3 tuần trước")) { w ->
                                    FilterChip(
                                        selected = true,
                                        onClick = {},
                                        label = { Text(w, fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFFEFF6FF)
                                        )
                                    )
                                }
                            }
                        }
                        VipRevenueTimeFilter.MONTH -> {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items((1..12).toList()) { m ->
                                    val isSelected = m == selectedPeriodIndex
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedPeriodIndex = m },
                                        label = { Text("Tháng $m", fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFFFEF3C7)
                                        )
                                    )
                                }
                            }
                        }
                        VipRevenueTimeFilter.QUARTER -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                (1..4).forEach { q ->
                                    val isSelected = q == selectedPeriodIndex
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedPeriodIndex = q },
                                        label = { Text("Quý $q (2026)", fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFFFEF3C7)
                                        )
                                    )
                                }
                            }
                        }
                        VipRevenueTimeFilter.YEAR -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(2025, 2026, 2027).forEach { y ->
                                    val isSelected = y == 2026
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {},
                                        label = { Text("Năm $y", fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFFFEF3C7)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Stat Cards Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Doanh thu ${timeFilter.label} (${if (timeFilter == VipRevenueTimeFilter.MONTH) "Tháng $selectedPeriodIndex" else "Toàn kỳ"})",
                                    fontSize = 13.sp,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = formatCurrencyVND(totalWonRevenue),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Tăng trưởng so với kỳ trước", fontSize = 11.5.sp, color = Color(0xFF64748B))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("↑ +24.8%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Tổng số Deal thắng", fontSize = 11.5.sp, color = Color(0xFF64748B))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("${wonDeals.size} đơn chốt", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ProfessionalPrimary)
                            }
                        }
                    }
                }
            }

            // Tab chuyển đổi: Báo cáo theo Khách hàng & Báo cáo theo Đơn hàng
            item {
                TabRow(
                    selectedTabIndex = selectedReportTab,
                    containerColor = Color.White,
                    contentColor = Color(0xFFD97706),
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedReportTab]),
                            color = Color(0xFFD97706)
                        )
                    }
                ) {
                    Tab(
                        selected = selectedReportTab == 0,
                        onClick = { selectedReportTab = 0 },
                        text = {
                            Text(
                                text = "Báo cáo theo Khách hàng",
                                fontWeight = if (selectedReportTab == 0) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    )
                    Tab(
                        selected = selectedReportTab == 1,
                        onClick = { selectedReportTab = 1 },
                        text = {
                            Text(
                                text = "Chi tiết Đơn hàng & Hợp đồng",
                                fontWeight = if (selectedReportTab == 1) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            if (selectedReportTab == 0) {
                // Báo cáo theo từng Khách hàng
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "XẾP HẠNG DOANH THU THEO KHÁCH HÀNG",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "${customerRevenueMap.size} khách hàng",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                items(customerRevenueMap) { (cust, rev, count) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (rev > 0) Color(0xFFFEF3C7) else Color(0xFFF1F5F9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cust.name.firstOrNull()?.toString()?.uppercase() ?: "K",
                                    fontWeight = FontWeight.Bold,
                                    color = if (rev > 0) Color(0xFFD97706) else Color(0xFF64748B),
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = cust.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF0F172A)
                                    )
                                    if (cust.status.equals("VIP", true)) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFFFEF3C7)
                                        ) {
                                            Text(
                                                text = "VIP",
                                                color = Color(0xFFD97706),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "$count đơn hàng chốt • ${cust.phone.ifBlank { "Chưa có SĐT" }}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = formatCurrencyVND(rev),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = if (rev > 0) Color(0xFF047857) else Color(0xFF94A3B8)
                                )
                                Text(
                                    text = if (rev > 0) "Doanh số thực" else "Chưa phát sinh",
                                    fontSize = 11.sp,
                                    color = if (rev > 0) Color(0xFF059669) else Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }
            } else {
                // Báo cáo chi tiết theo từng Đơn hàng & Báo giá
                item {
                    Text(
                        text = "CHI TIẾT HỢP ĐỒNG & BÁO GIÁ ĐÃ KÝ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        letterSpacing = 0.5.sp
                    )
                }

                if (wonDeals.isEmpty()) {
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
                                Text("Chưa có dữ liệu đơn hàng chi tiết.", color = Color(0xFF64748B))
                            }
                        }
                    }
                } else {
                    items(wonDeals) { item ->
                        val dateFormatted = if (item.deal.expectedCloseDate > 0) {
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(item.deal.expectedCloseDate))
                        } else {
                            "31/08/2026"
                        }
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.deal.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF0F172A)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Khách hàng: ${item.customerName}",
                                            fontSize = 12.sp,
                                            color = Color(0xFF475569)
                                        )
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFECFDF5)
                                    ) {
                                        Text(
                                            text = "ĐÃ CHỐT HĐ",
                                            color = Color(0xFF059669),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = Color(0xFFF8FAFC))
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Ngày hoàn thành: $dateFormatted",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF64748B)
                                    )
                                    Text(
                                        text = formatCurrencyVND(item.deal.value),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color(0xFF047857)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// 3. MÀN HÌNH HIỆU SUẤT & KPIS - THỐNG NHẤT FORMAT CHUẨN + KHỐI VUÔNG TÍNH NĂNG
//    Cả tài khoản FREE, VIP, BUSINESS đều hiện giao diện theo format chuẩn của tài khoản thường,
//    có thêm các khối vuông (feature cards) dán nhãn VIP / BUSINESS.
//    Khi click vào từng khối thì mở trang chi tiết tương ứng, nút Quay lại sẽ trở về trang Hiệu suất & KPIs.
// =========================================================================

enum class KpiFeatureDetail {
    VIP_AI_FORECAST,
    VIP_DEAL_VELOCITY,
    VIP_MULTI_TARGET,
    BUSINESS_KPI_EVALUATION,
    BUSINESS_DEPARTMENT_MATRIX,
    BUSINESS_KPI_COMMISSION
}

data class KpiFeatureItem(
    val detail: KpiFeatureDetail,
    val title: String,
    val description: String,
    val tier: AccountTier,
    val icon: ImageVector,
    val primaryColor: Color,
    val lightBgColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnifiedPerformanceKpiScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit,
    onNavigateToUpgrade: () -> Unit,
    onNavigateToPayroll: () -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val tier = userProfile.accountTier

    // State lưu trang chi tiết đang được mở từ các khối vuông
    var activeDetailScreen by remember { mutableStateOf<KpiFeatureDetail?>(null) }

    // Nếu đang mở 1 trang chi tiết con:
    if (activeDetailScreen != null) {
        when (activeDetailScreen) {
            KpiFeatureDetail.VIP_AI_FORECAST -> {
                VipAiForecastDetailScreen(
                    viewModel = viewModel,
                    userTier = tier,
                    onBack = { activeDetailScreen = null },
                    onNavigateToUpgrade = onNavigateToUpgrade
                )
            }
            KpiFeatureDetail.VIP_DEAL_VELOCITY -> {
                VipDealVelocityDetailScreen(
                    viewModel = viewModel,
                    userTier = tier,
                    onBack = { activeDetailScreen = null },
                    onNavigateToUpgrade = onNavigateToUpgrade
                )
            }
            KpiFeatureDetail.VIP_MULTI_TARGET -> {
                VipMultiTargetDetailScreen(
                    viewModel = viewModel,
                    userTier = tier,
                    onBack = { activeDetailScreen = null },
                    onNavigateToUpgrade = onNavigateToUpgrade
                )
            }
            KpiFeatureDetail.BUSINESS_KPI_EVALUATION -> {
                BusinessKpiEvaluationDetailScreen(
                    viewModel = viewModel,
                    userTier = tier,
                    onBack = { activeDetailScreen = null },
                    onNavigateToUpgrade = onNavigateToUpgrade,
                    onNavigateToPayroll = onNavigateToPayroll
                )
            }
            KpiFeatureDetail.BUSINESS_DEPARTMENT_MATRIX -> {
                BusinessDepartmentMatrixDetailScreen(
                    viewModel = viewModel,
                    userTier = tier,
                    onBack = { activeDetailScreen = null },
                    onNavigateToUpgrade = onNavigateToUpgrade
                )
            }
            KpiFeatureDetail.BUSINESS_KPI_COMMISSION -> {
                BusinessKpiCommissionDetailScreen(
                    viewModel = viewModel,
                    userTier = tier,
                    onBack = { activeDetailScreen = null },
                    onNavigateToUpgrade = onNavigateToUpgrade,
                    onNavigateToPayroll = onNavigateToPayroll
                )
            }
            null -> {}
        }
        return
    }

    // TRANG CHÍNH: FORMAT CHUẨN TÀI KHOẢN THƯỜNG (FREE) + KHỐI VUÔNG CÁC TÍNH NĂNG DÁN NHÃN
    val deals by viewModel.dealsWithCustomer.collectAsStateWithLifecycle()
    val allCustomers by viewModel.allRawCustomers.collectAsStateWithLifecycle()
    val quotes by viewModel.quotes.collectAsStateWithLifecycle()

    val wonDeals = deals.filter { it.deal.stage.equals("WON", true) }
    
    data class MonthFilterItem(
        val id: String,
        val label: String,
        val month: Int,
        val year: Int
    )

    val monthOptions = remember {
        listOf(
            MonthFilterItem("2025_12", "Thg 12/2025", 12, 2025),
            MonthFilterItem("2026_01", "Thg 1/2026", 1, 2026),
            MonthFilterItem("2026_02", "Thg 2/2026", 2, 2026),
            MonthFilterItem("2026_03", "Thg 3/2026", 3, 2026),
            MonthFilterItem("2026_04", "Thg 4/2026", 4, 2026),
            MonthFilterItem("2026_05", "Thg 5/2026", 5, 2026),
            MonthFilterItem("2026_06", "Thg 6/2026", 6, 2026),
            MonthFilterItem("2026_07", "Thg 7/2026", 7, 2026),
            MonthFilterItem("2026_08", "Thg 8/2026 (Nay)", 8, 2026),
            MonthFilterItem("2026_09", "Thg 9/2026", 9, 2026),
            MonthFilterItem("2026_10", "Thg 10/2026", 10, 2026),
            MonthFilterItem("2026_11", "Thg 11/2026", 11, 2026),
            MonthFilterItem("2026_12", "Thg 12/2026", 12, 2026),
            MonthFilterItem("ALL", "Tất cả", -1, -1)
        )
    }
    var selectedFilterId by remember { mutableStateOf("2026_08") }

    val selectedOption = remember(selectedFilterId) {
        monthOptions.firstOrNull { it.id == selectedFilterId } ?: monthOptions[8]
    }

    val dealsInSelectedMonth = remember(wonDeals, selectedOption) {
        if (selectedOption.id == "ALL") {
            wonDeals
        } else {
            val filtered = wonDeals.filter { item ->
                val cal = Calendar.getInstance()
                if (item.deal.expectedCloseDate > 0) {
                    cal.timeInMillis = item.deal.expectedCloseDate
                    (cal.get(Calendar.MONTH) + 1) == selectedOption.month && cal.get(Calendar.YEAR) == selectedOption.year
                } else {
                    (item.deal.id.toInt() % 12 + 1) == selectedOption.month
                }
            }
            if (filtered.isEmpty() && wonDeals.isNotEmpty()) {
                wonDeals.take(2)
            } else {
                filtered
            }
        }
    }

    // 5 Khối KPI theo yêu cầu: Khách hàng, Doanh thu, Lợi nhuận, Số báo giá, Số đơn hàng
    val customerCount = allCustomers.size
    val totalRevenueMonth = if (dealsInSelectedMonth.isNotEmpty()) dealsInSelectedMonth.sumOf { it.deal.value } else wonDeals.sumOf { it.deal.value }
    val profitMonth = totalRevenueMonth * 0.30 // Ước tính biên lợi nhuận 30%
    val quoteCount = quotes.size
    val orderCount = if (dealsInSelectedMonth.isNotEmpty()) dealsInSelectedMonth.size else wonDeals.size

    // Danh sách 6 khối vuông tính năng nâng cao có dán nhãn
    val featureItems = remember {
        listOf(
            KpiFeatureItem(
                detail = KpiFeatureDetail.VIP_AI_FORECAST,
                title = "Dự báo Doanh thu AI",
                description = "AI phân tích dữ liệu, dự báo % đạt target & gợi ý hành động",
                tier = AccountTier.VIP,
                icon = Icons.Default.AutoAwesome,
                primaryColor = Color(0xFFD97706),
                lightBgColor = Color(0xFFFEF3C7)
            ),
            KpiFeatureItem(
                detail = KpiFeatureDetail.VIP_DEAL_VELOCITY,
                title = "Tốc độ Deal & Win-rate",
                description = "Chu kỳ chốt hợp đồng trung bình & tỷ lệ thắng theo phân khúc",
                tier = AccountTier.VIP,
                icon = Icons.Default.Speed,
                primaryColor = Color(0xFF0284C7),
                lightBgColor = Color(0xFFE0F2FE)
            ),
            KpiFeatureItem(
                detail = KpiFeatureDetail.VIP_MULTI_TARGET,
                title = "Mục tiêu Cá nhân Đa chiều",
                description = "Thiết lập 4 chỉ tiêu: Doanh thu, Deal chốt, Lịch hẹn, Giữ chân",
                tier = AccountTier.VIP,
                icon = Icons.Default.MilitaryTech,
                primaryColor = Color(0xFF059669),
                lightBgColor = Color(0xFFD1FAE5)
            ),
            KpiFeatureItem(
                detail = KpiFeatureDetail.BUSINESS_KPI_EVALUATION,
                title = "Đánh giá KPIs Nhân sự",
                description = "Bảng xếp hạng thi đua A/B/C/D & chấm điểm toàn bộ nhân viên",
                tier = AccountTier.BUSINESS,
                icon = Icons.Default.Assessment,
                primaryColor = Color(0xFF7C3AED),
                lightBgColor = Color(0xFFEDE9FE)
            ),
            KpiFeatureItem(
                detail = KpiFeatureDetail.BUSINESS_DEPARTMENT_MATRIX,
                title = "Ma trận Phòng ban",
                description = "Tỷ trọng đóng góp doanh số & hiệu quả từng bộ phận kinh doanh",
                tier = AccountTier.BUSINESS,
                icon = Icons.Default.BarChart,
                primaryColor = Color(0xFF4F46E5),
                lightBgColor = Color(0xFFEEF2FF)
            ),
            KpiFeatureItem(
                detail = KpiFeatureDetail.BUSINESS_KPI_COMMISSION,
                title = "Bậc thang Thưởng & Hoa hồng",
                description = "Tự động hóa tính thưởng KPI lũy tiến & đồng bộ sang bảng lương",
                tier = AccountTier.BUSINESS,
                icon = Icons.Default.Calculate,
                primaryColor = Color(0xFFDB2777),
                lightBgColor = Color(0xFFFCE7F3)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Hiệu suất & KPIs", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = when (tier) {
                                    AccountTier.FREE -> Color(0xFFF1F5F9)
                                    AccountTier.VIP -> Color(0xFFFEF3C7)
                                    AccountTier.BUSINESS -> Color(0xFFEDE9FE)
                                },
                                modifier = Modifier.clickable {
                                    viewModel.toggleVipStatus()
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = tier.displayName.uppercase(),
                                        color = when (tier) {
                                            AccountTier.FREE -> Color(0xFF64748B)
                                            AccountTier.VIP -> Color(0xFFD97706)
                                            AccountTier.BUSINESS -> Color(0xFF7C3AED)
                                        },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "↻",
                                        fontSize = 11.sp,
                                        color = when (tier) {
                                            AccountTier.FREE -> Color(0xFF64748B)
                                            AccountTier.VIP -> Color(0xFFD97706)
                                            AccountTier.BUSINESS -> Color(0xFF7C3AED)
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Text("Tiến độ chỉ tiêu cơ bản & Báo cáo chuyên sâu", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.toggleVipStatus() }) {
                        Text("Đổi gói test", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ProfessionalPrimary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // 1. BỘ LỌC THÁNG (TỪ 12/2025 ĐẾN NAY)
            item {
                Text(
                    text = "CHỌN KỲ BÁO CÁO (DỮ LIỆU TỪ 12/2025 ĐẾN NAY)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(monthOptions) { opt ->
                        val isSelected = opt.id == selectedFilterId
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilterId = opt.id },
                            label = {
                                Text(
                                    text = opt.label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) ProfessionalPrimary else Color(0xFF475569)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFEFF6FF),
                                containerColor = Color.White
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) ProfessionalPrimary else Color(0xFFE2E8F0)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            // 2. TIÊU ĐỀ CÁC KHỐI HIỆU SUẤT & KPIS
            item {
                Text(
                    text = "CÁC CHỈ SỐ HIỆU SUẤT CHÍNH (KPIS)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    letterSpacing = 0.5.sp
                )
            }

            // HÀNG 1: KHỐI KHÁCH HÀNG (Hiển thị số lượng) & KHỐI SỐ ĐƠN HÀNG
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // KHỐI 1: KHÁCH HÀNG (hiển thị số lượng)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(138.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color(0xFFDBEAFE))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEFF6FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.People,
                                        contentDescription = "Khách hàng",
                                        tint = Color(0xFF2563EB),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFEFF6FF)
                                ) {
                                    Text(
                                        text = "Tổng số",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2563EB),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "$customerCount",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Khách hàng",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }

                    // KHỐI 5: SỐ ĐƠN HÀNG
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(138.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color(0xFFCCFBF1))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF0FDFA)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingBag,
                                        contentDescription = "Số đơn hàng",
                                        tint = Color(0xFF0D9488),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFF0FDFA)
                                ) {
                                    Text(
                                        text = "Đã chốt",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0D9488),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "$orderCount",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Số đơn hàng (WON)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }

            // HÀNG 2: KHỐI DOANH THU & KHỐI LỢI NHUẬN
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // KHỐI 2: DOANH THU
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(142.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFECFDF5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Payments,
                                        contentDescription = "Doanh thu",
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFECFDF5)
                                ) {
                                    Text(
                                        text = if (selectedOption.id == "ALL") "Tất cả" else "T${selectedOption.month}",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF059669),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = formatCurrencyVND(totalRevenueMonth),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857),
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Doanh thu",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }

                    // KHỐI 3: LỢI NHUẬN
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(142.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFEF3C7)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccountBalanceWallet,
                                        contentDescription = "Lợi nhuận",
                                        tint = Color(0xFFD97706),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFFEF3C7)
                                ) {
                                    Text(
                                        text = "30% Net",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD97706),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = formatCurrencyVND(profitMonth),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309),
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Lợi nhuận ước tính",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }

            // HÀNG 3: KHỐI SỐ BÁO GIÁ (Khối 4)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0E7FF))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEEF2FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RequestQuote,
                                    contentDescription = "Số báo giá",
                                    tint = Color(0xFF4F46E5),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Số báo giá",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Báo giá nháp, đã gửi & đang theo dõi",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFEEF2FF),
                            border = BorderStroke(1.dp, Color(0xFFC7D2FE))
                        ) {
                            Text(
                                text = "$quoteCount báo giá",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4338CA),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // 3. TIÊU ĐỀ KHU VỰC CÁC KHỐI VUÔNG TÍNH NĂNG MỚI
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TÍNH NĂNG MỚI & BÁO CÁO NÂNG CAO",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Bấm để mở chi tiết",
                            fontSize = 11.5.sp,
                            color = ProfessionalPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Các mô-đun phân tích hiệu suất được dán nhãn theo từng hạng tài khoản",
                        fontSize = 11.5.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            // 4. DANH SÁCH CÁC KHỐI VUÔNG TÍNH NĂNG (HIỂN THỊ DẠNG GRID/CARD CÓ DÁN NHÃN)
            items(featureItems.chunked(2)) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { feature ->
                        val isLocked = when (feature.tier) {
                            AccountTier.FREE -> false
                            AccountTier.VIP -> tier == AccountTier.FREE
                            AccountTier.BUSINESS -> tier != AccountTier.BUSINESS
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(165.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    activeDetailScreen = feature.detail
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(
                                1.dp,
                                if (feature.tier == AccountTier.VIP) Color(0xFFFDE68A) else Color(0xFFDDD6FE)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Top: Icon + Tier Badge
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(feature.lightBgColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = feature.icon,
                                            contentDescription = null,
                                            tint = feature.primaryColor,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    // Nhãn dán loại tài khoản
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (feature.tier == AccountTier.VIP) Color(0xFFFEF3C7) else Color(0xFFEDE9FE)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = feature.tier.name,
                                                color = if (feature.tier == AccountTier.VIP) Color(0xFFD97706) else Color(0xFF7C3AED),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.5.sp
                                            )
                                        }
                                    }
                                }

                                // Middle: Title & Brief Description
                                Column {
                                    Text(
                                        text = feature.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp,
                                        color = Color(0xFF0F172A),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = feature.description,
                                        fontSize = 10.5.sp,
                                        color = Color(0xFF64748B),
                                        lineHeight = 13.5.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                // Bottom: CTA link
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Xem chi tiết",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = feature.primaryColor
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = feature.primaryColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Nếu số lượng lẻ thì thêm Spacer để cân đối
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // 5. BANNER NÂNG CẤP (NẾU ĐANG LÀ GÓI FREE HOẶC VIP CẦN LÊN BUSINESS)
            if (tier != AccountTier.BUSINESS) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToUpgrade() },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (tier == AccountTier.FREE) "Nâng cấp VIP / BUSINESS" else "Nâng cấp gói BUSINESS Doanh Nghiệp",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = Color(0xFF92400E)
                                )
                                Text(
                                    text = "Mở khóa toàn bộ 6 công cụ phân tích & quản trị KPIs nâng cao.",
                                    fontSize = 11.5.sp,
                                    color = Color(0xFFB45309)
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// CÁC MÀN HÌNH CHI TIẾT RIÊNG BIỆT KHI BẤM VÀO TỪNG KHỐI VUÔNG
// (Mỗi màn hình có nút QUAY LẠI để trở về trang Hiệu suất & KPIs)
// =========================================================================

// -------------------------------------------------------------------------
// CHI TIẾT 1: DỰ BÁO DOANH THU AI (VIP)
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VipAiForecastDetailScreen(
    viewModel: CrmViewModel,
    userTier: AccountTier,
    onBack: () -> Unit,
    onNavigateToUpgrade: () -> Unit
) {
    val deals by viewModel.dealsWithCustomer.collectAsStateWithLifecycle()
    val wonDeals = deals.filter { it.deal.stage.equals("WON", true) }
    val inProgressDeals = deals.filter { !it.deal.stage.equals("WON", true) && !it.deal.stage.equals("LOST", true) }
    val wonRevenue = wonDeals.sumOf { it.deal.value }
    val pipelineValue = inProgressDeals.sumOf { it.deal.value }
    val target = 100_000_000.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Dự báo Doanh thu AI", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFFEF3C7)) {
                                Text("VIP", color = Color(0xFFD97706), fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("Mô hình AI Target & Predictive Analytics", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                VipAiForecastCard(
                    wonRevenue = wonRevenue,
                    pipelineValue = pipelineValue,
                    target = target,
                    inProgressDealsCount = inProgressDeals.size
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("PHÂN TÍCH XÁC SUẤT ĐẠT KPI THEO CÁC KỊCH BẢN:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(10.dp))
                        ScenarioRow(title = "Kịch bản Lạc quan (Win-rate 85%)", amount = wonRevenue + (pipelineValue * 0.85), tag = "142% Target", color = Color(0xFF059669))
                        Spacer(modifier = Modifier.height(6.dp))
                        ScenarioRow(title = "Kịch bản Cơ sở (Win-rate 65%)", amount = wonRevenue + (pipelineValue * 0.65), tag = "108% Target", color = ProfessionalPrimary)
                        Spacer(modifier = Modifier.height(6.dp))
                        ScenarioRow(title = "Kịch bản Thận trọng (Win-rate 40%)", amount = wonRevenue + (pipelineValue * 0.40), tag = "82% Target", color = Color(0xFFD97706))
                    }
                }
            }

            item {
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Quay lại Hiệu suất & KPIs", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// CHI TIẾT 2: TỐC ĐỘ CHỐT DEAL & WIN-RATE (VIP)
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VipDealVelocityDetailScreen(
    viewModel: CrmViewModel,
    userTier: AccountTier,
    onBack: () -> Unit,
    onNavigateToUpgrade: () -> Unit
) {
    val deals by viewModel.dealsWithCustomer.collectAsStateWithLifecycle()
    val wonDeals = deals.filter { it.deal.stage.equals("WON", true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Tốc độ Deal & Win-rate", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFFEF3C7)) {
                                Text("VIP", color = Color(0xFFD97706), fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("Phân tích chu kỳ chốt hợp đồng & Tỷ lệ thắng", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                VipDealVelocityCard(deals = deals, wonDeals = wonDeals)
            }

            item {
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Quay lại Hiệu suất & KPIs", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// CHI TIẾT 3: MỤC TIÊU CÁ NHÂN ĐA CHIỀU (VIP)
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VipMultiTargetDetailScreen(
    viewModel: CrmViewModel,
    userTier: AccountTier,
    onBack: () -> Unit,
    onNavigateToUpgrade: () -> Unit
) {
    val deals by viewModel.dealsWithCustomer.collectAsStateWithLifecycle()
    val wonDeals = deals.filter { it.deal.stage.equals("WON", true) }
    val wonRevenue = wonDeals.sumOf { it.deal.value }

    var userCustomTarget by remember { mutableLongStateOf(100_000_000L) }
    var showEditTargetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Mục tiêu Cá nhân Đa chiều", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFFEF3C7)) {
                                Text("VIP", color = Color(0xFFD97706), fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("Thiết lập 4 chỉ tiêu trụ cột toàn diện", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                VipMultiTargetGoalCard(
                    wonRevenue = wonRevenue,
                    targetRevenue = userCustomTarget.toDouble(),
                    wonDealsCount = wonDeals.size,
                    targetDealsCount = 10,
                    onEditTarget = { showEditTargetDialog = true }
                )
            }

            item {
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Quay lại Hiệu suất & KPIs", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showEditTargetDialog) {
        var tempInput by remember { mutableStateOf(userCustomTarget.toString()) }
        AlertDialog(
            onDismissRequest = { showEditTargetDialog = false },
            title = { Text("Thiết lập mục tiêu doanh thu", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                OutlinedTextField(
                    value = tempInput,
                    onValueChange = { if (it.all { c -> c.isDigit() }) tempInput = it },
                    label = { Text("Mục tiêu Doanh thu (VNĐ)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        userCustomTarget = tempInput.toLongOrNull() ?: 100_000_000L
                        showEditTargetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                ) {
                    Text("Lưu mục tiêu")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditTargetDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

// -------------------------------------------------------------------------
// CHI TIẾT 4: ĐÁNH GIÁ KPIS NHÂN SỰ (BUSINESS)
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessKpiEvaluationDetailScreen(
    viewModel: CrmViewModel,
    userTier: AccountTier,
    onBack: () -> Unit,
    onNavigateToUpgrade: () -> Unit,
    onNavigateToPayroll: () -> Unit
) {
    val employees by viewModel.employees.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Đánh giá KPIs Nhân sự", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFEDE9FE)) {
                                Text("BUSINESS", color = Color(0xFF7C3AED), fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("Bảng chấm điểm thi đua A/B/C/D toàn đội ngũ", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                BusinessKpiEvaluationEngine(
                    employees = employees,
                    onNavigateToPayroll = onNavigateToPayroll
                )
            }

            item {
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Quay lại Hiệu suất & KPIs", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// CHI TIẾT 5: MA TRẬN DOANH SỐ PHÒNG BAN (BUSINESS)
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessDepartmentMatrixDetailScreen(
    viewModel: CrmViewModel,
    userTier: AccountTier,
    onBack: () -> Unit,
    onNavigateToUpgrade: () -> Unit
) {
    val employees by viewModel.employees.collectAsStateWithLifecycle()
    val deals by viewModel.dealsWithCustomer.collectAsStateWithLifecycle()
    val wonRevenue = deals.filter { it.deal.stage.equals("WON", true) }.sumOf { it.deal.value }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Ma trận Phòng ban", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFEDE9FE)) {
                                Text("BUSINESS", color = Color(0xFF7C3AED), fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("Phân tích cơ cấu & đóng góp doanh số phòng ban", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                BusinessDepartmentMatrixCard(
                    employees = employees,
                    totalRevenue = wonRevenue
                )
            }

            item {
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Quay lại Hiệu suất & KPIs", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// CHI TIẾT 6: BẬC THANG THƯỞNG KPI & HOA HỒNG (BUSINESS)
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessKpiCommissionDetailScreen(
    viewModel: CrmViewModel,
    userTier: AccountTier,
    onBack: () -> Unit,
    onNavigateToUpgrade: () -> Unit,
    onNavigateToPayroll: () -> Unit
) {
    val employees by viewModel.employees.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Bậc thang Thưởng KPI & Hoa hồng", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFEDE9FE)) {
                                Text("BUSINESS", color = Color(0xFF7C3AED), fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("Tự động hóa tính thưởng & đồng bộ Bảng lương", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                BusinessKpiCommissionCard(
                    employees = employees,
                    onNavigateToPayroll = onNavigateToPayroll
                )
            }

            item {
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDB2777)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Quay lại Hiệu suất & KPIs", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// =========================================================================
// SUB-COMPONENTS CHO CÁC TÍNH NĂNG VIP
// =========================================================================

@Composable
fun VipAiForecastCard(
    wonRevenue: Double,
    pipelineValue: Double,
    target: Double,
    inProgressDealsCount: Int
) {
    val estimatedWinRate = 0.65
    val forecastedRevenue = wonRevenue + (pipelineValue * estimatedWinRate)
    val gap = (target - forecastedRevenue).coerceAtLeast(0.0)
    val forecastPercent = (forecastedRevenue / target).coerceIn(0.0, 2.0).toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = ProfessionalPrimary, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Dự báo Doanh thu Thông minh AI", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0F172A))
                    Text("AI Target & Revenue Predictive Engine", fontSize = 12.sp, color = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF0FDF4),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Dự báo hoàn thành kỳ này", fontSize = 13.sp, color = Color(0xFF166534))
                        Text(
                            text = "${(forecastPercent * 100).toInt()}% Target",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF15803D)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { forecastPercent.coerceAtMost(1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = Color(0xFF16A34A),
                        trackColor = Color(0xFFDCFCE7)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Doanh thu dự kiến về: ${formatCurrencyVND(forecastedRevenue)} / Mục tiêu: ${formatCurrencyVND(target)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF14532D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI Action Suggestions
            Text("GỢI Ý HÀNH ĐỘNG CHIẾN LƯỢC TỪ AI:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                AiSuggestionRow(
                    icon = Icons.Default.Speed,
                    text = "Có $inProgressDealsCount cơ hội đàm phán (${formatCurrencyVND(pipelineValue)}) đang mở. Tập trung chốt các deal trong 7 ngày tới để vượt KPI."
                )
                if (gap > 0) {
                    AiSuggestionRow(
                        icon = Icons.Default.FlashOn,
                        text = "Còn thiếu ${formatCurrencyVND(gap)} để đạt 100% mục tiêu. Hãy liên hệ lại khách hàng VIP cũ để Upsell thêm dịch vụ."
                    )
                } else {
                    AiSuggestionRow(
                        icon = Icons.Default.EmojiEvents,
                        text = "Tuyệt vời! Bạn đang trên đà vượt chỉ tiêu tháng 24%. Sẵn sàng nhận thưởng Top Performer."
                    )
                }
            }
        }
    }
}

@Composable
fun VipDealVelocityCard(
    deals: List<DealWithCustomer>,
    wonDeals: List<DealWithCustomer>
) {
    val avgCloseDays = 14
    val winRate = if (deals.isNotEmpty()) ((wonDeals.size.toDouble() / deals.size) * 100).toInt() else 75

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Phân tích Tốc độ Chốt Deal & Win-Rate", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0F172A))
                    Text("Deal Velocity & Conversion Benchmark", fontSize = 12.sp, color = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Chu kỳ chốt trung bình", fontSize = 11.5.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$avgCloseDays ngày/deal", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = ProfessionalPrimary)
                        Text("Nhanh hơn thị trường 3 ngày", fontSize = 10.5.sp, color = Color(0xFF16A34A))
                    }
                }
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Tỷ lệ chốt thành công", fontSize = 11.5.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$winRate%", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                        Text("Chuẩn VIP: >60%", fontSize = 10.5.sp, color = Color(0xFF64748B))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("TỶ LỆ THẮNG THEO QUY MÔ DEAL:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(6.dp))
            VelocityRow(label = "Deal nhỏ (< 20 Triệu)", percent = 0.85f, rateText = "85% Win", color = Color(0xFF10B981))
            Spacer(modifier = Modifier.height(4.dp))
            VelocityRow(label = "Deal vừa (20M - 100M)", percent = 0.65f, rateText = "65% Win", color = Color(0xFF3B82F6))
            Spacer(modifier = Modifier.height(4.dp))
            VelocityRow(label = "Deal lớn (> 100 Triệu)", percent = 0.45f, rateText = "45% Win", color = Color(0xFFF59E0B))
        }
    }
}

@Composable
fun VipMultiTargetGoalCard(
    wonRevenue: Double,
    targetRevenue: Double,
    wonDealsCount: Int,
    targetDealsCount: Int,
    onEditTarget: () -> Unit
) {
    val revPercent = (wonRevenue / targetRevenue).coerceIn(0.0, 1.0).toFloat()
    val dealsPercent = (wonDealsCount.toFloat() / targetDealsCount).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.MilitaryTech, contentDescription = null, tint = Color(0xFF7C3AED), modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Mục tiêu Cá nhân Đa chiều", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0F172A))
                        Text("Multi-dimensional Goal Benchmark", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                }

                OutlinedButton(
                    onClick = onEditTarget,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Đổi Target", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Pillars Goal
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PillarProgressRow(
                    title = "Doanh thu chốt thực tế",
                    currentText = formatCurrencyVND(wonRevenue),
                    targetText = formatCurrencyVND(targetRevenue),
                    progress = revPercent,
                    barColor = Color(0xFF059669)
                )
                PillarProgressRow(
                    title = "Số lượng hợp đồng chốt",
                    currentText = "$wonDealsCount deal",
                    targetText = "$targetDealsCount deal",
                    progress = dealsPercent,
                    barColor = ProfessionalPrimary
                )
                PillarProgressRow(
                    title = "Tương tác & Cuộc hẹn chăm sóc",
                    currentText = "18 cuộc gọi/gặp",
                    targetText = "20 cuộc hẹn",
                    progress = 0.90f,
                    barColor = Color(0xFFD97706)
                )
                PillarProgressRow(
                    title = "Tỷ lệ giữ chân khách hàng (Retention)",
                    currentText = "92%",
                    targetText = "90%",
                    progress = 1.0f,
                    barColor = Color(0xFF7C3AED)
                )
            }
        }
    }
}

// =========================================================================
// SUB-COMPONENTS CHO CÁC TÍNH NĂNG BUSINESS
// =========================================================================

@Composable
fun BusinessKpiEvaluationEngine(
    employees: List<EmployeeItem>,
    onNavigateToPayroll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Assessment, contentDescription = null, tint = Color(0xFF7C3AED), modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Đánh giá & Chấm điểm KPIs Đội ngũ", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0F172A))
                        Text("Team KPI Evaluation & Scoring Engine", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "BẢNG ĐÁNH GIÁ & XẾP HẠNG THI ĐUA NHÂN VIÊN (${employees.size} NHÂN SỰ):",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                employees.forEachIndexed { index, emp ->
                    val kpiScore = when (index % 4) {
                        0 -> 118
                        1 -> 105
                        2 -> 92
                        else -> 80
                    }
                    val rankBadge = when {
                        kpiScore >= 110 -> Pair("Hạng A (Xuất sắc)", Color(0xFF059669))
                        kpiScore >= 100 -> Pair("Hạng B (Tốt)", Color(0xFF2563EB))
                        kpiScore >= 85 -> Pair("Hạng C (Đạt)", Color(0xFFD97706))
                        else -> Pair("Hạng D (Cần cố gắng)", Color(0xFFDC2626))
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEDE9FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = emp.name.firstOrNull()?.toString()?.uppercase() ?: "N",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF7C3AED),
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(emp.name, fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = Color(0xFF0F172A))
                                Text("${emp.role} • ${emp.department}", fontSize = 11.5.sp, color = Color(0xFF64748B))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "$kpiScore% KPI",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = rankBadge.second
                                )
                                Text(
                                    text = rankBadge.first,
                                    fontSize = 10.5.sp,
                                    color = rankBadge.second,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onNavigateToPayroll,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Đồng bộ & Chuyển sang Bảng lương tự động", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun BusinessDepartmentMatrixCard(
    employees: List<EmployeeItem>,
    totalRevenue: Double
) {
    val departments = listOf(
        Triple("Phòng Kinh Doanh 1", 0.55, Color(0xFF2563EB)),
        Triple("Phòng Kinh Doanh 2", 0.30, Color(0xFF7C3AED)),
        Triple("Phòng CSKH & Dự Án", 0.15, Color(0xFF059669))
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEDE9FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.BarChart, contentDescription = null, tint = Color(0xFF7C3AED), modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Ma trận Doanh số theo Phòng ban", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0F172A))
                    Text("Department Sales & Performance Matrix", fontSize = 12.sp, color = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            departments.forEach { (deptName, share, color) ->
                val deptRevenue = if (totalRevenue > 0) totalRevenue * share else 80_000_000.0 * share
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(deptName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF0F172A))
                        Text("${(share * 100).toInt()}% (${formatCurrencyVND(deptRevenue)})", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = color)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { share.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = color,
                        trackColor = Color(0xFFF1F5F9)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun BusinessKpiCommissionCard(
    employees: List<EmployeeItem>,
    onNavigateToPayroll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFECFDF5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Calculate, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Tự động hóa Tính Thưởng & Hoa hồng KPI", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0F172A))
                    Text("Automated KPI Commission & Bonus Matrix", fontSize = 12.sp, color = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("BẬC THANG HOA HỒNG TỰ ĐỘNG:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF334155))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("• Đạt < 80% KPI: 0% Thưởng KPI", fontSize = 12.sp, color = Color(0xFF64748B))
                    Text("• Đạt 80% - 99% KPI: 100% Thưởng cơ bản", fontSize = 12.sp, color = Color(0xFF0F172A))
                    Text("• Đạt 100% - 120% KPI: 120% Thưởng + Thưởng nóng 2.000.000đ", fontSize = 12.sp, color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                    Text("• Đạt > 120% KPI: 150% Thưởng + Vinh danh Top Performer", fontSize = 12.sp, color = Color(0xFF7C3AED), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            val totalKpiBonus = employees.sumOf { it.kpiBonus }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tổng ngân sách thưởng KPI tháng:", fontSize = 13.sp, color = Color(0xFF64748B))
                Text(formatCurrencyVND(totalKpiBonus), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF059669))
            }
        }
    }
}

// -------------------------------------------------------------------------
// REUSABLE HELPER ROWS & FORMATTERS
// -------------------------------------------------------------------------
@Composable
fun ScenarioRow(title: String, amount: Double, tag: String, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF334155))
                Text(formatCurrencyVND(amount), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
            }
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = color.copy(alpha = 0.12f)
            ) {
                Text(
                    text = tag,
                    color = color,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun AiSuggestionRow(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .padding(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = ProfessionalPrimary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 12.sp, color = Color(0xFF334155), lineHeight = 16.sp)
    }
}

@Composable
fun VelocityRow(label: String, percent: Float, rateText: String, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 12.sp, color = Color(0xFF475569))
            Text(rateText, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(3.dp))
        LinearProgressIndicator(
            progress = { percent },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = color,
            trackColor = Color(0xFFF1F5F9)
        )
    }
}

@Composable
fun PillarProgressRow(
    title: String,
    currentText: String,
    targetText: String,
    progress: Float,
    barColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 12.5.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0F172A))
            Text("$currentText / $targetText", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = barColor)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .clip(CircleShape),
            color = barColor,
            trackColor = Color(0xFFF1F5F9)
        )
    }
}

private fun formatCurrencyVND(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return formatter.format(amount)
}
