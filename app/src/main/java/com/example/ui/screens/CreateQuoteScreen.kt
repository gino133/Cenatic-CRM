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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
    var amountStr by remember {
        mutableStateOf(
            if (quoteToEdit != null && quoteToEdit.amount > 0) {
                quoteToEdit.amount.toLong().toString()
            } else "0"
        )
    }
    var selectedCategory by remember { mutableStateOf(quoteToEdit?.category ?: "Chọn loại hàng") }
    var notes by remember { mutableStateOf(quoteToEdit?.notes ?: "") }

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

                // 3. GIÁ TRỊ (VNĐ)
                Column {
                    Text(
                        text = "GIÁ TRỊ (VNĐ)",
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            BasicTextField(
                                value = amountStr,
                                onValueChange = { input ->
                                    val digitsOnly = input.filter { it.isDigit() }
                                    amountStr = if (digitsOnly.isBlank()) "0" else digitsOnly
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = TextStyle(
                                    color = ContentTextColor,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.End,
                                    fontWeight = FontWeight.Normal
                                ),
                                cursorBrush = SolidColor(PrimaryBlueColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_quote_amount")
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "đ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline,
                            color = ContentTextColor
                        )
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
                            notes = notes.trim()
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
}
