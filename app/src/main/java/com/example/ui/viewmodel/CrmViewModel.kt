package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.LocalAccount
import com.example.data.local.UserPreferences
import com.example.data.model.AnnexItem
import com.example.data.model.AttendanceRecord
import com.example.data.model.AttendanceType
import com.example.data.model.ContractAnnex
import com.example.data.model.ContractItem
import com.example.data.model.ContractNamingRule
import com.example.data.model.ContractStatus
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
import com.example.data.repository.CrmSeedData
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
    val userPreferences = UserPreferences(application)
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

    // Additional States matching user's exact design mockups with persistent storage
    private val _isLoggedIn = MutableStateFlow(userPreferences.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userProfile = MutableStateFlow(userPreferences.getUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _notificationSettings = MutableStateFlow(NotificationSettings())
    val notificationSettings: StateFlow<NotificationSettings> = _notificationSettings.asStateFlow()

    private val _securitySettings = MutableStateFlow(userPreferences.getSecuritySettings())
    val securitySettings: StateFlow<SecuritySettings> = _securitySettings.asStateFlow()

    // Quotes List matching 12/2025 - 08/2026 data
    private val _quotes = MutableStateFlow<List<QuoteItem>>(CrmSeedData.getSampleQuotes())
    val quotes: StateFlow<List<QuoteItem>> = _quotes.asStateFlow()

    // Contracts List & Naming Rule
    private val _contracts = MutableStateFlow<List<ContractItem>>(CrmSeedData.getSampleContracts())
    val contracts: StateFlow<List<ContractItem>> = _contracts.asStateFlow()

    private val _contractNamingRule = MutableStateFlow(CrmSeedData.getContractNamingRule())
    val contractNamingRule: StateFlow<ContractNamingRule> = _contractNamingRule.asStateFlow()

    // Projects Progress matching 12/2025 - 08/2026 data
    private val _projects = MutableStateFlow<List<ProjectProgressItem>>(CrmSeedData.getSampleProjects())
    val projects: StateFlow<List<ProjectProgressItem>> = _projects.asStateFlow()

    // Employees list matching design & department/seniority requirements
    private val _employees = MutableStateFlow<List<EmployeeItem>>(CrmSeedData.getSampleEmployees())
    val employees: StateFlow<List<EmployeeItem>> = _employees.asStateFlow()

    // Payroll Policy Settings (Chuẩn 8h/ngày, 26 ngày/tháng, OT theo giờ, Thâm niên 5 năm +1 ngày phép, Thưởng 5 năm & chu kỳ tiếp theo)
    private val _payrollPolicy = MutableStateFlow(PayrollPolicySettings())
    val payrollPolicy: StateFlow<PayrollPolicySettings> = _payrollPolicy.asStateFlow()

    // Attendance Records (Bảng chấm công theo ngày)
    private val _attendanceRecords = MutableStateFlow<List<AttendanceRecord>>(CrmSeedData.getSampleAttendance())
    val attendanceRecords: StateFlow<List<AttendanceRecord>> = _attendanceRecords.asStateFlow()

    // Actions for User, Auth & Settings
    fun login(email: String, name: String = "", avatarUrl: String? = null) {
        _isLoggedIn.value = true
        userPreferences.setLoggedIn(true)
        if (email.isNotBlank()) {
            val current = _userProfile.value
            val savedProfile = userPreferences.getUserProfile()
            val updated = current.copy(
                email = email.trim(),
                fullName = if (name.isNotBlank()) name.trim() else current.fullName,
                avatarUrl = avatarUrl ?: current.avatarUrl,
                isVip = savedProfile.accountTier.isVipOrHigher,
                accountTier = savedProfile.accountTier,
                role = when (savedProfile.accountTier) {
                    com.example.data.model.AccountTier.FREE -> "FREE MEMBER"
                    com.example.data.model.AccountTier.VIP -> "VIP MEMBER"
                    com.example.data.model.AccountTier.BUSINESS -> "VIP ENTERPRISE"
                }
            )
            _userProfile.value = updated
            userPreferences.saveUserProfile(updated)
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        userPreferences.setLoggedIn(false)
    }

    fun deleteAccount(email: String? = null) {
        val targetEmail = email ?: _userProfile.value.email
        if (targetEmail.isNotBlank()) {
            userPreferences.deleteAccount(targetEmail)
        }
        userPreferences.clearUserData()
        _isLoggedIn.value = false
        _userProfile.value = UserProfile()
    }

    fun updateUserProfile(profile: UserProfile) {
        _userProfile.value = profile
        userPreferences.saveUserProfile(profile)
    }

    fun setAccountTier(tier: com.example.data.model.AccountTier) {
        val updated = _userProfile.value.copy(
            accountTier = tier,
            isVip = tier.isVipOrHigher,
            role = when (tier) {
                com.example.data.model.AccountTier.FREE -> "FREE MEMBER"
                com.example.data.model.AccountTier.VIP -> "VIP MEMBER"
                com.example.data.model.AccountTier.BUSINESS -> "VIP ENTERPRISE"
            }
        )
        _userProfile.value = updated
        userPreferences.saveUserProfile(updated)
    }

    fun setVipStatus(isVip: Boolean) {
        setAccountTier(if (isVip) com.example.data.model.AccountTier.VIP else com.example.data.model.AccountTier.FREE)
    }

    fun toggleVipStatus() {
        val nextTier = when (_userProfile.value.accountTier) {
            com.example.data.model.AccountTier.FREE -> com.example.data.model.AccountTier.VIP
            com.example.data.model.AccountTier.VIP -> com.example.data.model.AccountTier.BUSINESS
            com.example.data.model.AccountTier.BUSINESS -> com.example.data.model.AccountTier.FREE
        }
        setAccountTier(nextTier)
    }

    fun resetToComprehensiveSeedData(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                repository.resetToComprehensiveSeedData()
                _quotes.value = CrmSeedData.getSampleQuotes()
                _contracts.value = CrmSeedData.getSampleContracts()
                _contractNamingRule.value = CrmSeedData.getContractNamingRule()
                _projects.value = CrmSeedData.getSampleProjects()
                _employees.value = CrmSeedData.getSampleEmployees()
                _attendanceRecords.value = CrmSeedData.getSampleAttendance()
                _payrollPolicy.value = PayrollPolicySettings()
            } finally {
                onComplete?.invoke()
            }
        }
    }

    fun updateNotificationSettings(settings: NotificationSettings) {
        _notificationSettings.value = settings
    }

    fun updateSecuritySettings(settings: SecuritySettings) {
        _securitySettings.value = settings
        userPreferences.saveSecuritySettings(settings)
    }

    fun updateContractNamingRule(rule: ContractNamingRule) {
        _contractNamingRule.value = rule
    }

    // Contract Actions
    fun addContract(contract: ContractItem) {
        val newId = if (contract.id != 0L) contract.id else System.currentTimeMillis()
        val item = contract.copy(id = newId)
        _contracts.value = listOf(item) + _contracts.value
    }

    fun updateContract(contract: ContractItem) {
        _contracts.value = _contracts.value.map {
            if (it.id == contract.id) contract else it
        }
    }

    fun deleteContract(id: Long) {
        _contracts.value = _contracts.value.filter { it.id != id }
    }

    fun signContract(contractId: Long) {
        var signedContract: ContractItem? = null
        _contracts.value = _contracts.value.map { contract ->
            if (contract.id == contractId) {
                val updated = contract.copy(status = ContractStatus.SIGNED)
                signedContract = updated
                updated
            } else contract
        }
        signedContract?.let { c ->
            // Ensure project step 1 is marked completed
            val existingProject = _projects.value.find { it.quoteId == c.quoteId || (it.title == c.title && it.customerName == c.customerName) }
            if (existingProject != null) {
                val updatedSteps = existingProject.steps.mapIndexed { idx, s ->
                    if (idx == 0) s.copy(status = StepStatus.COMPLETED, dateLabel = "Hoàn thành: ${c.signedDate}") else s
                }
                val updatedProj = existingProject.copy(steps = updatedSteps)
                val calc = updatedProj.calculateCalculatedProgress()
                _projects.value = _projects.value.map { if (it.id == updatedProj.id) updatedProj.copy(progressPercent = calc) else it }
            } else {
                createProjectFromContract(c)
            }
        }
    }

    fun addContractAnnex(contractId: Long, annex: ContractAnnex) {
        _contracts.value = _contracts.value.map { contract ->
            if (contract.id == contractId) {
                val newAnnexId = if (annex.id != 0L) annex.id else System.currentTimeMillis()
                val finalAnnex = annex.copy(id = newAnnexId, contractId = contractId)
                val updatedAnnexes = contract.annexes + finalAnnex
                contract.copy(annexes = updatedAnnexes)
            } else contract
        }
    }

    fun deleteContractAnnex(contractId: Long, annexId: Long) {
        _contracts.value = _contracts.value.map { contract ->
            if (contract.id == contractId) {
                val updatedAnnexes = contract.annexes.filter { it.id != annexId }
                contract.copy(annexes = updatedAnnexes)
            } else contract
        }
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

    fun acceptQuoteWithOption(quoteId: Long, isSignContract: Boolean): Pair<QuoteItem?, ContractItem?> {
        var acceptedQuote: QuoteItem? = null
        var createdContract: ContractItem? = null

        val todayStr = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date())

        val targetQuote = _quotes.value.find { it.id == quoteId } ?: return Pair(null, null)

        if (isSignContract) {
            val nextContractNum = _contractNamingRule.value.generateNextContractNumber(_contracts.value)
            val newContract = ContractItem(
                id = System.currentTimeMillis(),
                quoteId = targetQuote.id,
                contractNumber = nextContractNum,
                title = "Hợp đồng: ${targetQuote.title}",
                customerName = targetQuote.customerName,
                customerId = targetQuote.customerId,
                originalAmount = targetQuote.amount,
                signedDate = todayStr,
                status = ContractStatus.DRAFT,
                notes = "Hợp đồng chuyển từ Báo giá ${targetQuote.quoteNumber}"
            )
            createdContract = newContract
            _contracts.value = listOf(newContract) + _contracts.value

            _quotes.value = _quotes.value.map { quote ->
                if (quote.id == quoteId) {
                    val updated = quote.copy(
                        status = "Accepted",
                        contractId = newContract.id,
                        contractNumber = newContract.contractNumber
                    )
                    acceptedQuote = updated
                    updated
                } else quote
            }
            createProjectFromQuote(targetQuote)
        } else {
            _quotes.value = _quotes.value.map { quote ->
                if (quote.id == quoteId) {
                    val updated = quote.copy(status = "Accepted")
                    acceptedQuote = updated
                    updated
                } else quote
            }
            acceptedQuote?.let { createProjectFromQuote(it) }
        }

        return Pair(acceptedQuote, createdContract)
    }

    fun updateContractTerms(contractId: Long, payment: String, delivery: String, warranty: String, notes: String) {
        _contracts.value = _contracts.value.map { contract ->
            if (contract.id == contractId) {
                contract.copy(
                    paymentTerms = payment,
                    deliveryTerms = delivery,
                    warrantyTerms = warranty,
                    notes = notes
                )
            } else contract
        }
    }

    private fun createProjectFromContract(contract: ContractItem) {
        val existing = _projects.value.find { it.quoteId == contract.quoteId || (it.title == contract.title && it.customerName == contract.customerName) }
        if (existing == null) {
            val newProject = ProjectProgressItem(
                id = System.currentTimeMillis(),
                quoteId = contract.quoteId,
                title = contract.title,
                customerName = contract.customerName.ifBlank { "Khách hàng" },
                statusType = ProjectStatusType.ON_TRACK,
                progressPercent = 0,
                steps = listOf(
                    ProjectStep(1, "Ký kết hợp đồng & Đặt cọc", StepStatus.COMPLETED, "Hoàn thành: ${contract.signedDate}"),
                    ProjectStep(2, "Khảo sát / Tiếp nhận yêu cầu", StepStatus.PENDING, "Dự kiến: 7 ngày tới"),
                    ProjectStep(3, "Triển khai thực hiện", StepStatus.PENDING, "Dự kiến: 20 ngày tới"),
                    ProjectStep(4, "Nghiệm thu & Bàn giao", StepStatus.PENDING, "Dự kiến: 30 ngày tới")
                )
            )
            val calculated = newProject.calculateCalculatedProgress()
            _projects.value = listOf(newProject.copy(progressPercent = calculated)) + _projects.value
        }
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

    fun updateProjectStep(projectId: Long, updatedStep: ProjectStep) {
        _projects.value = _projects.value.map { project ->
            if (project.id == projectId) {
                val updatedSteps = project.steps.map { step ->
                    if (step.id == updatedStep.id) updatedStep else step
                }
                val updatedProject = project.copy(steps = updatedSteps)
                val newProgress = updatedProject.calculateCalculatedProgress()
                val newStatusType = if (newProgress == 100) ProjectStatusType.ON_TRACK else project.statusType
                updatedProject.copy(progressPercent = newProgress, statusType = newStatusType)
            } else project
        }
    }

    fun updateProjectStep(projectId: Long, stepId: Long, title: String, deadline: String, customWeight: Int?) {
        val targetProject = _projects.value.find { it.id == projectId } ?: return
        val currentStep = targetProject.steps.find { it.id == stepId } ?: return
        val dateText = if (deadline.startsWith("Deadline") || deadline.startsWith("Dự kiến")) deadline else "Deadline: $deadline"
        val updatedStep = currentStep.copy(
            title = title,
            dateLabel = dateText,
            customWeightPercent = customWeight
        )
        updateProjectStep(projectId, updatedStep)
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
