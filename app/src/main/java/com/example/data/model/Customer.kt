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

data class QuoteItem(
    val id: Long = 0,
    val quoteNumber: String, // e.g. "BG-20231027-001"
    val title: String, // e.g. "Website Redesign"
    val amount: Double, // e.g. 125000000.0
    val dateStr: String, // e.g. "27/10/2023"
    val status: String, // "Draft", "Sent", "Accepted"
    val customerName: String = "",
    val customerId: Long? = null,
    val category: String = "Chọn loại hàng", // e.g. "Thiết kế & Thi công", "Phần mềm", "Dịch vụ"
    val notes: String = "",
    val version: Int = 1,
    val revisions: List<QuoteRevision> = emptyList()
)

// Project Progress & Milestone steps (Tiến độ dự án)
enum class ProjectStatusType(val label: String, val colorHex: String, val bgHex: String) {
    ON_TRACK("Đúng tiến độ", "#047857", "#DEF7EC"),
    NEARING("Sắp tới hạn", "#D97706", "#FEF3C7"),
    DELAYED("Chậm trễ", "#E02424", "#FDE8E8")
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

// User Profile model
data class UserProfile(
    val fullName: String = "Quản Trị Viên VIP",
    val email: String = "admin@crm.vn",
    val phone: String = "901234567",
    val dob: String = "01/01/1990",
    val address: String = "123 Đường Lê Lợi, Quận 1, TP.HCM",
    val role: String = "VIP ENTERPRISE",
    val avatarUrl: String? = null,
    val isVip: Boolean = true
)

// Notification Settings model
data class NotificationSettings(
    val newTask: Boolean = true,
    val deadlineReminder: Boolean = true,
    val customerUpdate: Boolean = false,
    val systemNotice: Boolean = true,
    val weeklyReportEmail: Boolean = false
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

