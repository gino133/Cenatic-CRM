package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.theme.ProfessionalPrimaryContainer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class DatePickerViewMode {
    DAYS,
    MONTHS,
    YEARS
}

/**
 * Modern, bright light-themed DatePickerDialog matching the app's aesthetic.
 * Supports instant selection of Year (1920-2050) and Month without slow month-by-month flipping.
 */
@Composable
fun AppDatePickerDialog(
    initialDateStr: String = "",
    title: String = "Chọn ngày",
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val todayCal = remember { Calendar.getInstance() }
    val todayYear = todayCal.get(Calendar.YEAR)
    val todayMonth = todayCal.get(Calendar.MONTH) + 1
    val todayDay = todayCal.get(Calendar.DAY_OF_MONTH)

    val initialCalendar = remember(initialDateStr) {
        val cal = Calendar.getInstance()
        if (initialDateStr.isNotBlank()) {
            try {
                val parts = initialDateStr.split("/", "-")
                if (parts.size == 3) {
                    val d = parts[0].trim().toIntOrNull() ?: todayDay
                    val m = (parts[1].trim().toIntOrNull() ?: todayMonth) - 1
                    val y = parts[2].trim().toIntOrNull() ?: todayYear
                    cal.set(y, m, d)
                }
            } catch (_: Exception) {}
        }
        cal
    }

    var selectedYear by remember { mutableIntStateOf(initialCalendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableIntStateOf(initialCalendar.get(Calendar.MONTH) + 1) } // 1..12
    var selectedDay by remember { mutableIntStateOf(initialCalendar.get(Calendar.DAY_OF_MONTH)) }

    var viewMode by remember { mutableStateOf(DatePickerViewMode.DAYS) }

    // List of years from 1920 to 2050
    val yearList = remember { (1920..2050).toList() }
    val yearGridState = rememberLazyGridState()

    LaunchedEffect(viewMode) {
        if (viewMode == DatePickerViewMode.YEARS) {
            val index = yearList.indexOf(selectedYear)
            if (index >= 0) {
                // Scroll so selected year is visible
                val targetIndex = (index - 3).coerceAtLeast(0)
                yearGridState.scrollToItem(targetIndex)
            }
        }
    }

    val monthNames = listOf(
        "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
        "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8",
        "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
    )

    val dayOfWeekHeaders = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp)),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // 1. Header Banner (Clean Light theme with Primary accent)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ProfessionalPrimary)
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = title.uppercase(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.85f),
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Large formatted date display
                        val dayOfWeekStr = remember(selectedYear, selectedMonth, selectedDay) {
                            val c = Calendar.getInstance()
                            c.set(selectedYear, selectedMonth - 1, selectedDay)
                            val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
                            when (dayOfWeek) {
                                Calendar.MONDAY -> "Thứ Hai"
                                Calendar.TUESDAY -> "Thứ Ba"
                                Calendar.WEDNESDAY -> "Thứ Tư"
                                Calendar.THURSDAY -> "Thứ Năm"
                                Calendar.FRIDAY -> "Thứ Sáu"
                                Calendar.SATURDAY -> "Thứ Bảy"
                                Calendar.SUNDAY -> "Chủ Nhật"
                                else -> ""
                            }
                        }

                        Text(
                            text = String.format("%02d/%02d/%04d", selectedDay, selectedMonth, selectedYear),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "$dayOfWeekStr, ngày $selectedDay tháng $selectedMonth, $selectedYear",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                // 2. Quick Navigation Bar: Jump to Month or Year
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Month & Year Selector Chips
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Month Button
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (viewMode == DatePickerViewMode.MONTHS) ProfessionalPrimaryContainer else Color(0xFFF1F5F9),
                            modifier = Modifier
                                .clickable {
                                    viewMode = if (viewMode == DatePickerViewMode.MONTHS) DatePickerViewMode.DAYS else DatePickerViewMode.MONTHS
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = monthNames[selectedMonth - 1],
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (viewMode == DatePickerViewMode.MONTHS) ProfessionalPrimary else Color(0xFF1E293B)
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Chọn tháng",
                                    tint = if (viewMode == DatePickerViewMode.MONTHS) ProfessionalPrimary else Color(0xFF64748B),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Year Button
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (viewMode == DatePickerViewMode.YEARS) ProfessionalPrimaryContainer else Color(0xFFF1F5F9),
                            modifier = Modifier
                                .clickable {
                                    viewMode = if (viewMode == DatePickerViewMode.YEARS) DatePickerViewMode.DAYS else DatePickerViewMode.YEARS
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$selectedYear",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (viewMode == DatePickerViewMode.YEARS) ProfessionalPrimary else Color(0xFF1E293B)
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Chọn năm",
                                    tint = if (viewMode == DatePickerViewMode.YEARS) ProfessionalPrimary else Color(0xFF64748B),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Prev / Next Month buttons (only active in Days mode)
                    if (viewMode == DatePickerViewMode.DAYS) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    if (selectedMonth == 1) {
                                        selectedMonth = 12
                                        selectedYear -= 1
                                    } else {
                                        selectedMonth -= 1
                                    }
                                    val maxDays = getDaysInMonth(selectedYear, selectedMonth)
                                    if (selectedDay > maxDays) selectedDay = maxDays
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Tháng trước",
                                    tint = Color(0xFF334155)
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (selectedMonth == 12) {
                                        selectedMonth = 1
                                        selectedYear += 1
                                    } else {
                                        selectedMonth += 1
                                    }
                                    val maxDays = getDaysInMonth(selectedYear, selectedMonth)
                                    if (selectedDay > maxDays) selectedDay = maxDays
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Tháng sau",
                                    tint = Color(0xFF334155)
                                )
                            }
                        }
                    }
                }

                // 3. Content Body (Animated between Days / Months / Years)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(290.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    AnimatedContent(
                        targetState = viewMode,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "DatePickerBody"
                    ) { mode ->
                        when (mode) {
                            // MODE A: DAY GRID
                            DatePickerViewMode.DAYS -> {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    // Day of week header row
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        dayOfWeekHeaders.forEach { header ->
                                            Text(
                                                text = header,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (header == "CN") Color(0xFFEF4444) else Color(0xFF64748B),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.width(36.dp)
                                            )
                                        }
                                    }

                                    // Days matrix
                                    val daysInMonth = getDaysInMonth(selectedYear, selectedMonth)
                                    val firstDayOfWeek = getFirstDayOfWeek(selectedYear, selectedMonth) // 1=Mon .. 7=Sun
                                    val prevMonthDays = getDaysInMonth(
                                        if (selectedMonth == 1) selectedYear - 1 else selectedYear,
                                        if (selectedMonth == 1) 12 else selectedMonth - 1
                                    )

                                    val totalCells = 42 // 6 rows of 7 days
                                    val cells = remember(selectedYear, selectedMonth) {
                                        val list = mutableListOf<DayCell>()
                                        val offset = firstDayOfWeek - 1 // 0-indexed offset from Monday

                                        // Previous month trailing days
                                        for (i in offset - 1 downTo 0) {
                                            list.add(DayCell(day = prevMonthDays - i, isCurrentMonth = false, isPrev = true))
                                        }
                                        // Current month days
                                        for (d in 1..daysInMonth) {
                                            list.add(DayCell(day = d, isCurrentMonth = true))
                                        }
                                        // Next month leading days
                                        var nextDay = 1
                                        while (list.size < totalCells) {
                                            list.add(DayCell(day = nextDay++, isCurrentMonth = false, isPrev = false))
                                        }
                                        list
                                    }

                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(7),
                                        modifier = Modifier.fillMaxWidth(),
                                        userScrollEnabled = false
                                    ) {
                                        items(cells) { cell ->
                                            val isSelected = cell.isCurrentMonth && cell.day == selectedDay
                                            val isToday = cell.isCurrentMonth && cell.day == todayDay && selectedMonth == todayMonth && selectedYear == todayYear

                                            Box(
                                                modifier = Modifier
                                                    .aspectRatio(1f)
                                                    .padding(2.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        when {
                                                            isSelected -> ProfessionalPrimary
                                                            isToday -> ProfessionalPrimaryContainer
                                                            else -> Color.Transparent
                                                        }
                                                    )
                                                    .clickable {
                                                        if (cell.isCurrentMonth) {
                                                            selectedDay = cell.day
                                                        } else if (cell.isPrev) {
                                                            if (selectedMonth == 1) {
                                                                selectedMonth = 12
                                                                selectedYear -= 1
                                                            } else {
                                                                selectedMonth -= 1
                                                            }
                                                            selectedDay = cell.day
                                                        } else {
                                                            if (selectedMonth == 12) {
                                                                selectedMonth = 1
                                                                selectedYear += 1
                                                            } else {
                                                                selectedMonth += 1
                                                            }
                                                            selectedDay = cell.day
                                                        }
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "${cell.day}",
                                                    fontSize = 13.sp,
                                                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                                    color = when {
                                                        isSelected -> Color.White
                                                        isToday -> ProfessionalPrimary
                                                        cell.isCurrentMonth -> Color(0xFF0F172A)
                                                        else -> Color(0xFFCBD5E1)
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // MODE B: MONTH SELECTOR GRID (12 Months)
                            DatePickerViewMode.MONTHS -> {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "Chọn tháng trong năm $selectedYear",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF64748B),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(3),
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(4.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items((1..12).toList()) { m ->
                                            val isSelected = m == selectedMonth
                                            val isCurrentMonthOfToday = m == todayMonth && selectedYear == todayYear

                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = when {
                                                    isSelected -> ProfessionalPrimary
                                                    isCurrentMonthOfToday -> ProfessionalPrimaryContainer
                                                    else -> Color(0xFFF8FAFC)
                                                },
                                                border = if (!isSelected && isCurrentMonthOfToday) {
                                                    androidx.compose.foundation.BorderStroke(1.dp, ProfessionalPrimary)
                                                } else if (!isSelected) {
                                                    androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                                                } else null,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(48.dp)
                                                    .clickable {
                                                        selectedMonth = m
                                                        val maxDays = getDaysInMonth(selectedYear, selectedMonth)
                                                        if (selectedDay > maxDays) selectedDay = maxDays
                                                        viewMode = DatePickerViewMode.DAYS
                                                    }
                                            ) {
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(
                                                        text = monthNames[m - 1],
                                                        fontWeight = if (isSelected || isCurrentMonthOfToday) FontWeight.Bold else FontWeight.Medium,
                                                        fontSize = 13.sp,
                                                        color = when {
                                                            isSelected -> Color.White
                                                            isCurrentMonthOfToday -> ProfessionalPrimary
                                                            else -> Color(0xFF1E293B)
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // MODE C: YEAR SELECTOR GRID (1920..2050)
                            DatePickerViewMode.YEARS -> {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "Chọn năm nhanh",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF64748B),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    LazyVerticalGrid(
                                        state = yearGridState,
                                        columns = GridCells.Fixed(4),
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(4.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(yearList) { y ->
                                            val isSelected = y == selectedYear
                                            val isThisYear = y == todayYear

                                            Surface(
                                                shape = RoundedCornerShape(10.dp),
                                                color = when {
                                                    isSelected -> ProfessionalPrimary
                                                    isThisYear -> ProfessionalPrimaryContainer
                                                    else -> Color(0xFFF8FAFC)
                                                },
                                                border = if (!isSelected && isThisYear) {
                                                    androidx.compose.foundation.BorderStroke(1.dp, ProfessionalPrimary)
                                                } else if (!isSelected) {
                                                    androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                                                } else null,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(42.dp)
                                                    .clickable {
                                                        selectedYear = y
                                                        val maxDays = getDaysInMonth(selectedYear, selectedMonth)
                                                        if (selectedDay > maxDays) selectedDay = maxDays
                                                        // Switch to Months or Days view
                                                        viewMode = DatePickerViewMode.MONTHS
                                                    }
                                            ) {
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(
                                                        text = "$y",
                                                        fontWeight = if (isSelected || isThisYear) FontWeight.Bold else FontWeight.Medium,
                                                        fontSize = 13.sp,
                                                        color = when {
                                                            isSelected -> Color.White
                                                            isThisYear -> ProfessionalPrimary
                                                            else -> Color(0xFF1E293B)
                                                        }
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

                Spacer(modifier = Modifier.height(10.dp))

                // 4. Bottom Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quick "Hôm nay" button
                    TextButton(
                        onClick = {
                            selectedYear = todayYear
                            selectedMonth = todayMonth
                            selectedDay = todayDay
                            viewMode = DatePickerViewMode.DAYS
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = null,
                            tint = ProfessionalPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Hôm nay",
                            fontWeight = FontWeight.Bold,
                            color = ProfessionalPrimary,
                            fontSize = 13.sp
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF475569)
                            )
                        ) {
                            Text("Hủy", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                val formatted = String.format("%02d/%02d/%04d", selectedDay, selectedMonth, selectedYear)
                                onDateSelected(formatted)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ProfessionalPrimary,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Chọn", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

private data class DayCell(
    val day: Int,
    val isCurrentMonth: Boolean,
    val isPrev: Boolean = false
)

private fun getDaysInMonth(year: Int, month: Int): Int {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month - 1)
    cal.set(Calendar.DAY_OF_MONTH, 1)
    return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
}

private fun getFirstDayOfWeek(year: Int, month: Int): Int {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month - 1)
    cal.set(Calendar.DAY_OF_MONTH, 1)
    val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // 1=Sun, 2=Mon ... 7=Sat
    // Convert so 1=Mon .. 7=Sun
    return when (dayOfWeek) {
        Calendar.MONDAY -> 1
        Calendar.TUESDAY -> 2
        Calendar.WEDNESDAY -> 3
        Calendar.THURSDAY -> 4
        Calendar.FRIDAY -> 5
        Calendar.SATURDAY -> 6
        Calendar.SUNDAY -> 7
        else -> 1
    }
}
