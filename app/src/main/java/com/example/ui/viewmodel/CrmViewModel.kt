package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AttendanceRecord
import com.example.data.model.AttendanceType
import com.example.data.model.CustomerEntity
import com.example.data.model.CustomerStatus
import com.example.data.model.CustomerTypeEntity
import com.example.data.model.CustomerWithDetails
import com.example.data.model.DealEntity
import com.example.data.model.DealStage
import com.example.data.model.DealWithCustomer
import com.example.data.model.EmployeeItem
import com.example.data.model.InteractionEntity
import com.example.data.model.InteractionWithCustomer
import com.example.data.model.NotificationSettings
import com.example.data.model.OvertimeRateType
import com.example.data.model.PayrollPolicySettings
import com.example.data.model.ProjectProgressItem
import com.example.data.model.ProjectStatusType
import com.example.data.model.ProjectStep
import com.example.data.model.QuoteItem
import com.example.data.model.QuoteRevision
import com.example.data.model.SecuritySettings
import com.example.data.model.StepStatus
import com.example.data.model.TaskEntity
import com.example.data.model.TaskWithCustomer
import com.example.data.model.UserProfile
import com.example.data.repository.CrmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

enum class CustomerSort(val label: String) {
    RECENT("Mới cập nhật"),
    CREATED_DESC("Mới tạo gần đây"),
    NAME_ASC("Tên khách hàng (A - Z)"),
    NAME_DESC("Tên khách hàng (Z - A)"),
    DEAL_VALUE_DESC("Giá trị dự kiến cao nhất"),
    DEAL_COUNT_DESC("Nhiều cơ hội kinh doanh nhất")
}

enum class TimeRangeFilter(val label: String) {
    ALL("Tất cả thời gian"),
    TODAY("Hôm nay"),
    LAST_7_DAYS("7 ngày qua"),
    LAST_30_DAYS("30 ngày qua"),
    THIS_MONTH("Tháng này")
}

enum class ValueRangeFilter(val label: String, val min: Double, val max: Double) {
    ALL("Tất cả mức giá trị", 0.0, Double.MAX_VALUE),
    UNDER_50M("Dưới 50 Triệu", 0.0, 50_000_000.0),
    FROM_50M_TO_200M("50 Tr - 200 Triệu", 50_000_000.0, 200_000_000.0),
    OVER_200M("Trên 200 Triệu", 200_000_000.0, Double.MAX_VALUE)
}

data class DashboardMetrics(
    val totalCustomers: Int = 0,
    val vipCustomers: Int = 0,
    val currentCustomers: Int = 0,
    val partnerCustomers: Int = 0,
    val leadCustomers: Int = 0,
    val totalWonRevenue: Double = 0.0,
    val totalPipelineValue: Double = 0.0,
    val winRatePercent: Int = 0,
    val pendingTasksCount: Int = 0,
    val todayTasksCount: Int = 0,
    val totalDealsCount: Int = 0,
    val wonDealsCount: Int = 0
)

class CrmViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = CrmRepository(
        database.customerDao(),
        database.customerTypeDao(),
        database.dealDao(),
        database.interactionDao(),
        database.taskDao()
    )

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // Customer Types (customizable classification by user)
    val allCustomerTypes: StateFlow<List<CustomerTypeEntity>> = repository.allCustomerTypes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Search and filter states for Customers
    private val _customerSearchQuery = MutableStateFlow("")
    val customerSearchQuery: StateFlow<String> = _customerSearchQuery.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow<CustomerStatus?>(null)
    val selectedStatusFilter: StateFlow<CustomerStatus?> = _selectedStatusFilter.asStateFlow()

    private val _selectedCustomTypeFilter = MutableStateFlow<String?>(null)
    val selectedCustomTypeFilter: StateFlow<String?> = _selectedCustomTypeFilter.asStateFlow()

    private val _selectedTimeRangeFilter = MutableStateFlow(TimeRangeFilter.ALL)
    val selectedTimeRangeFilter: StateFlow<TimeRangeFilter> = _selectedTimeRangeFilter.asStateFlow()

    private val _selectedValueRangeFilter = MutableStateFlow(ValueRangeFilter.ALL)
    val selectedValueRangeFilter: StateFlow<ValueRangeFilter> = _selectedValueRangeFilter.asStateFlow()

    private val _selectedSourceFilter = MutableStateFlow<String?>(null)
    val selectedSourceFilter: StateFlow<String?> = _selectedSourceFilter.asStateFlow()

    private val _customerSort = MutableStateFlow(CustomerSort.RECENT)
    val customerSort: StateFlow<CustomerSort> = _customerSort.asStateFlow()

    // Active customer filter count
    val activeCustomerFilterCount: StateFlow<Int> = combine(
        _selectedStatusFilter,
        _selectedCustomTypeFilter,
        _selectedTimeRangeFilter,
        _selectedValueRangeFilter,
        _selectedSourceFilter
    ) { status, customType, time, value, source ->
        var count = 0
        if (status != null || !customType.isNullOrBlank()) count++
        if (time != TimeRangeFilter.ALL) count++
        if (value != ValueRangeFilter.ALL) count++
        if (!source.isNullOrBlank()) count++
        count
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    data class CustomerFilterCriteria(
        val query: String = "",
        val status: CustomerStatus? = null,
        val customType: String? = null,
        val timeRange: TimeRangeFilter = TimeRangeFilter.ALL,
        val valueRange: ValueRangeFilter = ValueRangeFilter.ALL,
        val source: String? = null,
        val sort: CustomerSort = CustomerSort.RECENT
    )

    private val filterCriteria1 = combine(
        _customerSearchQuery,
        _selectedStatusFilter,
        _selectedCustomTypeFilter,
        _selectedTimeRangeFilter
    ) { query, status, customType, timeRange ->
        listOf(query, status, customType, timeRange)
    }

    private val filterCriteria2 = combine(
        _selectedValueRangeFilter,
        _selectedSourceFilter,
        _customerSort
    ) { valueRange, source, sort ->
        Triple(valueRange, source, sort)
    }

    private val filterCriteria = combine(
        filterCriteria1,
        filterCriteria2
    ) { part1, (valueRange, source, sort) ->
        CustomerFilterCriteria(
            query = part1[0] as String,
            status = part1[1] as? CustomerStatus,
            customType = part1[2] as? String,
            timeRange = part1[3] as TimeRangeFilter,
            valueRange = valueRange,
            source = source,
            sort = sort
        )
    }

    private fun String.removeVietnameseAccents(): String {
        val temp = java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
        val pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(temp).replaceAll("")
            .replace('đ', 'd').replace('Đ', 'D')
    }

    private fun textMatchesQuery(target: String, query: String): Boolean {
        if (query.isBlank()) return true
        val t = target.lowercase()
        val q = query.trim().lowercase()
        return t.contains(q) || t.removeVietnameseAccents().contains(q.removeVietnameseAccents())
    }

    // Filtered Customers Flow with Comprehensive Multi-Criteria Search & Filter
    val customersWithDetails: StateFlow<List<CustomerWithDetails>> = combine(
        repository.customersWithDetails,
        filterCriteria
    ) { list, criteria ->
        var filtered = list
        val now = System.currentTimeMillis()

        // 1. Classification / Status filter
        if (criteria.status != null) {
            filtered = filtered.filter { 
                CustomerStatus.fromString(it.customer.status) == criteria.status ||
                it.customer.status.equals(criteria.status.name, ignoreCase = true) ||
                it.customer.status.equals(criteria.status.label, ignoreCase = true) ||
                it.customer.status.contains(criteria.status.label, ignoreCase = true)
            }
        } else if (!criteria.customType.isNullOrBlank()) {
            val typeStr = criteria.customType.trim()
            filtered = filtered.filter { 
                it.customer.status.equals(typeStr, ignoreCase = true) ||
                it.customer.status.contains(typeStr, ignoreCase = true) ||
                it.customer.tags.contains(typeStr, ignoreCase = true)
            }
        }

        // 2. Source filter
        if (!criteria.source.isNullOrBlank()) {
            filtered = filtered.filter { it.customer.source.contains(criteria.source, ignoreCase = true) }
        }

        // 3. Time range filter (by creation date or updated date)
        if (criteria.timeRange != TimeRangeFilter.ALL) {
            val cal = Calendar.getInstance()
            when (criteria.timeRange) {
                TimeRangeFilter.TODAY -> {
                    cal.set(Calendar.HOUR_OF_DAY, 0)
                    cal.set(Calendar.MINUTE, 0)
                    cal.set(Calendar.SECOND, 0)
                    val startOfDay = cal.timeInMillis
                    filtered = filtered.filter { it.customer.createdAt >= startOfDay || it.customer.updatedAt >= startOfDay }
                }
                TimeRangeFilter.LAST_7_DAYS -> {
                    val sevenDaysAgo = now - 7L * 24 * 60 * 60 * 1000
                    filtered = filtered.filter { it.customer.createdAt >= sevenDaysAgo || it.customer.updatedAt >= sevenDaysAgo }
                }
                TimeRangeFilter.LAST_30_DAYS -> {
                    val thirtyDaysAgo = now - 30L * 24 * 60 * 60 * 1000
                    filtered = filtered.filter { it.customer.createdAt >= thirtyDaysAgo || it.customer.updatedAt >= thirtyDaysAgo }
                }
                TimeRangeFilter.THIS_MONTH -> {
                    cal.set(Calendar.DAY_OF_MONTH, 1)
                    cal.set(Calendar.HOUR_OF_DAY, 0)
                    cal.set(Calendar.MINUTE, 0)
                    val startOfMonth = cal.timeInMillis
                    filtered = filtered.filter { it.customer.createdAt >= startOfMonth || it.customer.updatedAt >= startOfMonth }
                }
                else -> Unit
            }
        }

        // 4. Estimated / Deal Value range filter
        if (criteria.valueRange != ValueRangeFilter.ALL) {
            filtered = filtered.filter { item ->
                val totalVal = if (item.totalDealValue > 0) item.totalDealValue else item.customer.estimatedValue
                totalVal >= criteria.valueRange.min && totalVal <= criteria.valueRange.max
            }
        }

        // 5. Multi-field search query: Name, Company, Phone, Email, Address, Tags, Notes (supports Vietnamese unaccented search)
        if (criteria.query.isNotBlank()) {
            val q = criteria.query.trim()
            filtered = filtered.filter {
                textMatchesQuery(it.customer.name, q) ||
                textMatchesQuery(it.customer.company, q) ||
                it.customer.phone.contains(q) ||
                textMatchesQuery(it.customer.email, q) ||
                textMatchesQuery(it.customer.address, q) ||
                textMatchesQuery(it.customer.tags, q) ||
                textMatchesQuery(it.customer.notes, q)
            }
        }

        // 6. Sorting
        when (criteria.sort) {
            CustomerSort.RECENT -> filtered.sortedByDescending { it.customer.updatedAt }
            CustomerSort.CREATED_DESC -> filtered.sortedByDescending { it.customer.createdAt }
            CustomerSort.NAME_ASC -> filtered.sortedBy { it.customer.name.lowercase() }
            CustomerSort.NAME_DESC -> filtered.sortedByDescending { it.customer.name.lowercase() }
            CustomerSort.DEAL_VALUE_DESC -> filtered.sortedByDescending {
                if (it.totalDealValue > 0) it.totalDealValue else it.customer.estimatedValue
            }
            CustomerSort.DEAL_COUNT_DESC -> filtered.sortedByDescending { it.dealCount }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Pipeline & Deals
    private val _dealSearchQuery = MutableStateFlow("")
    val dealSearchQuery: StateFlow<String> = _dealSearchQuery.asStateFlow()

    private val _selectedStageFilter = MutableStateFlow<DealStage?>(null)
    val selectedStageFilter: StateFlow<DealStage?> = _selectedStageFilter.asStateFlow()

    val dealsWithCustomer: StateFlow<List<DealWithCustomer>> = combine(
        repository.dealsWithCustomer,
        _dealSearchQuery,
        _selectedStageFilter
    ) { deals, query, stage ->
        var result = deals
        if (stage != null) {
            result = result.filter { it.deal.stage.equals(stage.name, ignoreCase = true) }
        }
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            result = result.filter {
                it.deal.title.lowercase().contains(q) ||
                it.customerName.lowercase().contains(q) ||
                it.company.lowercase().contains(q)
            }
        }
        result
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Tasks & Calendar Events
    val tasksWithCustomer: StateFlow<List<TaskWithCustomer>> = repository.tasksWithCustomer
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val interactionsWithCustomer: StateFlow<List<InteractionWithCustomer>> = repository.interactionsWithCustomer
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allRawCustomers: StateFlow<List<CustomerEntity>> = repository.allCustomers
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Dashboard aggregated metrics
    val dashboardMetrics: StateFlow<DashboardMetrics> = combine(
        repository.allCustomers,
        repository.allDeals,
        repository.allTasks
    ) { customers, deals, tasks ->
        val totalCustomers = customers.size
        val vipCount = customers.count { it.status.equals(CustomerStatus.VIP.name, ignoreCase = true) }
        val currentCount = customers.count { it.status.equals(CustomerStatus.CUSTOMER.name, ignoreCase = true) }
        val partnerCount = customers.count { it.status.equals(CustomerStatus.PARTNER.name, ignoreCase = true) }
        val leadCount = customers.count { it.status.equals(CustomerStatus.LEAD.name, ignoreCase = true) }

        val wonDeals = deals.filter { it.stage.equals(DealStage.WON.name, ignoreCase = true) }
        val wonRevenue = wonDeals.sumOf { it.value }
        val activeDeals = deals.filter { !it.stage.equals(DealStage.LOST.name, ignoreCase = true) }
        val pipelineValue = activeDeals.sumOf { it.value }
        val totalDecidedDeals = deals.count { it.stage == DealStage.WON.name || it.stage == DealStage.LOST.name }
        val winRate = if (totalDecidedDeals > 0) {
            (wonDeals.size * 100) / totalDecidedDeals
        } else if (deals.isNotEmpty()) {
            (wonDeals.size * 100) / deals.size
        } else {
            0
        }

        val now = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        val startOfToday = cal.timeInMillis
        val endOfToday = startOfToday + 24L * 3600 * 1000

        val pendingCount = tasks.count { !it.isCompleted }
        val todayCount = tasks.count { !it.isCompleted && it.dueDate in startOfToday..endOfToday }

        DashboardMetrics(
            totalCustomers = totalCustomers,
            vipCustomers = vipCount,
            currentCustomers = currentCount,
            partnerCustomers = partnerCount,
            leadCustomers = leadCount,
            totalWonRevenue = wonRevenue,
            totalPipelineValue = pipelineValue,
            winRatePercent = winRate,
            pendingTasksCount = pendingCount,
            todayTasksCount = todayCount,
            totalDealsCount = deals.size,
            wonDealsCount = wonDeals.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardMetrics()
    )

    // Selected Customer Details for Detail Screen
    private val _selectedCustomerId = MutableStateFlow<Long?>(null)
    val selectedCustomerId: StateFlow<Long?> = _selectedCustomerId.asStateFlow()

    val selectedCustomer: StateFlow<CustomerEntity?> = _selectedCustomerId.flatMapLatest { id ->
        if (id == null) flowOf(null) else repository.getCustomerById(id)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val selectedCustomerDeals: StateFlow<List<DealEntity>> = _selectedCustomerId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList()) else repository.getDealsByCustomer(id)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val selectedCustomerInteractions: StateFlow<List<InteractionEntity>> = _selectedCustomerId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList()) else repository.getInteractionsByCustomer(id)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val selectedCustomerTasks: StateFlow<List<TaskEntity>> = _selectedCustomerId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList()) else repository.getTasksByCustomer(id)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Customer filters and actions
    fun setCustomerSearchQuery(query: String) {
        _customerSearchQuery.value = query
    }

    fun setStatusFilter(status: CustomerStatus?) {
        _selectedStatusFilter.value = status
        if (status != null) {
            _selectedCustomTypeFilter.value = null
        }
    }

    fun setCustomTypeFilter(customType: String?) {
        _selectedCustomTypeFilter.value = customType
        if (customType != null) {
            _selectedStatusFilter.value = null
        }
    }

    fun setTimeRangeFilter(timeRange: TimeRangeFilter) {
        _selectedTimeRangeFilter.value = timeRange
    }

    fun setValueRangeFilter(valueRange: ValueRangeFilter) {
        _selectedValueRangeFilter.value = valueRange
    }

    fun setSourceFilter(source: String?) {
        _selectedSourceFilter.value = source
    }

    fun resetCustomerFilters() {
        _selectedStatusFilter.value = null
        _selectedCustomTypeFilter.value = null
        _selectedTimeRangeFilter.value = TimeRangeFilter.ALL
        _selectedValueRangeFilter.value = ValueRangeFilter.ALL
        _selectedSourceFilter.value = null
        _customerSearchQuery.value = ""
        _customerSort.value = CustomerSort.RECENT
    }

    fun saveCustomerType(type: CustomerTypeEntity) {
        viewModelScope.launch {
            if (type.id == 0L) {
                repository.insertCustomerType(type)
            } else {
                repository.updateCustomerType(type)
            }
        }
    }

    fun deleteCustomerType(type: CustomerTypeEntity) {
        viewModelScope.launch {
            repository.deleteCustomerType(type)
        }
    }

    fun deleteCustomerTypeById(id: Long) {
        viewModelScope.launch {
            repository.deleteCustomerTypeById(id)
        }
    }

    fun resetCustomerTypesToDefault() {
        viewModelScope.launch {
            repository.resetCustomerTypesToDefault()
        }
    }

    fun setCustomerSort(sort: CustomerSort) {
        _customerSort.value = sort
    }

    fun selectCustomer(id: Long?) {
        _selectedCustomerId.value = id
    }

    fun saveCustomer(customer: CustomerEntity) {
        viewModelScope.launch {
            if (customer.id == 0L) {
                repository.insertCustomer(customer.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis()))
            } else {
                repository.updateCustomer(customer.copy(updatedAt = System.currentTimeMillis()))
            }
        }
    }

    fun deleteCustomer(id: Long) {
        viewModelScope.launch {
            repository.deleteCustomerById(id)
            if (_selectedCustomerId.value == id) {
                _selectedCustomerId.value = null
            }
        }
    }

    // Deal filters and actions
    fun setDealSearchQuery(query: String) {
        _dealSearchQuery.value = query
    }

    fun setStageFilter(stage: DealStage?) {
        _selectedStageFilter.value = stage
    }

    fun saveDeal(deal: DealEntity) {
        viewModelScope.launch {
            if (deal.id == 0L) {
                repository.insertDeal(deal.copy(createdAt = System.currentTimeMillis()))
            } else {
                repository.updateDeal(deal)
            }
            repository.getCustomerByIdDirect(deal.customerId)?.let { customer ->
                repository.updateCustomer(customer.copy(updatedAt = System.currentTimeMillis()))
            }
        }
    }

    fun updateDealStage(dealId: Long, newStage: DealStage) {
        viewModelScope.launch {
            repository.updateDealStage(dealId, newStage.name)
        }
    }

    // Additional States matching user's exact design mockups
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _notificationSettings = MutableStateFlow(NotificationSettings())
    val notificationSettings: StateFlow<NotificationSettings> = _notificationSettings.asStateFlow()

    private val _securitySettings = MutableStateFlow(SecuritySettings())
    val securitySettings: StateFlow<SecuritySettings> = _securitySettings.asStateFlow()

    // Quotes List matching design #bao_gia_tien_do.png
    private val _quotes = MutableStateFlow<List<QuoteItem>>(
        listOf(
            QuoteItem(
                id = 1,
                quoteNumber = "BG-20231015-001",
                title = "Acme Corp Website Redesign",
                amount = 125000000.0,
                dateStr = "15/10/2023",
                status = "Draft",
                customerName = "Acme Corporation",
                category = "Thiết kế & Thi công",
                notes = "Thiết kế lại toàn bộ website doanh nghiệp, chuẩn responsive"
            ),
            QuoteItem(
                id = 2,
                quoteNumber = "BG-20231020-004",
                title = "TechSolutions Cloud Migration",
                amount = 82000000.0,
                dateStr = "20/10/2023",
                status = "Draft",
                customerName = "TechSolutions Inc",
                category = "Dịch vụ & Bảo trì",
                notes = "Di chuyển hạ tầng lên AWS Cloud"
            ),
            QuoteItem(
                id = 3,
                quoteNumber = "BG-20231012-002",
                title = "GlobalReach Marketing Campaign",
                amount = 45000000.0,
                dateStr = "12/10/2023",
                status = "Sent",
                customerName = "GlobalReach Ltd",
                category = "Tư vấn giải pháp",
                notes = "Chiến dịch Digital Marketing Q4",
                version = 1
            ),
            QuoteItem(
                id = 4,
                quoteNumber = "BG-20231008-003",
                title = "Innova App Development",
                amount = 320000000.0,
                dateStr = "08/10/2023",
                status = "Accepted",
                customerName = "Innova Tech",
                category = "Phần mềm & Bản quyền",
                notes = "Phát triển app di động iOS và Android"
            )
        )
    )
    val quotes: StateFlow<List<QuoteItem>> = _quotes.asStateFlow()

    // Projects Progress matching design #tien_do_du_an.png
    private val _projects = MutableStateFlow<List<ProjectProgressItem>>(
        listOf(
            ProjectProgressItem(
                id = 1,
                title = "Thi công nội thất Villa A",
                customerName = "Nguyễn Văn B",
                statusType = ProjectStatusType.ON_TRACK,
                progressPercent = 66,
                steps = listOf(
                    ProjectStep(1, "Khảo sát & Đo đạc", StepStatus.COMPLETED, "Hoàn thành: 15/10/2023", customWeightPercent = 30),
                    ProjectStep(2, "Thiết kế 3D", StepStatus.COMPLETED, "Hoàn thành: 28/10/2023", customWeightPercent = 35),
                    ProjectStep(3, "Thi công lắp đặt", StepStatus.PENDING, "Deadline: 20/11/2023", customWeightPercent = 35)
                )
            ),
            ProjectProgressItem(
                id = 2,
                title = "Cải tạo văn phòng TechCorp",
                customerName = "TechCorp JSC",
                statusType = ProjectStatusType.NEARING,
                progressPercent = 33,
                steps = listOf(
                    ProjectStep(1, "Tháo dỡ hiện trạng", StepStatus.COMPLETED, "Hoàn thành: 01/11/2023"),
                    ProjectStep(2, "Thi công điện nước", StepStatus.WARNING, "Deadline: Hôm nay", isHighlighted = true),
                    ProjectStep(3, "Sơn bả & Hoàn thiện", StepStatus.PENDING, "Dự kiến: 15/11/2023")
                )
            ),
            ProjectProgressItem(
                id = 3,
                title = "Showroom thời trang X",
                customerName = "Lê Thị C",
                statusType = ProjectStatusType.DELAYED,
                progressPercent = 50,
                warningNote = "Nguy cơ phạt hợp đồng",
                steps = listOf(
                    ProjectStep(1, "Thiết kế concept", StepStatus.COMPLETED, "Hoàn thành: 20/10/2023"),
                    ProjectStep(2, "Sản xuất đồ gỗ nội thất", StepStatus.OVERDUE, "Quá hạn: 5 ngày (02/11/2023)", isHighlighted = true)
                )
            ),
            ProjectProgressItem(
                id = 4,
                quoteId = 4,
                title = "Innova App Development",
                customerName = "Innova Tech",
                statusType = ProjectStatusType.ON_TRACK,
                progressPercent = 25,
                steps = listOf(
                    ProjectStep(1, "Phân tích yêu cầu & Thiết kế UI/UX", StepStatus.COMPLETED, "Hoàn thành: 15/10/2023"),
                    ProjectStep(2, "Phát triển module chức năng cốt lõi", StepStatus.PENDING, "Deadline: 15/11/2023"),
                    ProjectStep(3, "Kiểm thử chất lượng & UAT", StepStatus.PENDING, "Deadline: 30/11/2023"),
                    ProjectStep(4, "Triển khai lên App Store / Google Play", StepStatus.PENDING, "Deadline: 15/12/2023")
                )
            )
        )
    )
    val projects: StateFlow<List<ProjectProgressItem>> = _projects.asStateFlow()

    // Employees list matching design & department/seniority requirements
    private val _employees = MutableStateFlow<List<EmployeeItem>>(
        listOf(
            EmployeeItem(
                id = 1,
                name = "Nguyễn Thị An",
                role = "Trưởng phòng Sales",
                department = "Phòng Kinh doanh",
                status = "Đang làm việc",
                initials = "NA",
                isWorking = true,
                phone = "0908123456",
                email = "an.nguyen@nexus.vn",
                startDate = "10/02/2018",
                baseSalary = 22000000.0,
                allowance = 3000000.0,
                kpiBonus = 5000000.0
            ),
            EmployeeItem(
                id = 2,
                name = "Trần Văn Bình",
                role = "Nhân viên Kinh doanh",
                department = "Phòng Kinh doanh",
                status = "Đang làm việc",
                initials = "TB",
                isWorking = true,
                phone = "0912345678",
                email = "binh.tran@nexus.vn",
                startDate = "15/06/2021",
                baseSalary = 14000000.0,
                allowance = 1500000.0,
                kpiBonus = 3500000.0
            ),
            EmployeeItem(
                id = 3,
                name = "Lê Hoàng Nam",
                role = "Chuyên viên Kỹ thuật",
                department = "Phòng Kỹ thuật",
                status = "Nghỉ phép",
                initials = "LN",
                isWorking = false,
                phone = "0987654321",
                email = "nam.le@nexus.vn",
                startDate = "01/03/2023",
                baseSalary = 16000000.0,
                allowance = 2000000.0,
                kpiBonus = 2000000.0
            ),
            EmployeeItem(
                id = 4,
                name = "Phạm Minh Đức",
                role = "Kế toán tổng hợp",
                department = "Phòng Kế toán",
                status = "Đang làm việc",
                initials = "MD",
                isWorking = true,
                phone = "0934567890",
                email = "duc.pham@nexus.vn",
                startDate = "20/08/2017",
                baseSalary = 18000000.0,
                allowance = 2000000.0,
                kpiBonus = 3000000.0
            ),
            EmployeeItem(
                id = 5,
                name = "Hoàng Thị Mai",
                role = "Chuyên viên Marketing",
                department = "Phòng Marketing",
                status = "Nghỉ việc",
                initials = "HM",
                isWorking = false,
                phone = "0978901234",
                email = "mai.hoang@nexus.vn",
                startDate = "05/11/2022",
                baseSalary = 13000000.0,
                allowance = 1000000.0,
                kpiBonus = 2000000.0
            ),
            EmployeeItem(
                id = 6,
                name = "Vũ Đình Tuấn",
                role = "Nhân viên CSKH",
                department = "Phòng Kinh doanh",
                status = "Đang làm việc",
                initials = "VT",
                isWorking = true,
                phone = "0945678901",
                email = "tuan.vu@nexus.vn",
                startDate = "12/01/2020",
                baseSalary = 12000000.0,
                allowance = 1200000.0,
                kpiBonus = 2500000.0
            )
        )
    )
    val employees: StateFlow<List<EmployeeItem>> = _employees.asStateFlow()

    // Payroll Policy Settings (Chuẩn 8h/ngày, 26 ngày/tháng, OT theo giờ, Thâm niên 5 năm +1 ngày phép, Thưởng 5 năm & chu kỳ tiếp theo)
    private val _payrollPolicy = MutableStateFlow(PayrollPolicySettings())
    val payrollPolicy: StateFlow<PayrollPolicySettings> = _payrollPolicy.asStateFlow()

    // Attendance Records (Bảng chấm công theo ngày)
    private val _attendanceRecords = MutableStateFlow<List<AttendanceRecord>>(
        listOf(
            AttendanceRecord(1, 1, "2026-08-27", AttendanceType.FULL_WORK, 8.0f, 2.0f, OvertimeRateType.WEEKDAY, "Hỗ trợ chốt deal hợp đồng lớn"),
            AttendanceRecord(2, 2, "2026-08-27", AttendanceType.FULL_WORK, 8.0f, 0.0f, OvertimeRateType.WEEKDAY, ""),
            AttendanceRecord(3, 3, "2026-08-27", AttendanceType.FULL_LEAVE, 0.0f, 0.0f, OvertimeRateType.WEEKDAY, "Nghỉ phép có đơn xin"),
            AttendanceRecord(4, 4, "2026-08-27", AttendanceType.FULL_WORK, 8.0f, 1.5f, OvertimeRateType.WEEKDAY, "Lập báo cáo tài chính tháng"),
            AttendanceRecord(5, 6, "2026-08-27", AttendanceType.HALF_LEAVE, 4.0f, 0.0f, OvertimeRateType.WEEKDAY, "Nghỉ phép nửa buổi chiều"),
            // Historical sample records for month calculations
            AttendanceRecord(6, 1, "2026-08-26", AttendanceType.FULL_WORK, 8.0f, 3.0f, OvertimeRateType.WEEKDAY, "Tăng ca meeting khách hàng"),
            AttendanceRecord(7, 2, "2026-08-26", AttendanceType.OVERTIME, 8.0f, 2.5f, OvertimeRateType.WEEKDAY, "OT xử lý báo giá"),
            AttendanceRecord(8, 4, "2026-08-26", AttendanceType.FULL_WORK, 8.0f, 0.0f, OvertimeRateType.WEEKDAY, "")
        )
    )
    val attendanceRecords: StateFlow<List<AttendanceRecord>> = _attendanceRecords.asStateFlow()

    // Actions for User, Auth & Settings
    fun login(email: String, pass: String) {
        _isLoggedIn.value = true
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    fun updateUserProfile(profile: UserProfile) {
        _userProfile.value = profile
    }

    fun setVipStatus(isVip: Boolean) {
        _userProfile.value = _userProfile.value.copy(
            isVip = isVip,
            role = if (isVip) "VIP ENTERPRISE" else "ADMIN"
        )
    }

    fun toggleVipStatus() {
        setVipStatus(!_userProfile.value.isVip)
    }

    fun updateNotificationSettings(settings: NotificationSettings) {
        _notificationSettings.value = settings
    }

    fun updateSecuritySettings(settings: SecuritySettings) {
        _securitySettings.value = settings
    }

    // Quote Actions
    fun addQuote(quote: QuoteItem) {
        val newId = if (quote.id != 0L) quote.id else System.currentTimeMillis()
        val quoteWithId = quote.copy(id = newId)
        _quotes.value = listOf(quoteWithId) + _quotes.value
        if (quoteWithId.status == "Accepted") {
            createProjectFromQuote(quoteWithId)
        }
    }

    fun updateQuote(quote: QuoteItem) {
        _quotes.value = _quotes.value.map {
            if (it.id == quote.id) quote else it
        }
        if (quote.status == "Accepted") {
            createProjectFromQuote(quote)
        }
    }

    fun deleteQuote(id: Long) {
        _quotes.value = _quotes.value.filter { it.id != id }
    }

    fun sendQuote(quoteId: Long) {
        _quotes.value = _quotes.value.map {
            if (it.id == quoteId) it.copy(status = "Sent") else it
        }
    }

    fun addQuoteRevision(quoteId: Long, note: String, newAmount: Double? = null) {
        _quotes.value = _quotes.value.map { quote ->
            if (quote.id == quoteId) {
                val nextVer = quote.version + 1
                val revAmount = newAmount ?: quote.amount
                val newRev = QuoteRevision(
                    version = nextVer,
                    title = "Bản cập nhật v$nextVer",
                    amount = revAmount,
                    dateStr = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                    notes = note
                )
                quote.copy(
                    version = nextVer,
                    amount = revAmount,
                    notes = if (note.isNotBlank()) note else quote.notes,
                    revisions = quote.revisions + newRev
                )
            } else quote
        }
    }

    fun acceptQuote(quoteId: Long) {
        var acceptedQuote: QuoteItem? = null
        _quotes.value = _quotes.value.map { quote ->
            if (quote.id == quoteId) {
                val updated = quote.copy(status = "Accepted")
                acceptedQuote = updated
                updated
            } else quote
        }
        acceptedQuote?.let { createProjectFromQuote(it) }
    }

    private fun createProjectFromQuote(quote: QuoteItem) {
        val existing = _projects.value.find { it.quoteId == quote.id || (it.title == quote.title && it.customerName == quote.customerName) }
        if (existing == null) {
            val newProject = ProjectProgressItem(
                id = System.currentTimeMillis(),
                quoteId = quote.id,
                title = quote.title,
                customerName = quote.customerName.ifBlank { "Khách hàng" },
                statusType = ProjectStatusType.ON_TRACK,
                progressPercent = 0,
                steps = listOf(
                    ProjectStep(1, "Ký kết hợp đồng & Đặt cọc", StepStatus.COMPLETED, "Hoàn thành: ${quote.dateStr}"),
                    ProjectStep(2, "Khảo sát / Tiếp nhận yêu cầu", StepStatus.PENDING, "Dự kiến: 7 ngày tới"),
                    ProjectStep(3, "Triển khai thực hiện", StepStatus.PENDING, "Dự kiến: 20 ngày tới"),
                    ProjectStep(4, "Nghiệm thu & Bàn giao", StepStatus.PENDING, "Dự kiến: 30 ngày tới")
                )
            )
            val calculated = newProject.calculateCalculatedProgress()
            _projects.value = listOf(newProject.copy(progressPercent = calculated)) + _projects.value
        }
    }

    // Project Progress Actions
    fun toggleProjectStep(projectId: Long, stepId: Long) {
        _projects.value = _projects.value.map { project ->
            if (project.id == projectId) {
                val updatedSteps = project.steps.map { step ->
                    if (step.id == stepId) {
                        val newStatus = if (step.status == StepStatus.COMPLETED) StepStatus.PENDING else StepStatus.COMPLETED
                        step.copy(status = newStatus)
                    } else step
                }
                val updatedProject = project.copy(steps = updatedSteps)
                val newProgress = updatedProject.calculateCalculatedProgress()
                val newStatusType = if (newProgress == 100) ProjectStatusType.ON_TRACK else project.statusType
                updatedProject.copy(progressPercent = newProgress, statusType = newStatusType)
            } else project
        }
    }

    fun updateProjectStepWeight(projectId: Long, stepId: Long, customWeight: Int?) {
        _projects.value = _projects.value.map { project ->
            if (project.id == projectId) {
                val updatedSteps = project.steps.map { step ->
                    if (step.id == stepId) step.copy(customWeightPercent = customWeight) else step
                }
                val updatedProject = project.copy(steps = updatedSteps)
                val newProgress = updatedProject.calculateCalculatedProgress()
                updatedProject.copy(progressPercent = newProgress)
            } else project
        }
    }

    fun addProjectStep(projectId: Long, stepTitle: String, deadline: String, customWeight: Int? = null) {
        _projects.value = _projects.value.map { project ->
            if (project.id == projectId) {
                val newStep = ProjectStep(
                    id = System.currentTimeMillis(),
                    title = stepTitle,
                    status = StepStatus.PENDING,
                    dateLabel = if (deadline.startsWith("Deadline") || deadline.startsWith("Dự kiến")) deadline else "Deadline: $deadline",
                    customWeightPercent = customWeight
                )
                val updatedProject = project.copy(steps = project.steps + newStep)
                val newProgress = updatedProject.calculateCalculatedProgress()
                updatedProject.copy(progressPercent = newProgress)
            } else project
        }
    }

    fun deleteProjectStep(projectId: Long, stepId: Long) {
        _projects.value = _projects.value.map { project ->
            if (project.id == projectId) {
                val updatedSteps = project.steps.filter { it.id != stepId }
                val updatedProject = project.copy(steps = updatedSteps)
                val newProgress = updatedProject.calculateCalculatedProgress()
                updatedProject.copy(progressPercent = newProgress)
            } else project
        }
    }

    fun deleteProject(projectId: Long) {
        _projects.value = _projects.value.filter { it.id != projectId }
    }

    fun addEmployee(
        name: String,
        role: String,
        department: String = "Phòng Kinh doanh",
        status: String = "Đang làm việc",
        phone: String = "",
        email: String = "",
        startDate: String = "01/01/2024",
        baseSalary: Double = 15000000.0,
        allowance: Double = 1500000.0,
        kpiBonus: Double = 3000000.0
    ) {
        val initials = name.trim().split(" ").takeLast(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")
        val newEmp = EmployeeItem(
            id = System.currentTimeMillis(),
            name = name,
            role = role,
            department = department,
            status = status,
            initials = if (initials.isNotEmpty()) initials else "NV",
            isWorking = (status == "Đang làm việc"),
            phone = phone,
            email = email,
            startDate = startDate,
            baseSalary = baseSalary,
            allowance = allowance,
            kpiBonus = kpiBonus
        )
        _employees.value = _employees.value + newEmp
    }

    fun updateEmployee(emp: EmployeeItem) {
        _employees.value = _employees.value.map {
            if (it.id == emp.id) {
                emp.copy(isWorking = (emp.status == "Đang làm việc"))
            } else it
        }
    }

    fun updateEmployeeStatus(id: Long, status: String) {
        _employees.value = _employees.value.map {
            if (it.id == id) {
                it.copy(status = status, isWorking = (status == "Đang làm việc"))
            } else it
        }
    }

    fun deleteEmployee(id: Long) {
        _employees.value = _employees.value.filter { it.id != id }
    }

    // Attendance / Timekeeping Actions
    fun recordAttendance(
        employeeId: Long,
        date: String,
        type: AttendanceType,
        workHours: Float = 8.0f,
        overtimeHours: Float = 0.0f,
        overtimeRateType: OvertimeRateType = OvertimeRateType.WEEKDAY,
        note: String = ""
    ) {
        val existingIndex = _attendanceRecords.value.indexOfFirst { it.employeeId == employeeId && it.date == date }
        if (existingIndex >= 0) {
            val updated = _attendanceRecords.value[existingIndex].copy(
                type = type,
                workHours = workHours,
                overtimeHours = overtimeHours,
                overtimeRateType = overtimeRateType,
                note = note
            )
            _attendanceRecords.value = _attendanceRecords.value.toMutableList().apply {
                set(existingIndex, updated)
            }
        } else {
            val newRecord = AttendanceRecord(
                id = System.currentTimeMillis(),
                employeeId = employeeId,
                date = date,
                type = type,
                workHours = workHours,
                overtimeHours = overtimeHours,
                overtimeRateType = overtimeRateType,
                note = note
            )
            _attendanceRecords.value = _attendanceRecords.value + newRecord
        }

        // Auto update employee status if today's attendance indicates on leave
        if (type == AttendanceType.FULL_LEAVE || type == AttendanceType.UNPAID_LEAVE) {
            updateEmployeeStatus(employeeId, "Nghỉ phép")
        } else if (type == AttendanceType.FULL_WORK || type == AttendanceType.HALF_LEAVE || type == AttendanceType.OVERTIME) {
            val emp = _employees.value.find { it.id == employeeId }
            if (emp != null && emp.status != "Nghỉ việc") {
                updateEmployeeStatus(employeeId, "Đang làm việc")
            }
        }
    }

    fun batchRecordAttendance(
        employeeIds: List<Long>,
        date: String,
        type: AttendanceType,
        workHours: Float = 8.0f,
        overtimeHours: Float = 0.0f,
        overtimeRateType: OvertimeRateType = OvertimeRateType.WEEKDAY,
        note: String = ""
    ) {
        employeeIds.forEach { empId ->
            recordAttendance(empId, date, type, workHours, overtimeHours, overtimeRateType, note)
        }
    }

    fun deleteAttendanceRecord(id: Long) {
        _attendanceRecords.value = _attendanceRecords.value.filter { it.id != id }
    }

    // Payroll & Seniority Policy
    fun updatePayrollPolicy(policy: PayrollPolicySettings) {
        _payrollPolicy.value = policy
    }

    fun deleteDeal(id: Long) {
        viewModelScope.launch {
            repository.deleteDealById(id)
        }
    }

    // Interaction actions with Reminder support
    fun logInteraction(interaction: InteractionEntity) {
        viewModelScope.launch {
            repository.insertInteraction(interaction.copy(date = if (interaction.date == 0L) System.currentTimeMillis() else interaction.date))
            repository.getCustomerByIdDirect(interaction.customerId)?.let { customer ->
                repository.updateCustomer(customer.copy(updatedAt = System.currentTimeMillis()))
            }
        }
    }

    fun deleteInteraction(id: Long) {
        viewModelScope.launch {
            repository.deleteInteractionById(id)
        }
    }

    // Task & Calendar event actions with Outcome reporting and reminders
    fun saveTask(task: TaskEntity) {
        viewModelScope.launch {
            if (task.id == 0L) {
                repository.insertTask(task.copy(createdAt = System.currentTimeMillis()))
            } else {
                repository.updateTask(task)
            }
        }
    }

    fun saveTaskResult(taskId: Long, rating: Int, summary: String) {
        viewModelScope.launch {
            val task = repository.allTasksDirect().find { it.id == taskId }
            if (task != null) {
                val updated = task.copy(
                    isCompleted = true,
                    resultRating = rating,
                    resultSummary = summary.trim(),
                    completedAt = System.currentTimeMillis()
                )
                repository.updateTask(updated)
            }
        }
    }

    fun updateTaskOutcome(taskId: Long, rating: Int, report: String) {
        saveTaskResult(taskId, rating, report)
    }

    fun toggleTaskCompletion(taskId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.setTaskCompleted(taskId, isCompleted)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTaskById(id)
        }
    }
}
