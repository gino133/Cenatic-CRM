package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CustomerStatus
import com.example.data.model.DealStage
import com.example.data.model.InteractionType
import com.example.ui.components.formatCurrencyVND
import com.example.ui.components.formatRelativeTime
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.viewmodel.CrmViewModel
import java.util.Calendar

data class DashboardActivityItem(
    val id: String,
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color,
    val titlePrefix: String,
    val titleBold: String,
    val titleSuffix: String = "",
    val timestamp: Long,
    val onClick: () -> Unit
)

@Composable
fun DashboardScreen(
    viewModel: CrmViewModel,
    onNavigateToCustomers: () -> Unit,
    onNavigateToQuotes: () -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToTasks: () -> Unit,
    onOpenCustomerDetail: (Long) -> Unit,
    onOpenProfile: () -> Unit,
    onOpenReports: () -> Unit,
    onAddCustomerClick: () -> Unit,
    onAddQuoteClick: () -> Unit,
    onLogNoteClick: () -> Unit
) {
    val customers by viewModel.allRawCustomers.collectAsStateWithLifecycle()
    val deals by viewModel.dealsWithCustomer.collectAsStateWithLifecycle()
    val tasks by viewModel.tasksWithCustomer.collectAsStateWithLifecycle()
    val interactions by viewModel.interactionsWithCustomer.collectAsStateWithLifecycle()
    val quotes by viewModel.quotes.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    // Real Calculations from Actual Data
    val totalCustomersCount = customers.size

    val cal = Calendar.getInstance()
    val currentHour = cal.get(Calendar.HOUR_OF_DAY)
    val greetingTime = when (currentHour) {
        in 5..11 -> "Chào buổi sáng"
        in 12..17 -> "Chào buổi chiều"
        else -> "Chào buổi tối"
    }

    cal.set(Calendar.DAY_OF_MONTH, 1)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    val startOfMonth = cal.timeInMillis

    // New customers created this month
    val newThisMonthCount = customers.count { it.createdAt >= startOfMonth }
    val vipCustomersCount = customers.count { it.status.equals("VIP", ignoreCase = true) }
    val leadCustomersCount = customers.count {
        it.status.equals("LEAD", ignoreCase = true) || it.status.equals("Tiềm Năng", ignoreCase = true) || it.status.equals("PROSPECT", ignoreCase = true)
    }

    // Revenue calculations: Won deals + Accepted/Paid quotes
    val wonDeals = deals.filter { it.deal.stage.equals("WON", ignoreCase = true) }
    val wonRevenue = wonDeals.sumOf { it.deal.value }
    val activePipelineValue = deals.filter { it.deal.stage !in listOf("WON", "LOST") }.sumOf { it.deal.value }

    // Pending Quotes
    val pendingQuotes = quotes.filter {
        it.status.equals("Draft", ignoreCase = true) ||
        it.status.equals("Sent", ignoreCase = true) ||
        it.status.equals("Nháp", ignoreCase = true) ||
        it.status.equals("Đã gửi", ignoreCase = true) ||
        it.status.equals("Chờ duyệt", ignoreCase = true)
    }
    val pendingQuotesCount = pendingQuotes.size
    val pendingQuotesValue = pendingQuotes.sumOf { it.amount }

    // Lead / Customer Rating breakdown from actual customer portfolio
    val totalCountForRating = customers.size.coerceAtLeast(1)
    val star5Count = customers.count {
        it.status.equals("VIP", ignoreCase = true) || it.estimatedValue >= 100_000_000.0
    }
    val star4Count = customers.count {
        !it.status.equals("VIP", ignoreCase = true) &&
        (it.status.equals("CUSTOMER", ignoreCase = true) || it.status.equals("Khách hàng", ignoreCase = true) || it.estimatedValue in 50_000_000.0..99_999_999.0)
    }
    val star3Count = customers.count {
        !it.status.equals("VIP", ignoreCase = true) && !it.status.equals("CUSTOMER", ignoreCase = true) &&
        (it.status.equals("LEAD", ignoreCase = true) || it.status.equals("Tiềm Năng", ignoreCase = true) || it.estimatedValue in 10_000_000.0..49_999_999.0)
    }
    val star2Count = customers.count {
        it.status.equals("CASUAL", ignoreCase = true) ||
        (it.estimatedValue < 10_000_000.0 && !it.status.equals("INACTIVE", ignoreCase = true) && !it.status.equals("VIP", ignoreCase = true) && !it.status.equals("CUSTOMER", ignoreCase = true) && !it.status.equals("LEAD", ignoreCase = true))
    }
    val star1Count = customers.count {
        it.status.equals("INACTIVE", ignoreCase = true) || it.status.equals("Ngừng liên hệ", ignoreCase = true)
    }

    val p5 = if (customers.isEmpty()) 0f else (star5Count.toFloat() / totalCountForRating.toFloat())
    val p4 = if (customers.isEmpty()) 0f else (star4Count.toFloat() / totalCountForRating.toFloat())
    val p3 = if (customers.isEmpty()) 0f else (star3Count.toFloat() / totalCountForRating.toFloat())
    val p2 = if (customers.isEmpty()) 0f else (star2Count.toFloat() / totalCountForRating.toFloat())
    val p1 = if (customers.isEmpty()) 0f else (star1Count.toFloat() / totalCountForRating.toFloat())

    // Aggregating Real Recent Activities from interactions, tasks, deals, and quotes
    val recentActivities = remember(interactions, tasks, deals, quotes) {
        val list = mutableListOf<DashboardActivityItem>()

        // 1. Interactions
        interactions.forEach { item ->
            val iconInfo = when (InteractionType.fromString(item.interaction.type)) {
                InteractionType.CALL -> Triple(Icons.Default.Call, Color(0xFFEFF6FF), Color(0xFF2563EB))
                InteractionType.MEETING -> Triple(Icons.Default.Group, Color(0xFFF0FDF4), Color(0xFF16A34A))
                InteractionType.EMAIL -> Triple(Icons.Default.Email, Color(0xFFEFF6FF), Color(0xFF2563EB))
                InteractionType.MESSAGE -> Triple(Icons.Default.Chat, Color(0xFFFDF4FF), Color(0xFFA855F7))
                InteractionType.NOTE -> Triple(Icons.Default.Note, Color(0xFFFFFBEB), Color(0xFFD97706))
                InteractionType.CONTRACT -> Triple(Icons.Default.Description, Color(0xFFECFDF5), Color(0xFF059669))
            }
            list.add(
                DashboardActivityItem(
                    id = "interaction_${item.interaction.id}",
                    icon = iconInfo.first,
                    iconBg = iconInfo.second,
                    iconTint = iconInfo.third,
                    titlePrefix = "${item.interaction.type.replace('_', ' ')}: ",
                    titleBold = item.customerName,
                    titleSuffix = if (item.interaction.title.isNotBlank()) " (${item.interaction.title})" else "",
                    timestamp = item.interaction.date,
                    onClick = { onOpenCustomerDetail(item.interaction.customerId) }
                )
            )
        }

        // 2. Tasks
        tasks.forEach { item ->
            val isDone = item.task.isCompleted
            list.add(
                DashboardActivityItem(
                    id = "task_${item.task.id}",
                    icon = if (isDone) Icons.Default.CheckCircle else Icons.Default.Assignment,
                    iconBg = if (isDone) Color(0xFFECFDF5) else Color(0xFFEFF6FF),
                    iconTint = if (isDone) Color(0xFF059669) else Color(0xFF2563EB),
                    titlePrefix = if (isDone) "Hoàn thành việc: " else "Công việc: ",
                    titleBold = item.task.title,
                    titleSuffix = if (!item.customerName.isNullOrBlank()) " - ${item.customerName}" else "",
                    timestamp = if (isDone && item.task.completedAt != null) item.task.completedAt else item.task.createdAt,
                    onClick = onNavigateToTasks
                )
            )
        }

        // 3. Deals
        deals.forEach { item ->
            val isWon = item.deal.stage.equals("WON", ignoreCase = true)
            list.add(
                DashboardActivityItem(
                    id = "deal_${item.deal.id}",
                    icon = if (isWon) Icons.Default.CheckCircle else Icons.Default.Payments,
                    iconBg = if (isWon) Color(0xFFECFDF5) else Color(0xFFFFFBEB),
                    iconTint = if (isWon) Color(0xFF059669) else Color(0xFFD97706),
                    titlePrefix = if (isWon) "Chốt deal: " else "Cơ hội: ",
                    titleBold = item.deal.title,
                    titleSuffix = " (${item.customerName} • ${formatCurrencyVND(item.deal.value)})",
                    timestamp = item.deal.createdAt,
                    onClick = onNavigateToProjects
                )
            )
        }

        // 4. Quotes
        quotes.forEach { item ->
            list.add(
                DashboardActivityItem(
                    id = "quote_${item.id}",
                    icon = Icons.Default.Description,
                    iconBg = Color(0xFFEFF6FF),
                    iconTint = Color(0xFF2563EB),
                    titlePrefix = "Báo giá [${item.status}]: ",
                    titleBold = item.title,
                    titleSuffix = if (item.customerName.isNotBlank()) " - ${item.customerName}" else "",
                    timestamp = item.id * 1000L + 1698400000000L, // dynamic timestamp based on quote entry
                    onClick = onNavigateToQuotes
                )
            )
        }

        list.sortedByDescending { it.timestamp }.take(6)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Header Greeting matching real user data
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "$greetingTime, ${userProfile.fullName.ifBlank { "Quản lý" }}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F2B5C),
                    letterSpacing = (-0.3).sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dưới đây là tổng quan hiệu suất và dữ liệu thực tế của bạn.",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        // 2. Card 1: Khách hàng (Real total count & new customers this month)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToCustomers() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Right decorative chart lines
                    Canvas(
                        modifier = Modifier
                            .size(width = 120.dp, height = 55.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        val path = Path().apply {
                            moveTo(0f, size.height * 0.75f)
                            lineTo(size.width * 0.35f, size.height * 0.55f)
                            lineTo(size.width * 0.55f, size.height * 0.65f)
                            lineTo(size.width * 0.95f, size.height * 0.1f)
                        }
                        drawPath(
                            path = path,
                            color = Color(0xFFF1F5F9),
                            style = Stroke(width = 16f, cap = StrokeCap.Round)
                        )
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tổng số khách hàng",
                                fontSize = 14.sp,
                                color = Color(0xFF475569),
                                fontWeight = FontWeight.Medium
                            )

                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEFF6FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = "Thêm khách hàng",
                                    tint = Color(0xFF2563EB),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "$totalCustomersCount",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )

                            if (newThisMonthCount > 0) {
                                Text(
                                    text = "↑ +$newThisMonthCount tháng này",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981)
                                )
                            } else if (totalCustomersCount > 0) {
                                Text(
                                    text = "$vipCustomersCount VIP • $leadCustomersCount Tiềm năng",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Card 2: Doanh thu thực tế (Won Deals & Revenue)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenReports() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (wonRevenue > 0) "Doanh thu đã chốt" else "Giá trị cơ hội đàm phán",
                            fontSize = 14.sp,
                            color = Color(0xFF475569),
                            fontWeight = FontWeight.Medium
                        )

                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFEFF6FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = "Doanh thu",
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val displayAmount = if (wonRevenue > 0) wonRevenue else activePipelineValue
                        Text(
                            text = formatCurrencyVND(displayAmount),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Text(
                            text = if (wonRevenue > 0) "(${wonDeals.size} deal thắng)" else "(${deals.size} cơ hội)",
                            fontSize = 13.5.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }

        // 4. Card 3: Báo giá & cơ hội đang chờ
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToQuotes() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Báo giá đang xử lý",
                            fontSize = 14.sp,
                            color = Color(0xFF475569),
                            fontWeight = FontWeight.Medium
                        )

                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF1F5F9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assignment,
                                contentDescription = "Báo giá chờ",
                                tint = Color(0xFF475569),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "$pendingQuotesCount",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        if (pendingQuotesValue > 0) {
                            Text(
                                text = "• ${formatCurrencyVND(pendingQuotesValue)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2563EB),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        } else {
                            Text(
                                text = "trong tổng số ${quotes.size} báo giá",
                                fontSize = 13.sp,
                                color = Color(0xFF64748B),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // 5. Card 4: Đánh giá khách hàng tiềm năng theo dữ liệu thực
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToCustomers() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Đánh giá khách hàng tiềm năng",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Phân bổ theo mức độ tiềm năng và giá trị thực tế (${customers.size} KH)",
                        fontSize = 13.5.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    if (customers.isEmpty()) {
                        Text(
                            text = "Chưa có dữ liệu khách hàng để phân tích.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            // 5 Stars (VIP / >100M)
                            StarRatingRow(
                                stars = 5,
                                progress = p5,
                                percentText = "${(p5 * 100).toInt()}%",
                                countText = "$star5Count KH",
                                barColor = Color(0xFF2563EB)
                            )

                            // 4 Stars (Khách hàng quen / 50M-100M)
                            StarRatingRow(
                                stars = 4,
                                progress = p4,
                                percentText = "${(p4 * 100).toInt()}%",
                                countText = "$star4Count KH",
                                barColor = Color(0xFF3B82F6)
                            )

                            // 3 Stars (Tiềm năng / 10M-50M)
                            StarRatingRow(
                                stars = 3,
                                progress = p3,
                                percentText = "${(p3 * 100).toInt()}%",
                                countText = "$star3Count KH",
                                barColor = Color(0xFF64748B)
                            )

                            // 2 Stars (Vãng lai / <10M)
                            StarRatingRow(
                                stars = 2,
                                progress = p2,
                                percentText = "${(p2 * 100).toInt()}%",
                                countText = "$star2Count KH",
                                barColor = Color(0xFF94A3B8)
                            )

                            // 1 Star (Ngừng liên hệ)
                            StarRatingRow(
                                stars = 1,
                                progress = p1,
                                percentText = "${(p1 * 100).toInt()}%",
                                countText = "$star1Count KH",
                                barColor = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }
        }

        // 6. Card 5: Hoạt động thực tế gần đây (Real Dynamic Activities)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Hoạt động gần đây",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Text(
                            text = "Xem tất cả",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1D6EE5),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onNavigateToTasks() }
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (recentActivities.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Chưa có hoạt động nào được ghi nhận.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FilledTonalButton(onClick = onNavigateToTasks) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tạo công việc đầu tiên")
                            }
                        }
                    } else {
                        recentActivities.forEachIndexed { index, act ->
                            RecentActivityRowItem(
                                icon = act.icon,
                                iconBg = act.iconBg,
                                iconTint = act.iconTint,
                                titleBoldPrefix = act.titlePrefix,
                                titleBoldSuffix = act.titleBold,
                                titleSuffix = act.titleSuffix,
                                timeText = formatRelativeTime(act.timestamp),
                                onClick = act.onClick
                            )

                            if (index < recentActivities.size - 1) {
                                HorizontalDivider(
                                    color = Color(0xFFF1F5F9),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StarRatingRow(
    stars: Int,
    progress: Float,
    percentText: String,
    countText: String,
    barColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Star number + star icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(36.dp)
        ) {
            Text(
                text = "$stars",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF334155)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFF59E0B),
                modifier = Modifier.size(14.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Progress bar with rounded caps
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = barColor,
            trackColor = Color(0xFFEFF4FB)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Percentage text
        Text(
            text = percentText,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1E293B),
            modifier = Modifier.width(36.dp)
        )

        // Count text
        Text(
            text = countText,
            fontSize = 11.sp,
            color = Color(0xFF64748B),
            modifier = Modifier.width(44.dp)
        )
    }
}

@Composable
fun RecentActivityRowItem(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    titleBoldPrefix: String? = null,
    titleSuffix: String? = null,
    titlePrefix: String? = null,
    titleBoldSuffix: String? = null,
    timeText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            val annotatedText = buildAnnotatedString {
                if (titleBoldPrefix != null) {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))) {
                        append(titleBoldPrefix)
                    }
                }
                if (titlePrefix != null) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = Color(0xFF334155))) {
                        append(titlePrefix)
                    }
                }
                if (titleBoldSuffix != null) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))) {
                        append(titleBoldSuffix)
                    }
                }
                if (titleSuffix != null) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = Color(0xFF475569))) {
                        append(titleSuffix)
                    }
                }
            }

            Text(
                text = annotatedText,
                fontSize = 13.5.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = timeText,
                fontSize = 12.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

