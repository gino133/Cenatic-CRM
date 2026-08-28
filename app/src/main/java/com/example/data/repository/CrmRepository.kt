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

    // Initial Seed Data
    suspend fun seedInitialDataIfEmpty() {
        if (customerTypeDao.getCount() == 0) {
            customerTypeDao.insertCustomerTypes(getDefaultCustomerTypes())
        }

        val count = customerDao.getAllCustomers().first().size
        if (count == 0) {
            val now = System.currentTimeMillis()
            val oneDay = 24L * 60 * 60 * 1000
            val oneHour = 60L * 60 * 1000

            val sampleCustomers = listOf(
                CustomerEntity(
                    id = 1,
                    name = "Nguyễn Văn A",
                    company = "Công ty CP Công Nghệ TechVN",
                    position = "Giám đốc Công nghệ",
                    phone = "0988 123 456",
                    email = "nguyenvana@techvn.com",
                    address = "Tòa nhà TechVN, Duy Tân, Cầu Giấy, Hà Nội",
                    status = CustomerStatus.LEAD.name,
                    source = "Website",
                    tags = "Phần mềm, Cloud, Tiềm năng cao",
                    estimatedValue = 180_000_000.0,
                    progressPercent = 70,
                    notes = "Khách hàng rất quan tâm tới phân hệ quản lý quan hệ khách hàng và quy trình bán hàng tự động.",
                    avatarColorHex = "#2563EB",
                    createdAt = now - 15 * oneDay,
                    updatedAt = now - 2 * oneHour
                ),
                CustomerEntity(
                    id = 2,
                    name = "Trần Thị B",
                    company = "Tập đoàn Xây dựng Hưng Phát",
                    position = "Tổng Giám Đốc",
                    phone = "0912 345 678",
                    email = "tran.b@hungphatgroup.vn",
                    address = "128 Nguyễn Đình Chiểu, Quận 3, TP. Hồ Chí Minh",
                    status = CustomerStatus.CLOSED.name,
                    source = "Giới thiệu",
                    tags = "Xây dựng, Hợp đồng lớn, Đã chốt",
                    estimatedValue = 350_000_000.0,
                    progressPercent = 100,
                    notes = "Đã ký kết hợp đồng thi công và triển khai giải pháp quản lý tiến độ dự án toàn diện.",
                    avatarColorHex = "#10B981",
                    createdAt = now - 30 * oneDay,
                    updatedAt = now - 4 * oneHour
                ),
                CustomerEntity(
                    id = 3,
                    name = "Lê Hoàng C",
                    company = "Công ty TNHH Thương Mại Toàn Cầu",
                    position = "Trưởng phòng Kinh doanh",
                    phone = "0977 654 321",
                    email = "lehoangc@toancau.vn",
                    address = "Số 45 Lê Thánh Tông, Ngô Quyền, Hải Phòng",
                    status = CustomerStatus.LEAD.name,
                    source = "Sự kiện",
                    tags = "Thương mại, Xuất nhập khẩu",
                    estimatedValue = 95_000_000.0,
                    progressPercent = 40,
                    notes = "Đang xem xét báo giá gói quản lý kho và kết nối cổng thanh toán.",
                    avatarColorHex = "#0EA5E9",
                    createdAt = now - 8 * oneDay,
                    updatedAt = now - 1 * oneDay
                ),
                CustomerEntity(
                    id = 4,
                    name = "Phạm Thị D",
                    company = "Dịch vụ Tư vấn ABC",
                    position = "Phó Giám Đốc",
                    phone = "0933 888 999",
                    email = "phamthid@tuvanabc.vn",
                    address = "250 Võ Văn Kiệt, Quận 1, TP. Hồ Chí Minh",
                    status = CustomerStatus.INACTIVE.name,
                    source = "Mạng xã hội",
                    tags = "Tư vấn tài chính, Tạm ngưng",
                    estimatedValue = 25_000_000.0,
                    progressPercent = 15,
                    notes = "Tạm ngưng dự án do thay đổi kế hoạch ngân sách nội bộ.",
                    avatarColorHex = "#64748B",
                    createdAt = now - 45 * oneDay,
                    updatedAt = now - 10 * oneDay
                ),
                CustomerEntity(
                    id = 5,
                    name = "Nguyễn Văn Khang",
                    company = "Tập đoàn Khang Điền",
                    position = "Chủ Tịch HĐQT",
                    phone = "090 123 4567",
                    email = "khang.nguyen@khangdien.vn",
                    address = "Số 45, Đường Lê Lợi, Phường Bến Nghé, Quận 1, Thành phố Hồ Chí Minh",
                    status = CustomerStatus.VIP.name,
                    source = "Đối tác chiến lược",
                    tags = "Khách hàng VIP, Bất động sản, Trọng điểm",
                    estimatedValue = 500_000_000.0,
                    progressPercent = 85,
                    dob = "15/08/1982",
                    notes = "Khách hàng VIP đặc biệt cần chế độ chăm sóc ưu tiên 24/7.",
                    avatarColorHex = "#7C3AED",
                    createdAt = now - 60 * oneDay,
                    updatedAt = now - 1 * oneHour
                )
            )
            customerDao.insertCustomers(sampleCustomers)

            val sampleDeals = listOf(
                DealEntity(
                    id = 1,
                    customerId = 1,
                    title = "Triển khai ERP Cloud Core 100 Người dùng",
                    value = 280_000_000.0,
                    stage = DealStage.WON.name,
                    probability = 100,
                    notes = "Đã ký kết hợp đồng chính thức, tiến hành kick-off vào đầu tháng sau.",
                    createdAt = now - 18 * oneDay
                ),
                DealEntity(
                    id = 2,
                    customerId = 2,
                    title = "Gói Quản lý Bán lẻ & Tích điểm EcoMart",
                    value = 120_000_000.0,
                    stage = DealStage.WON.name,
                    probability = 100,
                    notes = "Đã thanh toán 100% hợp đồng năm đầu tiên.",
                    createdAt = now - 12 * oneDay
                ),
                DealEntity(
                    id = 3,
                    customerId = 3,
                    title = "Hợp đồng Đối tác Kết nối API Giao vận Toàn quốc",
                    value = 450_000_000.0,
                    stage = DealStage.NEGOTIATION.name,
                    probability = 75,
                    notes = "Thảo luận chia sẻ doanh thu và cam kết SLA tốc độ xử lý đơn hàng.",
                    createdAt = now - 25 * oneDay
                ),
                DealEntity(
                    id = 4,
                    customerId = 4,
                    title = "Phần mềm Quản lý Khách hàng Showroom Casa",
                    value = 45_000_000.0,
                    stage = DealStage.PROPOSAL.name,
                    probability = 40,
                    notes = "Đã gửi bảng báo giá kèm ưu đãi tặng 3 tháng sử dụng máy quét mã vạch.",
                    createdAt = now - 4 * oneDay
                ),
                DealEntity(
                    id = 5,
                    customerId = 5,
                    title = "Hệ thống Quản lý Phân phối Dược An Khang",
                    value = 350_000_000.0,
                    stage = DealStage.LEAD.name,
                    probability = 20,
                    notes = "Chuẩn bị tài liệu kỹ thuật tham gia vòng thuyết trình giải pháp.",
                    createdAt = now - 7 * oneDay
                )
            )
            dealDao.insertDeals(sampleDeals)

            val sampleInteractions = listOf(
                InteractionEntity(
                    id = 1,
                    customerId = 1,
                    type = InteractionType.CALL.name,
                    title = "Gọi điện thống nhất lịch họp kick-off dự án",
                    content = "Trao đổi với anh Nam về danh sách nhân sự tham gia đội triển khai dự án ERP.",
                    date = now - 2 * oneHour,
                    outcome = "Anh Nam chốt họp vào sáng thứ 2 tuần tới lúc 9:00 tại trụ sở VinaTech.",
                    followUpDate = now + 4 * oneDay
                ),
                InteractionEntity(
                    id = 2,
                    customerId = 2,
                    type = InteractionType.MEETING.name,
                    title = "Họp đánh giá hiệu quả triển khai tháng đầu tiên",
                    content = "Họp trực tiếp tại văn phòng EcoMart với chị Mai Phương cùng 3 quản lý cửa hàng.",
                    date = now - 2 * oneDay,
                    outcome = "Khách hàng rất hài lòng về tính ổn định, đề xuất tích hợp thêm hóa đơn điện tử.",
                    followUpDate = now + 7 * oneDay
                ),
                InteractionEntity(
                    id = 3,
                    customerId = 3,
                    type = InteractionType.CONTRACT.name,
                    title = "Ký kết thỏa thuận hợp tác đối tác chiến lược",
                    content = "Ký kết biên bản ghi nhớ hợp tác tích hợp API kết nối kho và giao vận logistics.",
                    date = now - 5 * oneDay,
                    outcome = "Hoàn tất thủ tục pháp lý, đội IT bắt đầu test API Sandbox.",
                    followUpDate = now + 10 * oneDay
                ),
                InteractionEntity(
                    id = 4,
                    customerId = 4,
                    type = InteractionType.MESSAGE.name,
                    title = "Nhắn tin Zalo gửi tài liệu và catalog giải pháp",
                    content = "Gửi anh Tuấn tài liệu hướng dẫn quản lý thông tin khách hàng và theo dõi lịch bảo hành nội thất.",
                    date = now - 10 * oneHour,
                    outcome = "Anh Tuấn phản hồi sẽ xem và liên hệ đặt lịch demo.",
                    followUpDate = now + 1 * oneDay
                ),
                InteractionEntity(
                    id = 5,
                    customerId = 5,
                    type = InteractionType.EMAIL.name,
                    title = "Gửi bảng báo giá chi tiết và hồ sơ năng lực",
                    content = "Gửi email báo giá phân hệ phân phối kèm chứng chỉ ISO 27001 và danh sách dự án tương tự.",
                    date = now - 1 * oneDay,
                    outcome = "Chị Thảo xác nhận đã nhận email và chuyển ban kiểm toán thẩm định.",
                    followUpDate = now + 3 * oneDay
                )
            )
            interactionDao.insertInteractions(sampleInteractions)

            val sampleTasks = listOf(
                TaskEntity(
                    id = 1,
                    customerId = 1,
                    title = "Gọi điện tư vấn sản phẩm mới cho Công ty CP Đầu tư X",
                    description = "Trao đổi về các tính năng mới trong bản cập nhật CRM quý 4.",
                    dueDate = now + 1 * oneHour,
                    priority = TaskPriority.HIGH.name,
                    isCompleted = false,
                    taskType = TaskType.CALL_BACK.name,
                    location = "10:00 AM"
                ),
                TaskEntity(
                    id = 2,
                    customerId = 2,
                    title = "Chuẩn bị hồ sơ dự thầu gói thầu xây dựng khu đô thị",
                    description = "Rà soát lại phụ lục bảng giá và chứng chỉ năng lực xây dựng.",
                    dueDate = now + 4 * oneHour,
                    priority = TaskPriority.MEDIUM.name,
                    isCompleted = false,
                    taskType = TaskType.SEND_PROPOSAL.name,
                    location = "14:30 PM"
                ),
                TaskEntity(
                    id = 3,
                    customerId = 3,
                    title = "Gửi email follow-up sau buổi họp với đối tác Nhật Bản",
                    description = "Gửi biên bản tổng kết cuộc họp và kế hoạch thực hiện các mốc tiếp theo.",
                    dueDate = now + 6 * oneHour,
                    priority = TaskPriority.HIGH.name,
                    isCompleted = false,
                    taskType = TaskType.FOLLOW_UP.name,
                    location = "16:00 PM"
                ),
                TaskEntity(
                    id = 4,
                    customerId = 1,
                    title = "Cập nhật báo cáo doanh thu",
                    description = "Tổng hợp dữ liệu doanh số tuần và gửi báo cáo cho ban giám đốc.",
                    dueDate = now + 4 * oneHour,
                    priority = TaskPriority.HIGH.name,
                    isCompleted = false,
                    taskType = TaskType.MEETING.name,
                    location = "14:00 PM"
                ),
                TaskEntity(
                    id = 5,
                    customerId = 2,
                    title = "Gặp khách hàng dự án Alpha",
                    description = "Gặp mặt đại diện khách hàng để thẩm định các yêu cầu bổ sung.",
                    dueDate = now + 6 * oneHour,
                    priority = TaskPriority.MEDIUM.name,
                    isCompleted = false,
                    taskType = TaskType.MEETING.name,
                    location = "16:30 PM"
                )
            )
            taskDao.insertTasks(sampleTasks)
        }
    }
}
