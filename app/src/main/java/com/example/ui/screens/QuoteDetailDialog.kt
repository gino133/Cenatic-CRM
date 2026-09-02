package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.QuoteItem
import com.example.ui.components.formatFullCurrencyVND
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.theme.ProfessionalPrimaryNavy

// Dummy line item for display if items are not individually listed
data class QuoteDisplayItem(
    val name: String,
    val quantity: Double,
    val unit: String,
    val unitPrice: Double
) {
    val totalPrice: Double get() = quantity * unitPrice
}

@Composable
fun QuoteDetailDialog(
    quote: QuoteItem,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onSend: () -> Unit,
    onAccept: () -> Unit,
    onGoToContract: (() -> Unit)? = null,
    onGoToProject: () -> Unit
) {
    val context = LocalContext.current
    var showExportPdfModal by remember { mutableStateOf(false) }
    var showExportExcelModal by remember { mutableStateOf(false) }

    // Generate sample items based on category and total amount
    val displayItems = remember(quote) {
        val total = quote.amount
        if (total <= 0) {
            listOf(
                QuoteDisplayItem("Hạng mục tư vấn & triển khai", 1.0, "Gói", 0.0)
            )
        } else {
            val item1Amount = (total * 0.6).toLong().toDouble()
            val item2Amount = total - item1Amount
            listOf(
                QuoteDisplayItem(quote.title, 1.0, "Gói", item1Amount),
                QuoteDisplayItem("Dịch vụ bảo hành & hỗ trợ kỹ thuật tiêu chuẩn", 1.0, "Gói", item2Amount)
            )
        }
    }

    val statusColor = when (quote.status.lowercase()) {
        "accepted" -> Color(0xFF059669)
        "sent" -> Color(0xFF2563EB)
        else -> Color(0xFFD97706)
    }

    val statusBg = when (quote.status.lowercase()) {
        "accepted" -> Color(0xFFDEF7EC)
        "sent" -> Color(0xFFEFF6FF)
        else -> Color(0xFFFEF3C7)
    }

    val statusText = when (quote.status.lowercase()) {
        "accepted" -> "Đã chốt"
        "sent" -> "Đã gửi"
        else -> "Bản nháp"
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF8FAFC)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Dialog Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ProfessionalPrimary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Description,
                                contentDescription = null,
                                tint = ProfessionalPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Chi tiết Báo giá",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = quote.quoteNumber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = ProfessionalPrimary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Đóng", tint = Color(0xFF64748B))
                    }
                }

                Divider(color = Color(0xFFE2E8F0))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Status & Actions Bar
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
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
                                Text("Trạng thái", fontSize = 11.sp, color = Color(0xFF64748B))
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(statusBg)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = statusText,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = statusColor
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Ngày lập", fontSize = 11.sp, color = Color(0xFF64748B))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = quote.dateStr,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1E293B)
                                )
                            }
                        }
                    }

                    // Customer & Project Info Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "THÔNG TIN BÁO GIÁ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = ProfessionalPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Khách hàng: ", fontSize = 13.sp, color = Color(0xFF64748B))
                                Text(quote.customerName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Business, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Hạng mục: ", fontSize = 13.sp, color = Color(0xFF64748B))
                                Text(quote.category, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF334155))
                            }

                            Text(
                                text = "Tiêu đề: ${quote.title}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A)
                            )

                            if (quote.notes.isNotBlank()) {
                                Text(
                                    text = "Ghi chú: ${quote.notes}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }

                    // Line Items Table
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "CHI TIẾT SẢN PHẨM / HẠNG MỤC",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            // Table Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF1F5F9), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tên sản phẩm / Dịch vụ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569), modifier = Modifier.weight(2f))
                                Text("SL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569), textAlign = TextAlign.Center, modifier = Modifier.weight(0.6f))
                                Text("Đơn giá", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569), textAlign = TextAlign.End, modifier = Modifier.weight(1.2f))
                                Text("Thành tiền", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569), textAlign = TextAlign.End, modifier = Modifier.weight(1.4f))
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Table Rows
                            displayItems.forEachIndexed { idx, item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${idx + 1}. ${item.name}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF1E293B),
                                        modifier = Modifier.weight(2f)
                                    )
                                    Text(
                                        text = "${item.quantity.toInt()} ${item.unit}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight(0.6f)
                                    )
                                    Text(
                                        text = formatFullCurrencyVND(item.unitPrice),
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B),
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.weight(1.2f)
                                    )
                                    Text(
                                        text = formatFullCurrencyVND(item.totalPrice),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF0F172A),
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.weight(1.4f)
                                    )
                                }
                                if (idx < displayItems.size - 1) {
                                    Divider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(horizontal = 4.dp))
                                }
                            }

                            Divider(color = Color(0xFFE2E8F0), modifier = Modifier.padding(vertical = 8.dp))

                            // Total Summary
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("TỔNG CỘNG THANH TOÁN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Text(
                                    text = formatFullCurrencyVND(quote.amount),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ProfessionalPrimary
                                )
                            }
                        }
                    }

                    // Export Options Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "XUẤT BÁO GIÁ CHO KHÁCH HÀNG",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { showExportExcelModal = true },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("btn_export_quote_excel"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TableChart,
                                        contentDescription = null,
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Xuất Excel", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                                }

                                OutlinedButton(
                                    onClick = { showExportPdfModal = true },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("btn_export_quote_pdf"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = Color(0xFFDC2626),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Xuất PDF", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFDC2626))
                                }
                            }
                        }
                    }
                }

                Divider(color = Color(0xFFE2E8F0))

                // Bottom Action Footer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Đóng", fontSize = 13.sp)
                    }

                    when (quote.status.lowercase()) {
                        "draft" -> {
                            Button(
                                onClick = {
                                    onDismiss()
                                    onSend()
                                },
                                modifier = Modifier.weight(1.5f),
                                colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Gửi báo giá", fontSize = 13.sp, color = Color.White)
                            }
                        }
                        "sent" -> {
                            Button(
                                onClick = {
                                    onDismiss()
                                    onAccept()
                                },
                                modifier = Modifier.weight(1.5f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Chốt đơn", fontSize = 13.sp, color = Color.White)
                            }
                        }
                        "accepted" -> {
                            if (!quote.contractNumber.isNullOrBlank()) {
                                Button(
                                    onClick = {
                                        onDismiss()
                                        onGoToContract?.invoke()
                                    },
                                    modifier = Modifier.weight(1.5f),
                                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimaryNavy),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Xem HĐ (${quote.contractNumber})", fontSize = 12.sp, color = Color.White)
                                }
                            } else {
                                Button(
                                    onClick = {
                                        onDismiss()
                                        onGoToProject()
                                    },
                                    modifier = Modifier.weight(1.5f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Xem tiến độ", fontSize = 13.sp, color = Color.White)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Export PDF Preview & Download
    if (showExportPdfModal) {
        Dialog(onDismissRequest = { showExportPdfModal = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEE2E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Xuất File PDF Báo Giá", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "File '${quote.quoteNumber}.pdf' đã được tạo sẵn theo chuẩn mẫu doanh nghiệp có chữ ký & đóng dấu.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Đã gửi lệnh in cho ${quote.quoteNumber}.pdf", Toast.LENGTH_SHORT).show()
                                showExportPdfModal = false
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("In", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                Toast.makeText(context, "Đã tải xuống ${quote.quoteNumber}.pdf về thiết bị", Toast.LENGTH_LONG).show()
                                showExportPdfModal = false
                            },
                            modifier = Modifier.weight(1.5f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tải PDF", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Modal Export Excel Preview & Download
    if (showExportExcelModal) {
        Dialog(onDismissRequest = { showExportExcelModal = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD1FAE5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.TableChart, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Xuất Bảng Tính Excel (.xlsx)", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Bảng tính '${quote.quoteNumber}.xlsx' bao gồm đầy đủ đơn giá, số lượng, thành tiền và công thức tính toán tự động.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Đã tạo liên kết chia sẻ file Excel", Toast.LENGTH_SHORT).show()
                                showExportExcelModal = false
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Chia sẻ", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                Toast.makeText(context, "Đã lưu file ${quote.quoteNumber}.xlsx vào thư mục Download", Toast.LENGTH_LONG).show()
                                showExportExcelModal = false
                            },
                            modifier = Modifier.weight(1.5f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tải Excel", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
