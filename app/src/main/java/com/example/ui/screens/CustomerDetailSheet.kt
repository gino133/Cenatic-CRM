package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CustomerEntity
import com.example.data.model.CustomerStatus
import com.example.data.model.DealEntity
import com.example.data.model.DealStage
import com.example.data.model.InteractionEntity
import com.example.data.model.InteractionType
import com.example.data.model.TaskEntity
import com.example.data.model.TaskPriority
import com.example.data.model.TaskType
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.CustomerAvatar
import com.example.ui.components.DealStageBadge
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.TaskTypeBadge
import com.example.ui.components.formatCurrencyVND
import com.example.ui.components.formatDateShort
import com.example.ui.components.formatDateTime
import com.example.ui.components.formatFullCurrencyVND
import com.example.ui.components.getInteractionIcon
import com.example.ui.viewmodel.CrmViewModel

@OptIn(ExperimentalLayoutApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit,
    onEditCustomer: (CustomerEntity) -> Unit,
    onAddDealForCustomer: (Long) -> Unit,
    onLogInteractionForCustomer: (Long, String) -> Unit,
    onAddTaskForCustomer: (Long) -> Unit
) {
    val context = LocalContext.current
    val customer by viewModel.selectedCustomer.collectAsStateWithLifecycle()
    val deals by viewModel.selectedCustomerDeals.collectAsStateWithLifecycle()
    val interactions by viewModel.selectedCustomerInteractions.collectAsStateWithLifecycle()
    val tasks by viewModel.selectedCustomerTasks.collectAsStateWithLifecycle()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val cust = customer ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cust.name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditCustomer(cust) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Chỉnh sửa")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = MaterialTheme.colorScheme.error)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("customer_detail_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomerAvatar(
                            name = cust.name,
                            avatarColorHex = cust.avatarColorHex,
                            size = 72.dp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = cust.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        if (cust.position.isNotBlank() || cust.company.isNotBlank()) {
                            Text(
                                text = listOf(cust.position, cust.company).filter { it.isNotBlank() }.joinToString(" • "),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        StatusBadge(status = CustomerStatus.fromString(cust.status))

                        if (cust.tags.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                cust.tags.split(",").map { it.trim() }.filter { it.isNotBlank() }.forEach { tag ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Text(
                                            text = "#$tag",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quick Call, SMS, Email Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            if (cust.phone.isNotBlank()) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${cust.phone.replace(" ", "")}"))
                                            try { context.startActivity(intent) } catch (e: Exception) {
                                                Toast.makeText(context, "Không thể mở ứng dụng gọi", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer)
                                    ) {
                                        Icon(Icons.Default.Call, contentDescription = "Gọi", tint = MaterialTheme.colorScheme.primary)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Gọi điện", style = MaterialTheme.typography.labelSmall)
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${cust.phone.replace(" ", "")}"))
                                            try { context.startActivity(intent) } catch (e: Exception) {
                                                Toast.makeText(context, "Không thể mở tin nhắn", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.secondaryContainer)
                                    ) {
                                        Icon(Icons.Default.Sms, contentDescription = "SMS", tint = MaterialTheme.colorScheme.secondary)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Nhắn tin", style = MaterialTheme.typography.labelSmall)
                                }
                            }

                            if (cust.email.isNotBlank()) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${cust.email}"))
                                            try { context.startActivity(intent) } catch (e: Exception) {
                                                Toast.makeText(context, "Không thể mở email", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                                    ) {
                                        Icon(Icons.Default.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.tertiary)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Email", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }

            // Quick Info & Contact Box
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Thông tin liên hệ & Đặc điểm chi tiết",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        if (cust.phone.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(cust.phone, style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        if (cust.email.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(cust.email, style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        if (cust.address.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(cust.address, style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        if (cust.source.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Business, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Nguồn liên hệ: ${cust.source}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AttachMoney, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Giá trị dự kiến: ${formatFullCurrencyVND(cust.estimatedValue)}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }

                        if (cust.notes.isNotBlank()) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.Note, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Ghi chú cá nhân: ${cust.notes}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // Tab Selector
            item {
                val tabTitles = listOf("Cơ hội (${deals.size})", "Nhật ký (${interactions.size})", "Lịch & Tác vụ (${tasks.size})")
                PrimaryTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }
                }
            }

            // Tab Contents
            when (selectedTabIndex) {
                0 -> {
                    // Deals Tab
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Cơ hội kinh doanh (Deals)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            FilledTonalButton(
                                onClick = { onAddDealForCustomer(cust.id) },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Thêm deal")
                            }
                        }
                    }

                    if (deals.isEmpty()) {
                        item {
                            Text("Chưa có cơ hội kinh doanh nào.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        items(deals) { deal ->
                            val stage = DealStage.fromString(deal.stage)
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(deal.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                        DealStageBadge(stage = stage)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(formatFullCurrencyVND(deal.value), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        Text("Xác suất: ${deal.probability}%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    if (deal.notes.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(deal.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Interaction History Tab
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Lịch sử tương tác (${interactions.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            FilledTonalButton(
                                onClick = { onLogInteractionForCustomer(cust.id, cust.name) },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ghi nhật ký")
                            }
                        }
                    }

                    if (interactions.isEmpty()) {
                        item {
                            Text("Chưa có nhật ký tương tác nào.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        items(interactions) { interaction ->
                            val type = InteractionType.fromString(interaction.type)
                            Card(
                                shape = RoundedCornerShape(14.dp),
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
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(getInteractionIcon(type), contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(interaction.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                            Text(formatDateTime(interaction.date), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        if (interaction.content.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(interaction.content, style = MaterialTheme.typography.bodySmall)
                                        }
                                        if (interaction.outcome.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("Kết quả: ${interaction.outcome}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Tasks & Calendar Tab
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Lịch hẹn & Tác vụ (${tasks.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            FilledTonalButton(
                                onClick = { onAddTaskForCustomer(cust.id) },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Thêm lịch / việc")
                            }
                        }
                    }

                    if (tasks.isEmpty()) {
                        item {
                            Text("Chưa có lịch hẹn hoặc tác vụ nào.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        items(tasks) { task ->
                            val taskType = TaskType.fromString(task.taskType)
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { viewModel.toggleTaskCompletion(task.id, !task.isCompleted) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
                                            contentDescription = null,
                                            tint = if (task.isCompleted) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant
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
                                                task.title,
                                                fontWeight = FontWeight.SemiBold,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = if (task.isCompleted) Color(0xFF16A34A) else MaterialTheme.colorScheme.onSurface
                                            )
                                            TaskTypeBadge(type = taskType)
                                        }

                                        if (task.description.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(task.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }

                                        if (task.location.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(task.location, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("Hạn chót: ${formatDateTime(task.dueDate)}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                                            PriorityBadge(priority = TaskPriority.fromString(task.priority))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        if (showDeleteDialog) {
            ConfirmDeleteDialog(
                title = "Xóa khách hàng",
                message = "Bạn có chắc chắn muốn xóa khách hàng '${cust.name}'? Mọi dữ liệu liên quan sẽ bị xóa vĩnh viễn.",
                onConfirm = {
                    viewModel.deleteCustomer(cust.id)
                    showDeleteDialog = false
                    onBack()
                },
                onDismiss = { showDeleteDialog = false }
            )
        }
    }
}
