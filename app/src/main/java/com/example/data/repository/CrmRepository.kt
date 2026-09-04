package com.example.data.repository

import com.example.data.local.CustomerDao
import com.example.data.local.CustomerTypeDao
import com.example.data.local.DealDao
import com.example.data.local.InteractionDao
import com.example.data.local.TaskDao
import com.example.data.model.CustomerEntity
import com.example.data.model.CustomerStatus
import com.example.data.model.CustomerTypeEntity
import com.example.data.model.CustomerWithDetails
import com.example.data.model.DealEntity
import com.example.data.model.DealStage
import com.example.data.model.DealWithCustomer
import com.example.data.model.InteractionEntity
import com.example.data.model.InteractionType
import com.example.data.model.InteractionWithCustomer
import com.example.data.model.TaskEntity
import com.example.data.model.TaskPriority
import com.example.data.model.TaskType
import com.example.data.model.TaskWithCustomer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class CrmRepository(
    private val customerDao: CustomerDao,
    private val customerTypeDao: CustomerTypeDao,
    private val dealDao: DealDao,
    private val interactionDao: InteractionDao,
    private val taskDao: TaskDao
) {
    val allCustomerTypes: Flow<List<CustomerTypeEntity>> = customerTypeDao.getAllCustomerTypes()
    val allCustomers: Flow<List<CustomerEntity>> = customerDao.getAllCustomers()
    val allDeals: Flow<List<DealEntity>> = dealDao.getAllDeals()
    val allInteractions: Flow<List<InteractionEntity>> = interactionDao.getAllInteractions()
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    val pendingTasks: Flow<List<TaskEntity>> = taskDao.getPendingTasks()
    val upcomingTasks: Flow<List<TaskEntity>> = taskDao.getUpcomingTasks(10)
    val totalWonRevenue: Flow<Double?> = dealDao.getTotalWonRevenue()
    val totalPipelineValue: Flow<Double?> = dealDao.getTotalPipelineValue()
    val customerCount: Flow<Int> = customerDao.getCustomerCount()
    val vipCustomerCount: Flow<Int> = customerDao.getVipCustomerCount()
    val pendingTasksCount: Flow<Int> = taskDao.getPendingTasksCount()

    fun getCustomerById(id: Long): Flow<CustomerEntity?> = customerDao.getCustomerById(id)
    suspend fun getCustomerByIdDirect(id: Long): CustomerEntity? = customerDao.getCustomerByIdDirect(id)

    suspend fun getDealsByCustomer(customerId: Long): Flow<List<DealEntity>> = dealDao.getDealsByCustomer(customerId)
    suspend fun allTasksDirect(): List<TaskEntity> = taskDao.getAllTasks().first()
    fun getInteractionsByCustomer(customerId: Long): Flow<List<InteractionEntity>> = interactionDao.getInteractionsByCustomer(customerId)
    fun getTasksByCustomer(customerId: Long): Flow<List<TaskEntity>> = taskDao.getTasksByCustomer(customerId)

    fun searchCustomers(query: String): Flow<List<CustomerEntity>> {
        return if (query.isBlank()) {
            customerDao.getAllCustomers()
        } else {
            customerDao.searchCustomers(query.trim())
        }
    }

    // Rich combined customers
    val customersWithDetails: Flow<List<CustomerWithDetails>> = combine(
        allCustomers,
        allDeals,
        allInteractions,
        allTasks
    ) { customers, deals, interactions, tasks ->
        customers.map { customer ->
            val customerDeals = deals.filter { it.customerId == customer.id }
            val customerInteractions = interactions.filter { it.customerId == customer.id }
            val customerTasks = tasks.filter { it.customerId == customer.id && !it.isCompleted }

            CustomerWithDetails(
                customer = customer,
                dealCount = customerDeals.size,
                totalDealValue = customerDeals.sumOf { it.value },
                latestInteraction = customerInteractions.maxByOrNull { it.date },
                pendingTasksCount = customerTasks.size
            )
        }
    }

    // Rich combined deals
    val dealsWithCustomer: Flow<List<DealWithCustomer>> = combine(
        allDeals,
        allCustomers
    ) { deals, customers ->
        val customerMap = customers.associateBy { it.id }
        deals.map { deal ->
            val customer = customerMap[deal.customerId]
            DealWithCustomer(
                deal = deal,
                customerName = customer?.name ?: "Khách hàng không xác định",
                company = customer?.company ?: "",
                customerPhone = customer?.phone ?: ""
            )
        }
    }

    // Rich combined tasks
    val tasksWithCustomer: Flow<List<TaskWithCustomer>> = combine(
        allTasks,
        allCustomers
    ) { tasks, customers ->
        val customerMap = customers.associateBy { it.id }
        tasks.map { task ->
            val customer = task.customerId?.let { customerMap[it] }
            TaskWithCustomer(
                task = task,
                customerName = customer?.name,
                company = customer?.company,
                customerPhone = customer?.phone
            )
        }
    }

    // Rich combined interactions
    val interactionsWithCustomer: Flow<List<InteractionWithCustomer>> = combine(
        allInteractions,
        allCustomers
    ) { interactions, customers ->
        val customerMap = customers.associateBy { it.id }
        interactions.map { interaction ->
            val customer = customerMap[interaction.customerId]
            InteractionWithCustomer(
                interaction = interaction,
                customerName = customer?.name ?: "Khách hàng",
                company = customer?.company ?: "",
                customerPhone = customer?.phone
            )
        }
    }

    // Customer operations
    suspend fun insertCustomer(customer: CustomerEntity): Long = customerDao.insertCustomer(customer)
    suspend fun updateCustomer(customer: CustomerEntity) = customerDao.updateCustomer(customer)
    suspend fun deleteCustomerById(id: Long) = customerDao.deleteCustomerById(id)

    // Deal operations
    suspend fun insertDeal(deal: DealEntity): Long = dealDao.insertDeal(deal)
    suspend fun updateDeal(deal: DealEntity) = dealDao.updateDeal(deal)
    suspend fun updateDealStage(dealId: Long, newStage: String) = dealDao.updateDealStage(dealId, newStage)
    suspend fun deleteDealById(id: Long) = dealDao.deleteDealById(id)

    // Interaction operations
    suspend fun insertInteraction(interaction: InteractionEntity): Long = interactionDao.insertInteraction(interaction)
    suspend fun deleteInteractionById(id: Long) = interactionDao.deleteInteractionById(id)

    // Task operations
    suspend fun insertTask(task: TaskEntity): Long = taskDao.insertTask(task)
    suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)
    suspend fun setTaskCompleted(taskId: Long, isCompleted: Boolean) = taskDao.setTaskCompleted(taskId, isCompleted)
    suspend fun deleteTaskById(id: Long) = taskDao.deleteTaskById(id)

    // Customer Type operations
    suspend fun insertCustomerType(type: CustomerTypeEntity): Long = customerTypeDao.insertCustomerType(type)
    suspend fun updateCustomerType(type: CustomerTypeEntity) = customerTypeDao.updateCustomerType(type)
    suspend fun deleteCustomerType(type: CustomerTypeEntity) = customerTypeDao.deleteCustomerType(type)
    suspend fun deleteCustomerTypeById(id: Long) = customerTypeDao.deleteCustomerTypeById(id)
    suspend fun resetCustomerTypesToDefault() {
        customerTypeDao.deleteAllCustomerTypes()
        customerTypeDao.insertCustomerTypes(getDefaultCustomerTypes())
    }

    private fun getDefaultCustomerTypes(): List<CustomerTypeEntity> {
        return listOf(
            CustomerTypeEntity(
                id = 1,
                code = "LEAD",
                name = "Tiềm Năng",
                colorHex = "#2563EB",
                description = "Khách hàng mới tiếp cận, đang tìm hiểu sản phẩm/dịch vụ",
                isSystemDefault = true,
                sortOrder = 1
            ),
            CustomerTypeEntity(
                id = 2,
                code = "VIP",
                name = "Khách hàng VIP",
                colorHex = "#7C3AED",
                description = "Khách hàng có giá trị cao, chính sách chăm sóc đặc biệt",
                isSystemDefault = true,
                sortOrder = 2
            ),
            CustomerTypeEntity(
                id = 3,
                code = "CUSTOMER",
                name = "Khách hàng",
                colorHex = "#10B981",
                description = "Khách hàng đã ký hợp đồng hoặc giao dịch định kỳ",
                isSystemDefault = true,
                sortOrder = 3
            ),
            CustomerTypeEntity(
                id = 4,
                code = "CASUAL",
                name = "Khách hàng Vãng Lai",
                colorHex = "#F59E0B",
                description = "Khách hàng phát sinh giao dịch không thường xuyên",
                isSystemDefault = true,
                sortOrder = 4
            ),
            CustomerTypeEntity(
                id = 5,
                code = "PARTNER",
                name = "Đối tác",
                colorHex = "#0EA5E9",
                description = "Đại lý phân phối, đối tác liên kết thương mại",
                isSystemDefault = true,
                sortOrder = 5
            ),
            CustomerTypeEntity(
                id = 6,
                code = "INACTIVE",
                name = "Ngừng liên hệ",
                colorHex = "#64748B",
                description = "Khách hàng tạm dừng hoặc không còn nhu cầu",
                isSystemDefault = true,
                sortOrder = 6
            )
        )
    }

    // Clear all user data for fresh/new accounts
    suspend fun clearAllUserData() {
        taskDao.deleteAllTasks()
        interactionDao.deleteAllInteractions()
        dealDao.deleteAllDeals()
        customerDao.deleteAllCustomers()
        customerTypeDao.deleteAllCustomerTypes()
        customerTypeDao.insertCustomerTypes(getDefaultCustomerTypes())
    }

    // Reset & Comprehensive Seed Data from Dec 2025 to Aug 2026
    suspend fun resetToComprehensiveSeedData() {
        taskDao.deleteAllTasks()
        interactionDao.deleteAllInteractions()
        dealDao.deleteAllDeals()
        customerDao.deleteAllCustomers()
        customerTypeDao.deleteAllCustomerTypes()

        customerTypeDao.insertCustomerTypes(getDefaultCustomerTypes())
        customerDao.insertCustomers(CrmSeedData.getSampleCustomers())
        dealDao.insertDeals(CrmSeedData.getSampleDeals())
        interactionDao.insertInteractions(CrmSeedData.getSampleInteractions())
        taskDao.insertTasks(CrmSeedData.getSampleTasks())
    }

    // Initial Seed Data
    suspend fun seedInitialDataIfEmpty() {
        if (customerTypeDao.getCount() == 0) {
            customerTypeDao.insertCustomerTypes(getDefaultCustomerTypes())
        }

        val count = customerDao.getAllCustomers().first().size
        if (count == 0) {
            customerDao.insertCustomers(CrmSeedData.getSampleCustomers())
            dealDao.insertDeals(CrmSeedData.getSampleDeals())
            interactionDao.insertInteractions(CrmSeedData.getSampleInteractions())
            taskDao.insertTasks(CrmSeedData.getSampleTasks())
        }
    }
}
