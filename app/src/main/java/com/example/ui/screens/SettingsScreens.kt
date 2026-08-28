package com.example.ui.screens

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.data.model.SeniorityMilestone
import com.example.data.model.defaultSeniorityMilestones
import kotlinx.coroutines.launch
import com.example.R
import com.example.data.model.AttendanceRecord
import com.example.data.model.AttendanceType
import com.example.data.model.EmployeeItem
import com.example.data.model.NotificationSettings
import com.example.data.model.OvertimeRateType
import com.example.data.model.PayrollCalculationResult
import com.example.data.model.PayrollPolicySettings
import com.example.data.model.SecuritySettings
import com.example.data.model.SeniorityResult
import com.example.data.model.UserProfile
import com.example.data.model.calculateEmployeePayroll
import com.example.data.model.calculateSeniority
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.theme.ProfessionalPrimaryNavy
import com.example.ui.viewmodel.CrmViewModel

@Composable
fun SecuritySettingsScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit
) {
    SecurityScreen(viewModel = viewModel, onBack = onBack)
}

@Composable
fun UpgradeAccountScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit
) {
    VipUpgradeScreen(onBack = onBack)
}

enum class SettingsSubScreen {
    MAIN,
    PROFILE_EDIT,
    EMPLOYEES,
    CUSTOMER_TYPES,
    NOTIFICATIONS,
    SECURITY,
    VIP_UPGRADE
}

@Composable
fun SettingsHubScreen(
    viewModel: CrmViewModel,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToQuotes: () -> Unit = {},
    onNavigateToEmployees: () -> Unit,
    onNavigateToTimekeeping: () -> Unit,
    onNavigateToPayroll: () -> Unit,
    onNavigateToSenioritySettings: () -> Unit = {},
    onNavigateToCustomerTypes: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSecurity: () -> Unit,
    onNavigateToUpgrade: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToOverview: () -> Unit,
    onLogout: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FB))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // User Profile Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToEditProfile() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!userProfile.avatarUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = userProfile.avatarUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_user_avatar),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userProfile.fullName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = userProfile.email,
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFEFF6FF)
                    ) {
                        Text(
                            text = userProfile.role,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ProfessionalPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Edit Profile",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Upgrade Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToUpgrade() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFDE047),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Nâng cấp gói Doanh nghiệp",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Mở khóa báo cáo chuyên sâu & không giới hạn nhân viên",
                            color = Color(0xFFDBEAFE),
                            fontSize = 12.sp
                        )
                    }
                    Button(
                        onClick = onNavigateToUpgrade,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = ProfessionalPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Khám phá", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Group 1: DOANH NGHIỆP & BÁO CÁO
        SettingsGroupHeader("DOANH NGHIỆP & BÁO CÁO")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                SettingsRowItem(
                    icon = Icons.Default.Description,
                    title = "Phòng kinh doanh - Báo giá & Tiến độ",
                    subtitle = "Quản lý báo giá, hợp đồng & tiến độ thực hiện dự án",
                    badge = "HOT",
                    badgeBg = Color(0xFFEFF6FF),
                    badgeColor = Color(0xFF1D4ED8),
                    onClick = onNavigateToQuotes
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                SettingsRowItem(
                    icon = Icons.Default.TrendingUp,
                    title = "Báo cáo thống kê hiệu suất",
                    subtitle = "Doanh thu, tỷ lệ chốt deal và tăng trưởng",
                    onClick = onNavigateToReports
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                SettingsRowItem(
                    icon = Icons.Default.SupervisorAccount,
                    title = "Tổng quan tài khoản & KPI",
                    subtitle = "Xem tiến độ đạt chỉ tiêu tháng",
                    onClick = onNavigateToOverview
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                SettingsRowItem(
                    icon = Icons.Default.SupervisorAccount,
                    title = "Quản lý nhân viên",
                    subtitle = "Phân quyền, theo dõi nhóm & cập nhật trạng thái",
                    badge = "VIP",
                    badgeBg = Color(0xFFFEF3C7),
                    badgeColor = Color(0xFFD97706),
                    onClick = onNavigateToEmployees
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                SettingsRowItem(
                    icon = Icons.Default.AccessTime,
                    title = "Chấm công & Điểm danh",
                    subtitle = "Chuẩn 8h/ngày, 26 ngày/tháng, Tăng ca OT",
                    badge = "NEW",
                    badgeBg = Color(0xFFDEF7EC),
                    badgeColor = Color(0xFF047857),
                    onClick = onNavigateToTimekeeping
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                SettingsRowItem(
                    icon = Icons.Default.Payments,
                    title = "Bảng lương & Thâm niên",
                    subtitle = "Tự động tính thâm niên, ngày nghỉ, thưởng 5 năm & KPI",
                    badge = "NEW",
                    badgeBg = Color(0xFFEFF6FF),
                    badgeColor = Color(0xFF1D4ED8),
                    onClick = onNavigateToPayroll
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                SettingsRowItem(
                    icon = Icons.Default.WorkspacePremium,
                    title = "Cài đặt quy chế thâm niên",
                    subtitle = "Tùy chỉnh mốc năm, mức thưởng và số ngày phép tích lũy",
                    badge = "MỚI",
                    badgeBg = Color(0xFFFEF3C7),
                    badgeColor = Color(0xFFD97706),
                    onClick = onNavigateToSenioritySettings
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                SettingsRowItem(
                    icon = Icons.Default.Star,
                    title = "Phân loại khách hàng",
                    subtitle = "Tùy chỉnh nhóm, phân hạng khách hàng",
                    onClick = onNavigateToCustomerTypes
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Group 2: HỆ THỐNG & BẢO MẬT
        SettingsGroupHeader("HỆ THỐNG & BẢO MẬT")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                SettingsRowItem(
                    icon = Icons.Default.Notifications,
                    title = "Cài đặt thông báo",
                    subtitle = "Lịch hẹn, báo giá và nhiệm vụ mới",
                    onClick = onNavigateToNotifications
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                SettingsRowItem(
                    icon = Icons.Default.Security,
                    title = "Bảo mật tài khoản",
                    subtitle = "Đổi mật khẩu, 2FA, sinh trắc học",
                    onClick = onNavigateToSecurity
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logout Button
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFEE2E2),
                contentColor = Color(0xFFDC2626)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Logout",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Đăng xuất tài khoản", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun SettingsMainHost(
    viewModel: CrmViewModel,
    onBackToHome: () -> Unit
) {
    var currentSubScreen by remember { mutableStateOf(SettingsSubScreen.MAIN) }

    when (currentSubScreen) {
        SettingsSubScreen.MAIN -> AccountSettingsScreen(
            viewModel = viewModel,
            onNavigate = { currentSubScreen = it },
            onBack = onBackToHome
        )
        SettingsSubScreen.PROFILE_EDIT -> ProfileEditScreen(
            viewModel = viewModel,
            onBack = { currentSubScreen = SettingsSubScreen.MAIN }
        )
        SettingsSubScreen.EMPLOYEES -> EmployeeManagementScreen(
            viewModel = viewModel,
            onBack = { currentSubScreen = SettingsSubScreen.MAIN }
        )
        SettingsSubScreen.CUSTOMER_TYPES -> CustomerTypesSettingsScreen(
            viewModel = viewModel,
            onBack = { currentSubScreen = SettingsSubScreen.MAIN }
        )
        SettingsSubScreen.NOTIFICATIONS -> NotificationSettingsScreen(
            viewModel = viewModel,
            onBack = { currentSubScreen = SettingsSubScreen.MAIN }
        )
        SettingsSubScreen.SECURITY -> SecurityScreen(
            viewModel = viewModel,
            onBack = { currentSubScreen = SettingsSubScreen.MAIN }
        )
        SettingsSubScreen.VIP_UPGRADE -> VipUpgradeScreen(
            onBack = { currentSubScreen = SettingsSubScreen.MAIN }
        )
    }
}

// 1. Account Settings Screen (#cai_dat_tai_khoan.png)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(
    viewModel: CrmViewModel,
    onNavigate: (SettingsSubScreen) -> Unit,
    onBack: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt tài khoản", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Card Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2E8F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!userProfile.avatarUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = userProfile.avatarUrl,
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_user_avatar),
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(ProfessionalPrimary)
                                .clickable { onNavigate(SettingsSubScreen.PROFILE_EDIT) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit photo",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userProfile.fullName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE2E8F0))
                            .padding(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = userProfile.role,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Thay đổi ảnh đại diện",
                        fontSize = 12.sp,
                        color = ProfessionalPrimary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clickable { onNavigate(SettingsSubScreen.PROFILE_EDIT) }
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // GROUP: TÀI KHOẢN
            SettingsGroupHeader("TÀI KHOẢN")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.Person,
                        title = "Cập nhật thông tin cá nhân",
                        onClick = { onNavigate(SettingsSubScreen.PROFILE_EDIT) }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    SettingsRowItem(
                        icon = Icons.Default.SupervisorAccount,
                        title = "Quản lý nhân viên",
                        onClick = { onNavigate(SettingsSubScreen.EMPLOYEES) }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    SettingsRowItem(
                        icon = Icons.Default.Star,
                        title = "Phân loại khách hàng",
                        onClick = { onNavigate(SettingsSubScreen.CUSTOMER_TYPES) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // GROUP: DỊCH VỤ
            SettingsGroupHeader("DỊCH VỤ")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                SettingsRowItem(
                    icon = Icons.Default.Star,
                    title = "Nâng cấp tài khoản VIP",
                    badge = "VIP",
                    badgeColor = Color(0xFF7C3AED),
                    badgeBg = Color(0xFFEDE9FE),
                    onClick = { onNavigate(SettingsSubScreen.VIP_UPGRADE) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // GROUP: HỆ THỐNG
            SettingsGroupHeader("HỆ THỐNG")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.Notifications,
                        title = "Cài đặt thông báo",
                        onClick = { onNavigate(SettingsSubScreen.NOTIFICATIONS) }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    SettingsRowItem(
                        icon = Icons.Default.Security,
                        title = "Bảo mật",
                        onClick = { onNavigate(SettingsSubScreen.SECURITY) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Logout Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable { viewModel.logout() }
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = Color(0xFFE02424),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Đăng xuất",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE02424)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// Country model for phone number picker
data class CountryDial(
    val code: String,
    val name: String,
    val dialCode: String,
    val flag: String
)

val countryDialList = listOf(
    CountryDial("VN", "Việt Nam", "+84", "🇻🇳"),
    CountryDial("US", "Hoa Kỳ", "+1", "🇺🇸"),
    CountryDial("JP", "Nhật Bản", "+81", "🇯🇵"),
    CountryDial("KR", "Hàn Quốc", "+82", "🇰🇷"),
    CountryDial("SG", "Singapore", "+65", "🇸🇬"),
    CountryDial("GB", "Vương Quốc Anh", "+44", "🇬🇧"),
    CountryDial("AU", "Úc", "+61", "🇦🇺"),
    CountryDial("CN", "Trung Quốc", "+86", "🇨🇳"),
    CountryDial("DE", "Đức", "+49", "🇩🇪"),
    CountryDial("FR", "Pháp", "+33", "🇫🇷"),
    CountryDial("TH", "Thái Lan", "+66", "🇹🇭")
)

// Helper to format date as MM/DD/YYYY
fun formatMmDdYyyy(input: String): String {
    val digits = input.filter { it.isDigit() }.take(8)
    val sb = StringBuilder()
    for (i in digits.indices) {
        sb.append(digits[i])
        if ((i == 1 || i == 3) && i < digits.lastIndex) {
            sb.append("/")
        }
    }
    return sb.toString()
}

// 2. Profile Edit Screen (#cap_nhat_thong_tin_ca_nhan.png)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val currentProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    var name by remember { mutableStateOf(currentProfile.fullName) }
    var email by remember { mutableStateOf(currentProfile.email) }

    var selectedCountry by remember {
        val initialCountry = countryDialList.firstOrNull { currentProfile.phone.startsWith(it.dialCode) } ?: countryDialList[0]
        mutableStateOf(initialCountry)
    }

    val initialPhone = remember(currentProfile.phone, selectedCountry) {
        val withoutDial = currentProfile.phone.removePrefix(selectedCountry.dialCode).trim()
        if (withoutDial.startsWith("0")) withoutDial.dropWhile { it == '0' } else withoutDial
    }
    var phone by remember { mutableStateOf(initialPhone) }

    var dobValue by remember {
        mutableStateOf(TextFieldValue(currentProfile.dob, TextRange(currentProfile.dob.length)))
    }

    var address by remember { mutableStateOf(currentProfile.address) }
    var avatarUri by remember { mutableStateOf(currentProfile.avatarUrl) }
    var expandedCountryDropdown by remember { mutableStateOf(false) }

    fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val dateStr = dobValue.text
        if (dateStr.isNotBlank()) {
            try {
                val parts = dateStr.split("/", "-")
                if (parts.size == 3) {
                    val d = parts[0].trim().toIntOrNull() ?: 1
                    val m = (parts[1].trim().toIntOrNull() ?: 1) - 1
                    val y = parts[2].trim().toIntOrNull() ?: cal.get(Calendar.YEAR)
                    cal.set(y, m, d)
                }
            } catch (_: Exception) {}
        }
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDay = String.format("%02d", selectedDay)
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDate = "$formattedDay/$formattedMonth/$selectedYear"
                dobValue = TextFieldValue(formattedDate, TextRange(formattedDate.length))
            },
            year,
            month,
            day
        ).show()
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            avatarUri = it.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cập nhật thông tin", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0F172A)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF0F172A))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar with camera action button
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .border(2.dp, ProfessionalPrimary, CircleShape)
                        .background(Color(0xFFEFF6FF))
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (!avatarUri.isNullOrBlank()) {
                        AsyncImage(
                            model = avatarUri,
                            contentDescription = "Ảnh đại diện",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_user_avatar),
                            contentDescription = "Ảnh đại diện",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(ProfessionalPrimary)
                        .border(2.dp, Color.White, CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Tải ảnh từ điện thoại",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // 1. Họ và tên: viết hoa mặc định, không cho phép nhập số
                    FormFieldLabel("Họ và tên")
                    OutlinedTextField(
                        value = name,
                        onValueChange = { input ->
                            // Lọc bỏ số, tự động chuyển thành chữ HOA
                            val filtered = input.filter { !it.isDigit() }.uppercase()
                            name = filtered
                        },
                        placeholder = { Text("NGUYỄN VĂN A", color = Color(0xFF94A3B8)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_name_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = customFieldColors(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2. Email: định dạng email chuẩn
                    FormFieldLabel("Email")
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it.trim() },
                        placeholder = { Text("nguyenvana@example.com", color = Color(0xFF94A3B8)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_email_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = customFieldColors(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. Số điện thoại: Dạng number & có tùy chọn quốc gia (bỏ số 0 đầu)
                    FormFieldLabel("Số điện thoại")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Country Selector
                        Box {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .clickable { expandedCountryDropdown = true }
                                    .padding(horizontal = 10.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(selectedCountry.flag, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    selectedCountry.dialCode,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Chọn quốc gia",
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = expandedCountryDropdown,
                                onDismissRequest = { expandedCountryDropdown = false }
                            ) {
                                countryDialList.forEach { country ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(country.flag, fontSize = 18.sp)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    "${country.name} (${country.dialCode})",
                                                    fontSize = 14.sp,
                                                    color = Color(0xFF0F172A),
                                                    fontWeight = if (country.code == selectedCountry.code) FontWeight.Bold else FontWeight.Normal
                                                )
                                            }
                                        },
                                        onClick = {
                                            selectedCountry = country
                                            expandedCountryDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Phone Number Input (bỏ số 0 đầu tiên)
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { input ->
                                // Only allow numeric digits and remove leading 0
                                var digits = input.filter { it.isDigit() }
                                if (digits.startsWith("0")) {
                                    digits = digits.dropWhile { it == '0' }
                                }
                                phone = digits
                            },
                            placeholder = { Text("901234567", color = Color(0xFF94A3B8)) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_phone_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = customFieldColors(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. Ngày sinh: Định dạng DD/MM/YYYY
                    FormFieldLabel("Ngày sinh (dd/mm/yyyy)")
                    OutlinedTextField(
                        value = dobValue,
                        onValueChange = { newTfv ->
                            val newText = newTfv.text
                            // If backspacing
                            if (newText.length < dobValue.text.length) {
                                dobValue = newTfv
                                return@OutlinedTextField
                            }
                            // Take max 8 digits
                            val digits = newText.filter { it.isDigit() }.take(8)
                            val formatted = buildString {
                                for (i in digits.indices) {
                                    append(digits[i])
                                    if ((i == 1 || i == 3) && i < digits.lastIndex) {
                                        append("/")
                                    }
                                }
                            }
                            // Set cursor at the end of formatted string so cursor never rewinds backwards
                            dobValue = TextFieldValue(
                                text = formatted,
                                selection = TextRange(formatted.length)
                            )
                        },
                        placeholder = { Text("dd/mm/yyyy (VD: 25/08/1990)", color = Color(0xFF94A3B8)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_dob_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = customFieldColors(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            IconButton(onClick = { showDatePickerDialog() }) {
                                Icon(
                                    Icons.Default.CalendarMonth,
                                    contentDescription = "Chọn ngày sinh",
                                    tint = ProfessionalPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { showDatePickerDialog() }) {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = "Mở lịch chọn ngày",
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 5. Địa chỉ: Định dạng phù hợp
                    FormFieldLabel("Địa chỉ")
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        placeholder = { Text("123 Đường Lê Lợi, Phường Bến Nghé, Quận 1, TP.HCM", color = Color(0xFF94A3B8)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_address_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = customFieldColors(),
                        singleLine = false,
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val savedPhone = if (phone.startsWith("0")) phone.dropWhile { it == '0' } else phone
                    viewModel.updateUserProfile(
                        UserProfile(
                            fullName = name.ifBlank { currentProfile.fullName },
                            email = email.ifBlank { currentProfile.email },
                            phone = savedPhone,
                            dob = dobValue.text.ifBlank { currentProfile.dob },
                            address = address,
                            role = currentProfile.role,
                            avatarUrl = avatarUri,
                            isVip = currentProfile.isVip
                        )
                    )
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_profile_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
            ) {
                Text(
                    text = "Lưu thay đổi",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// 3. Employee Management Screen (#quan_ly_nhan_vien.png) - Gated for VIP Account
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeManagementScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit,
    onNavigateToTimekeeping: () -> Unit = {},
    onNavigateToPayroll: () -> Unit = {},
    onNavigateToSenioritySettings: () -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val employees by viewModel.employees.collectAsStateWithLifecycle()
    val payrollPolicy by viewModel.payrollPolicy.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf("Tất cả") }
    var selectedDepartmentFilter by remember { mutableStateOf("Tất cả") }

    var showAddEmployeeDialog by remember { mutableStateOf(false) }
    var employeeToEdit by remember { mutableStateOf<EmployeeItem?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf<EmployeeItem?>(null) }

    val departments = listOf("Tất cả", "Phòng Kinh doanh", "Phòng Kỹ thuật", "Phòng Kế toán", "Phòng Marketing")
    val statusFilters = listOf("Tất cả", "Đang làm việc", "Nghỉ phép", "Nghỉ việc")

    val filtered = employees.filter { emp ->
        val matchesSearch = emp.name.contains(searchQuery, ignoreCase = true) ||
                emp.role.contains(searchQuery, ignoreCase = true) ||
                emp.department.contains(searchQuery, ignoreCase = true) ||
                emp.phone.contains(searchQuery)
        val matchesStatus = if (selectedStatusFilter == "Tất cả") true else emp.status.equals(selectedStatusFilter, ignoreCase = true)
        val matchesDept = if (selectedDepartmentFilter == "Tất cả") true else emp.department.equals(selectedDepartmentFilter, ignoreCase = true)
        matchesSearch && matchesStatus && matchesDept
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Quản lý nhân viên", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        if (userProfile.isVip) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFFEF3C7)
                            ) {
                                Text(
                                    text = "VIP ACTIVE",
                                    color = Color(0xFFD97706),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (userProfile.isVip) {
                        IconButton(onClick = onNavigateToTimekeeping) {
                            Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Chấm công", tint = ProfessionalPrimary)
                        }
                        IconButton(onClick = onNavigateToPayroll) {
                            Icon(imageVector = Icons.Default.Payments, contentDescription = "Bảng lương", tint = Color(0xFF047857))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            if (userProfile.isVip) {
                FloatingActionButton(
                    onClick = {
                        employeeToEdit = null
                        showAddEmployeeDialog = true
                    },
                    containerColor = ProfessionalPrimary,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm nhân viên")
                }
            }
        },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        if (!userProfile.isVip) {
            // VIP Activation Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "VIP",
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFFEF3C7)
                ) {
                    Text(
                        text = "👑 TÍNH NĂNG TÀI KHOẢN VIP",
                        color = Color(0xFFB45309),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Quản lý nhân viên, Chấm công & Bảng lương",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Phần quản lý nhân viên, chấm công chuẩn 8h/26 ngày, tăng ca OT và tự động tính thâm niên chỉ được kích hoạt trong tài khoản VIP.",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        VipFeatureBullet(
                            title = "Quản lý trạng thái nhân sự",
                            desc = "Cập nhật Đang làm việc, Nghỉ phép, Nghỉ việc theo phòng ban"
                        )
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        VipFeatureBullet(
                            title = "Ứng dụng chấm công & Tăng ca OT",
                            desc = "Chấm công chuẩn 8h/ngày, 26 ngày/tháng, nhân hệ số làm thêm giờ x1.5 / x2.0 / x3.0"
                        )
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        VipFeatureBullet(
                            title = "Tự động tính thâm niên & Thưởng KPI",
                            desc = "Mỗi 5 năm +1 ngày nghỉ phép, thưởng mốc 5 năm, chu kỳ tiếp theo & cài đặt KPI"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.setVipStatus(true)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD97706)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Kích hoạt tài khoản VIP",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        } else {
            // Unlocked Employee Management Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Quick Navigation Action Cards: Chấm công, Bảng lương & Quy chế thâm niên
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToTimekeeping() },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDBEAFE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccessTime, contentDescription = null, tint = ProfessionalPrimary, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Chấm công", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ProfessionalPrimaryNavy)
                            Text("8h • OT", fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToPayroll() },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA7F3D0))
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD1FAE5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Payments, contentDescription = null, tint = Color(0xFF047857), modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Bảng lương", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF065F46))
                            Text("Lương & KPI", fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToSenioritySettings() },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Thâm niên", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF92400E))
                            Text("Mốc & Phép", fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Tìm tên, SĐT, chức vụ, phòng ban...", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Xóa", tint = Color(0xFF64748B))
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = customFieldColors()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Status Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    statusFilters.forEach { status ->
                        val count = if (status == "Tất cả") employees.size else employees.count { it.status.equals(status, ignoreCase = true) }
                        val isSelected = selectedStatusFilter == status
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) ProfessionalPrimary else Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ProfessionalPrimary else Color(0xFFE2E8F0)),
                            modifier = Modifier.clickable { selectedStatusFilter = status }
                        ) {
                            Text(
                                text = "$status ($count)",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFF334155),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Department Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    departments.forEach { dept ->
                        val isSelected = selectedDepartmentFilter == dept
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFF0F172A) else Color(0xFFEFF4FB),
                            modifier = Modifier.clickable { selectedDepartmentFilter = dept }
                        ) {
                            Text(
                                text = dept,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF475569),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Employee Count Summary
                Text(
                    text = "Hiển thị ${filtered.size} / ${employees.size} nhân viên",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(items = filtered, key = { it.id }) { emp: EmployeeItem ->
                        EmployeeDetailedCardItem(
                            emp = emp,
                            policy = payrollPolicy,
                            onStatusChange = { newStatus ->
                                viewModel.updateEmployeeStatus(emp.id, newStatus)
                            },
                            onEdit = {
                                employeeToEdit = emp
                                showAddEmployeeDialog = true
                            },
                            onDelete = {
                                showDeleteConfirmDialog = emp
                            }
                        )
                    }
                }
            }
        }
    }

    // Add / Edit Employee Dialog
    if (showAddEmployeeDialog) {
        AddEditEmployeeDialog(
            employee = employeeToEdit,
            onDismiss = {
                showAddEmployeeDialog = false
                employeeToEdit = null
            },
            onSave = { name, role, dept, status, phone, email, startDate, baseSalary, allowance, kpiBonus ->
                if (employeeToEdit == null) {
                    viewModel.addEmployee(
                        name = name,
                        role = role,
                        department = dept,
                        status = status,
                        phone = phone,
                        email = email,
                        startDate = startDate,
                        baseSalary = baseSalary,
                        allowance = allowance,
                        kpiBonus = kpiBonus
                    )
                } else {
                    viewModel.updateEmployee(
                        employeeToEdit!!.copy(
                            name = name,
                            role = role,
                            department = dept,
                            status = status,
                            phone = phone,
                            email = email,
                            startDate = startDate,
                            baseSalary = baseSalary,
                            allowance = allowance,
                            kpiBonus = kpiBonus
                        )
                    )
                }
                showAddEmployeeDialog = false
                employeeToEdit = null
            }
        )
    }

    // Delete Confirmation Dialog
    showDeleteConfirmDialog?.let { emp ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = null },
            containerColor = Color.White,
            title = { Text("Xác nhận xóa nhân viên", fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)) },
            text = { Text("Bạn có chắc chắn muốn xóa nhân viên ${emp.name} khỏi hệ thống?", color = Color(0xFF475569)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteEmployee(emp.id)
                        showDeleteConfirmDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Xóa", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = null }) {
                    Text("Hủy", color = Color(0xFF64748B))
                }
            }
        )
    }
}

@Composable
fun EmployeeDetailedCardItem(
    emp: EmployeeItem,
    policy: PayrollPolicySettings,
    onStatusChange: (String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showStatusMenu by remember { mutableStateOf(false) }
    var showActionMenu by remember { mutableStateOf(false) }

    val seniority = remember(emp.startDate, policy) {
        calculateSeniority(emp.startDate, policy)
    }

    val (badgeBg, badgeColor) = when (emp.status) {
        "Đang làm việc" -> Pair(Color(0xFFDEF7EC), Color(0xFF047857))
        "Nghỉ phép" -> Pair(Color(0xFFFEF3C7), Color(0xFFD97706))
        "Nghỉ việc" -> Pair(Color(0xFFF1F5F9), Color(0xFF64748B))
        else -> Pair(Color(0xFFEFF6FF), Color(0xFF2563EB))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Avatar, Name, Role, Status Badge & Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emp.initials,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = emp.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF0F172A)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = emp.role,
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                            Text(" • ", fontSize = 12.sp, color = Color(0xFFCBD5E1))
                            Text(
                                text = emp.department,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = ProfessionalPrimary
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Status Badge with dropdown switcher
                    Box {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = badgeBg,
                            modifier = Modifier.clickable { showStatusMenu = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = emp.status,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = badgeColor
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = badgeColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showStatusMenu,
                            onDismissRequest = { showStatusMenu = false }
                        ) {
                            listOf("Đang làm việc", "Nghỉ phép", "Nghỉ việc").forEach { statusOption ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = statusOption,
                                            fontWeight = if (statusOption == emp.status) FontWeight.Bold else FontWeight.Normal,
                                            color = if (statusOption == emp.status) ProfessionalPrimary else Color(0xFF0F172A)
                                        )
                                    },
                                    onClick = {
                                        onStatusChange(statusOption)
                                        showStatusMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Box {
                        IconButton(onClick = { showActionMenu = true }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                        }
                        DropdownMenu(
                            expanded = showActionMenu,
                            onDismissRequest = { showActionMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Chỉnh sửa thông tin") },
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    showActionMenu = false
                                    onEdit()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Xóa nhân viên", color = Color(0xFFDC2626)) },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    showActionMenu = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(8.dp))

            // Seniority & Policy Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = Color(0xFF475569), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Thâm niên: ${seniority.years} năm ${seniority.months} tháng",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E293B)
                        )
                    }
                    Text(
                        text = "Vào làm: ${emp.startDate}",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.BeachAccess, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Phép năm: ${seniority.totalLeaveDays} ngày (+${seniority.extraLeaveDays})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0369A1)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bonuses & Salary Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Lương cơ bản", fontSize = 10.sp, color = Color(0xFF64748B))
                    Text(String.format("%,d đ", emp.baseSalary.toLong()), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                }

                if (seniority.bonusAmount > 0) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Thưởng thâm niên", fontSize = 10.sp, color = Color(0xFFD97706))
                        Text(String.format("+%,d đ", seniority.bonusAmount.toLong()), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Thưởng KPI", fontSize = 10.sp, color = Color(0xFF059669))
                    Text(String.format("+%,d đ", emp.kpiBonus.toLong()), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                }
            }
        }
    }
}

// Add / Edit Employee Dialog
@Composable
fun AddEditEmployeeDialog(
    employee: EmployeeItem?,
    onDismiss: () -> Unit,
    onSave: (name: String, role: String, dept: String, status: String, phone: String, email: String, startDate: String, baseSalary: Double, allowance: Double, kpiBonus: Double) -> Unit
) {
    var name by remember { mutableStateOf(employee?.name ?: "") }
    var role by remember { mutableStateOf(employee?.role ?: "Nhân viên Kinh doanh") }
    var department by remember { mutableStateOf(employee?.department ?: "Phòng Kinh doanh") }
    var status by remember { mutableStateOf(employee?.status ?: "Đang làm việc") }
    var phone by remember { mutableStateOf(employee?.phone ?: "0901234567") }
    var email by remember { mutableStateOf(employee?.email ?: "nhanvien@crm.vn") }
    var startDate by remember { mutableStateOf(employee?.startDate ?: "01/01/2021") }
    var baseSalaryStr by remember { mutableStateOf(employee?.baseSalary?.toLong()?.toString() ?: "15000000") }
    var allowanceStr by remember { mutableStateOf(employee?.allowance?.toLong()?.toString() ?: "1000000") }
    var kpiBonusStr by remember { mutableStateOf(employee?.kpiBonus?.toLong()?.toString() ?: "2000000") }

    var showDeptDropdown by remember { mutableStateOf(false) }
    var showStatusDropdown by remember { mutableStateOf(false) }

    val departments = listOf("Phòng Kinh doanh", "Phòng Kỹ thuật", "Phòng Kế toán", "Phòng Marketing", "Phòng CSKH")
    val statuses = listOf("Đang làm việc", "Nghỉ phép", "Nghỉ việc")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = if (employee == null) "Thêm Nhân Viên Mới" else "Chỉnh Sửa Nhân Viên",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Họ và tên") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = customFieldColors()
                )

                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Chức vụ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = customFieldColors()
                )

                // Department Selector Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = department,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Phòng ban") },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDeptDropdown = true },
                        colors = customFieldColors()
                    )
                    DropdownMenu(
                        expanded = showDeptDropdown,
                        onDismissRequest = { showDeptDropdown = false }
                    ) {
                        departments.forEach { dept ->
                            DropdownMenuItem(
                                text = { Text(dept) },
                                onClick = {
                                    department = dept
                                    showDeptDropdown = false
                                }
                            )
                        }
                    }
                }

                // Status Selector Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Trạng thái nhân sự") },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showStatusDropdown = true },
                        colors = customFieldColors()
                    )
                    DropdownMenu(
                        expanded = showStatusDropdown,
                        onDismissRequest = { showStatusDropdown = false }
                    ) {
                        statuses.forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st) },
                                onClick = {
                                    status = st
                                    showStatusDropdown = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Số điện thoại") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = customFieldColors()
                    )
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Ngày vào làm") },
                        placeholder = { Text("DD/MM/YYYY") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = customFieldColors()
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = customFieldColors()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = baseSalaryStr,
                        onValueChange = { baseSalaryStr = it },
                        label = { Text("Lương cơ bản (đ)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = customFieldColors()
                    )
                    OutlinedTextField(
                        value = allowanceStr,
                        onValueChange = { allowanceStr = it },
                        label = { Text("Phụ cấp (đ)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = customFieldColors()
                    )
                }

                OutlinedTextField(
                    value = kpiBonusStr,
                    onValueChange = { kpiBonusStr = it },
                    label = { Text("Thưởng KPI (đ)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = customFieldColors()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val baseSalary = baseSalaryStr.toDoubleOrNull() ?: 15000000.0
                        val allowance = allowanceStr.toDoubleOrNull() ?: 1000000.0
                        val kpiBonus = kpiBonusStr.toDoubleOrNull() ?: 2000000.0
                        onSave(name, role, department, status, phone, email, startDate, baseSalary, allowance, kpiBonus)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
            ) {
                Text(if (employee == null) "Thêm nhân viên" else "Lưu cập nhật", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy", color = Color(0xFF64748B)) }
        }
    )
}

// -------------------------------------------------------------
// 3.1 Timekeeping Screen (Chấm công & Điểm danh)
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimekeepingScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit,
    onNavigateToPayroll: () -> Unit = {}
) {
    val employees by viewModel.employees.collectAsStateWithLifecycle()
    val payrollPolicy by viewModel.payrollPolicy.collectAsStateWithLifecycle()
    val attendanceRecords by viewModel.attendanceRecords.collectAsStateWithLifecycle()

    var selectedDate by remember { mutableStateOf("27/08/2026") }
    var selectedDepartment by remember { mutableStateOf("Tất cả") }
    var showBatchConfirmDialog by remember { mutableStateOf(false) }

    val departments = listOf("Tất cả", "Phòng Kinh doanh", "Phòng Kỹ thuật", "Phòng Kế toán", "Phòng Marketing")

    val filteredEmployees = remember(employees, selectedDepartment) {
        if (selectedDepartment == "Tất cả") employees
        else employees.filter { it.department.equals(selectedDepartment, ignoreCase = true) }
    }

    val todayRecords = remember(attendanceRecords, selectedDate) {
        attendanceRecords.filter { it.date == selectedDate }
    }

    val checkedCount = todayRecords.size
    val totalOtHours = todayRecords.sumOf { it.overtimeHours.toDouble() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Chấm công nhân viên", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            text = "Chuẩn 8h/ngày • 26 ngày/tháng • OT theo giờ",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToPayroll) {
                        Icon(imageVector = Icons.Default.Payments, contentDescription = "Bảng lương", tint = Color(0xFF047857))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Date Selector Bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEFF6FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = ProfessionalPrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Ngày chấm công", fontSize = 11.sp, color = Color(0xFF64748B))
                            Text(selectedDate, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedDate == "27/08/2026") ProfessionalPrimary else Color(0xFFF1F5F9),
                            modifier = Modifier.clickable { selectedDate = "27/08/2026" }
                        ) {
                            Text(
                                "Hôm nay",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedDate == "27/08/2026") Color.White else Color(0xFF475569),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedDate == "26/08/2026") ProfessionalPrimary else Color(0xFFF1F5F9),
                            modifier = Modifier.clickable { selectedDate = "26/08/2026" }
                        ) {
                            Text(
                                "Hôm qua",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedDate == "26/08/2026") Color.White else Color(0xFF475569),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Standard Work Benchmark & Stats Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Tiến độ chấm công", fontSize = 11.sp, color = Color(0xFF1E40AF))
                        Text(
                            text = "Đã chấm: $checkedCount / ${filteredEmployees.size} nhân sự",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Tổng giờ OT hôm nay", fontSize = 11.sp, color = Color(0xFF1E40AF))
                        Text(
                            text = "${totalOtHours}h (OT)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD97706)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Batch Check-in & Dept filters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    departments.forEach { dept ->
                        val isSelected = selectedDepartment == dept
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFF0F172A) else Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFF0F172A) else Color(0xFFE2E8F0)),
                            modifier = Modifier.clickable { selectedDepartment = dept }
                        ) {
                            Text(
                                text = dept,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF475569),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { showBatchConfirmDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chấm nhanh", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = filteredEmployees, key = { it.id }) { emp ->
                    val existingRecord = todayRecords.find { it.employeeId == emp.id }
                    AttendanceEmployeeCard(
                        employee = emp,
                        record = existingRecord,
                        policy = payrollPolicy,
                        onSaveAttendance = { type, otHours, otRate, note ->
                            viewModel.recordAttendance(
                                employeeId = emp.id,
                                date = selectedDate,
                                type = type,
                                workHours = 8.0f,
                                overtimeHours = otHours,
                                overtimeRateType = otRate,
                                note = note
                            )
                        }
                    )
                }
            }
        }
    }

    if (showBatchConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showBatchConfirmDialog = false },
            containerColor = Color.White,
            title = { Text("Chấm công nhanh hàng loạt", fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)) },
            text = {
                Text(
                    "Ghi nhận 'Làm việc (Trọn ngày 8h)' cho tất cả ${filteredEmployees.count { it.status == "Đang làm việc" }} nhân viên đang làm việc trong ngày $selectedDate?",
                    color = Color(0xFF475569)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val activeEmps = filteredEmployees.filter { it.status == "Đang làm việc" }
                        viewModel.batchRecordAttendance(
                            employeeIds = activeEmps.map { it.id },
                            date = selectedDate,
                            type = AttendanceType.FULL_WORK,
                            workHours = 8.0f,
                            overtimeHours = 0.0f,
                            overtimeRateType = OvertimeRateType.WEEKDAY,
                            note = "Chấm công hàng loạt"
                        )
                        showBatchConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
                ) {
                    Text("Đồng ý", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBatchConfirmDialog = false }) { Text("Hủy", color = Color(0xFF64748B)) }
            }
        )
    }
}

@Composable
fun AttendanceEmployeeCard(
    employee: EmployeeItem,
    record: AttendanceRecord?,
    policy: PayrollPolicySettings,
    onSaveAttendance: (AttendanceType, Float, OvertimeRateType, String) -> Unit
) {
    var selectedType by remember(record) { mutableStateOf(record?.type ?: AttendanceType.FULL_WORK) }
    var overtimeHours by remember(record) { mutableStateOf(record?.overtimeHours ?: 0.0f) }
    var overtimeRate by remember(record) { mutableStateOf(record?.overtimeRateType ?: OvertimeRateType.WEEKDAY) }
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(employee.initials, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(employee.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                        Text("${employee.role} • ${employee.department}", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                }

                // Current Record Status Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (record != null) Color(0xFFECFDF5) else Color(0xFFFEF3C7)
                ) {
                    Text(
                        text = if (record != null) "✓ ${record.type.label}" else "Chưa chấm",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (record != null) Color(0xFF047857) else Color(0xFFD97706),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Attendance Type Selector Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AttendanceType.entries.forEach { type ->
                    val isSelected = selectedType == type
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) ProfessionalPrimary else Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ProfessionalPrimary else Color(0xFFE2E8F0)),
                        modifier = Modifier.clickable {
                            selectedType = type
                            onSaveAttendance(type, overtimeHours, overtimeRate, "")
                        }
                    ) {
                        Text(
                            text = type.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF334155),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Overtime & Save Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Tăng ca (OT):", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF334155))
                    Spacer(modifier = Modifier.width(6.dp))

                    // OT Quick Selector
                    listOf(0.0f, 1.0f, 1.5f, 2.0f, 3.0f).forEach { hours ->
                        val isSelected = overtimeHours == hours
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) Color(0xFFD97706) else Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFFD97706) else Color(0xFFCBD5E1)),
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .clickable {
                                    overtimeHours = hours
                                    onSaveAttendance(selectedType, hours, overtimeRate, "")
                                }
                        ) {
                            Text(
                                text = if (hours == 0.0f) "0h" else "${hours}h",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFF475569),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // OT Multiplier Selector (if hours > 0)
                if (overtimeHours > 0) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFFEF3C7),
                        modifier = Modifier.clickable {
                            overtimeRate = when (overtimeRate) {
                                OvertimeRateType.WEEKDAY -> OvertimeRateType.WEEKEND
                                OvertimeRateType.WEEKEND -> OvertimeRateType.HOLIDAY
                                OvertimeRateType.HOLIDAY -> OvertimeRateType.WEEKDAY
                            }
                            onSaveAttendance(selectedType, overtimeHours, overtimeRate, "")
                        }
                    ) {
                        Text(
                            text = "Hệ số: x${overtimeRate.multiplier}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3.2 Payroll & Seniority Screen (Bảng lương & Thâm niên)
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollAndSeniorityScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit,
    onNavigateToTimekeeping: () -> Unit = {},
    onNavigateToSenioritySettings: () -> Unit = {}
) {
    val employees by viewModel.employees.collectAsStateWithLifecycle()
    val payrollPolicy by viewModel.payrollPolicy.collectAsStateWithLifecycle()
    val attendanceRecords by viewModel.attendanceRecords.collectAsStateWithLifecycle()

    var showPolicySettingsDialog by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf("08/2026") }

    val payrollResults = remember(employees, attendanceRecords, payrollPolicy, selectedMonth) {
        employees.map { emp ->
            val empRecords = attendanceRecords.filter { it.employeeId == emp.id }
            calculateEmployeePayroll(emp, empRecords, payrollPolicy)
        }
    }

    val totalCompanyPayroll = payrollResults.sumOf { it.totalSalary }
    val totalOtPayout = payrollResults.sumOf { it.overtimeSalary }
    val totalSeniorityBonus = payrollResults.sumOf { it.seniorityResult.bonusAmount }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Bảng lương & Thâm niên", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            text = "Chuẩn 8h/ngày • 26 ngày/tháng • Tự động thâm niên",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToTimekeeping) {
                        Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Chấm công", tint = ProfessionalPrimary)
                    }
                    IconButton(onClick = onNavigateToSenioritySettings) {
                        Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = "Quy chế thâm niên", tint = Color(0xFFD97706))
                    }
                    IconButton(onClick = { showPolicySettingsDialog = true }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Cài đặt hệ số", tint = Color(0xFF0F172A))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Month Header & Policy Settings Quick Button
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFECFDF5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Payments, contentDescription = null, tint = Color(0xFF047857), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Kỳ tính lương", fontSize = 11.sp, color = Color(0xFF64748B))
                            Text("Tháng $selectedMonth", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = onNavigateToSenioritySettings,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF3C7), contentColor = Color(0xFFB45309)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.WorkspacePremium, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Thâm niên", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { showPolicySettingsDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFF6FF), contentColor = ProfessionalPrimary),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Hệ số Lương/OT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Policy Highlights Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tổng quỹ lương tháng:", fontSize = 12.sp, color = Color(0xFF1E40AF))
                        Text(
                            String.format("%,d đ", totalCompanyPayroll.toLong()),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("• Thưởng thâm niên: +${String.format("%,d đ", totalSeniorityBonus.toLong())}", fontSize = 11.sp, color = Color(0xFFD97706))
                        Text("• Chi trả tăng ca: +${String.format("%,d đ", totalOtPayout.toLong())}", fontSize = 11.sp, color = Color(0xFF059669))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Bảng chi tiết lương nhân viên (${payrollResults.size} người)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = payrollResults, key = { it.employee.id }) { result ->
                    EmployeePayrollCard(result = result, policy = payrollPolicy)
                }
            }
        }
    }

    if (showPolicySettingsDialog) {
        PayrollPolicySettingsDialog(
            currentPolicy = payrollPolicy,
            onDismiss = { showPolicySettingsDialog = false },
            onSave = { updatedPolicy ->
                viewModel.updatePayrollPolicy(updatedPolicy)
                showPolicySettingsDialog = false
            }
        )
    }
}

@Composable
fun EmployeePayrollCard(
    result: PayrollCalculationResult,
    policy: PayrollPolicySettings
) {
    val emp = result.employee
    val seniority = result.seniorityResult

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emp.initials, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(emp.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                        Text("${emp.role} • ${emp.department}", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFECFDF5)
                ) {
                    Text(
                        text = "Thực nhận: ${String.format("%,d đ", result.totalSalary.toLong())}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(8.dp))

            // Seniority Info & Days Leave
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "⏳ Thâm niên: ${seniority.years} năm ${seniority.months} tháng (Vào: ${emp.startDate})",
                    fontSize = 11.sp,
                    color = Color(0xFF334155),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "🏖️ Phép: ${seniority.totalLeaveDays} ngày (+${seniority.extraLeaveDays})",
                    fontSize = 11.sp,
                    color = Color(0xFF0284C7),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Payroll Breakdown Grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("• Lương cơ bản tháng (${policy.standardWorkDays} ngày):", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text(String.format("%,d đ", emp.baseSalary.toLong()), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("• Công thực tế (${result.actualWorkDays} / ${policy.standardWorkDays} ngày):", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text(String.format("%,d đ", result.actualWorkSalary.toLong()), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                }
                if (result.totalOvertimeHours > 0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("• Tăng ca OT (${result.totalOvertimeHours} giờ):", fontSize = 11.sp, color = Color(0xFFD97706))
                        Text(String.format("+%,d đ", result.overtimeSalary.toLong()), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                    }
                }
                if (seniority.bonusAmount > 0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("• Thưởng thâm niên:", fontSize = 11.sp, color = Color(0xFFB45309))
                        Text(String.format("+%,d đ", seniority.bonusAmount.toLong()), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                    }
                }
                if (emp.kpiBonus > 0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("• Thưởng KPI:", fontSize = 11.sp, color = Color(0xFF059669))
                        Text(String.format("+%,d đ", emp.kpiBonus.toLong()), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                    }
                }
                if (emp.allowance > 0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("• Phụ cấp:", fontSize = 11.sp, color = Color(0xFF64748B))
                        Text(String.format("+%,d đ", emp.allowance.toLong()), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                    }
                }
            }
        }
    }
}

// Payroll & Seniority Policy Settings Dialog
@Composable
fun PayrollPolicySettingsDialog(
    currentPolicy: PayrollPolicySettings,
    onDismiss: () -> Unit,
    onSave: (PayrollPolicySettings) -> Unit
) {
    var standardHours by remember { mutableStateOf(currentPolicy.standardHoursPerDay.toString()) }
    var standardDays by remember { mutableStateOf(currentPolicy.standardWorkDays.toString()) }
    var otWeekday by remember { mutableStateOf(currentPolicy.otRateWeekday.toString()) }
    var otWeekend by remember { mutableStateOf(currentPolicy.otRateWeekend.toString()) }
    var otHoliday by remember { mutableStateOf(currentPolicy.otRateHoliday.toString()) }
    var enableBonus5Years by remember { mutableStateOf(currentPolicy.enableSeniorityBonus5Years) }
    var bonus5YearsStr by remember { mutableStateOf(currentPolicy.seniorityBonus5Years.toLong().toString()) }
    var intervalYearsStr by remember { mutableStateOf(currentPolicy.seniorityIntervalYears.toString()) }
    var bonusPerIntervalStr by remember { mutableStateOf(currentPolicy.seniorityBonusPerInterval.toLong().toString()) }
    var leaveDaysPer5YearsStr by remember { mutableStateOf(currentPolicy.leaveDaysPer5Years.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text("Cài Đặt Quy Chế Lương & Thâm Niên", fontWeight = FontWeight.Bold, color = Color(0xFF0F172A), fontSize = 17.sp)
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("1. MỐC LÀM VIỆC CHUẨN", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ProfessionalPrimary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = standardHours,
                        onValueChange = { standardHours = it },
                        label = { Text("Giờ chuẩn/ngày") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = customFieldColors()
                    )
                    OutlinedTextField(
                        value = standardDays,
                        onValueChange = { standardDays = it },
                        label = { Text("Ngày chuẩn/tháng") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = customFieldColors()
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text("2. HỆ SỐ TĂNG CA (OT)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ProfessionalPrimary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        value = otWeekday,
                        onValueChange = { otWeekday = it },
                        label = { Text("Ngày thường") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = customFieldColors()
                    )
                    OutlinedTextField(
                        value = otWeekend,
                        onValueChange = { otWeekend = it },
                        label = { Text("Cuối tuần") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = customFieldColors()
                    )
                    OutlinedTextField(
                        value = otHoliday,
                        onValueChange = { otHoliday = it },
                        label = { Text("Ngày lễ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = customFieldColors()
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text("3. QUY CHẾ THÂM NIÊN & NGHỈ PHÉP", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ProfessionalPrimary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Thưởng mốc 5 năm làm việc", fontSize = 13.sp, color = Color(0xFF1E293B))
                    Switch(
                        checked = enableBonus5Years,
                        onCheckedChange = { enableBonus5Years = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ProfessionalPrimary)
                    )
                }

                if (enableBonus5Years) {
                    OutlinedTextField(
                        value = bonus5YearsStr,
                        onValueChange = { bonus5YearsStr = it },
                        label = { Text("Mức thưởng 5 năm (đ)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = customFieldColors()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = intervalYearsStr,
                        onValueChange = { intervalYearsStr = it },
                        label = { Text("Mỗi chu kỳ (năm)") },
                        placeholder = { Text("1, 2 hoặc 3") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = customFieldColors()
                    )
                    OutlinedTextField(
                        value = bonusPerIntervalStr,
                        onValueChange = { bonusPerIntervalStr = it },
                        label = { Text("Thưởng thêm (đ)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1.2f),
                        colors = customFieldColors()
                    )
                }

                OutlinedTextField(
                    value = leaveDaysPer5YearsStr,
                    onValueChange = { leaveDaysPer5YearsStr = it },
                    label = { Text("Tăng ngày nghỉ mỗi 5 năm (ngày)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = customFieldColors()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = currentPolicy.copy(
                        standardHoursPerDay = standardHours.toFloatOrNull() ?: 8.0f,
                        standardWorkDays = standardDays.toIntOrNull() ?: 26,
                        otRateWeekday = otWeekday.toFloatOrNull() ?: 1.5f,
                        otRateWeekend = otWeekend.toFloatOrNull() ?: 2.0f,
                        otRateHoliday = otHoliday.toFloatOrNull() ?: 3.0f,
                        enableSeniorityBonus5Years = enableBonus5Years,
                        seniorityBonus5Years = bonus5YearsStr.toDoubleOrNull() ?: 5000000.0,
                        seniorityIntervalYears = intervalYearsStr.toIntOrNull() ?: 1,
                        seniorityBonusPerInterval = bonusPerIntervalStr.toDoubleOrNull() ?: 1000000.0,
                        leaveDaysPer5Years = leaveDaysPer5YearsStr.toIntOrNull() ?: 1
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
            ) {
                Text("Lưu quy chế", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy", color = Color(0xFF64748B)) }
        }
    )
}

// -------------------------------------------------------------
// 3.3 Seniority Policy Settings Screen (Màn hình Cài đặt Quy chế Thâm niên)
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeniorityPolicySettingsScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit
) {
    val currentPolicy by viewModel.payrollPolicy.collectAsStateWithLifecycle()
    val employees by viewModel.employees.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Form states
    var baseLeaveDays by remember(currentPolicy) { mutableIntStateOf(currentPolicy.baseLeaveDaysPerYear) }
    var leaveAccrualIntervalYears by remember(currentPolicy) { mutableIntStateOf(currentPolicy.leaveAccrualIntervalYears) }
    var leaveDaysPerInterval by remember(currentPolicy) { mutableIntStateOf(currentPolicy.leaveDaysPerInterval) }
    var maxLeaveDays by remember(currentPolicy) { mutableIntStateOf(currentPolicy.maxLeaveDays) }

    var enableMilestoneBonuses by remember(currentPolicy) { mutableStateOf(currentPolicy.enableMilestoneBonuses) }
    var milestones by remember(currentPolicy) { mutableStateOf(currentPolicy.milestones.sortedBy { it.years }) }

    var enableRecurringBonusAfterMax by remember(currentPolicy) { mutableStateOf(currentPolicy.enableRecurringBonusAfterMax) }
    var recurringIntervalYears by remember(currentPolicy) { mutableIntStateOf(currentPolicy.recurringIntervalYears) }
    var recurringBonusPerIntervalStr by remember(currentPolicy) {
        mutableStateOf(currentPolicy.recurringBonusPerInterval.toLong().toString())
    }

    // Milestone Add/Edit Dialog
    var showMilestoneDialog by remember { mutableStateOf(false) }
    var editingMilestoneId by remember { mutableStateOf<Long?>(null) }
    var milestoneYearsInput by remember { mutableStateOf("1") }
    var milestoneBonusInput by remember { mutableStateOf("1000000") }
    var milestoneLeaveDaysInput by remember { mutableStateOf("0") }
    var milestoneTitleInput by remember { mutableStateOf("") }
    var milestoneDescInput by remember { mutableStateOf("") }

    // Delete confirmation
    var milestoneToDelete by remember { mutableStateOf<SeniorityMilestone?>(null) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    // Simulator states
    var simYears by remember { mutableStateOf("5") }
    var simMonths by remember { mutableStateOf("0") }
    var selectedSimEmployee by remember { mutableStateOf<EmployeeItem?>(null) }

    // Active Policy Preview for simulation
    val previewPolicy = remember(
        baseLeaveDays, leaveAccrualIntervalYears, leaveDaysPerInterval, maxLeaveDays,
        enableMilestoneBonuses, milestones, enableRecurringBonusAfterMax,
        recurringIntervalYears, recurringBonusPerIntervalStr, currentPolicy
    ) {
        val recurringBonus = recurringBonusPerIntervalStr.toDoubleOrNull() ?: 1000000.0
        currentPolicy.copy(
            baseLeaveDaysPerYear = baseLeaveDays,
            leaveAccrualIntervalYears = leaveAccrualIntervalYears,
            leaveDaysPerInterval = leaveDaysPerInterval,
            maxLeaveDays = maxLeaveDays,
            enableMilestoneBonuses = enableMilestoneBonuses,
            milestones = milestones.sortedBy { it.years },
            enableRecurringBonusAfterMax = enableRecurringBonusAfterMax,
            recurringIntervalYears = recurringIntervalYears,
            recurringBonusPerInterval = recurringBonus,
            // Keep legacy fields in sync
            leaveDaysPer5Years = leaveDaysPerInterval,
            enableSeniorityBonus5Years = enableMilestoneBonuses,
            seniorityBonus5Years = milestones.firstOrNull { it.years == 5 }?.bonusAmount ?: 5000000.0
        )
    }

    // Simulation calculation
    val simResult = remember(previewPolicy, simYears, simMonths, selectedSimEmployee) {
        if (selectedSimEmployee != null) {
            calculateSeniority(selectedSimEmployee!!.startDate, previewPolicy)
        } else {
            val y = (simYears.toIntOrNull() ?: 0).coerceIn(0, 50)
            val m = (simMonths.toIntOrNull() ?: 0).coerceIn(0, 11)
            val currentYear = 2026
            val currentMonth = 8
            var startYear = currentYear - y
            var startMonth = currentMonth - m
            while (startMonth <= 0) {
                startMonth += 12
                startYear -= 1
            }
            val fakeStartDateStr = String.format("%02d/%02d/%04d", 1, startMonth, startYear)
            calculateSeniority(fakeStartDateStr, previewPolicy)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Cài đặt quy chế thâm niên", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(
                            text = "Mốc năm • Mức thưởng • Ngày phép tích lũy",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showResetConfirmDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Khôi phục mặc định",
                            tint = Color(0xFF64748B)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { showResetConfirmDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF1F5F9),
                            contentColor = Color(0xFF475569)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mặc định", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            viewModel.updatePayrollPolicy(previewPolicy)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Đã lưu và áp dụng quy chế thâm niên mới thành công!")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(2f)
                            .testTag("save_seniority_policy_button")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Lưu quy chế thâm niên", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Overview & Quick Presets
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF3C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Quy chế đãi ngộ thâm niên", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0F172A))
                            Text("Tự động hóa tính thưởng & số ngày phép theo thời gian cống hiến", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Chọn nhanh mẫu quy chế doanh nghiệp:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Preset 1: Luật Lao Động chuẩn
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFEFF6FF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                            modifier = Modifier.clickable {
                                baseLeaveDays = 12
                                leaveAccrualIntervalYears = 5
                                leaveDaysPerInterval = 1
                                maxLeaveDays = 20
                                enableMilestoneBonuses = true
                                milestones = listOf(
                                    SeniorityMilestone(101L, 5, 5000000.0, 1, "Mốc 5 năm - Cống hiến Vàng", "Thưởng vinh danh 5 năm theo quy chuẩn"),
                                    SeniorityMilestone(102L, 10, 10000000.0, 2, "Mốc 10 năm - Kim Cương", "Thưởng tri ân 10 năm gắn bó")
                                )
                                enableRecurringBonusAfterMax = true
                                recurringIntervalYears = 1
                                recurringBonusPerIntervalStr = "1000000"
                            }
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                Text("Chuẩn Luật Lao Động", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF1D4ED8))
                                Text("12 ngày gốc • +1 ngày/5 năm", fontSize = 10.sp, color = Color(0xFF64748B))
                            }
                        }

                        // Preset 2: Doanh nghiệp Công nghệ & Startup
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFECFDF5),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA7F3D0)),
                            modifier = Modifier.clickable {
                                baseLeaveDays = 14
                                leaveAccrualIntervalYears = 3
                                leaveDaysPerInterval = 1
                                maxLeaveDays = 22
                                enableMilestoneBonuses = true
                                milestones = listOf(
                                    SeniorityMilestone(201L, 1, 1000000.0, 0, "Mốc 1 năm", "Gắn kết năm đầu tiên"),
                                    SeniorityMilestone(202L, 3, 3000000.0, 1, "Mốc 3 năm - Đồng hành", "Cống hiến tài năng 3 năm"),
                                    SeniorityMilestone(203L, 5, 6000000.0, 1, "Mốc 5 năm - Vàng", "Đội ngũ nòng cốt"),
                                    SeniorityMilestone(204L, 10, 15000000.0, 2, "Mốc 10 năm - Kim Cương", "Lãnh đạo & chuyên gia kỳ cựu")
                                )
                                enableRecurringBonusAfterMax = true
                                recurringIntervalYears = 1
                                recurringBonusPerIntervalStr = "2000000"
                            }
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                Text("Công Nghệ & Đãi Ngộ Cao", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF047857))
                                Text("14 ngày gốc • +1 ngày/3 năm", fontSize = 10.sp, color = Color(0xFF64748B))
                            }
                        }

                        // Preset 3: Tập đoàn Lớn & Thâm Niên Dài Hạn
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFFBEB),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                            modifier = Modifier.clickable {
                                baseLeaveDays = 12
                                leaveAccrualIntervalYears = 5
                                leaveDaysPerInterval = 1
                                maxLeaveDays = 25
                                enableMilestoneBonuses = true
                                milestones = listOf(
                                    SeniorityMilestone(301L, 1, 1000000.0, 0, "Mốc 1 năm", "Thâm niên năm 1"),
                                    SeniorityMilestone(302L, 3, 3000000.0, 0, "Mốc 3 năm", "Thâm niên năm 3"),
                                    SeniorityMilestone(303L, 5, 5000000.0, 1, "Mốc 5 năm", "Huy hiệu 5 năm"),
                                    SeniorityMilestone(304L, 10, 10000000.0, 2, "Mốc 10 năm", "Huy hiệu 10 năm"),
                                    SeniorityMilestone(305L, 15, 15000000.0, 3, "Mốc 15 năm", "Kỷ niệm chương 15 năm"),
                                    SeniorityMilestone(306L, 20, 25000000.0, 4, "Mốc 20 năm", "Huy chương cống hiến 20 năm")
                                )
                                enableRecurringBonusAfterMax = true
                                recurringIntervalYears = 1
                                recurringBonusPerIntervalStr = "1500000"
                            }
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                Text("Tập Đoàn & Dài Hạn", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFB45309))
                                Text("1-3-5-10-15-20 năm", fontSize = 10.sp, color = Color(0xFF64748B))
                            }
                        }
                    }
                }
            }

            // 2. Section: Annual Leave Accrual Configuration
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.BeachAccess, contentDescription = null, tint = ProfessionalPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("1. QUY TẮC NGÀY NGHỈ PHÉP NĂM & TÍCH LŨY", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = ProfessionalPrimaryNavy)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Base leave days
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Số ngày phép năm cơ bản:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                            Text("Áp dụng cho nhân viên mới vào làm", fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (baseLeaveDays > 1) baseLeaveDays -= 1 },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF1F5F9))
                            ) {
                                Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "$baseLeaveDays ngày",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 10.dp),
                                color = ProfessionalPrimary
                            )
                            IconButton(
                                onClick = { baseLeaveDays += 1 },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF1F5F9))
                            ) {
                                Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF8FAFC))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Accrual rules
                    Text("Quy tắc tăng ngày phép theo thâm niên:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Cứ mỗi", fontSize = 13.sp, color = Color(0xFF475569))
                        OutlinedTextField(
                            value = leaveAccrualIntervalYears.toString(),
                            onValueChange = { leaveAccrualIntervalYears = it.toIntOrNull() ?: 1 },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.width(64.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors()
                        )
                        Text("năm làm việc → Cộng thêm", fontSize = 13.sp, color = Color(0xFF475569))
                        OutlinedTextField(
                            value = leaveDaysPerInterval.toString(),
                            onValueChange = { leaveDaysPerInterval = it.toIntOrNull() ?: 1 },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.width(60.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors()
                        )
                        Text("ngày", fontSize = 13.sp, color = Color(0xFF475569))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Max leave cap
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Giới hạn ngày phép tối đa trong năm:", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF334155))
                            Text("Tránh tích lũy quá mức (0 = không giới hạn)", fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                        OutlinedTextField(
                            value = maxLeaveDays.toString(),
                            onValueChange = { maxLeaveDays = it.toIntOrNull() ?: 0 },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.width(80.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors()
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dynamic Formula Card
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFEFF6FF),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = ProfessionalPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Công thức: Ngày phép = $baseLeaveDays + (Số năm / $leaveAccrualIntervalYears) × $leaveDaysPerInterval ngày (Tối đa ${if (maxLeaveDays > 0) "$maxLeaveDays ngày" else "không giới hạn"}).",
                                fontSize = 11.sp,
                                color = Color(0xFF1E40AF),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // 3. Section: Seniority Milestones & Bonus Tiers
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("2. MỐC NĂM THÂM NIÊN & MỨC THƯỞNG", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = ProfessionalPrimaryNavy)
                        }
                        Switch(
                            checked = enableMilestoneBonuses,
                            onCheckedChange = { enableMilestoneBonuses = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ProfessionalPrimary)
                        )
                    }

                    if (enableMilestoneBonuses) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Danh sách các mốc năm xét thưởng vinh danh cống hiến (${milestones.size} mốc):",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Milestone Cards List
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            milestones.sortedBy { it.years }.forEach { milestone ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFF8FAFC),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(42.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(
                                                        when {
                                                            milestone.years >= 10 -> Color(0xFFFEF3C7)
                                                            milestone.years >= 5 -> Color(0xFFDBEAFE)
                                                            else -> Color(0xFFE2E8F0)
                                                        }
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text(
                                                        text = "${milestone.years}",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 15.sp,
                                                        color = when {
                                                            milestone.years >= 10 -> Color(0xFFB45309)
                                                            milestone.years >= 5 -> Color(0xFF1D4ED8)
                                                            else -> Color(0xFF334155)
                                                        }
                                                    )
                                                    Text(
                                                        text = "NĂM",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 8.sp,
                                                        color = Color(0xFF64748B)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column {
                                                Text(
                                                    text = milestone.title,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    color = Color(0xFF0F172A)
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Text(
                                                        text = String.format("%,d đ", milestone.bonusAmount.toLong()),
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 13.sp,
                                                        color = Color(0xFF059669)
                                                    )
                                                    if (milestone.extraLeaveDays > 0) {
                                                        Surface(
                                                            shape = RoundedCornerShape(4.dp),
                                                            color = Color(0xFFEFF6FF)
                                                        ) {
                                                            Text(
                                                                text = "+${milestone.extraLeaveDays} ngày phép",
                                                                fontSize = 10.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = ProfessionalPrimary,
                                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                                if (milestone.description.isNotBlank()) {
                                                    Text(
                                                        text = milestone.description,
                                                        fontSize = 11.sp,
                                                        color = Color(0xFF94A3B8)
                                                    )
                                                }
                                            }
                                        }

                                        // Actions
                                        Row {
                                            IconButton(
                                                onClick = {
                                                    editingMilestoneId = milestone.id
                                                    milestoneYearsInput = milestone.years.toString()
                                                    milestoneBonusInput = milestone.bonusAmount.toLong().toString()
                                                    milestoneLeaveDaysInput = milestone.extraLeaveDays.toString()
                                                    milestoneTitleInput = milestone.title
                                                    milestoneDescInput = milestone.description
                                                    showMilestoneDialog = true
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = "Sửa", tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                                            }
                                            IconButton(
                                                onClick = { milestoneToDelete = milestone },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Add Milestone Button
                        Button(
                            onClick = {
                                editingMilestoneId = null
                                val nextYearSuggest = (milestones.maxOfOrNull { it.years } ?: 0) + 2
                                milestoneYearsInput = nextYearSuggest.toString()
                                milestoneBonusInput = "5000000"
                                milestoneLeaveDaysInput = "1"
                                milestoneTitleInput = "Mốc $nextYearSuggest năm"
                                milestoneDescInput = "Vinh danh cống hiến $nextYearSuggest năm"
                                showMilestoneDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEFF6FF),
                                contentColor = ProfessionalPrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Thêm mốc thâm niên mới", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 4. Section: Recurring Bonus after Max Milestone
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Celebration, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("3. THƯỞNG ĐỊNH KỲ SAU MỐC CAO NHẤT", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = ProfessionalPrimaryNavy)
                        }
                        Switch(
                            checked = enableRecurringBonusAfterMax,
                            onCheckedChange = { enableRecurringBonusAfterMax = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ProfessionalPrimary)
                        )
                    }

                    if (enableRecurringBonusAfterMax) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Khi nhân viên vượt qua mốc cao nhất (${milestones.maxOfOrNull { it.years } ?: 10} năm), tiếp tục thưởng thêm theo chu kỳ:",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Cứ mỗi", fontSize = 13.sp, color = Color(0xFF475569))
                            OutlinedTextField(
                                value = recurringIntervalYears.toString(),
                                onValueChange = { recurringIntervalYears = it.toIntOrNull() ?: 1 },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.width(60.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = customFieldColors()
                            )
                            Text("năm → Thưởng thêm", fontSize = 13.sp, color = Color(0xFF475569))
                            OutlinedTextField(
                                value = recurringBonusPerIntervalStr,
                                onValueChange = { recurringBonusPerIntervalStr = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = customFieldColors()
                            )
                            Text("đ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                    }
                }
            }

            // 5. Section: Live Seniority Simulator (Công cụ Mô phỏng & Tính thử Trực quan)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Calculate, contentDescription = null, tint = Color(0xFF047857), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("4. CÔNG CỤ TÍNH THỬ & MÔ PHỎNG QUY CHẾ", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF065F46))
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Kiểm tra kết quả ngày phép và mức thưởng theo quy chế đang thiết lập:",
                        fontSize = 12.sp,
                        color = Color(0xFF065F46)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Input simulator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = simYears,
                            onValueChange = {
                                simYears = it
                                selectedSimEmployee = null
                            },
                            label = { Text("Số năm") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors()
                        )
                        OutlinedTextField(
                            value = simMonths,
                            onValueChange = {
                                simMonths = it
                                selectedSimEmployee = null
                            },
                            label = { Text("Số tháng") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = customFieldColors()
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Or select an employee
                    Text("Hoặc chọn nhân viên thực tế để xem:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF047857))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        employees.forEach { emp ->
                            val isSelected = selectedSimEmployee?.id == emp.id
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) Color(0xFF059669) else Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFF059669) else Color(0xFF86EFAC)),
                                modifier = Modifier.clickable {
                                    selectedSimEmployee = if (isSelected) null else emp
                                }
                            ) {
                                Text(
                                    text = emp.name,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else Color(0xFF065F46),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Simulator Result Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (selectedSimEmployee != null) "Kết quả: ${selectedSimEmployee!!.name}" else "Kết quả mô phỏng ($simYears năm $simMonths tháng):",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "${simResult.years} năm ${simResult.months} tháng",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF059669)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("• Mức thưởng thâm niên:", fontSize = 12.sp, color = Color(0xFF475569))
                                Text(
                                    String.format("%,d đ", simResult.bonusAmount.toLong()),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFB45309)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("• Tổng ngày phép năm được hưởng:", fontSize = 12.sp, color = Color(0xFF475569))
                                Text(
                                    "${simResult.totalLeaveDays} ngày (Gốc: $baseLeaveDays + Thâm niên: ${simResult.extraLeaveDays})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = ProfessionalPrimaryNavy
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("• Danh hiệu / Mốc đạt:", fontSize = 12.sp, color = Color(0xFF475569))
                                Text(
                                    simResult.currentMilestone?.title ?: "Chưa đạt mốc",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = Color(0xFF0F172A)
                                )
                            }

                            if (simResult.nextMilestone != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("• Mốc tiếp theo:", fontSize = 12.sp, color = Color(0xFF64748B))
                                    Text(
                                        "${simResult.nextMilestone?.title} (còn ${simResult.monthsToNextMilestone} tháng)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF3B82F6)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 6. Section: Real Employee Roster Seniority Status
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Bảng áp dụng thực tế (${employees.size} nhân sự)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Tự động cập nhật",
                            fontSize = 11.sp,
                            color = Color(0xFF10B981),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        employees.forEach { emp ->
                            val empSeniority = calculateSeniority(emp.startDate, previewPolicy)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF8FAFC),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(emp.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                        Text("${emp.role} • ${emp.department}", fontSize = 11.sp, color = Color(0xFF64748B))
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "${empSeniority.years} năm ${empSeniority.months} tháng",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color(0xFF0284C7)
                                        )
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text(
                                                text = "${empSeniority.totalLeaveDays} ngày phép",
                                                fontSize = 11.sp,
                                                color = Color(0xFF059669),
                                                fontWeight = FontWeight.Medium
                                            )
                                            if (empSeniority.bonusAmount > 0) {
                                                Text(
                                                    text = "+${String.format("%,d đ", empSeniority.bonusAmount.toLong())}",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFD97706)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Dialog: Add / Edit Milestone
    if (showMilestoneDialog) {
        AlertDialog(
            onDismissRequest = { showMilestoneDialog = false },
            containerColor = Color.White,
            title = {
                Text(
                    text = if (editingMilestoneId != null) "Chỉnh sửa Mốc Thâm Niên" else "Thêm Mốc Thâm Niên Mới",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF0F172A)
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = milestoneYearsInput,
                        onValueChange = { milestoneYearsInput = it },
                        label = { Text("Mốc năm làm việc (năm)") },
                        placeholder = { Text("VD: 1, 3, 5, 7, 10") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = customFieldColors()
                    )

                    OutlinedTextField(
                        value = milestoneBonusInput,
                        onValueChange = { milestoneBonusInput = it },
                        label = { Text("Mức thưởng thâm niên (VNĐ)") },
                        placeholder = { Text("VD: 5000000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = customFieldColors()
                    )

                    OutlinedTextField(
                        value = milestoneLeaveDaysInput,
                        onValueChange = { milestoneLeaveDaysInput = it },
                        label = { Text("Số ngày phép thưởng thêm tại mốc này") },
                        placeholder = { Text("VD: 1 hoặc 0") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = customFieldColors()
                    )

                    OutlinedTextField(
                        value = milestoneTitleInput,
                        onValueChange = { milestoneTitleInput = it },
                        label = { Text("Tên danh hiệu vinh danh") },
                        placeholder = { Text("VD: Mốc 5 năm - Cống hiến Vàng") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = customFieldColors()
                    )

                    OutlinedTextField(
                        value = milestoneDescInput,
                        onValueChange = { milestoneDescInput = it },
                        label = { Text("Mô tả / Ý nghĩa vinh danh") },
                        placeholder = { Text("VD: Vinh danh sự cống hiến và đồng hành") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = customFieldColors()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val y = milestoneYearsInput.toIntOrNull() ?: 1
                        val b = milestoneBonusInput.toDoubleOrNull() ?: 0.0
                        val l = milestoneLeaveDaysInput.toIntOrNull() ?: 0
                        val title = if (milestoneTitleInput.isNotBlank()) milestoneTitleInput else "Mốc $y năm"
                        val desc = milestoneDescInput

                        if (editingMilestoneId != null) {
                            milestones = milestones.map {
                                if (it.id == editingMilestoneId) {
                                    it.copy(
                                        years = y,
                                        bonusAmount = b,
                                        extraLeaveDays = l,
                                        title = title,
                                        description = desc
                                    )
                                } else it
                            }.sortedBy { it.years }
                        } else {
                            val newMilestone = SeniorityMilestone(
                                id = System.currentTimeMillis(),
                                years = y,
                                bonusAmount = b,
                                extraLeaveDays = l,
                                title = title,
                                description = desc
                            )
                            milestones = (milestones + newMilestone).sortedBy { it.years }
                        }
                        showMilestoneDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
                ) {
                    Text("Lưu mốc", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showMilestoneDialog = false }) {
                    Text("Hủy", color = Color(0xFF64748B))
                }
            }
        )
    }

    // Dialog: Delete Confirmation
    if (milestoneToDelete != null) {
        AlertDialog(
            onDismissRequest = { milestoneToDelete = null },
            containerColor = Color.White,
            title = { Text("Xác nhận xóa mốc thâm niên", fontWeight = FontWeight.Bold) },
            text = { Text("Bạn có chắc chắn muốn xóa ${milestoneToDelete!!.title} (${milestoneToDelete!!.years} năm)?") },
            confirmButton = {
                Button(
                    onClick = {
                        milestones = milestones.filter { it.id != milestoneToDelete!!.id }
                        milestoneToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Xóa mốc", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { milestoneToDelete = null }) {
                    Text("Hủy", color = Color(0xFF64748B))
                }
            }
        )
    }

    // Dialog: Reset Defaults Confirmation
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            containerColor = Color.White,
            title = { Text("Khôi phục quy chế mặc định", fontWeight = FontWeight.Bold) },
            text = { Text("Tất cả mốc thâm niên, mức thưởng và quy tắc ngày phép sẽ được đưa về giá trị chuẩn mặc định của công ty.") },
            confirmButton = {
                Button(
                    onClick = {
                        baseLeaveDays = 12
                        leaveAccrualIntervalYears = 5
                        leaveDaysPerInterval = 1
                        maxLeaveDays = 20
                        enableMilestoneBonuses = true
                        milestones = defaultSeniorityMilestones
                        enableRecurringBonusAfterMax = true
                        recurringIntervalYears = 1
                        recurringBonusPerIntervalStr = "1000000"
                        showResetConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
                ) {
                    Text("Khôi phục", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("Hủy", color = Color(0xFF64748B))
                }
            }
        )
    }
}

// 4. Notification Settings Screen (#cai_dat_thong_bao.png)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit
) {
    val settings by viewModel.notificationSettings.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt thông báo", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            SettingsGroupHeader("THÔNG BÁO ĐẨY")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    ToggleSettingItem(
                        title = "Công việc mới",
                        subtitle = "Nhận thông báo khi được giao việc",
                        checked = settings.newTask,
                        onCheckedChange = { viewModel.updateNotificationSettings(settings.copy(newTask = it)) }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    ToggleSettingItem(
                        title = "Nhắc nhở hạn chót",
                        subtitle = "Cảnh báo sắp đến hạn công việc",
                        checked = settings.deadlineReminder,
                        onCheckedChange = { viewModel.updateNotificationSettings(settings.copy(deadlineReminder = it)) }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    ToggleSettingItem(
                        title = "Cập nhật khách hàng",
                        subtitle = "Thay đổi trạng thái hồ sơ",
                        checked = settings.customerUpdate,
                        onCheckedChange = { viewModel.updateNotificationSettings(settings.copy(customerUpdate = it)) }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    ToggleSettingItem(
                        title = "Thông báo hệ thống",
                        subtitle = "Cập nhật ứng dụng và bảo trì",
                        checked = settings.systemNotice,
                        onCheckedChange = { viewModel.updateNotificationSettings(settings.copy(systemNotice = it)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SettingsGroupHeader("EMAIL")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                ToggleSettingItem(
                    title = "Báo cáo hàng tuần",
                    subtitle = "Tóm tắt hiệu suất thứ Hai",
                    checked = settings.weeklyReportEmail,
                    onCheckedChange = { viewModel.updateNotificationSettings(settings.copy(weeklyReportEmail = it)) }
                )
            }
        }
    }
}

// 5. Security Screen (#bao_mat.png)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit
) {
    val secSettings by viewModel.securitySettings.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bảo mật", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            SettingsGroupHeader("XÁC THỰC")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.VpnKey,
                        title = "Đổi mật khẩu",
                        subtitle = "Cập nhật mật khẩu định kỳ",
                        onClick = { }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    ToggleSettingItem(
                        title = "Xác thực 2 yếu tố (2FA)",
                        subtitle = "Tăng cường bảo mật đăng nhập",
                        checked = secSettings.twoFactorAuth,
                        onCheckedChange = { viewModel.updateSecuritySettings(secSettings.copy(twoFactorAuth = it)) }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    ToggleSettingItem(
                        title = "Đăng nhập sinh trắc học",
                        subtitle = "Sử dụng Face ID hoặc Vân tay",
                        checked = secSettings.biometricAuth,
                        onCheckedChange = { viewModel.updateSecuritySettings(secSettings.copy(biometricAuth = it)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingsGroupHeader("LỊCH SỬ ĐĂNG NHẬP")
                Text(
                    text = "Đăng xuất tất cả",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFE02424),
                    modifier = Modifier
                        .clickable { }
                        .padding(bottom = 6.dp)
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("iPhone 14 Pro", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                            Text("Hà Nội, Việt Nam • 192.168.1.1", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFDEF7EC))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Hiện tại", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("MacBook Pro M2", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                            Text("TP. Hồ Chí Minh • 192.168.1.5", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                    }
                }
            }
        }
    }
}

// 6. VIP Upgrade Screen (#nang_cap_tai_khoan.png)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VipUpgradeScreen(
    onBack: () -> Unit
) {
    var selectedPlan by remember { mutableStateOf(1) } // 0: Month, 1: Year

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nâng cấp VIP", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // VIP Enterprise Hero Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF1E3A8A), Color(0xFF2563EB), Color(0xFF3B82F6))
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFDE047),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "VIP Enterprise",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Mở khóa toàn bộ tính năng CRM cao cấp, báo cáo chuyên sâu và hỗ trợ ưu tiên 24/7.",
                        fontSize = 13.sp,
                        color = Color(0xFFDBEAFE),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "ĐẶC QUYỀN VIP",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    VipPerkItem(
                        icon = Icons.Default.TrendingUp,
                        title = "Báo cáo chuyên sâu",
                        desc = "Phân tích tự động doanh thu & tỷ lệ chuyển đổi"
                    )
                    VipPerkItem(
                        icon = Icons.Default.Person,
                        title = "Không giới hạn liên hệ",
                        desc = "Lưu trữ không giới hạn khách hàng & giao dịch"
                    )
                    VipPerkItem(
                        icon = Icons.Default.HeadsetMic,
                        title = "Hỗ trợ 24/7 Priority",
                        desc = "Đội ngũ chuyên viên tư vấn riêng xử lý ngay lập tức"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "GÓI DỊCH VỤ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Pricing Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Month Plan
                PlanCard(
                    modifier = Modifier.weight(1f),
                    title = "Gói Tháng",
                    price = "499k",
                    period = "/tháng",
                    subtitle = "Thanh toán linh hoạt",
                    isSelected = selectedPlan == 0,
                    onClick = { selectedPlan = 0 }
                )

                // Year Plan
                PlanCard(
                    modifier = Modifier.weight(1f),
                    title = "Gói Năm",
                    price = "4.990k",
                    period = "/năm",
                    subtitle = "Tiết kiệm 20%",
                    badge = "TIẾT KIỆM 20%",
                    isSelected = selectedPlan == 1,
                    onClick = { selectedPlan = 1 }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
            ) {
                Text(
                    text = "TIẾN HÀNH THANH TOÁN",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun VipPerkItem(icon: ImageVector, title: String, desc: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFEDE9FE)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF7C3AED), modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
            Text(desc, fontSize = 12.sp, color = Color(0xFF64748B))
        }
    }
}

@Composable
fun PlanCard(
    modifier: Modifier = Modifier,
    title: String,
    price: String,
    period: String,
    subtitle: String,
    badge: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) ProfessionalPrimary else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFF0F6FF) else Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            if (badge != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFDEF7EC))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(badge, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(price, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Text(period, fontSize = 11.sp, color = Color(0xFF64748B), modifier = Modifier.padding(bottom = 2.dp))
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 11.sp, color = Color(0xFF64748B))
        }
    }
}

@Composable
fun SettingsGroupHeader(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF64748B),
        letterSpacing = 1.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    )
}

@Composable
fun SettingsRowItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    badge: String? = null,
    badgeColor: Color = Color.Unspecified,
    badgeBg: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF475569),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0F172A))
                if (subtitle != null) {
                    Text(text = subtitle, fontSize = 12.sp, color = Color(0xFF64748B))
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (badge != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(badge, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = badgeColor)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
fun ToggleSettingItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0F172A))
            Text(text = subtitle, fontSize = 12.sp, color = Color(0xFF64748B))
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ProfessionalPrimary
            )
        )
    }
}

@Composable
fun VipFeatureBullet(
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(Color(0xFFFEF3C7)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFFD97706),
                modifier = Modifier.size(12.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 12.5.sp,
                color = Color(0xFF64748B),
                lineHeight = 17.sp
            )
        }
    }
}

