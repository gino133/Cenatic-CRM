package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "customer_types")
data class CustomerTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String,
    val name: String,
    val colorHex: String = "#2563EB",
    val description: String = "",
    val isSystemDefault: Boolean = false,
    val sortOrder: Int = 0
)

enum class CustomerStatus(val label: String, val colorHex: String) {
    LEAD("Tiềm Năng", "#2563EB"),
    VIP("Khách hàng VIP", "#7C3AED"),
    CASUAL("Khách hàng Vãng Lai", "#F59E0B"),
    CUSTOMER("Khách hàng", "#10B981"),
    CLOSED("Đã chốt", "#10B981"),
    PARTNER("Đối tác", "#0EA5E9"),
    INACTIVE("Ngừng liên hệ", "#64748B");

    companion object {
        fun fromString(value: String): CustomerStatus {
            return when (value.trim().uppercase()) {
                "PROSPECT" -> LEAD
                "LEAD" -> LEAD
                "TIỀM NĂNG", "TIEM NANG" -> LEAD
                "VIP", "KHÁCH HÀNG VIP", "KHACH HANG VIP" -> VIP
                "CASUAL", "WALK_IN", "VÃNG LAI", "VANG LAI", "KHÁCH HÀNG VÃNG LAI", "KHACH HANG VANG LAI" -> CASUAL
                "CUSTOMER", "KHÁCH HÀNG", "KHACH HANG", "KHÁCH HÀNG HIỆN TẠI", "KHACH HANG HIEN TAI" -> CUSTOMER
                "WON", "CLOSED", "ĐÃ CHỐT", "DA CHOT" -> CLOSED
                "PARTNER", "ĐỐI TÁC", "DOI TAC" -> PARTNER
                "INACTIVE", "NGỪNG LIÊN HỆ", "NGUNG LIEN HE" -> INACTIVE
                else -> entries.find { it.name.equals(value, ignoreCase = true) || it.label.equals(value, ignoreCase = true) } ?: LEAD
            }
        }
    }
}

enum class DealStage(val label: String, val colorHex: String, val defaultProbability: Int) {
    LEAD("Tiếp cận mới", "#3B82F6", 20),
    PROPOSAL("Gửi báo giá", "#0EA5E9", 40),
    NEGOTIATION("Đàm phán", "#F59E0B", 70),
    WON("Chốt thành công", "#10B981", 100),
    LOST("Thất bại", "#EF4444", 0);

    companion object {
        fun fromString(value: String): DealStage {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: LEAD
        }
    }
}

enum class InteractionType(val label: String, val iconName: String) {
    CALL("Cuộc gọi", "call"),
    MEETING("Cuộc họp / Gặp gỡ", "group"),
    EMAIL("Gửi Email", "email"),
    MESSAGE("Tin nhắn / Zalo", "chat"),
    NOTE("Ghi chú tư vấn", "note"),
    CONTRACT("Ký kết hợp đồng", "description");

    companion object {
        fun fromString(value: String): InteractionType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: NOTE
        }
    }
}

enum class TaskPriority(val label: String, val colorHex: String) {
    HIGH("Ưu tiên cao", "#EF4444"),
    MEDIUM("Trung bình", "#F59E0B"),
    LOW("Thấp", "#10B981");

    companion object {
        fun fromString(value: String): TaskPriority {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: MEDIUM
        }
    }
}

enum class TaskType(val label: String, val isCalendarEvent: Boolean = false) {
    MEETING("Cuộc họp / Gặp mặt", true),
    CALL("Cuộc gọi hẹn trước", true),
    DEMO("Demo sản phẩm", true),
    CALL_BACK("Gọi lại chăm sóc", false),
    SEND_PROPOSAL("Gửi báo giá", false),
    FOLLOW_UP("Theo dõi sau bán hàng", false),
    CONTRACT("Ký kết hợp đồng", false);

    companion object {
        fun fromString(value: String): TaskType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: FOLLOW_UP
        }
    }
}

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val company: String = "",
    val position: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val status: String = CustomerStatus.LEAD.name,
    val source: String = "Giới thiệu",
    val tags: String = "",
    val estimatedValue: Double = 0.0,
    val notes: String = "",
    val avatarColorHex: String = "#2563EB",
    val isCorporate: Boolean = false,
    val taxCode: String = "",
    val taxAddress: String = "",
    val contactPerson: String = "",
    val dob: String = "",
    val progressPercent: Int = 70,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "deals",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["customerId"])]
)
data class DealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerId: Long,
    val title: String,
    val value: Double,
    val stage: String = DealStage.LEAD.name,
    val probability: Int = 20,
    val expectedCloseDate: Long = System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "interactions",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["customerId"])]
)
data class InteractionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerId: Long,
    val type: String = InteractionType.CALL.name,
    val title: String,
    val content: String,
    val date: Long = System.currentTimeMillis(),
    val outcome: String = "",
    val followUpDate: Long? = null,
    val hasReminder: Boolean = false,
    val reminderMinutesBefore: Int = 15,
    val ratingScore: Int = 0 // 1 to 5
)

@Entity(
    tableName = "tasks",
    indices = [Index(value = ["customerId"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerId: Long? = null,
    val title: String,
    val description: String = "",
    val dueDate: Long = System.currentTimeMillis() + 24L * 60 * 60 * 1000,
    val priority: String = TaskPriority.MEDIUM.name,
    val isCompleted: Boolean = false,
    val taskType: String = TaskType.FOLLOW_UP.name,
    val location: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val resultRating: Int = 0, // 1 to 5 stars (0 = chưa đánh giá)
    val resultSummary: String = "", // Báo cáo kết quả thực hiện
    val completedAt: Long? = null,
    val hasReminder: Boolean = true,
    val reminderMinutesBefore: Int = 15
)

// Combined UI models for rich display
data class CustomerWithDetails(
    val customer: CustomerEntity,
    val dealCount: Int = 0,
    val totalDealValue: Double = 0.0,
    val latestInteraction: InteractionEntity? = null,
    val pendingTasksCount: Int = 0
)

data class DealWithCustomer(
    val deal: DealEntity,
    val customerName: String,
    val company: String,
    val customerPhone: String
)

data class TaskWithCustomer(
    val task: TaskEntity,
    val customerName: String?,
    val company: String?,
    val customerPhone: String? = null,
    val assignee: String = "Tôi",
    val reminder: String = "15p"
)

data class InteractionWithCustomer(
    val interaction: InteractionEntity,
    val customerName: String,
    val company: String,
    val customerPhone: String? = null
)

// Quotes Model (Báo giá)
data class QuoteRevision(
    val version: Int,
    val title: String,
    val amount: Double,
    val dateStr: String,
    val notes: String = ""
)

data class QuoteProductItem(
    val id: Long = System.currentTimeMillis(),
    val name: String = "",
    val notes: String = "",
    val unit: String = "Gói",
    val quantity: Double = 1.0,
    val unitPrice: Double = 0.0,
    val vatPercent: Double = 10.0 // Thuế VAT: 0, 5, 8, 10%
) {
    val subtotal: Double get() = quantity * unitPrice
    val vatAmount: Double get() = subtotal * (vatPercent / 100.0)
    val totalWithVat: Double get() = subtotal + vatAmount
}

data class VatGroupSummary(
    val vatPercent: Double,
    val preVatAmount: Double,
    val vatAmount: Double,
    val postVatAmount: Double
)

data class QuoteItem(
    val id: Long = 0,
    val quoteNumber: String, // e.g. "BG-20231027-001"
    val title: String, // e.g. "Website Redesign"
    val amount: Double, // Giá trị trước thuế (Pre-VAT)
    val dateStr: String, // e.g. "27/10/2023"
    val status: String, // "Draft", "Sent", "Accepted"
    val customerName: String = "",
    val customerId: Long? = null,
    val category: String = "Chọn loại hàng", // e.g. "Thiết kế & Thi công", "Phần mềm", "Dịch vụ"
    val notes: String = "",
    val version: Int = 1,
    val revisions: List<QuoteRevision> = emptyList(),
    val contractId: Long? = null,
    val contractNumber: String? = null,
    val items: List<QuoteProductItem> = emptyList()
) {
    val totalPreVat: Double get() = if (items.isNotEmpty()) items.sumOf { it.subtotal } else amount
    val totalVat: Double get() = items.sumOf { it.vatAmount }
    val totalPostVat: Double get() = totalPreVat + totalVat

    fun getVatSummaries(): List<VatGroupSummary> {
        if (items.isEmpty()) return emptyList()
        return items.groupBy { it.vatPercent }
            .map { (vat, productList) ->
                val preVat = productList.sumOf { it.subtotal }
                val vatAmt = productList.sumOf { it.vatAmount }
                VatGroupSummary(
                    vatPercent = vat,
                    preVatAmount = preVat,
                    vatAmount = vatAmt,
                    postVatAmount = preVat + vatAmt
                )
            }
            .sortedBy { it.vatPercent } // Sắp xếp theo VAT tăng dần
    }
}

// Contract Management Models (Quản lý Hợp đồng & Phụ lục)
enum class ContractStatus(val label: String, val badgeColorHex: String, val badgeBgHex: String) {
    DRAFT("Dự thảo / Chưa ký", "#475569", "#F1F5F9"),
    SIGNED("Đã ký kết", "#047857", "#DEF7EC"),
    IN_PROGRESS("Đang thực hiện", "#1E429F", "#E1EFFE"),
    COMPLETED("Đã thanh lý", "#0E7490", "#CFFAFE"),
    CANCELLED("Đã hủy", "#E02424", "#FDE8E8")
}

data class AnnexItem(
    val id: Long = System.currentTimeMillis(),
    val name: String = "",
    val quantity: Double = 1.0,
    val unit: String = "Gói",
    val unitPrice: Double = 0.0
) {
    val totalPrice: Double get() = quantity * unitPrice
}

data class ContractAnnex(
    val id: Long = System.currentTimeMillis(),
    val contractId: Long = 0,
    val annexNumber: String = "",
    val title: String = "",
    val dateStr: String = "",
    val notes: String = "",
    val increaseItems: List<AnnexItem> = emptyList(), // Bảng tăng sản phẩm / đơn giá (Màu xanh)
    val decreaseItems: List<AnnexItem> = emptyList(), // Bảng giảm sản phẩm / đơn giá (Màu đỏ)
    val paymentTerms: String = "",
    val deliveryTerms: String = "",
    val warrantyTerms: String = "",
    val isSigned: Boolean = true
) {
    val totalIncrease: Double get() = increaseItems.sumOf { it.totalPrice }
    val totalDecrease: Double get() = decreaseItems.sumOf { it.totalPrice }
    val netChange: Double get() = totalIncrease - totalDecrease
}

data class ContractItem(
    val id: Long = 0,
    val quoteId: Long? = null,
    val contractNumber: String = "",
    val title: String = "",
    val customerName: String = "",
    val customerId: Long? = null,
    val originalAmount: Double = 0.0,
    val signedDate: String = "",
    val status: ContractStatus = ContractStatus.SIGNED,
    val paymentTerms: String = "• Đợt 1: Tạm ứng 30% ngay sau khi ký hợp đồng.\n• Đợt 2: Thanh toán 40% khi hoàn thành 50% khối lượng công việc.\n• Đợt 3: Quyết toán 30% còn lại trong vòng 07 ngày kể từ khi ký biên bản nghiệm thu.",
    val deliveryTerms: String = "• Thời gian triển khai & bàn giao: Trong vòng 30 ngày làm việc kể từ ngày nhận đủ tiền tạm ứng đợt 1.\n• Địa điểm bàn giao: Trực tiếp tại trụ sở Bên Mua hoặc qua hệ thống số.",
    val warrantyTerms: String = "• Thời gian bảo hành: 12 tháng kể từ ngày ký biên bản nghiệm thu.\n• Hỗ trợ kỹ thuật và phản hồi khắc phục sự cố trong vòng 04 giờ làm việc.",
    val annexes: List<ContractAnnex> = emptyList(),
    val notes: String = "",
    val items: List<QuoteProductItem> = emptyList()
) {
    val totalAnnexAdjustment: Double get() = annexes.sumOf { it.netChange }
    val currentAmount: Double get() = originalAmount + totalAnnexAdjustment
}

enum class AnnexPosition(val label: String) {
    PREFIX("Tiền tố (Đầu số HĐ)"),
    SUFFIX("Hậu tố (Cuối số HĐ)")
}

data class ContractNamingRule(
    val prefix: String = "HĐ-", // Tiền tố (có hoặc không có đều được)
    val suffix: String = "", // Hậu tố (có hoặc không có đều được)
    val digitsCount: Int = 3, // Số thứ tự (độ dài số: 2 -> 01, 3 -> 001)
    val resetMonthly: Boolean = true, // Reset số thứ tự theo tháng
    val monthFormatPattern: String = "yyyyMM", // Khách hàng tự đặt: yy/MM, yyyyMM, yyyy-MM, MMyyyy, MM/yy, yyyy,...
    val monthFormatInPrefix: Boolean = true, // Chèn định dạng tháng/năm vào tiền tố
    val monthFormatInSuffix: Boolean = false, // Chèn định dạng tháng/năm vào hậu tố
    val annexPrefix: String = "PL",
    val annexSuffix: String = "",
    val annexPosition: AnnexPosition = AnnexPosition.PREFIX, // Tùy chọn chèn ở tiền tố hoặc hậu tố
    val annexSeparator: String = "/" // Dấu phân cách: /, -, _
) {
    fun formatDateWithPattern(pattern: String, targetDate: java.util.Date = java.util.Date()): String {
        return try {
            val sdf = java.text.SimpleDateFormat(pattern.ifBlank { "yyyyMM" }, java.util.Locale.getDefault())
            sdf.format(targetDate)
        } catch (e: Exception) {
            val sdf = java.text.SimpleDateFormat("yyyyMM", java.util.Locale.getDefault())
            sdf.format(targetDate)
        }
    }

    fun generateNextContractNumber(existingContracts: List<ContractItem>, targetDate: java.util.Date = java.util.Date()): String {
        val formattedDate = formatDateWithPattern(monthFormatPattern, targetDate)

        val count = if (resetMonthly) {
            existingContracts.count {
                it.contractNumber.contains(formattedDate)
            } + 1
        } else {
            existingContracts.size + 1
        }

        val seqFormatted = String.format(java.util.Locale.getDefault(), "%0${digitsCount.coerceIn(1, 8)}d", count)
        
        val actualPrefix = if (monthFormatInPrefix && resetMonthly) {
            val base = prefix
            if (base.isBlank()) "$formattedDate-"
            else if (base.endsWith("-") || base.endsWith("/")) "$base$formattedDate-"
            else "$base-$formattedDate-"
        } else {
            prefix
        }

        val actualSuffix = if (monthFormatInSuffix && resetMonthly) {
            val base = suffix
            if (base.isNotBlank()) "-$formattedDate$base" else "-$formattedDate"
        } else {
            suffix
        }

        return "$actualPrefix$seqFormatted$actualSuffix"
    }

    fun generateNextAnnexNumber(contract: ContractItem, targetDate: java.util.Date = java.util.Date()): String {
        val nextSeq = contract.annexes.size + 1
        val seqFormatted = String.format(java.util.Locale.getDefault(), "%02d", nextSeq)
        val annexCode = "$annexPrefix$seqFormatted$annexSuffix"
        val sep = annexSeparator.ifBlank { "/" }

        return if (annexPosition == AnnexPosition.PREFIX) {
            // Chèn ở tiền tố: PL01/HĐ-202609-001
            "$annexCode$sep${contract.contractNumber}"
        } else {
            // Chèn ở hậu tố: HĐ-202609-001/PL01
            "${contract.contractNumber}$sep$annexCode"
        }
    }
}

// Project Progress & Milestone steps (Tiến độ dự án)
enum class ProjectStatusType(val label: String, val colorHex: String, val bgHex: String) {
    ON_TRACK("Đúng tiến độ", "#047857", "#DEF7EC"),
    NEARING("Sắp tới hạn", "#D97706", "#FEF3C7"),
    DELAYED("Chậm trễ", "#E02424", "#FDE8E8"),
    COMPLETED("Hoàn thành", "#059669", "#ECFDF5")
}

enum class StepStatus {
    COMPLETED,
    WARNING,
    PENDING,
    OVERDUE
}

data class ProjectStep(
    val id: Long,
    val title: String,
    val status: StepStatus,
    val dateLabel: String,
    val isHighlighted: Boolean = false,
    val customWeightPercent: Int? = null // Người dùng tự cài % hoặc null để tự chia đều
)

data class ProjectProgressItem(
    val id: Long,
    val quoteId: Long? = null,
    val title: String,
    val customerName: String,
    val statusType: ProjectStatusType,
    val progressPercent: Int = 0,
    val warningNote: String? = null,
    val steps: List<ProjectStep> = emptyList()
) {
    val totalConfiguredWeight: Int get() = steps.sumOf { it.customWeightPercent ?: 0 }
    val isWeightExceeded: Boolean get() = totalConfiguredWeight > 100

    fun isStepCompletable(stepId: Long): Boolean {
        val index = steps.indexOfFirst { it.id == stepId }
        if (index <= 0) return true
        return steps.subList(0, index).all { it.status == StepStatus.COMPLETED }
    }

    // Calculate progress based on configured weights or even distribution
    fun calculateCalculatedProgress(): Int {
        if (steps.isEmpty()) return 0
        val totalConfigured = steps.sumOf { it.customWeightPercent ?: 0 }
        if (totalConfigured > 0 && steps.all { it.customWeightPercent != null }) {
            // All steps have custom weights
            val completedSum = steps.filter { it.status == StepStatus.COMPLETED }.sumOf { it.customWeightPercent ?: 0 }
            return completedSum.coerceIn(0, 100)
        } else {
            // Default even distribution: 100% / total steps
            val completedCount = steps.count { it.status == StepStatus.COMPLETED }
            val percentPerStep = 100f / steps.size
            return (completedCount * percentPerStep).toInt().coerceIn(0, 100)
        }
    }
}

// Employee / Team model (Quản lý nhân viên)
data class EmployeeItem(
    val id: Long,
    val name: String,
    val role: String,
    val department: String = "Phòng Kinh doanh",
    val status: String = "Đang làm việc", // "Đang làm việc", "Nghỉ phép", "Nghỉ việc"
    val initials: String = "NV",
    val isWorking: Boolean = true,
    val phone: String = "0901234567",
    val email: String = "employee@example.com",
    val startDate: String = "15/06/2019", // Ngày bắt đầu làm việc để tính thâm niên
    val baseSalary: Double = 15000000.0, // Lương cơ bản tháng
    val allowance: Double = 1500000.0, // Phụ cấp
    val kpiBonus: Double = 3000000.0 // Thưởng KPI
)

// Timekeeping & Attendance Enums and Models
enum class AttendanceType(val label: String, val shortCode: String, val workDayCredit: Float) {
    FULL_WORK("Làm việc (Trọn ngày)", "LV", 1.0f),
    HALF_LEAVE("Nghỉ phép (Nửa ngày)", "NP 1/2", 0.5f),
    FULL_LEAVE("Nghỉ phép (Trọn ngày)", "NP", 1.0f),
    UNPAID_LEAVE("Nghỉ không lương", "NKL", 0.0f),
    HOLIDAY_LEAVE("Nghỉ lễ", "NL", 1.0f),
    OVERTIME("Tăng ca (OT)", "TC", 1.0f)
}

enum class OvertimeRateType(val label: String, val multiplier: Float) {
    WEEKDAY("Ngày thường (x1.5)", 1.5f),
    WEEKEND("Cuối tuần / T7-CN (x2.0)", 2.0f),
    HOLIDAY("Ngày lễ, Tết (x3.0)", 3.0f)
}

data class AttendanceRecord(
    val id: Long = System.currentTimeMillis(),
    val employeeId: Long,
    val date: String, // "YYYY-MM-DD"
    val type: AttendanceType = AttendanceType.FULL_WORK,
    val workHours: Float = 8.0f, // Chuẩn 8h/ngày
    val overtimeHours: Float = 0.0f, // Số giờ tăng ca
    val overtimeRateType: OvertimeRateType = OvertimeRateType.WEEKDAY,
    val note: String = ""
)

// Seniority Milestone Model
data class SeniorityMilestone(
    val id: Long = System.currentTimeMillis(),
    val years: Int, // Mốc số năm (ví dụ: 1, 2, 3, 5, 10, 15, 20)
    val bonusAmount: Double, // Mức thưởng tiền mặt (VNĐ)
    val extraLeaveDays: Int = 0, // Số ngày phép cộng thêm riêng tại mốc này
    val title: String = "Mốc $years năm", // Tên danh hiệu mốc
    val description: String = ""
)

val defaultSeniorityMilestones = listOf(
    SeniorityMilestone(id = 1, years = 1, bonusAmount = 1000000.0, extraLeaveDays = 0, title = "Mốc 1 năm", description = "Hoàn thành 1 năm gắn bó & cống hiến"),
    SeniorityMilestone(id = 2, years = 3, bonusAmount = 3000000.0, extraLeaveDays = 0, title = "Mốc 3 năm", description = "Tri ân 3 năm gắn bó"),
    SeniorityMilestone(id = 3, years = 5, bonusAmount = 5000000.0, extraLeaveDays = 1, title = "Mốc 5 năm (Vàng)", description = "Huy hiệu 5 năm & +1 ngày phép thâm niên"),
    SeniorityMilestone(id = 4, years = 10, bonusAmount = 10000000.0, extraLeaveDays = 2, title = "Mốc 10 năm (Kim Cương)", description = "Vinh danh 10 năm & +2 ngày phép tích lũy")
)

// Payroll Policy & Seniority Settings
data class PayrollPolicySettings(
    val standardWorkDays: Int = 26, // 26 ngày/tháng
    val standardHoursPerDay: Float = 8.0f, // 8h/ngày
    val otRateWeekday: Float = 1.5f,
    val otRateWeekend: Float = 2.0f,
    val otRateHoliday: Float = 3.0f,
    // Seniority (Thâm niên) Settings
    val baseLeaveDaysPerYear: Int = 12, // Mặc định 12 ngày phép năm
    val leaveAccrualIntervalYears: Int = 5, // Cứ mỗi X năm làm việc (Mặc định 5 năm theo luật LĐ)
    val leaveDaysPerInterval: Int = 1, // Tăng thêm Y ngày phép mỗi chu kỳ
    val maxLeaveDays: Int = 20, // Số ngày phép tối đa (0 = không giới hạn)
    val enableMilestoneBonuses: Boolean = true, // Bật/tắt thưởng theo mốc
    val milestones: List<SeniorityMilestone> = defaultSeniorityMilestones,
    // Recurring bonus after max milestone
    val enableRecurringBonusAfterMax: Boolean = true,
    val recurringIntervalYears: Int = 1, // Cứ mỗi N năm sau mốc lớn nhất
    val recurringBonusPerInterval: Double = 1000000.0, // Thưởng thêm mỗi chu kỳ
    // Legacy support fields for backwards compatibility
    val seniorityBonus5Years: Double = 5000000.0,
    val enableSeniorityBonus5Years: Boolean = true,
    val seniorityIntervalYears: Int = 1,
    val seniorityBonusPerInterval: Double = 1000000.0,
    val leaveDaysPer5Years: Int = 1,
    // KPI
    val defaultKpiBonus: Double = 3000000.0,
    val enableKpiBonus: Boolean = true
)

data class SeniorityResult(
    val years: Int,
    val months: Int,
    val totalMonths: Int,
    val extraLeaveDays: Int,
    val totalLeaveDays: Int,
    val bonusAmount: Double,
    val bonusDescription: String,
    val currentMilestone: SeniorityMilestone? = null,
    val nextMilestone: SeniorityMilestone? = null,
    val monthsToNextMilestone: Int = 0
)

data class PayrollCalculationResult(
    val employee: EmployeeItem,
    val seniorityResult: SeniorityResult,
    val standardDays: Int,
    val actualWorkDays: Float,
    val paidLeaveDays: Float,
    val unpaidLeaveDays: Float,
    val holidayDays: Float,
    val totalOvertimeHours: Float,
    val actualWorkSalary: Double,
    val overtimeSalary: Double,
    val seniorityBonus: Double,
    val kpiBonus: Double,
    val allowance: Double,
    val totalSalary: Double
)

fun calculateSeniority(startDateStr: String, policy: PayrollPolicySettings): SeniorityResult {
    var totalMonths = 0
    try {
        val parts = startDateStr.split("/", "-")
        if (parts.size >= 3) {
            val (day, month, year) = if (parts[0].length == 4) {
                // YYYY-MM-DD
                Triple(parts[2].toIntOrNull() ?: 1, parts[1].toIntOrNull() ?: 1, parts[0].toIntOrNull() ?: 2020)
            } else {
                // DD/MM/YYYY
                Triple(parts[0].toIntOrNull() ?: 1, parts[1].toIntOrNull() ?: 1, parts[2].toIntOrNull() ?: 2020)
            }
            val currentYear = 2026
            val currentMonth = 8
            totalMonths = ((currentYear - year) * 12 + (currentMonth - month)).coerceAtLeast(0)
        }
    } catch (_: Exception) {
        totalMonths = 24
    }

    val years = totalMonths / 12
    val months = totalMonths % 12

    // Calculate extra leave days based on accrual interval (e.g. +1 day per 5 years)
    val intervalYears = if (policy.leaveAccrualIntervalYears > 0) policy.leaveAccrualIntervalYears else 5
    val intervalLeaveDays = (years / intervalYears) * policy.leaveDaysPerInterval

    // Check milestones
    val sortedMilestones = policy.milestones.sortedBy { it.years }
    val achievedMilestones = sortedMilestones.filter { it.years <= years }
    val currentMilestone = achievedMilestones.lastOrNull()
    val nextMilestone = sortedMilestones.firstOrNull { it.years > years }
    val monthsToNext = if (nextMilestone != null) {
        (nextMilestone.years * 12 - totalMonths).coerceAtLeast(0)
    } else 0

    // Milestone extra leave days if any
    val milestoneExtraLeave = currentMilestone?.extraLeaveDays ?: 0
    val totalExtraLeave = intervalLeaveDays + milestoneExtraLeave
    val rawTotalLeave = policy.baseLeaveDaysPerYear + totalExtraLeave
    val totalLeaveDays = if (policy.maxLeaveDays > 0) rawTotalLeave.coerceAtMost(policy.maxLeaveDays) else rawTotalLeave

    var bonusAmount = 0.0
    var bonusDesc = "Chưa đạt mốc thâm niên"

    if (policy.enableMilestoneBonuses && currentMilestone != null) {
        val baseBonus = currentMilestone.bonusAmount
        val maxMilestone = sortedMilestones.lastOrNull()
        var recurringBonus = 0.0

        if (policy.enableRecurringBonusAfterMax && maxMilestone != null && years > maxMilestone.years) {
            val postMaxYears = years - maxMilestone.years
            val recInterval = if (policy.recurringIntervalYears > 0) policy.recurringIntervalYears else 1
            val recurringCycles = postMaxYears / recInterval
            recurringBonus = recurringCycles * policy.recurringBonusPerInterval
        }

        bonusAmount = baseBonus + recurringBonus
        bonusDesc = if (recurringBonus > 0) {
            "${currentMilestone.title} (+${String.format("%,d đ", recurringBonus.toLong())} chu kỳ vượt mốc)"
        } else {
            "${currentMilestone.title} - ${String.format("%,d đ", bonusAmount.toLong())}"
        }
    } else if (nextMilestone != null) {
        bonusDesc = "Còn $monthsToNext tháng đến ${nextMilestone.title}"
    }

    return SeniorityResult(
        years = years,
        months = months,
        totalMonths = totalMonths,
        extraLeaveDays = totalExtraLeave,
        totalLeaveDays = totalLeaveDays,
        bonusAmount = bonusAmount,
        bonusDescription = bonusDesc,
        currentMilestone = currentMilestone,
        nextMilestone = nextMilestone,
        monthsToNextMilestone = monthsToNext
    )
}

fun calculateEmployeePayroll(
    employee: EmployeeItem,
    records: List<AttendanceRecord>,
    policy: PayrollPolicySettings,
    monthStr: String = ""
): PayrollCalculationResult {
    val empRecords = records.filter { it.employeeId == employee.id }
    val seniority = calculateSeniority(employee.startDate, policy)

    var actualWorkDays = 0.0f
    var paidLeaveDays = 0.0f
    var unpaidLeaveDays = 0.0f
    var holidayDays = 0.0f
    var totalOvertimeHours = 0.0f
    var overtimeSalary = 0.0

    val standardDailySalary = employee.baseSalary / policy.standardWorkDays.coerceAtLeast(1)
    val standardHourlySalary = standardDailySalary / policy.standardHoursPerDay.coerceAtLeast(1.0f)

    if (empRecords.isEmpty()) {
        // Default to standard days if no records logged yet for demo
        actualWorkDays = policy.standardWorkDays.toFloat()
    } else {
        empRecords.forEach { record ->
            when (record.type) {
                AttendanceType.FULL_WORK -> actualWorkDays += 1.0f
                AttendanceType.HALF_LEAVE -> {
                    actualWorkDays += 0.5f
                    paidLeaveDays += 0.5f
                }
                AttendanceType.FULL_LEAVE -> paidLeaveDays += 1.0f
                AttendanceType.UNPAID_LEAVE -> unpaidLeaveDays += 1.0f
                AttendanceType.HOLIDAY_LEAVE -> {
                    holidayDays += 1.0f
                    actualWorkDays += 1.0f // Holiday is paid
                }
                AttendanceType.OVERTIME -> actualWorkDays += 1.0f
            }

            if (record.overtimeHours > 0) {
                totalOvertimeHours += record.overtimeHours
                val multiplier = when (record.overtimeRateType) {
                    OvertimeRateType.WEEKDAY -> policy.otRateWeekday
                    OvertimeRateType.WEEKEND -> policy.otRateWeekend
                    OvertimeRateType.HOLIDAY -> policy.otRateHoliday
                }
                overtimeSalary += (record.overtimeHours * standardHourlySalary * multiplier)
            }
        }
    }

    val actualWorkSalary = (actualWorkDays + paidLeaveDays) * standardDailySalary
    val seniorityBonus = seniority.bonusAmount
    val kpiBonus = if (policy.enableKpiBonus) employee.kpiBonus else 0.0
    val allowance = employee.allowance
    val totalSalary = actualWorkSalary + overtimeSalary + seniorityBonus + kpiBonus + allowance

    return PayrollCalculationResult(
        employee = employee,
        seniorityResult = seniority,
        standardDays = policy.standardWorkDays,
        actualWorkDays = actualWorkDays,
        paidLeaveDays = paidLeaveDays,
        unpaidLeaveDays = unpaidLeaveDays,
        holidayDays = holidayDays,
        totalOvertimeHours = totalOvertimeHours,
        actualWorkSalary = actualWorkSalary,
        overtimeSalary = overtimeSalary,
        seniorityBonus = seniorityBonus,
        kpiBonus = kpiBonus,
        allowance = allowance,
        totalSalary = totalSalary
    )
}

enum class AccountTier(val displayName: String, val badge: String) {
    FREE("Tài khoản Miễn phí", "FREE"),
    VIP("Tài khoản VIP", "VIP"),
    BUSINESS("Tài khoản BUSINESS", "BUSINESS");

    val isFree: Boolean get() = this == FREE
    val isVipOrHigher: Boolean get() = this == VIP || this == BUSINESS
    val isBusiness: Boolean get() = this == BUSINESS
}

// User Profile model
data class UserProfile(
    val fullName: String = "Quản Trị Viên VIP",
    val email: String = "admin@crm.vn",
    val phone: String = "901234567",
    val dob: String = "01/01/1990",
    val address: String = "123 Đường Lê Lợi, Quận 1, TP.HCM",
    val role: String = "VIP ENTERPRISE",
    val avatarUrl: String? = null,
    val isVip: Boolean = true,
    val accountTier: AccountTier = AccountTier.VIP
)

// Email Report Schedule model
data class EmailReportSchedule(
    // Fiscal year start configuration
    val fiscalStartDay: Int = 1,          // 1..31
    val fiscalStartMonth: Int = 1,        // 1..12 (1 = Tháng 1)

    // Weekly reports: choose days of the week (2 = T2, 3 = T3, ..., 7 = T7, 1 = CN)
    val enableWeeklyReport: Boolean = true,
    val weeklyDays: Set<Int> = setOf(2),  // Mặc định Thứ Hai

    // Monthly reports: Start of month, End of month
    val enableMonthlyReport: Boolean = false,
    val monthlyStart: Boolean = false,    // Ngày đầu mỗi tháng tài chính
    val monthlyEnd: Boolean = true,       // Ngày cuối mỗi tháng tài chính

    // Quarterly reports: Start of quarter, End of quarter
    val enableQuarterlyReport: Boolean = false,
    val quarterlyStart: Boolean = false,  // Ngày đầu mỗi quý tài chính
    val quarterlyEnd: Boolean = true,     // Ngày cuối mỗi quý tài chính

    // Semi-annual (6 months / Giữa năm) reports: Start, End
    val enableSemiAnnualReport: Boolean = false,
    val semiAnnualStart: Boolean = false, // Ngày đầu kỳ 6 tháng tài chính
    val semiAnnualEnd: Boolean = true,    // Ngày cuối kỳ 6 tháng tài chính

    // Annual reports (Năm tài chính): Start, End
    val enableAnnualReport: Boolean = false,
    val annualStart: Boolean = false,     // Ngày đầu năm tài chính
    val annualEnd: Boolean = true         // Ngày cuối năm tài chính
) {
    /**
     * Helper to compute fiscal year string label based on current reference year
     * e.g. If starts 01/01/2026 -> ends 31/12/2026 -> "Năm tài chính 2026"
     * e.g. If starts 01/02/2026 -> ends 31/01/2027 -> "Năm tài chính 2026 - 2027"
     */
    fun getFiscalYearLabel(currentYear: Int = 2026): String {
        return if (fiscalStartMonth == 1 && fiscalStartDay == 1) {
            "Năm tài chính $currentYear"
        } else {
            "Năm tài chính $currentYear - ${currentYear + 1}"
        }
    }

    /**
     * Helper to format the fiscal period dates (e.g. "01/02/2026 - 31/01/2027")
     */
    fun getFiscalYearRangeString(currentYear: Int = 2026): String {
        val startDayStr = fiscalStartDay.toString().padStart(2, '0')
        val startMonthStr = fiscalStartMonth.toString().padStart(2, '0')
        val startDateStr = "$startDayStr/$startMonthStr/$currentYear"

        // End date is 1 day before start of next year
        val cal = java.util.Calendar.getInstance()
        cal.set(currentYear + 1, fiscalStartMonth - 1, fiscalStartDay)
        cal.add(java.util.Calendar.DAY_OF_MONTH, -1)
        val endDayStr = cal.get(java.util.Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
        val endMonthStr = (cal.get(java.util.Calendar.MONTH) + 1).toString().padStart(2, '0')
        val endYearStr = cal.get(java.util.Calendar.YEAR)

        return "$startDateStr - $endDayStr/$endMonthStr/$endYearStr"
    }
}

// Notification Settings model
data class NotificationSettings(
    // 1. Basic Channels & Free Tier
    val systemNotice: Boolean = true,             // Thông báo ứng dụng (Push) [FREE]
    val soundEnabled: Boolean = true,             // Âm thanh chuông báo [FREE]
    val vibrateEnabled: Boolean = true,           // Rung phản hồi [FREE]
    val deadlineReminder: Boolean = true,         // Nhắc nhở hạn chót Deadline [FREE]

    // 2. VIP Tier Features
    val weeklyReportEmail: Boolean = false,        // Email tổng kết & Báo cáo [VIP]
    val emailReportSchedule: EmailReportSchedule = EmailReportSchedule(),
    val enableQuietHours: Boolean = false,        // Chế độ yên tĩnh ban đêm (DND) [VIP]
    val quietHourStart: String = "22:00",
    val quietHourEnd: String = "07:00",
    val allowVipOverride: Boolean = true,
    val paymentReminder: Boolean = true,          // Đợt thanh toán & Thu hồi công nợ [VIP]

    // 3. BUSINESS Tier Features
    val newTask: Boolean = true,                  // Nhiệm vụ & Công việc mới [BUSINESS]
    val customerUpdate: Boolean = false,          // Cập nhật hồ sơ khách hàng [BUSINESS]
    val quoteApproval: Boolean = true             // Báo giá & Duyệt hợp đồng [BUSINESS]
)

// Security Settings model
data class SecuritySettings(
    val twoFactorAuth: Boolean = true,
    val biometricAuth: Boolean = false
)

// Login Session Item
data class LoginDeviceSession(
    val id: Long,
    val deviceName: String,
    val locationAndIp: String,
    val isCurrent: Boolean = false
)

