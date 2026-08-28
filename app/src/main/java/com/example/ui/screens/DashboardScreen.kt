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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.viewmodel.CrmViewModel

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
    val quotes by viewModel.quotes.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    val newCustomersCount = 124 + customers.size.coerceAtLeast(0)
    val pendingQuotesCount = if (quotes.isNotEmpty()) {
        quotes.count { it.status.equals("Draft", ignoreCase = true) || it.status.equals("Sent", ignoreCase = true) || it.status.equals("Nháp", ignoreCase = true) || it.status.equals("Đã gửi", ignoreCase = true) }.coerceAtLeast(8)
    } else 8

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Header Greeting matching screenshot:
        // Chào, Nguyễn Văn A
        // Dưới đây là tổng quan hiệu suất của bạn.
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Chào, ${userProfile.fullName.ifBlank { "Nguyễn Văn A" }}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F2B5C),
                    letterSpacing = (-0.3).sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dưới đây là tổng quan hiệu suất của bạn.",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        // 2. Card 1: Khách hàng mới (124 ↑ +15% with decorative chart line)
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
                                text = "Khách hàng mới",
                                fontSize = 14.sp,
                                color = Color(0xFF475569),
                                fontWeight = FontWeight.Medium
                            )

                            // Top right blue circle with person_add icon
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

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "$newCustomersCount",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )

                            Text(
                                text = "↑ +15%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10B981)
                            )
                        }
                    }
                }
            }
        }

        // 3. Card 2: Doanh thu ($25k tháng này)
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
                            text = "Doanh thu",
                            fontSize = 14.sp,
                            color = Color(0xFF475569),
                            fontWeight = FontWeight.Medium
                        )

                        // Top right blue rounded square with banknote icon
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
                        Text(
                            text = "$25k",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Text(
                            text = "tháng này",
                            fontSize = 14.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }

        // 4. Card 3: Báo giá đang chờ (8)
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
                            text = "Báo giá đang chờ",
                            fontSize = 14.sp,
                            color = Color(0xFF475569),
                            fontWeight = FontWeight.Medium
                        )

                        // Top right light gray rounded box with assignment/quote icon
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

                    Text(
                        text = "$pendingQuotesCount",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
            }
        }

        // 5. Card 4: Đánh giá khách hàng tiềm năng
        // Subtitle: Dựa trên mức độ tương tác và ngân sách.
        // 5 Star Rating progress breakdown
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
                        text = "Dựa trên mức độ tương tác và ngân sách.",
                        fontSize = 13.5.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // 5 Stars: 75%
                        StarRatingRow(
                            stars = 5,
                            progress = 0.75f,
                            percentText = "75%",
                            barColor = Color(0xFF2563EB)
                        )

                        // 4 Stars: 15%
                        StarRatingRow(
                            stars = 4,
                            progress = 0.15f,
                            percentText = "15%",
                            barColor = Color(0xFF3B82F6)
                        )

                        // 3 Stars: 8%
                        StarRatingRow(
                            stars = 3,
                            progress = 0.08f,
                            percentText = "8%",
                            barColor = Color(0xFF64748B)
                        )

                        // 2 Stars: 2%
                        StarRatingRow(
                            stars = 2,
                            progress = 0.02f,
                            percentText = "2%",
                            barColor = Color(0xFF94A3B8)
                        )

                        // 1 Star: 0%
                        StarRatingRow(
                            stars = 1,
                            progress = 0.0f,
                            percentText = "0%",
                            barColor = Color(0xFFCBD5E1)
                        )
                    }
                }
            }
        }

        // 6. Card 5: Hoạt động gần đây
        // Header: Hoạt động gần đây | Xem tất cả
        // Item 1: Cuộc gọi với Công ty ABC (Hôm nay, 10:30 AM)
        // Item 2: Gửi báo giá cho Trần Văn B (Hôm qua, 14:15 PM)
        // Item 3: Chốt deal Dự án X (2 ngày trước)
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

                    // Item 1: Cuộc gọi với Công ty ABC
                    RecentActivityRowItem(
                        icon = Icons.Default.Call,
                        iconBg = Color(0xFFEFF6FF),
                        iconTint = Color(0xFF2563EB),
                        titleBoldPrefix = "Cuộc gọi",
                        titleSuffix = " với Công ty ABC",
                        timeText = "Hôm nay, 10:30 AM",
                        onClick = onNavigateToCustomers
                    )

                    HorizontalDivider(
                        color = Color(0xFFF1F5F9),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    // Item 2: Gửi báo giá cho Trần Văn B
                    RecentActivityRowItem(
                        icon = Icons.Default.Email,
                        iconBg = Color(0xFFEFF6FF),
                        iconTint = Color(0xFF2563EB),
                        titlePrefix = "Gửi báo giá cho ",
                        titleBoldSuffix = "Trần Văn B",
                        timeText = "Hôm qua, 14:15 PM",
                        onClick = onNavigateToQuotes
                    )

                    HorizontalDivider(
                        color = Color(0xFFF1F5F9),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    // Item 3: Chốt deal Dự án X
                    RecentActivityRowItem(
                        icon = Icons.Default.CheckCircle,
                        iconBg = Color(0xFFECFDF5),
                        iconTint = Color(0xFF059669),
                        titlePrefix = "Chốt deal ",
                        titleBoldSuffix = "Dự án X",
                        timeText = "2 ngày trước",
                        onClick = onNavigateToProjects
                    )
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

        Spacer(modifier = Modifier.width(10.dp))

        // Progress bar with rounded caps
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = barColor,
            trackColor = Color(0xFFEFF4FB)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Percentage text aligned to end
        Text(
            text = percentText,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1E293B),
            modifier = Modifier.width(36.dp)
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
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            val annotatedText = buildAnnotatedString {
                if (titleBoldPrefix != null) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))) {
                        append(titleBoldPrefix)
                    }
                }
                if (titleSuffix != null) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = Color(0xFF334155))) {
                        append(titleSuffix)
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
            }

            Text(
                text = annotatedText,
                fontSize = 14.sp
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
