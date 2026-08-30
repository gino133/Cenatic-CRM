package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.CustomerEntity
import com.example.data.model.DealEntity
import com.example.data.model.InteractionEntity
import com.example.data.model.TaskEntity
import com.example.ui.components.AddEditCustomerDialog
import com.example.ui.components.AddEditDealDialog
import com.example.ui.components.AddEditTaskDialog
import com.example.ui.components.LogInteractionDialog
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.viewmodel.CrmViewModel

enum class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("Trang chủ", Icons.Filled.Home, Icons.Outlined.Home),
    CUSTOMERS("Khách hàng", Icons.Filled.People, Icons.Outlined.People),
    TASKS("Công việc", Icons.Filled.Checklist, Icons.Outlined.Checklist),
    ACCOUNT("Tài khoản", Icons.Filled.Person, Icons.Outlined.Person)
}

enum class FullScreenMode {
    NONE,
    LOGIN,
    ADD_CORPORATE_CUSTOMER,
    ADD_INDIVIDUAL_CUSTOMER,
    QUOTES_AND_PROJECTS,
    CREATE_QUOTE,
    REPORTS,
    ACCOUNT_OVERVIEW,
    EMPLOYEES,
    TIMEKEEPING,
    PAYROLL,
    CUSTOMER_TYPES,
    NOTIFICATIONS,
    SECURITY,
    UPGRADE,
    EDIT_PROFILE,
    SENIORITY_SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: CrmViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    var currentNavIndex by remember { mutableIntStateOf(0) }
    var fullScreenMode by remember { mutableStateOf(FullScreenMode.NONE) }
    var quotesInitialTab by remember { mutableIntStateOf(0) }

    val selectedCustomerId by viewModel.selectedCustomerId.collectAsStateWithLifecycle()
    val allCustomers by viewModel.allRawCustomers.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    // Dialog & Edit States
    var customerToEdit by remember { mutableStateOf<CustomerEntity?>(null) }
    var quoteToEdit by remember { mutableStateOf<com.example.data.model.QuoteItem?>(null) }
    var showAddDealDialog by remember { mutableStateOf(false) }
    var dealToEdit by remember { mutableStateOf<DealEntity?>(null) }
    var preselectedCustomerIdForDeal by remember { mutableStateOf<Long?>(null) }

    var showAddTaskDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<TaskEntity?>(null) }
    var preselectedCustomerIdForTask by remember { mutableStateOf<Long?>(null) }

    var showLogInteractionDialog by remember { mutableStateOf(false) }
    var interactionCustomerId by remember { mutableStateOf<Long?>(null) }
    var interactionCustomerName by remember { mutableStateOf("") }

    if (!isLoggedIn) {
        LoginScreen(
            userPreferences = viewModel.userPreferences,
            onLoginSuccess = { email, name ->
                viewModel.login(email, name)
            }
        )
        return
    }

    // Handle system back button for full screen modes
    BackHandler(enabled = fullScreenMode != FullScreenMode.NONE) {
        when (fullScreenMode) {
            FullScreenMode.TIMEKEEPING,
            FullScreenMode.PAYROLL,
            FullScreenMode.SENIORITY_SETTINGS -> {
                fullScreenMode = FullScreenMode.EMPLOYEES
            }
            FullScreenMode.CREATE_QUOTE -> {
                quoteToEdit = null
                fullScreenMode = FullScreenMode.QUOTES_AND_PROJECTS
            }
            else -> {
                customerToEdit = null
                fullScreenMode = FullScreenMode.NONE
            }
        }
    }

    // Handle system back button for selected customer
    BackHandler(enabled = fullScreenMode == FullScreenMode.NONE && selectedCustomerId != null) {
        viewModel.selectCustomer(null)
    }

    // Handle back button for bottom navigation tabs to return to home
    BackHandler(enabled = fullScreenMode == FullScreenMode.NONE && selectedCustomerId == null && currentNavIndex != 0) {
        currentNavIndex = 0
    }

    // Full screen sub-pages
    when (fullScreenMode) {
        FullScreenMode.ADD_CORPORATE_CUSTOMER -> {
            AddCustomerFormScreen(
                isCorporate = true,
                existingCustomer = customerToEdit,
                viewModel = viewModel,
                onBack = {
                    customerToEdit = null
                    fullScreenMode = FullScreenMode.NONE
                },
                onSaveCustomer = { customer ->
                    viewModel.saveCustomer(customer)
                    customerToEdit = null
                    fullScreenMode = FullScreenMode.NONE
                },
                onNavigateToTab = { tabIndex ->
                    customerToEdit = null
                    currentNavIndex = tabIndex
                    fullScreenMode = FullScreenMode.NONE
                },
                onOpenCustomerTypesSettings = {
                    fullScreenMode = FullScreenMode.CUSTOMER_TYPES
                }
            )
            return
        }
        FullScreenMode.ADD_INDIVIDUAL_CUSTOMER -> {
            AddCustomerFormScreen(
                isCorporate = false,
                existingCustomer = customerToEdit,
                viewModel = viewModel,
                onBack = {
                    customerToEdit = null
                    fullScreenMode = FullScreenMode.NONE
                },
                onSaveCustomer = { customer ->
                    viewModel.saveCustomer(customer)
                    customerToEdit = null
                    fullScreenMode = FullScreenMode.NONE
                },
                onNavigateToTab = { tabIndex ->
                    customerToEdit = null
                    currentNavIndex = tabIndex
                    fullScreenMode = FullScreenMode.NONE
                },
                onOpenCustomerTypesSettings = {
                    fullScreenMode = FullScreenMode.CUSTOMER_TYPES
                }
            )
            return
        }
        FullScreenMode.QUOTES_AND_PROJECTS -> {
            QuotesAndProjectsScreen(
                viewModel = viewModel,
                initialTab = quotesInitialTab,
                onBack = { fullScreenMode = FullScreenMode.NONE },
                onOpenProfile = { fullScreenMode = FullScreenMode.EDIT_PROFILE },
                onCreateQuote = {
                    quoteToEdit = null
                    fullScreenMode = FullScreenMode.CREATE_QUOTE
                },
                onEditQuote = { quote ->
                    quoteToEdit = quote
                    fullScreenMode = FullScreenMode.CREATE_QUOTE
                }
            )
            return
        }
        FullScreenMode.CREATE_QUOTE -> {
            CreateQuoteScreen(
                viewModel = viewModel,
                quoteToEdit = quoteToEdit,
                onBack = {
                    quoteToEdit = null
                    fullScreenMode = FullScreenMode.NONE
                },
                onNavigateToTab = { _ ->
                    quoteToEdit = null
                    fullScreenMode = FullScreenMode.QUOTES_AND_PROJECTS
                }
            )
            return
        }
        FullScreenMode.REPORTS -> {
            ReportsAnalyticsScreen(
                viewModel = viewModel,
                onBack = { fullScreenMode = FullScreenMode.NONE },
                onNavigateToTab = { tabIndex ->
                    fullScreenMode = FullScreenMode.NONE
                    currentNavIndex = tabIndex
                },
                onOpenProfile = {
                    fullScreenMode = FullScreenMode.EDIT_PROFILE
                }
            )
            return
        }
        FullScreenMode.ACCOUNT_OVERVIEW -> {
            AccountOverviewScreen(
                viewModel = viewModel,
                onBack = { fullScreenMode = FullScreenMode.NONE },
                onNavigateToUpgrade = { fullScreenMode = FullScreenMode.UPGRADE }
            )
            return
        }
        FullScreenMode.EDIT_PROFILE -> {
            ProfileEditScreen(
                viewModel = viewModel,
                onBack = { fullScreenMode = FullScreenMode.NONE }
            )
            return
        }
        FullScreenMode.EMPLOYEES -> {
            EmployeeManagementScreen(
                viewModel = viewModel,
                onBack = { fullScreenMode = FullScreenMode.NONE },
                onNavigateToTimekeeping = { fullScreenMode = FullScreenMode.TIMEKEEPING },
                onNavigateToPayroll = { fullScreenMode = FullScreenMode.PAYROLL },
                onNavigateToSenioritySettings = { fullScreenMode = FullScreenMode.SENIORITY_SETTINGS }
            )
            return
        }
        FullScreenMode.TIMEKEEPING -> {
            TimekeepingScreen(
                viewModel = viewModel,
                onBack = { fullScreenMode = FullScreenMode.EMPLOYEES },
                onNavigateToPayroll = { fullScreenMode = FullScreenMode.PAYROLL }
            )
            return
        }
        FullScreenMode.PAYROLL -> {
            PayrollAndSeniorityScreen(
                viewModel = viewModel,
                onBack = { fullScreenMode = FullScreenMode.EMPLOYEES },
                onNavigateToTimekeeping = { fullScreenMode = FullScreenMode.TIMEKEEPING },
                onNavigateToSenioritySettings = { fullScreenMode = FullScreenMode.SENIORITY_SETTINGS }
            )
            return
        }
        FullScreenMode.SENIORITY_SETTINGS -> {
            SeniorityPolicySettingsScreen(
                viewModel = viewModel,
                onBack = { fullScreenMode = FullScreenMode.EMPLOYEES }
            )
            return
        }
        FullScreenMode.CUSTOMER_TYPES -> {
            CustomerTypesSettingsScreen(
                viewModel = viewModel,
                onBack = { fullScreenMode = FullScreenMode.NONE }
            )
            return
        }
        FullScreenMode.NOTIFICATIONS -> {
            NotificationSettingsScreen(
                viewModel = viewModel,
                onBack = { fullScreenMode = FullScreenMode.NONE }
            )
            return
        }
        FullScreenMode.SECURITY -> {
            SecuritySettingsScreen(
                viewModel = viewModel,
                onBack = { fullScreenMode = FullScreenMode.NONE }
            )
            return
        }
        FullScreenMode.UPGRADE -> {
            UpgradeAccountScreen(
                viewModel = viewModel,
                onBack = { fullScreenMode = FullScreenMode.NONE }
            )
            return
        }
        else -> {}
    }

    // If customer is selected for detail sheet/screen
    if (selectedCustomerId != null) {
        CustomerDetailScreen(
            viewModel = viewModel,
            onBack = { viewModel.selectCustomer(null) },
            onEditCustomer = { cust ->
                customerToEdit = cust
                if (cust.isCorporate) {
                    fullScreenMode = FullScreenMode.ADD_CORPORATE_CUSTOMER
                } else {
                    fullScreenMode = FullScreenMode.ADD_INDIVIDUAL_CUSTOMER
                }
            },
            onAddDealForCustomer = { custId ->
                preselectedCustomerIdForDeal = custId
                dealToEdit = null
                showAddDealDialog = true
            },
            onLogInteractionForCustomer = { custId, custName ->
                interactionCustomerId = custId
                interactionCustomerName = custName
                showLogInteractionDialog = true
            },
            onAddTaskForCustomer = { custId ->
                preselectedCustomerIdForTask = custId
                taskToEdit = null
                showAddTaskDialog = true
            }
        )
    } else {
        Scaffold(
            topBar = {
                val currentItem = NavigationItem.entries[currentNavIndex]
                if (currentItem == NavigationItem.HOME) {
                    // Custom Home Header matching screen.png: Avatar, "Tổng quan" and Notification Bell
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { fullScreenMode = FullScreenMode.EDIT_PROFILE }
                            ) {
                                // Avatar image supporting uploaded avatar or default drawable
                                if (!userProfile.avatarUrl.isNullOrBlank()) {
                                    AsyncImage(
                                        model = userProfile.avatarUrl,
                                        contentDescription = "Ảnh đại diện",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .border(1.dp, Color(0xFFCBD5E1), CircleShape)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_user_avatar),
                                        contentDescription = "Ảnh đại diện",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .border(1.dp, Color(0xFFCBD5E1), CircleShape)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Tổng quan",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F2B5C)
                                )
                            }

                            IconButton(
                                onClick = { fullScreenMode = FullScreenMode.NOTIFICATIONS },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.TopEnd) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsNone,
                                        contentDescription = "Thông báo",
                                        tint = Color(0xFF1E3A8A),
                                        modifier = Modifier.size(26.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 1.dp, end = 1.dp)
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFEF4444))
                                    )
                                }
                            }
                        }
                    }
                } else {
                    TopAppBar(
                        title = {
                            Text(
                                text = when (currentItem) {
                                    NavigationItem.HOME -> "CRM Doanh Nghiệp"
                                    NavigationItem.CUSTOMERS -> "Danh Sách Khách Hàng"
                                    NavigationItem.TASKS -> "Quản Lý Công Việc"
                                    NavigationItem.ACCOUNT -> "Cài Đặt Tài Khoản"
                                },
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White,
                            titleContentColor = Color(0xFF0F172A)
                        )
                    )
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 4.dp
                ) {
                    NavigationItem.entries.forEachIndexed { index, item ->
                        val isSelected = currentNavIndex == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentNavIndex = index },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color(0xFFEFF6FF),
                                selectedIconColor = ProfessionalPrimary,
                                selectedTextColor = ProfessionalPrimary,
                                unselectedIconColor = Color(0xFF64748B),
                                unselectedTextColor = Color(0xFF64748B)
                            ),
                            modifier = Modifier.testTag("nav_item_${item.name.lowercase()}")
                        )
                    }
                }
            },
            floatingActionButton = {
                if (currentNavIndex == 1) {
                    // Customers tab FAB
                    FloatingActionButton(
                        onClick = {
                            customerToEdit = null
                            fullScreenMode = FullScreenMode.ADD_CORPORATE_CUSTOMER
                        },
                        containerColor = ProfessionalPrimary,
                        contentColor = Color.White,
                        modifier = Modifier.testTag("main_fab")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Thêm khách hàng")
                    }
                } else if (currentNavIndex == 2) {
                    // Tasks tab FAB
                    FloatingActionButton(
                        onClick = {
                            taskToEdit = null
                            preselectedCustomerIdForTask = null
                            showAddTaskDialog = true
                        },
                        containerColor = ProfessionalPrimary,
                        contentColor = Color.White,
                        modifier = Modifier.testTag("main_fab")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Thêm công việc")
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentNavIndex,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "screen_transition"
                ) { targetIndex ->
                    when (NavigationItem.entries[targetIndex]) {
                        NavigationItem.HOME -> DashboardScreen(
                            viewModel = viewModel,
                            onNavigateToCustomers = { currentNavIndex = 1 },
                            onNavigateToQuotes = {
                                quotesInitialTab = 0
                                fullScreenMode = FullScreenMode.QUOTES_AND_PROJECTS
                            },
                            onNavigateToProjects = {
                                quotesInitialTab = 1
                                fullScreenMode = FullScreenMode.QUOTES_AND_PROJECTS
                            },
                            onNavigateToTasks = { currentNavIndex = 2 },
                            onOpenCustomerDetail = { id -> viewModel.selectCustomer(id) },
                            onOpenProfile = { fullScreenMode = FullScreenMode.EDIT_PROFILE },
                            onOpenReports = { fullScreenMode = FullScreenMode.REPORTS },
                            onAddCustomerClick = {
                                customerToEdit = null
                                fullScreenMode = FullScreenMode.ADD_CORPORATE_CUSTOMER
                            },
                            onAddQuoteClick = {
                                quoteToEdit = null
                                fullScreenMode = FullScreenMode.CREATE_QUOTE
                            },
                            onLogNoteClick = {
                                if (allCustomers.isNotEmpty()) {
                                    val first = allCustomers.first()
                                    interactionCustomerId = first.id
                                    interactionCustomerName = first.name
                                    showLogInteractionDialog = true
                                }
                            }
                        )
                        NavigationItem.CUSTOMERS -> CustomersScreen(
                            viewModel = viewModel,
                            onCustomerClick = { id -> viewModel.selectCustomer(id) },
                            onEditCustomer = { cust ->
                                customerToEdit = cust
                                if (cust.isCorporate) {
                                    fullScreenMode = FullScreenMode.ADD_CORPORATE_CUSTOMER
                                } else {
                                    fullScreenMode = FullScreenMode.ADD_INDIVIDUAL_CUSTOMER
                                }
                            },
                            onAddCorporateClick = {
                                customerToEdit = null
                                fullScreenMode = FullScreenMode.ADD_CORPORATE_CUSTOMER
                            },
                            onAddIndividualClick = {
                                customerToEdit = null
                                fullScreenMode = FullScreenMode.ADD_INDIVIDUAL_CUSTOMER
                            }
                        )
                        NavigationItem.TASKS -> TasksScreen(
                            viewModel = viewModel,
                            onEditTask = { task ->
                                taskToEdit = task
                                preselectedCustomerIdForTask = task.customerId
                                showAddTaskDialog = true
                            },
                            onCustomerClick = { id -> viewModel.selectCustomer(id) },
                            onAddTaskClick = {
                                taskToEdit = null
                                preselectedCustomerIdForTask = null
                                showAddTaskDialog = true
                            },
                            onLogInteractionClick = {
                                if (allCustomers.isNotEmpty()) {
                                    val first = allCustomers.first()
                                    interactionCustomerId = first.id
                                    interactionCustomerName = first.name
                                    showLogInteractionDialog = true
                                }
                            }
                        )
                        NavigationItem.ACCOUNT -> SettingsHubScreen(
                            viewModel = viewModel,
                            onNavigateToEditProfile = { fullScreenMode = FullScreenMode.EDIT_PROFILE },
                            onNavigateToQuotes = {
                                quotesInitialTab = 0
                                fullScreenMode = FullScreenMode.QUOTES_AND_PROJECTS
                            },
                            onNavigateToEmployees = { fullScreenMode = FullScreenMode.EMPLOYEES },
                            onNavigateToTimekeeping = { fullScreenMode = FullScreenMode.TIMEKEEPING },
                            onNavigateToPayroll = { fullScreenMode = FullScreenMode.PAYROLL },
                            onNavigateToSenioritySettings = { fullScreenMode = FullScreenMode.SENIORITY_SETTINGS },
                            onNavigateToCustomerTypes = { fullScreenMode = FullScreenMode.CUSTOMER_TYPES },
                            onNavigateToNotifications = { fullScreenMode = FullScreenMode.NOTIFICATIONS },
                            onNavigateToSecurity = { fullScreenMode = FullScreenMode.SECURITY },
                            onNavigateToUpgrade = { fullScreenMode = FullScreenMode.UPGRADE },
                            onNavigateToReports = { fullScreenMode = FullScreenMode.REPORTS },
                            onNavigateToOverview = { fullScreenMode = FullScreenMode.ACCOUNT_OVERVIEW },
                            onLogout = { viewModel.logout() }
                        )
                    }
                }
            }
        }
    }

    if (showAddDealDialog || dealToEdit != null) {
        AddEditDealDialog(
            customers = allCustomers,
            preselectedCustomerId = preselectedCustomerIdForDeal,
            deal = dealToEdit,
            onDismiss = {
                showAddDealDialog = false
                dealToEdit = null
                preselectedCustomerIdForDeal = null
            },
            onSave = { savedDeal ->
                viewModel.saveDeal(savedDeal)
                showAddDealDialog = false
                dealToEdit = null
                preselectedCustomerIdForDeal = null
            }
        )
    }

    if (showAddTaskDialog || taskToEdit != null) {
        AddEditTaskDialog(
            customers = allCustomers,
            preselectedCustomerId = preselectedCustomerIdForTask,
            task = taskToEdit,
            onDismiss = {
                showAddTaskDialog = false
                taskToEdit = null
                preselectedCustomerIdForTask = null
            },
            onSave = { savedTask ->
                viewModel.saveTask(savedTask)
                showAddTaskDialog = false
                taskToEdit = null
                preselectedCustomerIdForTask = null
            }
        )
    }

    if (showLogInteractionDialog && interactionCustomerId != null) {
        LogInteractionDialog(
            customerId = interactionCustomerId!!,
            customerName = interactionCustomerName,
            onDismiss = {
                showLogInteractionDialog = false
                interactionCustomerId = null
                interactionCustomerName = ""
            },
            onSave = { interaction ->
                viewModel.logInteraction(interaction)
                showLogInteractionDialog = false
                interactionCustomerId = null
                interactionCustomerName = ""
            }
        )
    }
}
