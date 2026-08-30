package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.InteractionEntity
import com.example.data.model.InteractionType
import com.example.data.model.InteractionWithCustomer
import com.example.data.model.TaskEntity
import com.example.data.model.TaskPriority
import com.example.data.model.TaskType
import com.example.data.model.TaskWithCustomer
import com.example.ui.components.AppDatePickerDialog
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.PriorityBadge
import com.example.ui.components.TaskTypeBadge
import com.example.ui.components.formatDateShort
import com.example.ui.components.formatDateTime
import com.example.ui.components.formatDateWithDayOfWeek
import com.example.ui.components.formatRelativeTime
import com.example.ui.components.formatTimeOnly
import com.example.ui.components.getInteractionIcon
import com.example.ui.components.isSameDay
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.theme.ProfessionalPrimaryContainer
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Assessment
import com.example.ui.components.TaskOutcomeReportDialog
import com.example.ui.viewmodel.CrmViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TasksScreen(
    viewModel: CrmViewModel,
    onEditTask: (TaskEntity) -> Unit,
    onCustomerClick: (Long) -> Unit,
    onAddTaskClick: () -> Unit,
    onLogInteractionClick: () -> Unit
) {
    val context = LocalContext.current
    val tasks by viewModel.tasksWithCustomer.collectAsStateWithLifecycle()
    val interactions by viewModel.interactionsWithCustomer.collectAsStateWithLifecycle()

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Lịch theo ngày, 1: Danh sách tác vụ, 2: Nhật ký hoạt động
    var taskFilterCompleted by remember { mutableStateOf<Boolean?>(false) } // Default: Pending
    var selectedTaskTypeFilter by remember { mutableStateOf<TaskType?>(null) }

    // Selected Calendar Date
    val now = System.currentTimeMillis()
    var selectedCalendarDate by remember { mutableLongStateOf(now) }

    var taskToDelete by remember { mutableStateOf<TaskEntity?>(null) }
    var interactionToDelete by remember { mutableStateOf<InteractionEntity?>(null) }
    var reportingTask by remember { mutableStateOf<TaskEntity?>(null) }
    var showCustomDatePicker by remember { mutableStateOf(false) }

    // Extended 2-year range: 365 days in past to 365 days in future (731 days)
    val calendarDays = remember {
        val list = mutableListOf<Long>()
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        for (i in -365..365) {
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_YEAR, i)
            list.add(c.timeInMillis)
        }
        list
    }

    val coroutineScope = rememberCoroutineScope()
    val calendarStripListState = rememberLazyListState()

    // Helper to scroll calendar strip to a given date
    fun scrollToDate(targetDate: Long, animate: Boolean = true) {
        val index = calendarDays.indexOfFirst { isSameDay(it, targetDate) }
        if (index >= 0) {
            val scrollIndex = (index - 2).coerceAtLeast(0)
            coroutineScope.launch {
                if (animate) {
                    calendarStripListState.animateScrollToItem(scrollIndex)
                } else {
                    calendarStripListState.scrollToItem(scrollIndex)
                }
            }
        }
    }

    // Scroll to today / selected date on initial composition
    LaunchedEffect(Unit) {
        scrollToDate(selectedCalendarDate, animate = false)
    }

    // Filter status for selected date
    var dateTaskFilterCompleted by remember { mutableStateOf<Boolean?>(null) } // null = All, false = Pending, true = Completed

    val allTasksForSelectedDate = tasks.filter { isSameDay(it.task.dueDate, selectedCalendarDate) }
    val tasksForSelectedDate = allTasksForSelectedDate.filter { item ->
        when (dateTaskFilterCompleted) {
            null -> true
            true -> item.task.isCompleted
            false -> !item.task.isCompleted
        }
    }

    if (showCustomDatePicker) {
        val initialDateStr = remember(selectedCalendarDate) { formatDateShort(selectedCalendarDate) }
        AppDatePickerDialog(
            initialDateStr = initialDateStr,
            title = "Chọn ngày kiểm tra công việc",
            onDismiss = { showCustomDatePicker = false },
            onDateSelected = { selectedDateFormatted ->
                try {
                    val parts = selectedDateFormatted.split("/", "-")
                    if (parts.size == 3) {
                        val d = parts[0].trim().toInt()
                        val m = parts[1].trim().toInt() - 1
                        val y = parts[2].trim().toInt()
                        val cal = Calendar.getInstance().apply {
                            set(Calendar.YEAR, y)
                            set(Calendar.MONTH, m)
                            set(Calendar.DAY_OF_MONTH, d)
                            set(Calendar.HOUR_OF_DAY, 12)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        val chosenTimestamp = cal.timeInMillis
                        selectedCalendarDate = chosenTimestamp
                        scrollToDate(chosenTimestamp, animate = true)
                    }
                } catch (_: Exception) {}
                showCustomDatePicker = false
            }
        )
    }

    val filteredTaskList = tasks.filter { item ->
        val matchCompleted = when (taskFilterCompleted) {
            null -> true
            true -> item.task.isCompleted
            false -> !item.task.isCompleted
        }
        val matchType = if (selectedTaskTypeFilter == null) true else item.task.taskType == selectedTaskTypeFilter?.name
        matchCompleted && matchType
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FB))) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FB))
                .testTag("tasks_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Main Navigation Tabs
            item {
                PrimaryTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.clip(RoundedCornerShape(14.dp))
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            Text(
                                "Lịch & Sự kiện",
                                fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            Text(
                                "Công việc (${tasks.count { !it.task.isCompleted }})",
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        text = {
                            Text(
                                "Nhật ký (${interactions.size})",
                                fontWeight = if (selectedTabIndex == 2) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> {
                    // TAB 0: Lịch biểu ngày (Calendar Events & Tasks by Date with Extended Timeline)
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                                // Month & Year Navigation Header
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { showCustomDatePicker = true }
                                            .padding(horizontal = 6.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = "Chọn ngày nhanh",
                                            tint = ProfessionalPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = SimpleDateFormatMonthYear(selectedCalendarDate),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = null,
                                            tint = ProfessionalPrimary,
                                            modifier = Modifier.size(16.dp).padding(start = 4.dp)
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        // Prev week button
                                        IconButton(
                                            onClick = {
                                                val prevDate = selectedCalendarDate - 7 * 24 * 3600 * 1000L
                                                selectedCalendarDate = prevDate
                                                scrollToDate(prevDate, animate = true)
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                                contentDescription = "7 ngày trước",
                                                tint = Color(0xFF475569)
                                            )
                                        }

                                        // Today Button
                                        val isCurrentlyToday = isSameDay(selectedCalendarDate, now)
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isCurrentlyToday) ProfessionalPrimary else Color(0xFFF1F5F9),
                                            modifier = Modifier.clickable {
                                                selectedCalendarDate = now
                                                scrollToDate(now, animate = true)
                                            }
                                        ) {
                                            Text(
                                                text = "Hôm nay",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isCurrentlyToday) Color.White else Color(0xFF334155),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                            )
                                        }

                                        // Next week button
                                        IconButton(
                                            onClick = {
                                                val nextDate = selectedCalendarDate + 7 * 24 * 3600 * 1000L
                                                selectedCalendarDate = nextDate
                                                scrollToDate(nextDate, animate = true)
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                                contentDescription = "7 ngày sau",
                                                tint = Color(0xFF475569)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Extended Horizontal Date Selector Strip (Past & Future)
                                LazyRow(
                                    state = calendarStripListState,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(calendarDays, key = { it }) { dayTimestamp ->
                                        val isSelected = isSameDay(dayTimestamp, selectedCalendarDate)
                                        val isToday = isSameDay(dayTimestamp, now)
                                        val isFirstDayOfMonth = isStartOfMonth(dayTimestamp)

                                        val tasksOnThisDay = tasks.filter { isSameDay(it.task.dueDate, dayTimestamp) }
                                        val countPending = tasksOnThisDay.count { !it.task.isCompleted }
                                        val countCompleted = tasksOnThisDay.count { it.task.isCompleted }

                                        Surface(
                                            shape = RoundedCornerShape(16.dp),
                                            color = if (isSelected) {
                                                ProfessionalPrimary
                                            } else if (isToday) {
                                                Color(0xFFEBF3FE)
                                            } else {
                                                Color(0xFFF8FAFC)
                                            },
                                            border = if (isToday && !isSelected) {
                                                androidx.compose.foundation.BorderStroke(1.5.dp, ProfessionalPrimary)
                                            } else if (!isSelected) {
                                                androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                                            } else null,
                                            tonalElevation = if (isSelected) 4.dp else 0.dp,
                                            modifier = Modifier
                                                .width(66.dp)
                                                .clickable {
                                                    selectedCalendarDate = dayTimestamp
                                                    scrollToDate(dayTimestamp, animate = true)
                                                }
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                // Day of week (T2, T3, T4, T5, T6, T7, CN)
                                                val dayOfWeekStr = getDayOfWeekShort(dayTimestamp)
                                                val isSunday = dayOfWeekStr == "CN"

                                                Text(
                                                    text = dayOfWeekStr,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isSelected) {
                                                        Color.White.copy(alpha = 0.85f)
                                                    } else if (isSunday) {
                                                        Color(0xFFDC2626)
                                                    } else {
                                                        Color(0xFF64748B)
                                                    }
                                                )

                                                Spacer(modifier = Modifier.height(2.dp))

                                                // Day Number
                                                Text(
                                                    text = SimpleDateFormatDay(dayTimestamp),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = if (isSelected) Color.White else if (isToday) ProfessionalPrimary else Color(0xFF1E293B)
                                                )

                                                // Optional Month label if 1st day of month
                                                if (isFirstDayOfMonth) {
                                                    Text(
                                                        text = SimpleDateFormatMonthShort(dayTimestamp),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isSelected) Color.White.copy(alpha = 0.9f) else ProfessionalPrimary
                                                    )
                                                } else {
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                }

                                                // Task count badge
                                                if (tasksOnThisDay.isNotEmpty()) {
                                                    Spacer(modifier = Modifier.height(3.dp))
                                                    if (countPending > 0) {
                                                        // Badge with pending task count
                                                        Box(
                                                            modifier = Modifier
                                                                .clip(RoundedCornerShape(6.dp))
                                                                .background(if (isSelected) Color.White else Color(0xFFEF4444))
                                                                .padding(horizontal = 4.dp, vertical = 1.dp),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Text(
                                                                text = "$countPending",
                                                                style = MaterialTheme.typography.labelSmall,
                                                                fontSize = 9.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = if (isSelected) ProfessionalPrimary else Color.White
                                                            )
                                                        }
                                                    } else if (countCompleted > 0) {
                                                        // All tasks completed on this day
                                                        Box(
                                                            modifier = Modifier
                                                                .size(14.dp)
                                                                .clip(CircleShape)
                                                                .background(if (isSelected) Color.White else Color(0xFF10B981)),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Check,
                                                                contentDescription = null,
                                                                tint = if (isSelected) ProfessionalPrimary else Color.White,
                                                                modifier = Modifier.size(10.dp)
                                                            )
                                                        }
                                                    }
                                                } else {
                                                    Spacer(modifier = Modifier.height(6.dp))
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Quick Jump Shortcuts
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFF1F5F9),
                                        modifier = Modifier.clickable {
                                            val cal = Calendar.getInstance().apply {
                                                timeInMillis = selectedCalendarDate
                                                add(Calendar.MONTH, -1)
                                            }
                                            selectedCalendarDate = cal.timeInMillis
                                            scrollToDate(cal.timeInMillis, animate = true)
                                        }
                                    ) {
                                        Text(
                                            text = "« Tháng trước",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF475569),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFF1F5F9),
                                        modifier = Modifier.clickable {
                                            val cal = Calendar.getInstance().apply {
                                                timeInMillis = selectedCalendarDate
                                                add(Calendar.MONTH, 1)
                                            }
                                            selectedCalendarDate = cal.timeInMillis
                                            scrollToDate(cal.timeInMillis, animate = true)
                                        }
                                    ) {
                                        Text(
                                            text = "Tháng sau »",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF475569),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFEFF6FF),
                                        modifier = Modifier.clickable { showCustomDatePicker = true }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.DateRange,
                                                contentDescription = null,
                                                tint = ProfessionalPrimary,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Chọn ngày khác",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = ProfessionalPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Date Summary Header & Filter
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = formatDateFullVietnamese(selectedCalendarDate),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                        Text(
                                            text = if (allTasksForSelectedDate.isEmpty()) "Chưa có công việc nào" else "${allTasksForSelectedDate.size} công việc (${allTasksForSelectedDate.count { !it.task.isCompleted }} chưa làm, ${allTasksForSelectedDate.count { it.task.isCompleted }} đã xong)",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (allTasksForSelectedDate.any { !it.task.isCompleted }) Color(0xFFD97706) else Color(0xFF64748B),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    FilledTonalButton(
                                        onClick = onAddTaskClick,
                                        modifier = Modifier.height(36.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Giao việc", style = MaterialTheme.typography.labelMedium)
                                    }
                                }

                                if (allTasksForSelectedDate.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        FilterChip(
                                            selected = dateTaskFilterCompleted == null,
                                            onClick = { dateTaskFilterCompleted = null },
                                            label = { Text("Tất cả (${allTasksForSelectedDate.size})", style = MaterialTheme.typography.labelSmall) }
                                        )
                                        FilterChip(
                                            selected = dateTaskFilterCompleted == false,
                                            onClick = { dateTaskFilterCompleted = false },
                                            label = { Text("Chưa làm (${allTasksForSelectedDate.count { !it.task.isCompleted }})", style = MaterialTheme.typography.labelSmall) }
                                        )
                                        FilterChip(
                                            selected = dateTaskFilterCompleted == true,
                                            onClick = { dateTaskFilterCompleted = true },
                                            label = { Text("Đã xong (${allTasksForSelectedDate.count { it.task.isCompleted }})", style = MaterialTheme.typography.labelSmall) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Events & Tasks scheduled for selected date
                    if (tasksForSelectedDate.isEmpty()) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(28.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Event,
                                        contentDescription = null,
                                        tint = ProfessionalPrimary,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = if (allTasksForSelectedDate.isEmpty()) "Không có công việc vào ngày này" else "Không có công việc phù hợp với bộ lọc",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (allTasksForSelectedDate.isEmpty()) "Bạn chưa giao hoặc lên lịch công việc nào vào ngày ${formatDateShort(selectedCalendarDate)}." else "Thử chuyển sang bộ lọc 'Tất cả' để xem toàn bộ danh sách.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    FilledTonalButton(onClick = onAddTaskClick) {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Tạo công việc cho ngày này")
                                    }
                                }
                            }
                        }
                    } else {
                        items(tasksForSelectedDate, key = { it.task.id }) { item ->
                            TaskCard(
                                taskWithCustomer = item,
                                onToggleCompletion = { viewModel.toggleTaskCompletion(item.task.id, it) },
                                onEdit = { onEditTask(item.task) },
                                onDelete = { taskToDelete = item.task },
                                onReportOutcome = { reportingTask = item.task },
                                onCustomerClick = { item.task.customerId?.let { id -> onCustomerClick(id) } },
                                onCall = { phone ->
                                    if (phone.isNotBlank()) {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.replace(" ", "")}"))
                                        try { context.startActivity(intent) } catch (e: Exception) {}
                                    }
                                }
                            )
                        }
                    }
                }
                1 -> {
                    // TAB 1: Danh sách công việc cần làm (To-Do List with types & filters)
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Completion status chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = taskFilterCompleted == false,
                                    onClick = { taskFilterCompleted = false },
                                    label = { Text("Chưa làm (${tasks.count { !it.task.isCompleted }})") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                                FilterChip(
                                    selected = taskFilterCompleted == true,
                                    onClick = { taskFilterCompleted = true },
                                    label = { Text("Hoàn thành (${tasks.count { it.task.isCompleted }})") }
                                )
                                FilterChip(
                                    selected = taskFilterCompleted == null,
                                    onClick = { taskFilterCompleted = null },
                                    label = { Text("Tất cả (${tasks.size})") }
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Task Type Filter Chips
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                item {
                                    FilterChip(
                                        selected = selectedTaskTypeFilter == null,
                                        onClick = { selectedTaskTypeFilter = null },
                                        label = { Text("Tất cả loại việc") }
                                    )
                                }
                                items(TaskType.entries) { type ->
                                    FilterChip(
                                        selected = selectedTaskTypeFilter == type,
                                        onClick = { selectedTaskTypeFilter = if (selectedTaskTypeFilter == type) null else type },
                                        label = { Text(type.label) }
                                    )
                                }
                            }
                        }
                    }

                    if (filteredTaskList.isEmpty()) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Assignment,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(56.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = if (taskFilterCompleted == false) "Không có công việc nào đang chờ!" else "Chưa có công việc trong danh sách",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Tạo các công việc như: Gọi lại chăm sóc, Gửi báo giá, Theo dõi sau bán hàng...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    } else {
                        items(filteredTaskList, key = { it.task.id }) { item ->
                            TaskCard(
                                taskWithCustomer = item,
                                onToggleCompletion = { viewModel.toggleTaskCompletion(item.task.id, it) },
                                onEdit = { onEditTask(item.task) },
                                onDelete = { taskToDelete = item.task },
                                onReportOutcome = { reportingTask = item.task },
                                onCustomerClick = { item.task.customerId?.let { id -> onCustomerClick(id) } },
                                onCall = { phone ->
                                    if (phone.isNotBlank()) {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.replace(" ", "")}"))
                                        try { context.startActivity(intent) } catch (e: Exception) {}
                                    }
                                }
                            )
                        }
                    }
                }
                2 -> {
                    // TAB 2: Nhật ký hoạt động & tương tác
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dòng thời gian hoạt động chăm sóc",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            FilledTonalButton(onClick = onLogInteractionClick) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ghi nhật ký")
                            }
                        }
                    }

                    if (interactions.isEmpty()) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(56.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Chưa có nhật ký hoạt động nào",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Ghi lại nội dung trao đổi qua cuộc gọi, họp mặt và email",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    } else {
                        items(interactions, key = { it.interaction.id }) { item ->
                            InteractionCard(
                                interactionWithCustomer = item,
                                onDelete = { interactionToDelete = item.interaction },
                                onCustomerClick = { onCustomerClick(item.interaction.customerId) }
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        // Delete Dialogs
        taskToDelete?.let { task ->
            ConfirmDeleteDialog(
                title = "Xóa công việc",
                message = "Bạn có chắc muốn xóa công việc '${task.title}'?",
                onConfirm = {
                    viewModel.deleteTask(task.id)
                    taskToDelete = null
                },
                onDismiss = { taskToDelete = null }
            )
        }

        // Outcome Reporting Dialog
        reportingTask?.let { task ->
            TaskOutcomeReportDialog(
                task = task,
                onDismiss = { reportingTask = null },
                onSave = { rating, report ->
                    viewModel.updateTaskOutcome(task.id, rating, report)
                    reportingTask = null
                }
            )
        }

        interactionToDelete?.let { item ->
            ConfirmDeleteDialog(
                title = "Xóa nhật ký tương tác",
                message = "Bạn có chắc muốn xóa nhật ký '${item.title}'?",
                onConfirm = {
                    viewModel.deleteInteraction(item.id)
                    interactionToDelete = null
                },
                onDismiss = { interactionToDelete = null }
            )
        }
    }
}

private fun SimpleDateFormatDay(timestamp: Long): String {
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    return cal.get(Calendar.DAY_OF_MONTH).toString()
}

private fun SimpleDateFormatMonthYear(timestamp: Long): String {
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    val month = cal.get(Calendar.MONTH) + 1
    val year = cal.get(Calendar.YEAR)
    return "Tháng $month, $year"
}

private fun SimpleDateFormatMonthShort(timestamp: Long): String {
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    val month = cal.get(Calendar.MONTH) + 1
    return "Th.$month"
}

private fun getDayOfWeekShort(timestamp: Long): String {
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    return when (cal.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> "T2"
        Calendar.TUESDAY -> "T3"
        Calendar.WEDNESDAY -> "T4"
        Calendar.THURSDAY -> "T5"
        Calendar.FRIDAY -> "T6"
        Calendar.SATURDAY -> "T7"
        Calendar.SUNDAY -> "CN"
        else -> ""
    }
}

private fun isStartOfMonth(timestamp: Long): Boolean {
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    return cal.get(Calendar.DAY_OF_MONTH) == 1
}

private fun formatDateFullVietnamese(timestamp: Long): String {
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    val dayOfWeek = when (cal.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> "Thứ Hai"
        Calendar.TUESDAY -> "Thứ Ba"
        Calendar.WEDNESDAY -> "Thứ Tư"
        Calendar.THURSDAY -> "Thứ Năm"
        Calendar.FRIDAY -> "Thứ Sáu"
        Calendar.SATURDAY -> "Thứ Bảy"
        Calendar.SUNDAY -> "Chủ Nhật"
        else -> ""
    }
    val d = cal.get(Calendar.DAY_OF_MONTH)
    val m = cal.get(Calendar.MONTH) + 1
    val y = cal.get(Calendar.YEAR)
    val dStr = if (d < 10) "0$d" else "$d"
    val mStr = if (m < 10) "0$m" else "$m"
    return "$dayOfWeek, $dStr/$mStr/$y"
}

@Composable
fun TaskCard(
    taskWithCustomer: TaskWithCustomer,
    onToggleCompletion: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onReportOutcome: () -> Unit,
    onCustomerClick: () -> Unit,
    onCall: (String) -> Unit
) {
    val task = taskWithCustomer.task
    val taskType = TaskType.fromString(task.taskType)
    var showMenu by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("task_card_${task.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = { onToggleCompletion(!task.isCompleted) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
                    contentDescription = "Hoàn thành",
                    tint = if (task.isCompleted) Color(0xFF10B981) else Color(0xFF94A3B8),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.None,
                        color = if (task.isCompleted) Color(0xFF16A34A) else Color(0xFF0F172A),
                        modifier = Modifier.weight(1f)
                    )
                    TaskTypeBadge(type = taskType)
                }

                if (taskWithCustomer.customerName != null) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onCustomerClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = ProfessionalPrimary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${taskWithCustomer.customerName} ${if (!taskWithCustomer.company.isNullOrBlank()) "• ${taskWithCustomer.company}" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ProfessionalPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (task.location.isNotBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = task.location,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF475569)
                    )
                }

                // Outcome Report Display Section (Only if report actually exists)
                if (task.resultRating > 0 || task.resultSummary.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onReportOutcome() }
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val rating = if (task.resultRating > 0) task.resultRating else 4
                                val levelLabel = when (rating) {
                                    1 -> "Kém / Thất bại"
                                    2 -> "Dưới kỳ vọng"
                                    3 -> "Đạt yêu cầu"
                                    4 -> "Tốt / Thành công"
                                    else -> "Xuất sắc / Vượt kỳ vọng"
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    for (i in 1..5) {
                                        Icon(
                                            imageVector = if (i <= rating) Icons.Default.Star else Icons.Outlined.StarOutline,
                                            contentDescription = null,
                                            tint = if (i <= rating) Color(0xFFF59E0B) else Color(0xFFCBD5E1),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Mức $rating: $levelLabel",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = ProfessionalPrimary
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Chỉnh sửa kết quả",
                                    tint = ProfessionalPrimary,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            if (task.resultSummary.isNotBlank()) {
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = task.resultSummary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF475569)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = if (task.isCompleted) Color(0xFF16A34A) else ProfessionalPrimary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatDateTime(task.dueDate),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (task.isCompleted) Color(0xFF16A34A) else ProfessionalPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    PriorityBadge(priority = TaskPriority.fromString(task.priority))
                }
            }

            Box {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Tùy chọn")
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    if (!taskWithCustomer.customerPhone.isNullOrBlank()) {
                        DropdownMenuItem(
                            text = { Text("Gọi điện khách hàng") },
                            leadingIcon = { Icon(Icons.Default.Call, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                            onClick = {
                                showMenu = false
                                onCall(taskWithCustomer.customerPhone)
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("Báo cáo kết quả") },
                        leadingIcon = { Icon(Icons.Default.Assessment, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        onClick = {
                            showMenu = false
                            onReportOutcome()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Chỉnh sửa") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            onEdit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Xóa", color = MaterialTheme.colorScheme.error) },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                        onClick = {
                            showMenu = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InteractionCard(
    interactionWithCustomer: InteractionWithCustomer,
    onDelete: () -> Unit,
    onCustomerClick: () -> Unit
) {
    val interaction = interactionWithCustomer.interaction
    val type = InteractionType.fromString(interaction.type)
    var showMenu by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = getInteractionIcon(type),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = interaction.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${interactionWithCustomer.customerName} ${if (interactionWithCustomer.company.isNotBlank()) "• ${interactionWithCustomer.company}" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { onCustomerClick() }
                        )
                    }

                    Text(
                        text = formatRelativeTime(interaction.date),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (interaction.content.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = interaction.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (interaction.outcome.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "Kết quả: ${interaction.outcome}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Box {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Tùy chọn")
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Xóa nhật ký", color = MaterialTheme.colorScheme.error) },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                        onClick = {
                            showMenu = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}
