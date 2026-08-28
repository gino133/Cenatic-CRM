package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.CustomerStatus
import com.example.ui.viewmodel.CustomerSort
import com.example.ui.viewmodel.TimeRangeFilter
import com.example.ui.viewmodel.ValueRangeFilter

val AvailableCustomerSources = listOf(
    "Giới thiệu", "Website", "Sự kiện", "Hotline", "Mạng xã hội", "Khác"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AdvancedCustomerFilterSheet(
    currentStatus: CustomerStatus?,
    currentTimeRange: TimeRangeFilter,
    currentValueRange: ValueRangeFilter,
    currentSource: String?,
    currentSort: CustomerSort,
    onDismiss: () -> Unit,
    onApply: (
        status: CustomerStatus?,
        timeRange: TimeRangeFilter,
        valueRange: ValueRangeFilter,
        source: String?,
        sort: CustomerSort
    ) -> Unit,
    onReset: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var tempStatus by remember { mutableStateOf(currentStatus) }
    var tempTimeRange by remember { mutableStateOf(currentTimeRange) }
    var tempValueRange by remember { mutableStateOf(currentValueRange) }
    var tempSource by remember { mutableStateOf(currentSource) }
    var tempSort by remember { mutableStateOf(currentSort) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Bộ Lọc Khách Hàng Nâng Cao",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                TextButton(
                    onClick = {
                        tempStatus = null
                        tempTimeRange = TimeRangeFilter.ALL
                        tempValueRange = ValueRangeFilter.ALL
                        tempSource = null
                        tempSort = CustomerSort.RECENT
                        onReset()
                    }
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                    Text("Đặt lại")
                }
            }

            Text(
                text = "Tùy chỉnh tiêu chí tìm kiếm và sắp xếp danh sách khách hàng",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(modifier = Modifier.height(14.dp))

            // 1. Phân loại / Trạng thái khách hàng
            Text(
                text = "1. Phân loại khách hàng:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = tempStatus == null,
                    onClick = { tempStatus = null },
                    label = { Text("Tất cả phân loại") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                CustomerStatus.entries.forEach { status ->
                    FilterChip(
                        selected = tempStatus == status,
                        onClick = { tempStatus = if (tempStatus == status) null else status },
                        label = { Text(status.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Khoảng thời gian (Ngày tạo / cập nhật)
            Text(
                text = "2. Thời gian tương tác / Ngày tạo:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TimeRangeFilter.entries.forEach { time ->
                    FilterChip(
                        selected = tempTimeRange == time,
                        onClick = { tempTimeRange = time },
                        label = { Text(time.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Mức giá trị dự kiến / Hợp đồng
            Text(
                text = "3. Giá trị dự kiến / Doanh số:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ValueRangeFilter.entries.forEach { vr ->
                    FilterChip(
                        selected = tempValueRange == vr,
                        onClick = { tempValueRange = vr },
                        label = { Text(vr.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Nguồn khách hàng
            Text(
                text = "4. Nguồn liên hệ khách hàng:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = tempSource == null,
                    onClick = { tempSource = null },
                    label = { Text("Tất cả nguồn") }
                )
                AvailableCustomerSources.forEach { src ->
                    FilterChip(
                        selected = tempSource == src,
                        onClick = { tempSource = if (tempSource == src) null else src },
                        label = { Text(src) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Sắp xếp danh sách
            Text(
                text = "5. Sắp xếp danh sách theo:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CustomerSort.entries.forEach { sort ->
                    FilterChip(
                        selected = tempSort == sort,
                        onClick = { tempSort = sort },
                        label = { Text(sort.label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Đóng")
                }
                Button(
                    onClick = {
                        onApply(tempStatus, tempTimeRange, tempValueRange, tempSource, tempSort)
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .testTag("apply_customer_filters_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                    Text("Áp dụng bộ lọc", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
