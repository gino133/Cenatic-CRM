package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.QuoteItem
import com.example.data.model.QuoteProductItem
import com.example.ui.components.formatFullCurrencyVND
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.viewmodel.CrmViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Predefined quote categories
val QuoteCategories = listOf(
    "Chọn loại hàng",
    "Thiết kế & Thi công",
    "Phần mềm & Bản quyền",
    "Dịch vụ & Bảo trì",
    "Thiết bị phần cứng",
    "Tư vấn giải pháp",
    "Hàng hóa tiêu dùng",
    "Khác"
)

// Clean colors matching the UI mockup
private val InputBackgroundColor = Color(0xFFEFF4FB)
private val LabelTextColor = Color(0xFF475569)
private val PrimaryBlueColor = Color(0xFF1D6EE5)
private val DarkTitleColor = Color(0xFF0F2B5C)
private val PlaceholderTextColor = Color(0xFF94A3B8)
private val ContentTextColor = Color(0xFF1E293B)
private val BorderColor = Color(0xFFE2E8F0)

@Composable
fun CreateQuoteScreen(
    viewModel: CrmViewModel,
    quoteToEdit: QuoteItem? = null,
    onBack: () -> Unit,
    onNavigateToTab: (Int) -> Unit = {}
) {
    val allCustomers by viewModel.allRawCustomers.collectAsStateWithLifecycle()
    val quotes by viewModel.quotes.collectAsStateWithLifecycle()

    val dateFormatted = remember {
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    }
    val defaultQuoteNum = remember {
        val nextSeq = String.format(Locale.getDefault(), "%03d", quotes.size + 1)
        "BG-$dateFormatted-$nextSeq"
    }

    var customerName by remember { mutableStateOf(quoteToEdit?.customerName ?: "") }
    var quoteNumber by remember { mutableStateOf(quoteToEdit?.quoteNumber ?: defaultQuoteNum) }
    var selectedCategory by remember { mutableStateOf(quoteToEdit?.category ?: "Chọn loại hàng") }
    var notes by remember { mutableStateOf(quoteToEdit?.notes ?: "") }

    var productItems by remember {
        mutableStateOf(
            if (quoteToEdit != null && quoteToEdit.items.isNotEmpty()) {
                quoteToEdit.items
            } else if (quoteToEdit != null && quoteToEdit.amount > 0) {
                listOf(
                    QuoteProductItem(
                        name = quoteToEdit.title,
                        notes = "Theo yêu cầu",
                        unit = "Gói",
                        quantity = 1.0,
                        unitPrice = quoteToEdit.amount,
                        vatPercent = 10.0
                    )
                )
            } else {
                listOf(
                    QuoteProductItem(
                        name = "Dịch vụ tư vấn & triển khai",
                        notes = "Tiêu chuẩn",
                        unit = "Gói",
                        quantity = 1.0,
                        unitPrice = 10000000.0,
                        vatPercent = 10.0
                    )
                )
            }
        )
    }

    val totalPreVat = remember(productItems) { productItems.sumOf { it.subtotal } }
    val totalVat = remember(productItems) { productItems.sumOf { it.vatAmount } }
    val totalPostVat = remember(productItems) { totalPreVat + totalVat }
    val vatGroups = remember(productItems) {
        productItems.groupBy { it.vatPercent }
            .map { (vat, list) ->
                val pre = list.sumOf { it.subtotal }
                val v = list.sumOf { it.vatAmount }
                Triple(vat, pre, v)
            }
            .sortedBy { it.first }
    }

    var itemToEdit by remember { mutableStateOf<QuoteProductItem?>(null) }
    var itemToEditIndex by remember { mutableStateOf<Int?>(null) }
    var showAddOrEditProductDialog by remember { mutableStateOf(false) }

    var amountStr by remember {
        mutableStateOf(
            if (quoteToEdit != null && quoteToEdit.amount > 0) {
                quoteToEdit.amount.toLong().toString()
            } else totalPreVat.toLong().toString()
        )
    }

    var expandedCustomerDropdown by remember { mutableStateOf(false) }
    var expandedCategoryDropdown by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    val filteredCustomers = remember(customerName, allCustomers) {
        if (customerName.isBlank()) allCustomers else allCustomers.filter {
            it.name.contains(customerName, ignoreCase = true) || it.company.contains(customerName, ignoreCase = true)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            // Bottom Bar matching mockup design
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = BorderColor)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tab 0: Trang chủ
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onNavigateToTab(0) }
                            .padding(vertical = 4.dp, horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = "Trang chủ",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Trang\nchủ",
                            fontSize = 11.sp,
                            lineHeight = 12.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF64748B)
                        )
                    }

                    // Tab 1: Khách hàng
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onNavigateToTab(1) }
                            .padding(vertical = 4.dp, horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.People,
                            contentDescription = "Khách hàng",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Khách\nhàng",
                            fontSize = 11.sp,
                            lineHeight = 12.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF64748B)
                        )
                    }

                    // Tab 2: Báo giá (Active Pill)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onNavigateToTab(2) }
                            .padding(vertical = 2.dp, horizontal = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryBlueColor)
                                .padding(horizontal = 18.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = "Báo giá",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Báo\ngiá",
                            fontSize = 11.sp,
                            lineHeight = 12.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlueColor
                        )
                    }

                    // Tab 3: Công việc
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onNavigateToTab(3) }
                            .padding(vertical = 4.dp, horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Assignment,
                            contentDescription = "Công việc",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Công\nviệc",
                            fontSize = 11.sp,
                            lineHeight = 12.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = DarkTitleColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (quoteToEdit == null) "Tạo báo giá" else "Chỉnh sửa báo giá",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTitleColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Form Container exactly mirroring the screenshot
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. TÊN KHÁCH HÀNG
                Column {
                    Text(
                        text = "TÊN KHÁCH HÀNG",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LabelTextColor,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(InputBackgroundColor)
                                .border(
                                    width = if (isError && customerName.isBlank()) 1.dp else 0.dp,
                                    color = if (isError && customerName.isBlank()) MaterialTheme.colorScheme.error else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Tìm khách hàng",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (customerName.isEmpty()) {
                                    Text(
                                        text = "Tran cao hai",
                                        color = PlaceholderTextColor,
                                        fontSize = 15.sp
                                    )
                                }
                                BasicTextField(
                                    value = customerName,
                                    onValueChange = {
                                        customerName = it
                                        expandedCustomerDropdown = true
                                        if (it.isNotBlank()) isError = false
                                    },
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        color = ContentTextColor,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    cursorBrush = SolidColor(PrimaryBlueColor),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("input_quote_customer_name")
                                )
                            }
                        }

                        // Dropdown list for customer auto-complete
                        if (expandedCustomerDropdown && filteredCustomers.isNotEmpty()) {
                            DropdownMenu(
                                expanded = expandedCustomerDropdown,
                                onDismissRequest = { expandedCustomerDropdown = false },
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .background(Color.White)
                            ) {
                                filteredCustomers.take(6).forEach { cust ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(
                                                    text = cust.name,
                                                    fontWeight = FontWeight.Bold,
                                                    color = ContentTextColor,
                                                    fontSize = 14.sp
                                                )
                                                if (cust.company.isNotBlank()) {
                                                    Text(
                                                        text = cust.company,
                                                        fontSize = 12.sp,
                                                        color = Color(0xFF64748B)
                                                    )
                                                }
                                            }
                                        },
                                        onClick = {
                                            customerName = cust.name
                                            expandedCustomerDropdown = false
                                            isError = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. SỐ BÁO GIÁ
                Column {
                    Text(
                        text = "SỐ BÁO GIÁ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LabelTextColor,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(InputBackgroundColor)
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (quoteNumber.isEmpty()) {
                                Text(
                                    text = "BG-20231027-001",
                                    color = PlaceholderTextColor,
                                    fontSize = 15.sp
                                )
                            }
                            BasicTextField(
                                value = quoteNumber,
                                onValueChange = { quoteNumber = it },
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = ContentTextColor,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                cursorBrush = SolidColor(PrimaryBlueColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_quote_number")
                            )
                        }
                    }
                }

                // 3. BẢNG CHI TIẾT SẢN PHẨM & DỊCH VỤ (HỖ TRỢ CUỘN NGANG)
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CHI TIẾT SẢN PHẨM / DỊCH VỤ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LabelTextColor,
                            letterSpacing = 0.5.sp
                        )
                        TextButton(
                            onClick = {
                                itemToEdit = null
                                itemToEditIndex = null
                                showAddOrEditProductDialog = true
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = PrimaryBlueColor, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Thêm dòng", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryBlueColor)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            // Bảng cuộn ngang
                            val tableScrollState = rememberScrollState()
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(tableScrollState)
                            ) {
                                Column(modifier = Modifier.widthIn(min = 680.dp)) {
                                    // Header
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
                                            .padding(vertical = 8.dp, horizontal = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("STT", modifier = Modifier.width(36.dp), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF334155), textAlign = TextAlign.Center)
                                        Text("Tên sản phẩm / Dịch vụ", modifier = Modifier.width(180.dp), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF334155))
                                        Text("Ghi chú", modifier = Modifier.width(110.dp), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF334155))
                                        Text("ĐVT", modifier = Modifier.width(55.dp), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF334155), textAlign = TextAlign.Center)
                                        Text("SL", modifier = Modifier.width(45.dp), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF334155), textAlign = TextAlign.End)
                                        Text("Đơn giá (đ)", modifier = Modifier.width(95.dp), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF334155), textAlign = TextAlign.End)
                                        Text("Thành tiền (đ)", modifier = Modifier.width(105.dp), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF334155), textAlign = TextAlign.End)
                                        Text("VAT (%)", modifier = Modifier.width(60.dp), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF334155), textAlign = TextAlign.Center)
                                        Text("Sửa/Xóa", modifier = Modifier.width(65.dp), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF334155), textAlign = TextAlign.Center)
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    if (productItems.isEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Chưa có sản phẩm nào. Bấm '+ Thêm dòng' để tạo.", fontSize = 12.sp, color = Color(0xFF94A3B8))
                                        }
                                    } else {
                                        productItems.forEachIndexed { index, item ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 6.dp, horizontal = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("${index + 1}", modifier = Modifier.width(36.dp), fontSize = 11.sp, color = Color(0xFF475569), textAlign = TextAlign.Center)
                                                Text(item.name, modifier = Modifier.width(180.dp), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                                Text(item.notes.ifBlank { "-" }, modifier = Modifier.width(110.dp), fontSize = 11.sp, color = Color(0xFF64748B))
                                                Text(item.unit, modifier = Modifier.width(55.dp), fontSize = 11.sp, color = Color(0xFF475569), textAlign = TextAlign.Center)
                                                Text(if (item.quantity % 1.0 == 0.0) item.quantity.toInt().toString() else "%.1f".format(item.quantity), modifier = Modifier.width(45.dp), fontSize = 11.sp, color = Color(0xFF0F172A), textAlign = TextAlign.End)
                                                Text(formatFullCurrencyVND(item.unitPrice), modifier = Modifier.width(95.dp), fontSize = 11.sp, color = Color(0xFF0F172A), textAlign = TextAlign.End)
                                                Text(formatFullCurrencyVND(item.subtotal), modifier = Modifier.width(105.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A), textAlign = TextAlign.End)
                                                Text("${item.vatPercent.toInt()}%", modifier = Modifier.width(60.dp), fontSize = 11.sp, color = Color(0xFF047857), textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)

                                                Row(modifier = Modifier.width(65.dp), horizontalArrangement = Arrangement.Center) {
                                                    IconButton(
                                                        onClick = {
                                                            itemToEdit = item
                                                            itemToEditIndex = index
                                                            showAddOrEditProductDialog = true
                                                        },
                                                        modifier = Modifier.size(26.dp)
                                                    ) {
                                                        Icon(Icons.Default.Edit, contentDescription = "Sửa", tint = PrimaryBlueColor, modifier = Modifier.size(15.dp))
                                                    }
                                                    IconButton(
                                                        onClick = {
                                                            productItems = productItems.filterIndexed { i, _ -> i != index }
                                                        },
                                                        modifier = Modifier.size(26.dp)
                                                    ) {
                                                        Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color(0xFFE02424), modifier = Modifier.size(15.dp))
                                                    }
                                                }
                                            }
                                            Divider(color = Color(0xFFE2E8F0), thickness = 0.5.dp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // TỔNG HỢP GIÁ TRỊ & THUẾ VAT (THEO YÊU CẦU: HIỂN THỊ DƯỚI DÒNG TỔNG TRƯỚC THUẾ, SẮP XẾP VAT TĂNG DẦN)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Tổng giá trị trước thuế (Pre-VAT):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                    Text(formatFullCurrencyVND(totalPreVat), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PrimaryBlueColor)
                                }

                                if (vatGroups.isNotEmpty()) {
                                    Divider(color = Color(0xFFF1F5F9))
                                    vatGroups.forEach { (vat, preVatAmt, vatAmt) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "• Thuế VAT ${vat.toInt()}% (trên ${formatFullCurrencyVND(preVatAmt)}):",
                                                fontSize = 11.sp,
                                                color = Color(0xFF475569)
                                            )
                                            Text(
                                                text = formatFullCurrencyVND(vatAmt),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF047857)
                                            )
                                        }
                                    }
                                }

                                Divider(color = Color(0xFFE2E8F0))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Tổng tiền thuế VAT:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                                    Text(formatFullCurrencyVND(totalVat), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Tổng thanh toán (Sau VAT):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                    Text(formatFullCurrencyVND(totalPostVat), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                                }
                            }
                        }
                    }
                }

                // 4. LOẠI HÀNG
                Column {
                    Text(
                        text = "LOẠI HÀNG",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LabelTextColor,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(InputBackgroundColor)
                                .clickable { expandedCategoryDropdown = true }
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = selectedCategory,
                                fontSize = 15.sp,
                                color = if (selectedCategory == "Chọn loại hàng") ContentTextColor else ContentTextColor
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Chọn",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = expandedCategoryDropdown,
                            onDismissRequest = { expandedCategoryDropdown = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(Color.White)
                        ) {
                            QuoteCategories.forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = category,
                                            fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal,
                                            color = if (category == selectedCategory) PrimaryBlueColor else ContentTextColor,
                                            fontSize = 14.sp
                                        )
                                    },
                                    onClick = {
                                        selectedCategory = category
                                        expandedCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                // 5. GHI CHÚ (TÙY CHỌN)
                Column {
                    Text(
                        text = "GHI CHÚ (TÙY CHỌN)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LabelTextColor,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 96.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(InputBackgroundColor)
                            .padding(14.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        if (notes.isEmpty()) {
                            Text(
                                text = "Nhập ghi chú cho báo giá này...",
                                color = PlaceholderTextColor,
                                fontSize = 14.sp
                            )
                        }
                        BasicTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            textStyle = TextStyle(
                                color = ContentTextColor,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            ),
                            cursorBrush = SolidColor(PrimaryBlueColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 80.dp)
                                .testTag("input_quote_notes")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 6. ACTION BUTTON (+ Tạo báo giá)
                Button(
                    onClick = {
                        if (customerName.isBlank()) {
                            isError = true
                            return@Button
                        }
                        val amount = amountStr.toDoubleOrNull() ?: 0.0
                        val finalTitle = if (selectedCategory != "Chọn loại hàng") {
                            "$selectedCategory - $customerName"
                        } else {
                            "Báo giá cho $customerName"
                        }
                        val todayStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

                        val newQuote = (quoteToEdit ?: QuoteItem(
                            quoteNumber = quoteNumber,
                            title = finalTitle,
                            amount = amount,
                            dateStr = todayStr,
                            status = "Draft"
                        )).copy(
                            quoteNumber = quoteNumber.trim(),
                            title = finalTitle.trim(),
                            amount = amount,
                            dateStr = quoteToEdit?.dateStr ?: todayStr,
                            customerName = customerName.trim(),
                            category = selectedCategory,
                            notes = notes.trim(),
                            items = productItems
                        )

                        if (quoteToEdit == null) {
                            viewModel.addQuote(newQuote)
                        } else {
                            viewModel.updateQuote(newQuote)
                        }
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlueColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_create_quote_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (quoteToEdit == null) "Tạo báo giá" else "Lưu báo giá",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showAddOrEditProductDialog) {
        val initialItem = itemToEdit
        var name by remember(initialItem) { mutableStateOf(initialItem?.name ?: "") }
        var itemNotes by remember(initialItem) { mutableStateOf(initialItem?.notes ?: "") }
        var unit by remember(initialItem) { mutableStateOf(initialItem?.unit ?: "Gói") }
        var quantityStr by remember(initialItem) { mutableStateOf(initialItem?.quantity?.let { if (it % 1.0 == 0.0) it.toInt().toString() else it.toString() } ?: "1") }
        var unitPriceStr by remember(initialItem) { mutableStateOf(initialItem?.unitPrice?.toLong()?.toString() ?: "0") }
        var vatPercent by remember(initialItem) { mutableStateOf(initialItem?.vatPercent ?: 10.0) }

        AlertDialog(
            onDismissRequest = { showAddOrEditProductDialog = false },
            title = {
                Text(
                    text = if (initialItem == null) "Thêm sản phẩm / dịch vụ" else "Chỉnh sửa sản phẩm / dịch vụ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color(0xFF0F172A)
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Tên sản phẩm / Dịch vụ *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = itemNotes,
                        onValueChange = { itemNotes = it },
                        label = { Text("Ghi chú / Quy cách") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = unit,
                            onValueChange = { unit = it },
                            label = { Text("ĐVT") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = quantityStr,
                            onValueChange = { quantityStr = it },
                            label = { Text("Số lượng") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = unitPriceStr,
                        onValueChange = { unitPriceStr = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Đơn giá (VNĐ) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column {
                        Text("Thuế suất VAT:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(0.0, 5.0, 8.0, 10.0).forEach { vat ->
                                val isSelected = vatPercent == vat
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { vatPercent = vat },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) PrimaryBlueColor else Color(0xFFF1F5F9),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) PrimaryBlueColor else Color(0xFFE2E8F0))
                                ) {
                                    Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${vat.toInt()}%",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else Color(0xFF334155)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isBlank()) return@Button
                        val qty = quantityStr.toDoubleOrNull() ?: 1.0
                        val price = unitPriceStr.toDoubleOrNull() ?: 0.0
                        val newItem = QuoteProductItem(
                            id = initialItem?.id ?: System.currentTimeMillis(),
                            name = name.trim(),
                            notes = itemNotes.trim(),
                            unit = unit.trim().ifBlank { "Gói" },
                            quantity = qty,
                            unitPrice = price,
                            vatPercent = vatPercent
                        )
                        if (itemToEditIndex != null && itemToEditIndex!! in productItems.indices) {
                            productItems = productItems.toMutableList().also {
                                it[itemToEditIndex!!] = newItem
                            }
                        } else {
                            productItems = productItems + newItem
                        }
                        showAddOrEditProductDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlueColor)
                ) {
                    Text("Lưu sản phẩm", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddOrEditProductDialog = false }) {
                    Text("Hủy", color = Color(0xFF64748B))
                }
            }
        )
    }
}
