package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

    // Next 7 days generator for horizontal calendar strip
    val calendarDays = remember {
        val list = mutableListOf<Long>()
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        for (i in -1..6) {
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_YEAR, i)
            list.add(c.timeInMillis)
        }
        list
    }

    val tasksForSelectedDate = tasks.filter { isSameDay(it.task.dueDate, selectedCalendarDate) }

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
                    // TAB 0: Lịch biểu ngày (Calendar Events & Tasks by Date)
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Lịch biểu: ${formatDateWithDayOfWeek(selectedCalendarDate)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                TextButton(onClick = { selectedCalendarDate = now }) {
                                    Text("Hôm nay")
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Horizontal Date Selector Strip
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(calendarDays) { dayTimestamp ->
                                    val isSelected = isSameDay(dayTimestamp, selectedCalendarDate)
                                    val isToday = isSameDay(dayTimestamp, now)
                                    val countForDay = tasks.count { isSameDay(it.task.dueDate, dayTimestamp) && !it.task.isCompleted }

                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else if (isToday) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface,
                                        tonalElevation = if (isSelected) 4.dp else 1.dp,
                                        modifier = Modifier
                                            .width(62.dp)
                                            .clickable { selectedCalendarDate = dayTimestamp }
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = formatDateWithDayOfWeek(dayTimestamp).split(",").firstOrNull() ?: "",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (isSelected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = SimpleDateFormatDay(dayTimestamp),
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                            )
                                            if (countForDay > 0) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .size(6.dp)
                                                        .clip(CircleShape)
                                                        .background(if (isSelected) Color.White else MaterialTheme.colorScheme.primary)
                                                )
                                            }
                                        }
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
                                    .padding(top = 16.dp)
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
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Không có sự kiện hay công việc nào",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Ngày này bạn chưa có lịch hẹn hoặc công việc nào cần xử lý.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    FilledTonalButton(onClick = onAddTaskClick) {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Tạo công việc mới")
                                    }
                                }
                            }
                        }
                    } else {
                        item {
                            Text(
                                text = "Lịch trình (${tasksForSelectedDate.size} hoạt động):",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

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

    ElevatedCard(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
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
                    tint = if (task.isCompleted) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant,
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
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.None,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurface,
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
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${taskWithCustomer.customerName} ${if (!taskWithCustomer.company.isNullOrBlank()) "• ${taskWithCustomer.company}" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
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
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = task.location,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Outcome Report Display Section (5 levels)
                if (task.resultRating > 0 || task.resultSummary.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
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
                                            tint = if (i <= rating) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Mức $rating: $levelLabel",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Chỉnh sửa kết quả",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            if (task.resultSummary.isNotBlank()) {
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = task.resultSummary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else if (task.isCompleted) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                        modifier = Modifier.clickable { onReportOutcome() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assessment,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "+ Báo cáo kết quả thực hiện (5 mức)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
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
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatDateTime(task.dueDate),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
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
