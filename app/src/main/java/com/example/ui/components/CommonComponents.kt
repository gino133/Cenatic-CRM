package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.PhoneCallback
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.example.ui.theme.ProfessionalPrimary
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomerStatus
import com.example.data.model.DealStage
import com.example.data.model.InteractionType
import com.example.data.model.TaskPriority
import com.example.data.model.TaskType
import com.example.ui.theme.DealLostBg
import com.example.ui.theme.DealLostText
import com.example.ui.theme.DealWonBg
import com.example.ui.theme.DealWonText
import com.example.ui.theme.PriorityHighBg
import com.example.ui.theme.PriorityHighText
import com.example.ui.theme.PriorityLowBg
import com.example.ui.theme.PriorityLowText
import com.example.ui.theme.PriorityMediumBg
import com.example.ui.theme.PriorityMediumText
import com.example.ui.theme.StatusInactiveBg
import com.example.ui.theme.StatusInactiveText
import com.example.ui.theme.StatusLeadBg
import com.example.ui.theme.StatusLeadText
import com.example.ui.theme.StatusProspectBg
import com.example.ui.theme.StatusProspectText
import com.example.ui.theme.StatusVipBg
import com.example.ui.theme.StatusVipText
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Currency and Date helpers
fun formatCurrencyVND(amount: Double): String {
    return when {
        amount >= 1_000_000_000 -> {
            val billions = amount / 1_000_000_000.0
            String.format(Locale.getDefault(), "%.1f tỷ ₫", billions).replace(".0", "")
        }
        amount >= 1_000_000 -> {
            val millions = amount / 1_000_000.0
            String.format(Locale.getDefault(), "%.1f Tr ₫", millions).replace(".0", "")
        }
        amount >= 1_000 -> {
            val thousands = amount / 1_000.0
            String.format(Locale.getDefault(), "%.0f K ₫", thousands)
        }
        else -> String.format(Locale.getDefault(), "%.0f ₫", amount)
    }
}

fun formatFullCurrencyVND(amount: Double): String {
    val formatter = DecimalFormat("#,### ₫")
    return formatter.format(amount)
}

fun formatDateShort(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun formatTimeOnly(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun formatDateWithDayOfWeek(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEE, dd/MM", Locale("vi", "VN"))
    return sdf.format(Date(timestamp))
}

fun formatRelativeTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 30 -> formatDateShort(timestamp)
        days > 0 -> "$days ngày trước"
        hours > 0 -> "$hours giờ trước"
        minutes > 0 -> "$minutes phút trước"
        else -> "Vừa xong"
    }
}

fun isSameDay(t1: Long, t2: Long): Boolean {
    val c1 = Calendar.getInstance().apply { timeInMillis = t1 }
    val c2 = Calendar.getInstance().apply { timeInMillis = t2 }
    return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
            c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
}

// Avatar with initials
@Composable
fun CustomerAvatar(
    name: String,
    avatarColorHex: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    val parsedColor = try {
        Color(android.graphics.Color.parseColor(avatarColorHex))
    } catch (e: Exception) {
        Color(0xFF2563EB)
    }

    val initials = name.trim().split(" ")
        .filter { it.isNotBlank() }
        .takeLast(2)
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .joinToString("")
        .ifBlank { "KH" }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(parsedColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.38f).sp,
            textAlign = TextAlign.Center
        )
    }
}

// Customer Status Badge
@Composable
fun StatusBadge(
    status: CustomerStatus,
    modifier: Modifier = Modifier
) {
    StatusBadge(
        statusString = status.label,
        modifier = modifier,
        colorHex = status.colorHex
    )
}

@Composable
fun StatusBadge(
    statusString: String,
    modifier: Modifier = Modifier,
    colorHex: String? = null
) {
    val matchedEnum = CustomerStatus.fromString(statusString)
    val displayLabel = when {
        statusString.equals(matchedEnum.name, ignoreCase = true) -> matchedEnum.label
        statusString.isNotBlank() -> statusString
        else -> matchedEnum.label
    }

    val baseColor = if (!colorHex.isNullOrBlank()) {
        try {
            Color(android.graphics.Color.parseColor(colorHex))
        } catch (_: Exception) {
            Color(0xFF2563EB)
        }
    } else {
        try {
            Color(android.graphics.Color.parseColor(matchedEnum.colorHex))
        } catch (_: Exception) {
            Color(0xFF2563EB)
        }
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = baseColor.copy(alpha = 0.12f)
    ) {
        Text(
            text = displayLabel,
            color = baseColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

// Deal Stage Badge
@Composable
fun DealStageBadge(
    stage: DealStage,
    modifier: Modifier = Modifier
) {
    val colors = when (stage) {
        DealStage.LEAD -> StatusLeadBg to StatusLeadText
        DealStage.PROPOSAL -> Color(0xFFE0F2FE) to Color(0xFF0369A1)
        DealStage.NEGOTIATION -> StatusProspectBg to StatusProspectText
        DealStage.WON -> DealWonBg to DealWonText
        DealStage.LOST -> DealLostBg to DealLostText
    }
    val (bgColor, textColor) = colors

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = bgColor
    ) {
        Text(
            text = stage.label,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

// Task Priority Badge
@Composable
fun PriorityBadge(
    priority: TaskPriority,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (priority) {
        TaskPriority.HIGH -> PriorityHighBg to PriorityHighText
        TaskPriority.MEDIUM -> PriorityMediumBg to PriorityMediumText
        TaskPriority.LOW -> PriorityLowBg to PriorityLowText
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Text(
            text = priority.label,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

// Interaction Type Icon
@Composable
fun getInteractionIcon(type: InteractionType): ImageVector {
    return when (type) {
        InteractionType.CALL -> Icons.Default.Call
        InteractionType.MEETING -> Icons.Default.Group
        InteractionType.EMAIL -> Icons.Default.Email
        InteractionType.MESSAGE -> Icons.Default.Chat
        InteractionType.NOTE -> Icons.Default.Note
        InteractionType.CONTRACT -> Icons.Default.Description
    }
}

// Task Type Icon
@Composable
fun getTaskTypeIcon(type: TaskType): ImageVector {
    return when (type) {
        TaskType.MEETING -> Icons.Default.Group
        TaskType.CALL -> Icons.Default.Call
        TaskType.DEMO -> Icons.Default.Videocam
        TaskType.CALL_BACK -> Icons.Default.PhoneCallback
        TaskType.SEND_PROPOSAL -> Icons.Default.Send
        TaskType.FOLLOW_UP -> Icons.Default.SupportAgent
        TaskType.CONTRACT -> Icons.Default.Description
    }
}

// Task Type Badge
@Composable
fun TaskTypeBadge(
    type: TaskType,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = if (type.isCalendarEvent) {
        Color(0xFFE0E7FF) to Color(0xFF3730A3) // Indigo for calendar events
    } else {
        Color(0xFFF1F5F9) to Color(0xFF334155) // Slate for standard tasks
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = getTaskTypeIcon(type),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = type.label,
                color = textColor,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Stat Card for Dashboard
@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconBgColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
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
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                .background(iconBgColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconBgColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun crmSearchFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color(0xFF0F172A),
    unfocusedTextColor = Color(0xFF0F172A),
    cursorColor = ProfessionalPrimary,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedBorderColor = ProfessionalPrimary,
    unfocusedBorderColor = Color(0xFFCBD5E1),
    focusedPlaceholderColor = Color(0xFF94A3B8),
    unfocusedPlaceholderColor = Color(0xFF94A3B8),
    focusedLeadingIconColor = Color(0xFF64748B),
    unfocusedLeadingIconColor = Color(0xFF64748B),
    focusedTrailingIconColor = Color(0xFF64748B),
    unfocusedTrailingIconColor = Color(0xFF64748B)
)

@Composable
fun crmTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color(0xFF0F172A),
    unfocusedTextColor = Color(0xFF0F172A),
    cursorColor = ProfessionalPrimary,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedBorderColor = ProfessionalPrimary,
    unfocusedBorderColor = Color(0xFFCBD5E1),
    focusedLabelColor = ProfessionalPrimary,
    unfocusedLabelColor = Color(0xFF64748B),
    focusedPlaceholderColor = Color(0xFF94A3B8),
    unfocusedPlaceholderColor = Color(0xFF94A3B8),
    focusedLeadingIconColor = ProfessionalPrimary,
    unfocusedLeadingIconColor = Color(0xFF64748B),
    focusedTrailingIconColor = Color(0xFF64748B),
    unfocusedTrailingIconColor = Color(0xFF64748B)
)
