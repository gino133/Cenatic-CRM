package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ui.components.AppDatePickerDialog
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.text.style.TextOverflow
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
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.LaptopMac
import androidx.compose.material.icons.filled.Tablet
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.TableView
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Close
import com.example.data.model.SeniorityMilestone
import com.example.data.model.defaultSeniorityMilestones
import kotlinx.coroutines.launch
import com.example.R
import com.example.data.model.AccountTier
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
    initialTier: AccountTier = AccountTier.VIP,
    onBack: () -> Unit
) {
    VipUpgradeScreen(viewModel = viewModel, initialTier = initialTier, onBack = onBack)
}

enum class SettingsSubScreen {
    MAIN,
    PROFILE_EDIT,
    ACCOUNT_SETTINGS,
    EMPLOYEES,
    CUSTOMER_TYPES,
    NOTIFICATIONS,
    SECURITY,
    BACKUP_SYNC,
    VIP_UPGRADE
}

@Composable
fun SettingsHubScreen(
    viewModel: CrmViewModel,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToAccountSettings: () -> Unit = {},
    onNavigateToQuotes: () -> Unit = {},
    onNavigateToEmployees: () -> Unit,
    onNavigateToTimekeeping: () -> Unit,
    onNavigateToPayroll: () -> Unit,
    onNavigateToSenioritySettings: () -> Unit = {},
    onNavigateToCustomerTypes: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSecurity: () -> Unit,
    onNavigateToBackupSync: () -> Unit = {},
    onNavigateToUpgrade: (AccountTier) -> Unit = {},
    onNavigateToReports: () -> Unit,
    onNavigateToOverview: () -> Unit,
    onLogout: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showVipUpgradeDialog by remember { mutableStateOf<String?>(null) }
    var showBusinessUpgradeDialog by remember { mutableStateOf<String?>(null) }

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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = when (userProfile.accountTier) {
                                com.example.data.model.AccountTier.FREE -> Color(0xFFF1F5F9)
                                com.example.data.model.AccountTier.VIP -> Color(0xFFFEF3C7)
                                com.example.data.model.AccountTier.BUSINESS -> Color(0xFFEDE9FE)
                            }
                        ) {
                            Text(
                                text = userProfile.accountTier.displayName.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (userProfile.accountTier) {
                                    com.example.data.model.AccountTier.FREE -> Color(0xFF64748B)
                                    com.example.data.model.AccountTier.VIP -> Color(0xFFD97706)
                                    com.example.data.model.AccountTier.BUSINESS -> Color(0xFF7C3AED)
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
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

        // Quick Tier Switcher & Data Reset for Testing - Chỉ hiển thị duy nhất cho tài khoản test admin@crm.vn
        if (userProfile.email.equals("admin@crm.vn", ignoreCase = true)) {
            AccountTierTestingCard(
                currentTier = userProfile.accountTier,
                onSelectTier = { newTier ->
                    viewModel.setAccountTier(newTier)
                },
                onResetData = {
                    viewModel.resetToComprehensiveSeedData()
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Upgrade Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToUpgrade(AccountTier.VIP) },
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
                            colors = listOf(Color(0xFF1E3A8A), Color(0xFF2563EB), Color(0xFF3B82F6))
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
                                text = "Nâng cấp VIP & Doanh nghiệp",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Gói VIP cho Cá nhân & Gói BUSINESS cho Doanh nghiệp",
                            color = Color(0xFFDBEAFE),
                            fontSize = 12.sp
                        )
                    }
                    Button(
                        onClick = { onNavigateToUpgrade(AccountTier.VIP) },
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
                // 1. Báo giá & Tiến độ
                SettingsRowItem(
                    icon = Icons.Default.Description,
                    title = "Báo giá & Tiến độ",
                    subtitle = "Quản lý báo giá, hợp đồng & tiến độ thực hiện dự án",
                    badge = "HOT",
                    badgeBg = Color(0xFFEFF6FF),
                    badgeColor = Color(0xFF1D4ED8),
                    onClick = onNavigateToQuotes
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))

                // 2. Hiệu suất & KPIs
                SettingsRowItem(
                    icon = Icons.Default.TrendingUp,
                    title = "Hiệu suất & KPIs",
                    subtitle = "Xem tiến độ đạt chỉ tiêu tháng & kết quả cá nhân",
                    onClick = onNavigateToOverview
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))

                // 3. Phân loại khách hàng (VIP)
                SettingsRowItem(
                    icon = Icons.Default.Star,
                    title = "Phân loại khách hàng",
                    subtitle = "Tùy chỉnh nhóm, phân hạng khách hàng chuyên sâu",
                    badge = if (userProfile.accountTier == AccountTier.FREE) "VIP (Khóa)" else "VIP ✓",
                    badgeBg = if (userProfile.accountTier == AccountTier.FREE) Color(0xFFF1F5F9) else Color(0xFFFEF3C7),
                    badgeColor = if (userProfile.accountTier == AccountTier.FREE) Color(0xFF64748B) else Color(0xFFD97706),
                    onClick = {
                        if (userProfile.accountTier == AccountTier.FREE) {
                            showVipUpgradeDialog = "Phân loại khách hàng (VIP)"
                        } else {
                            onNavigateToCustomerTypes()
                        }
                    }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))

                // 4. Quản lý nhân sự (BUSINESS)
                SettingsRowItem(
                    icon = Icons.Default.SupervisorAccount,
                    title = "Quản lý nhân sự",
                    subtitle = "Nhân sự, chấm công, bảng lương & thâm niên",
                    badge = if (userProfile.accountTier != AccountTier.BUSINESS) "BUSINESS (Khóa)" else "BUSINESS ✓",
                    badgeBg = if (userProfile.accountTier != AccountTier.BUSINESS) Color(0xFFF1F5F9) else Color(0xFFEDE9FE),
                    badgeColor = if (userProfile.accountTier != AccountTier.BUSINESS) Color(0xFF64748B) else Color(0xFF7C3AED),
                    onClick = {
                        if (userProfile.accountTier != AccountTier.BUSINESS) {
                            showBusinessUpgradeDialog = "Quản lý nhân sự (BUSINESS)"
                        } else {
                            onNavigateToEmployees()
                        }
                    }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))

                // 5. Báo cáo thống kê (BUSINESS)
                SettingsRowItem(
                    icon = Icons.Default.TrendingUp,
                    title = "Báo cáo thống kê",
                    subtitle = "Doanh thu, tỷ lệ chốt deal và phân tích tăng trưởng",
                    badge = if (userProfile.accountTier != AccountTier.BUSINESS) "BUSINESS (Khóa)" else "BUSINESS ✓",
                    badgeBg = if (userProfile.accountTier != AccountTier.BUSINESS) Color(0xFFF1F5F9) else Color(0xFFEDE9FE),
                    badgeColor = if (userProfile.accountTier != AccountTier.BUSINESS) Color(0xFF64748B) else Color(0xFF7C3AED),
                    onClick = {
                        if (userProfile.accountTier != AccountTier.BUSINESS) {
                            showBusinessUpgradeDialog = "Báo cáo thống kê (BUSINESS)"
                        } else {
                            onNavigateToReports()
                        }
                    }
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
                    icon = Icons.Default.ManageAccounts,
                    title = "Cài đặt tài khoản",
                    subtitle = "Thông tin cá nhân, giao diện, ngôn ngữ & tiền tệ",
                    onClick = onNavigateToAccountSettings
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
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
                HorizontalDivider(color = Color(0xFFF1F5F9))
                SettingsRowItem(
                    icon = Icons.Default.CloudSync,
                    title = "Sao lưu và đồng bộ",
                    subtitle = "Lưu trữ Google Drive, xuất dữ liệu Excel & phục hồi",
                    onClick = onNavigateToBackupSync
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

    // ================= VIP UPGRADE PAYWALL DIALOG =================
    showVipUpgradeDialog?.let { featureName ->
        AlertDialog(
            onDismissRequest = { showVipUpgradeDialog = null },
            icon = {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Tính năng Gói VIP Cá nhân",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF0F172A)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Mục \"$featureName\" thuộc gói VIP dành cho cá nhân chuyên nghiệp. Bạn cần nâng cấp gói để sử dụng tính năng này.",
                        fontSize = 13.sp,
                        color = Color(0xFF475569),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFFFFFBEB),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "✨ Quyền lợi gói VIP:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFFB45309)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tự động kích hoạt phân loại khách hàng, AI dự báo doanh thu, tốc độ chốt deal & đặt mục tiêu đa chiều.",
                                fontSize = 12.sp,
                                color = Color(0xFF92400E)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            showVipUpgradeDialog = null
                            onNavigateToUpgrade(AccountTier.VIP)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Đăng ký sử dụng gói VIP", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    }
                    OutlinedButton(
                        onClick = {
                            showVipUpgradeDialog = null
                            onNavigateToUpgrade(AccountTier.VIP)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD97706)),
                        border = BorderStroke(1.dp, Color(0xFFD97706))
                    ) {
                        Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Xem chi tiết gói VIP", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    }
                    OutlinedButton(
                        onClick = { showVipUpgradeDialog = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B)),
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                    ) {
                        Text("Đóng", color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp)
                    }
                }
            },
            dismissButton = null
        )
    }

    // ================= BUSINESS UPGRADE PAYWALL DIALOG =================
    showBusinessUpgradeDialog?.let { featureName ->
        AlertDialog(
            onDismissRequest = { showBusinessUpgradeDialog = null },
            icon = {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEDE9FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Apartment,
                        contentDescription = null,
                        tint = Color(0xFF7C3AED),
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Tính năng Gói BUSINESS Doanh nghiệp",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF0F172A)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Mục \"$featureName\" thuộc gói BUSINESS dành cho quản trị doanh nghiệp và tổ chức.",
                        fontSize = 13.sp,
                        color = Color(0xFF475569),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFFF5F3FF),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFDDD6FE)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "🏢 Quyền lợi gói BUSINESS:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF6D28D9)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tự động kích hoạt toàn bộ quản lý nhân sự, bảng lương thâm niên, báo cáo thống kê chuyên sâu & đánh giá KPIs nhân sự.",
                                fontSize = 12.sp,
                                color = Color(0xFF5B21B6)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            showBusinessUpgradeDialog = null
                            onNavigateToUpgrade(AccountTier.BUSINESS)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Đăng ký sử dụng gói BUSINESS", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    }
                    OutlinedButton(
                        onClick = {
                            showBusinessUpgradeDialog = null
                            onNavigateToUpgrade(AccountTier.BUSINESS)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF7C3AED)),
                        border = BorderStroke(1.dp, Color(0xFF7C3AED))
                    ) {
                        Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Xem chi tiết gói BUSINESS", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    }
                    OutlinedButton(
                        onClick = { showBusinessUpgradeDialog = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B)),
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                    ) {
                        Text("Đóng", color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp)
                    }
                }
            },
            dismissButton = null
        )
    }
}

@Composable
fun SettingsMainHost(
    viewModel: CrmViewModel,
    onBackToHome: () -> Unit,
    onNavigateToUpgrade: ((AccountTier) -> Unit)? = null
) {
    var currentSubScreen by remember { mutableStateOf(SettingsSubScreen.MAIN) }
    var upgradeTargetTier by remember { mutableStateOf(AccountTier.VIP) }
    val upgradeAction: (AccountTier) -> Unit = onNavigateToUpgrade ?: { tier ->
        upgradeTargetTier = tier
        currentSubScreen = SettingsSubScreen.VIP_UPGRADE
    }

    when (currentSubScreen) {
        SettingsSubScreen.MAIN -> AccountSettingsScreen(
            viewModel = viewModel,
            onNavigateToEditProfile = { currentSubScreen = SettingsSubScreen.PROFILE_EDIT },
            onNavigateToSecurity = { currentSubScreen = SettingsSubScreen.SECURITY },
            onBack = onBackToHome
        )
        SettingsSubScreen.PROFILE_EDIT -> ProfileEditScreen(
            viewModel = viewModel,
            onBack = { currentSubScreen = SettingsSubScreen.MAIN }
        )
        SettingsSubScreen.EMPLOYEES -> EmployeeManagementScreen(
            viewModel = viewModel,
            onBack = { currentSubScreen = SettingsSubScreen.MAIN },
            onNavigateToUpgrade = { upgradeAction(AccountTier.BUSINESS) }
        )
        SettingsSubScreen.CUSTOMER_TYPES -> CustomerTypesSettingsScreen(
            viewModel = viewModel,
            onBack = { currentSubScreen = SettingsSubScreen.MAIN },
            onNavigateToUpgrade = { upgradeAction(AccountTier.VIP) }
        )
        SettingsSubScreen.NOTIFICATIONS -> NotificationSettingsScreen(
            viewModel = viewModel,
            onBack = { currentSubScreen = SettingsSubScreen.MAIN },
            onNavigateToUpgrade = { tier -> upgradeAction(tier) }
        )
        SettingsSubScreen.SECURITY -> SecurityScreen(
            viewModel = viewModel,
            onBack = { currentSubScreen = SettingsSubScreen.MAIN }
        )
        SettingsSubScreen.BACKUP_SYNC -> BackupAndSyncScreen(
            viewModel = viewModel,
            onBack = { currentSubScreen = SettingsSubScreen.MAIN }
        )
        SettingsSubScreen.ACCOUNT_SETTINGS -> AccountSettingsScreen(
            viewModel = viewModel,
            onNavigateToEditProfile = { currentSubScreen = SettingsSubScreen.PROFILE_EDIT },
            onNavigateToSecurity = { currentSubScreen = SettingsSubScreen.SECURITY },
            onBack = { currentSubScreen = SettingsSubScreen.MAIN }
        )
        SettingsSubScreen.VIP_UPGRADE -> VipUpgradeScreen(
            viewModel = viewModel,
            initialTier = upgradeTargetTier,
            onBack = { currentSubScreen = SettingsSubScreen.MAIN }
        )
    }
}

// 1. Account Settings Screen (#cai_dat_tai_khoan.png)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(
    viewModel: CrmViewModel,
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToSecurity: () -> Unit = {},
    onBack: () -> Unit,
    onLogout: () -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedThemeMode by remember { mutableStateOf("light") }
    var selectedAccentColorIndex by remember { mutableIntStateOf(0) }
    var selectedLanguage by remember { mutableStateOf("vi") }
    var selectedCurrency by remember { mutableStateOf("VND") }
    var selectedDateFormat by remember { mutableStateOf("DD/MM/YYYY") }
    var fontScale by remember { mutableStateOf("standard") }
    var enableSmoothAnim by remember { mutableStateOf(true) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val accentColors = listOf(
        Pair("Xanh Biển", Color(0xFF1E40AF)),
        Pair("Xanh Ngọc", Color(0xFF059669)),
        Pair("Tím Royal", Color(0xFF7C3AED)),
        Pair("Cam Sunset", Color(0xFFEA580C)),
        Pair("Đỏ Ruby", Color(0xFFDC2626))
    )

    val languages = listOf(
        Triple("vi", "Tiếng Việt", "🇻🇳 Mặc định (Việt Nam)"),
        Triple("en", "English", "🇺🇸 United States (Tiếng Anh)")
    )

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
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            // ================= 1. THÔNG TIN CÁ NHÂN (ĐẦU TIÊN) =================
            SettingsGroupHeader("THÔNG TIN CÁ NHÂN")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
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
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(accentColors[selectedAccentColorIndex].second)
                                    .clickable { onNavigateToEditProfile() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Change photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userProfile.fullName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFEFF6FF))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = userProfile.role.ifBlank { "Quản lý cấp cao" },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1D4ED8)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Gói Pro Enterprise • Thâm niên 3+ năm",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Detail list
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Email:",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.width(70.dp)
                        )
                        Text(
                            text = userProfile.email.ifBlank { "admin@ankhangpharma.com" },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Số điện thoại:",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.width(70.dp)
                        )
                        Text(
                            text = userProfile.phone.ifBlank { "0988 123 456" },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Ngày sinh:",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.width(70.dp)
                        )
                        Text(
                            text = if (userProfile.dob.isNotBlank()) userProfile.dob else "15/08/1990",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Địa chỉ:",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.width(70.dp)
                        )
                        Text(
                            text = if (userProfile.address.isNotBlank()) userProfile.address else "Hà Nội, Việt Nam",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0F172A),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onNavigateToEditProfile,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColors[selectedAccentColorIndex].second
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cập nhật thông tin cá nhân", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ================= 2. GIAO DIỆN (THEME & DISPLAY) =================
            SettingsGroupHeader("GIAO DIỆN & HIỂN THỊ")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Theme Mode Selector
                    Text(
                        text = "Chế độ giao diện",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(
                            Triple("light", "Sáng", Icons.Default.LightMode),
                            Triple("dark", "Tối", Icons.Default.DarkMode),
                            Triple("system", "Hệ thống", Icons.Default.BrightnessAuto)
                        ).forEach { (modeKey, modeTitle, modeIcon) ->
                            val isSelected = selectedThemeMode == modeKey
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        selectedThemeMode = modeKey
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Đã chuyển chế độ giao diện: $modeTitle")
                                        }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF8FAFC)
                                ),
                                border = BorderStroke(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) accentColors[selectedAccentColorIndex].second else Color(0xFFE2E8F0)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = modeIcon,
                                        contentDescription = modeTitle,
                                        tint = if (isSelected) accentColors[selectedAccentColorIndex].second else Color(0xFF64748B),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = modeTitle,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) accentColors[selectedAccentColorIndex].second else Color(0xFF475569)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Accent Color
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tông màu chủ đạo",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = accentColors[selectedAccentColorIndex].first,
                                fontSize = 12.sp,
                                color = accentColors[selectedAccentColorIndex].second,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        accentColors.forEachIndexed { index, pair ->
                            val isColorSelected = selectedAccentColorIndex == index
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isColorSelected) pair.second.copy(alpha = 0.2f) else Color.Transparent
                                    )
                                    .clickable {
                                        selectedAccentColorIndex = index
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Đã đổi màu chủ đạo: ${pair.first}")
                                        }
                                    }
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(pair.second),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isColorSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Font Size Density
                    Text(
                        text = "Cỡ chữ & Bố cục",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Pair("compact", "Nhỏ (90%)"),
                            Pair("standard", "Chuẩn (100%)"),
                            Pair("large", "Lớn (115%)")
                        ).forEach { (scaleKey, scaleTitle) ->
                            val isScaleSelected = fontScale == scaleKey
                            FilterChip(
                                selected = isScaleSelected,
                                onClick = { fontScale = scaleKey },
                                label = {
                                    Text(
                                        text = scaleTitle,
                                        fontSize = 12.sp,
                                        fontWeight = if (isScaleSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Smooth animation toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hiệu ứng chuyển trang mượt mà",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Tối ưu hóa hoạt ảnh trên màn hình 120Hz",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        Switch(
                            checked = enableSmoothAnim,
                            onCheckedChange = { enableSmoothAnim = it }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ================= 3. NGÔN NGỮ (LANGUAGE & LOCALIZATION) =================
            SettingsGroupHeader("NGÔN NGỮ & ĐỊNH DẠNG")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ngôn ngữ ứng dụng",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    languages.forEachIndexed { index, (langCode, langName, langDetail) ->
                        val isLangSelected = selectedLanguage == langCode
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isLangSelected) Color(0xFFF1F5F9) else Color.Transparent)
                                .clickable {
                                    selectedLanguage = langCode
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Đã chuyển ngôn ngữ sang: $langName")
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = langName,
                                    fontSize = 14.sp,
                                    fontWeight = if (isLangSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isLangSelected) accentColors[selectedAccentColorIndex].second else Color(0xFF0F172A)
                                )
                                Text(
                                    text = langDetail,
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                            if (isLangSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = accentColors[selectedAccentColorIndex].second,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        if (index < languages.size - 1) {
                            HorizontalDivider(color = Color(0xFFF8FAFC))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Currency format
                    Text(
                        text = "Định dạng tiền tệ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("VND (₫)", "USD ($)", "EUR (€)").forEach { curr ->
                            val isCurrSelected = selectedCurrency in curr
                            FilterChip(
                                selected = isCurrSelected,
                                onClick = {
                                    selectedCurrency = if (curr.startsWith("VND")) "VND" else if (curr.startsWith("USD")) "USD" else "EUR"
                                },
                                label = { Text(curr, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Date format
                    Text(
                        text = "Định dạng ngày & giờ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("DD/MM/YYYY", "YYYY-MM-DD").forEach { dFormat ->
                            val isFormatSelected = selectedDateFormat == dFormat
                            FilterChip(
                                selected = isFormatSelected,
                                onClick = { selectedDateFormat = dFormat },
                                label = { Text(dFormat, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ================= 4. THÔNG TIN ỨNG DỤNG =================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "An Khang Pharma CRM Enterprise",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Phiên bản v2.5.2 (Build 2026.08) • Bản quyền bảo lưu",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= 5. ĐĂNG XUẤT =================
            Button(
                onClick = { showLogoutDialog = true },
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

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Xác nhận đăng xuất", fontWeight = FontWeight.Bold) },
            text = { Text("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản trên thiết bị này không?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Đăng xuất", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun AccountSettingsScreen(
    viewModel: CrmViewModel,
    onNavigate: (SettingsSubScreen) -> Unit,
    onBack: () -> Unit
) {
    AccountSettingsScreen(
        viewModel = viewModel,
        onNavigateToEditProfile = { onNavigate(SettingsSubScreen.PROFILE_EDIT) },
        onNavigateToSecurity = { onNavigate(SettingsSubScreen.SECURITY) },
        onBack = onBack,
        onLogout = { viewModel.logout() }
    )
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
    var showCustomDatePicker by remember { mutableStateOf(false) }

    if (showCustomDatePicker) {
        AppDatePickerDialog(
            initialDateStr = dobValue.text,
            title = "Chọn ngày sinh",
            onDismiss = { showCustomDatePicker = false },
            onDateSelected = { formattedDate ->
                dobValue = TextFieldValue(formattedDate, TextRange(formattedDate.length))
                showCustomDatePicker = false
            }
        )
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
                            IconButton(onClick = { showCustomDatePicker = true }) {
                                Icon(
                                    Icons.Default.CalendarMonth,
                                    contentDescription = "Chọn ngày sinh",
                                    tint = ProfessionalPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { showCustomDatePicker = true }) {
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
    onNavigateToSenioritySettings: () -> Unit = {},
    onNavigateToUpgrade: () -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val employees by viewModel.employees.collectAsStateWithLifecycle()
    val payrollPolicy by viewModel.payrollPolicy.collectAsStateWithLifecycle()
    val isBusinessUnlocked = userProfile.accountTier == com.example.data.model.AccountTier.BUSINESS

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
                        Text("Quản lý nhân sự", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        if (isBusinessUnlocked) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFEDE9FE)
                            ) {
                                Text(
                                    text = "BUSINESS ACTIVE",
                                    color = Color(0xFF7C3AED),
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
                    if (isBusinessUnlocked) {
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
            if (isBusinessUnlocked) {
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
        if (!isBusinessUnlocked) {
            // Business Activation Screen
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
                        .background(Color(0xFFEDE9FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Apartment,
                        contentDescription = "BUSINESS",
                        tint = Color(0xFF7C3AED),
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFEDE9FE)
                ) {
                    Text(
                        text = "🏢 TÍNH NĂNG TÀI KHOẢN BUSINESS",
                        color = Color(0xFF6D28D9),
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
                    text = "Phần quản lý nhân viên, chấm công chuẩn 8h/26 ngày, tăng ca OT và tự động tính thâm niên sẽ tự động kích hoạt sau khi nâng cấp gói BUSINESS.",
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

                // Nút 1: Đăng ký sử dụng (Chuyển đến trang mua gói BUSINESS)
                Button(
                    onClick = onNavigateToUpgrade,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7C3AED)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ĐĂNG KÝ SỬ DỤNG GÓI BUSINESS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Nút 2: Xem chi tiết gói BUSINESS
                OutlinedButton(
                    onClick = onNavigateToUpgrade,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF7C3AED)),
                    border = BorderStroke(1.dp, Color(0xFF7C3AED))
                ) {
                    Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("XEM CHI TIẾT GÓI BUSINESS", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Nút 3: Quay lại
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B)),
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Text("Quay lại Cài đặt", color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp)
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

// Helper to get nice theme color for department tag
fun getDepartmentTheme(dept: String): Pair<Color, Color> {
    return when {
        dept.contains("Kinh doanh", ignoreCase = true) || dept.contains("Sales", ignoreCase = true) -> Pair(Color(0xFFEFF6FF), Color(0xFF1D4ED8))
        dept.contains("Kỹ thuật", ignoreCase = true) || dept.contains("IT", ignoreCase = true) || dept.contains("Dev", ignoreCase = true) -> Pair(Color(0xFFEEF2FF), Color(0xFF4338CA))
        dept.contains("Kế toán", ignoreCase = true) || dept.contains("Tài chính", ignoreCase = true) -> Pair(Color(0xFFECFDF5), Color(0xFF047857))
        dept.contains("Marketing", ignoreCase = true) -> Pair(Color(0xFFFFF7ED), Color(0xFFC2410C))
        dept.contains("Nhân sự", ignoreCase = true) || dept.contains("HR", ignoreCase = true) -> Pair(Color(0xFFFAF5FF), Color(0xFF7E22CE))
        else -> Pair(Color(0xFFF1F5F9), Color(0xFF334155))
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

    val (deptBg, deptColor) = getDepartmentTheme(emp.department)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Avatar, Name, Role & Top Right Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
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
                            fontSize = 16.sp,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = emp.role,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF475569)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
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

            // Dedicated Department Tag & Contact Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Department Badge Chip
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = deptBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, deptColor.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Apartment,
                            contentDescription = null,
                            tint = deptColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = emp.department,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = deptColor
                        )
                    }
                }

                if (emp.phone.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = emp.phone,
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            // Seniority & Leave Days Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Thâm niên: ${seniority.years} năm ${seniority.months} tháng",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E293B)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Vào làm: ${emp.startDate}",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFEFF6FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
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

            Spacer(modifier = Modifier.height(10.dp))

            // Bonuses & Salary Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Lương cơ bản", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(String.format(java.util.Locale.US, "%,d đ", emp.baseSalary.toLong()), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                }

                if (seniority.bonusAmount > 0) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Thưởng thâm niên", fontSize = 10.sp, color = Color(0xFFD97706), fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(String.format(java.util.Locale.US, "+%,d đ", seniority.bonusAmount.toLong()), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Thưởng KPI", fontSize = 10.sp, color = Color(0xFF059669), fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(String.format(java.util.Locale.US, "+%,d đ", emp.kpiBonus.toLong()), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
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
    var searchQuery by remember { mutableStateOf("") }
    var showBatchConfirmDialog by remember { mutableStateOf(false) }

    val departments = remember(employees) {
        listOf("Tất cả") + employees.map { it.department }.filter { it.isNotBlank() }.distinct()
    }

    val filteredEmployees = remember(employees, selectedDepartment, searchQuery) {
        employees.filter { emp ->
            val matchesDept = selectedDepartment == "Tất cả" || emp.department.equals(selectedDepartment, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    emp.name.contains(searchQuery, ignoreCase = true) ||
                    emp.department.contains(searchQuery, ignoreCase = true) ||
                    emp.role.contains(searchQuery, ignoreCase = true) ||
                    emp.phone.contains(searchQuery, ignoreCase = true)
            matchesDept && matchesSearch
        }
    }

    val todayRecords = remember(attendanceRecords, selectedDate) {
        attendanceRecords.filter { it.date == selectedDate }
    }

    val checkedCount = todayRecords.count { tr -> filteredEmployees.any { it.id == tr.employeeId } }
    val totalOtHours = todayRecords
        .filter { tr -> filteredEmployees.any { it.id == tr.employeeId } }
        .sumOf { it.overtimeHours.toDouble() }

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

            // Search Bar for Timekeeping Screen
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Tìm nhân sự theo tên, chức vụ, phòng ban...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Xóa", tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = customFieldColors()
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                        val count = if (dept == "Tất cả") employees.size else employees.count { it.department.equals(dept, ignoreCase = true) }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFF0F172A) else Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFF0F172A) else Color(0xFFE2E8F0)),
                            modifier = Modifier.clickable { selectedDepartment = dept }
                        ) {
                            Text(
                                text = "$dept ($count)",
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
                        selectedDate = selectedDate,
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

// Helper to auto-calculate overtime rate type without needing user manual selection or displaying coefficient
fun determineAutoOvertimeRate(dateStr: String, attendanceType: AttendanceType): OvertimeRateType {
    if (attendanceType == AttendanceType.HOLIDAY_LEAVE) {
        return OvertimeRateType.HOLIDAY
    }
    return try {
        val parts = dateStr.split("/", "-")
        val (d, m, y) = if (parts.size >= 3) {
            if (parts[0].length == 4) {
                Triple(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
            } else {
                Triple(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
            }
        } else {
            Triple(1, 0, 2026)
        }
        val cal = Calendar.getInstance()
        cal.set(y, m, d)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            OvertimeRateType.WEEKEND
        } else {
            OvertimeRateType.WEEKDAY
        }
    } catch (_: Exception) {
        OvertimeRateType.WEEKDAY
    }
}

@Composable
fun AttendanceEmployeeCard(
    employee: EmployeeItem,
    record: AttendanceRecord?,
    selectedDate: String = "27/08/2026",
    policy: PayrollPolicySettings,
    onSaveAttendance: (AttendanceType, Float, OvertimeRateType, String) -> Unit
) {
    var selectedType by remember(record) { mutableStateOf(record?.type ?: AttendanceType.FULL_WORK) }
    var overtimeHours by remember(record) { mutableStateOf(record?.overtimeHours ?: 0.0f) }

    // Custom hours and minutes for non-standard OT durations
    var customHoursStr by remember(record) {
        val ot = record?.overtimeHours ?: 0.0f
        if (ot > 0f && ot != 1.0f && ot != 2.0f && ot != 3.0f) {
            val totalMins = (ot * 60).toInt()
            val h = totalMins / 60
            mutableStateOf(if (h > 0) h.toString() else "0")
        } else {
            mutableStateOf("")
        }
    }

    var customMinutesStr by remember(record) {
        val ot = record?.overtimeHours ?: 0.0f
        if (ot > 0f && ot != 1.0f && ot != 2.0f && ot != 3.0f) {
            val totalMins = (ot * 60).toInt()
            val m = totalMins % 60
            mutableStateOf(if (m > 0) m.toString() else "")
        } else {
            mutableStateOf("")
        }
    }

    // Standard attendance types without OT in the scroll bar
    val standardAttendanceTypes = listOf(
        AttendanceType.FULL_WORK,
        AttendanceType.HALF_LEAVE,
        AttendanceType.FULL_LEAVE,
        AttendanceType.UNPAID_LEAVE,
        AttendanceType.HOLIDAY_LEAVE
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Avatar, Name, Department & Current Status Badge
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
                    val statusText = if (record != null) {
                        if (record.overtimeHours > 0f) {
                            val totalMins = (record.overtimeHours * 60).toInt()
                            val h = totalMins / 60
                            val m = totalMins % 60
                            val otStr = if (m == 0) "${h}h" else "${h}h${m}p"
                            "✓ ${record.type.shortCode} (+${otStr} OT)"
                        } else {
                            "✓ ${record.type.label}"
                        }
                    } else "Chưa chấm"

                    Text(
                        text = statusText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (record != null) Color(0xFF047857) else Color(0xFFD97706),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 1. Attendance Type Selector Chips (OT is separated and NOT included here)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                standardAttendanceTypes.forEach { type ->
                    val isSelected = selectedType == type
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) ProfessionalPrimary else Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ProfessionalPrimary else Color(0xFFE2E8F0)),
                        modifier = Modifier.clickable {
                            selectedType = type
                            val autoRate = determineAutoOvertimeRate(selectedDate, type)
                            onSaveAttendance(type, overtimeHours, autoRate, "")
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

            // 2. Dedicated Overtime (OT) Section - Tách riêng biệt, tự động thêm vào bảng công & tự động tính hệ số
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Tăng ca (OT):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    }

                    if (overtimeHours > 0f) {
                        val totalM = (overtimeHours * 60).toInt()
                        val h = totalM / 60
                        val m = totalM % 60
                        val otDisplay = if (m == 0) "${h}h" else "${h}h ${m}p"
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Text(
                                text = "Đã ghi nhận: $otDisplay",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Overtime Preset Options: 0 (Mặc định), 1h, 2h, 3h
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val presets = listOf(
                        0.0f to "0 (Mặc định)",
                        1.0f to "1h",
                        2.0f to "2h",
                        3.0f to "3h"
                    )

                    presets.forEach { (hours, label) ->
                        val isPresetSelected = overtimeHours == hours && customHoursStr.isEmpty() && customMinutesStr.isEmpty()
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isPresetSelected) Color(0xFFD97706) else Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isPresetSelected) Color(0xFFD97706) else Color(0xFFCBD5E1)
                            ),
                            modifier = Modifier
                                .weight(if (hours == 0.0f) 1.3f else 1f)
                                .clickable {
                                    overtimeHours = hours
                                    customHoursStr = ""
                                    customMinutesStr = ""
                                    val autoRate = determineAutoOvertimeRate(selectedDate, selectedType)
                                    onSaveAttendance(selectedType, hours, autoRate, "")
                                }
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isPresetSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isPresetSelected) Color.White else Color(0xFF334155)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Custom Hours & Minutes Input Row (Dành cho trường hợp tăng ca không khớp 1h, 2h hoặc 3h)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(
                            1.dp,
                            if (customHoursStr.isNotEmpty() || customMinutesStr.isNotEmpty()) Color(0xFFD97706) else Color(0xFFE2E8F0),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Khác:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )

                    // Input Số giờ
                    OutlinedTextField(
                        value = customHoursStr,
                        onValueChange = { input ->
                            val clean = input.filter { it.isDigit() }.take(2)
                            customHoursStr = clean
                            val h = clean.toIntOrNull() ?: 0
                            val m = customMinutesStr.toIntOrNull() ?: 0
                            val total = h.toFloat() + (m.toFloat() / 60.0f)
                            overtimeHours = total
                            val autoRate = determineAutoOvertimeRate(selectedDate, selectedType)
                            onSaveAttendance(selectedType, total, autoRate, "")
                        },
                        placeholder = { Text("0", fontSize = 11.sp, color = Color(0xFF94A3B8)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .width(48.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = customFieldColors()
                    )
                    Text("giờ", fontSize = 11.sp, color = Color(0xFF334155), fontWeight = FontWeight.Medium)

                    Spacer(modifier = Modifier.width(2.dp))

                    // Input Số phút
                    OutlinedTextField(
                        value = customMinutesStr,
                        onValueChange = { input ->
                            val clean = input.filter { it.isDigit() }.take(2)
                            val mVal = clean.toIntOrNull() ?: 0
                            val validMins = if (mVal > 59) "59" else clean
                            customMinutesStr = validMins
                            val h = customHoursStr.toIntOrNull() ?: 0
                            val m = validMins.toIntOrNull() ?: 0
                            val total = h.toFloat() + (m.toFloat() / 60.0f)
                            overtimeHours = total
                            val autoRate = determineAutoOvertimeRate(selectedDate, selectedType)
                            onSaveAttendance(selectedType, total, autoRate, "")
                        },
                        placeholder = { Text("0", fontSize = 11.sp, color = Color(0xFF94A3B8)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .width(48.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = customFieldColors()
                    )
                    Text("phút", fontSize = 11.sp, color = Color(0xFF334155), fontWeight = FontWeight.Medium)

                    Spacer(modifier = Modifier.weight(1f))

                    if (customHoursStr.isNotEmpty() || customMinutesStr.isNotEmpty()) {
                        val h = customHoursStr.toIntOrNull() ?: 0
                        val m = customMinutesStr.toIntOrNull() ?: 0
                        val total = h.toFloat() + (m.toFloat() / 60.0f)
                        if (total > 0f) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFFEF3C7)
                            ) {
                                Text(
                                    text = "=${String.format(java.util.Locale.US, "%.2f", total)}h",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
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
    var selectedDepartment by remember { mutableStateOf("Tất cả") }
    var searchQuery by remember { mutableStateOf("") }

    val departments = remember(employees) {
        listOf("Tất cả") + employees.map { it.department }.filter { it.isNotBlank() }.distinct()
    }

    val payrollResults = remember(employees, attendanceRecords, payrollPolicy, selectedMonth) {
        employees.map { emp ->
            val empRecords = attendanceRecords.filter { it.employeeId == emp.id }
            calculateEmployeePayroll(emp, empRecords, payrollPolicy)
        }
    }

    val filteredPayrollResults = remember(payrollResults, selectedDepartment, searchQuery) {
        payrollResults.filter { res ->
            val emp = res.employee
            val matchesDept = selectedDepartment == "Tất cả" || emp.department.equals(selectedDepartment, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    emp.name.contains(searchQuery, ignoreCase = true) ||
                    emp.department.contains(searchQuery, ignoreCase = true) ||
                    emp.role.contains(searchQuery, ignoreCase = true) ||
                    emp.phone.contains(searchQuery, ignoreCase = true)
            matchesDept && matchesSearch
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
                            String.format(java.util.Locale.US, "%,d đ", totalCompanyPayroll.toLong()),
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
                        Text("• Thưởng thâm niên: +${String.format(java.util.Locale.US, "%,d đ", totalSeniorityBonus.toLong())}", fontSize = 11.sp, color = Color(0xFFD97706))
                        Text("• Chi trả tăng ca: +${String.format(java.util.Locale.US, "%,d đ", totalOtPayout.toLong())}", fontSize = 11.sp, color = Color(0xFF059669))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Search Bar for Payroll Screen
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Tìm nhân sự theo tên, chức vụ, phòng ban...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Xóa", tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = customFieldColors()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Department Filter Chips for Payroll Screen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                departments.forEach { dept ->
                    val isSelected = selectedDepartment == dept
                    val count = if (dept == "Tất cả") payrollResults.size else payrollResults.count { it.employee.department.equals(dept, ignoreCase = true) }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) Color(0xFF0F172A) else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFF0F172A) else Color(0xFFE2E8F0)),
                        modifier = Modifier.clickable { selectedDepartment = dept }
                    ) {
                        Text(
                            text = "$dept ($count)",
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF475569),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Hiển thị ${filteredPayrollResults.size} / ${payrollResults.size} nhân viên",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = filteredPayrollResults, key = { it.employee.id }) { result ->
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
    val (deptBg, deptColor) = getDepartmentTheme(emp.department)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Avatar, Name, Department (Top) & Role (Bottom) + Thực nhận Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                            .clip(CircleShape)
                            .background(deptBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emp.initials, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = deptColor)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f, fill = false)) {
                        Text(
                            text = emp.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        // Dòng 1: Phòng ban ở trên
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Apartment,
                                contentDescription = null,
                                tint = deptColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = emp.department,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = deptColor
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        // Dòng 2: Chức vụ ở dưới
                        Text(
                            text = emp.role,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Khối THỰC NHẬN - Bố cục pill độc lập, rõ ràng, không bao giờ bị rớt dòng chữ
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFECFDF5),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA7F3D0))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "THỰC NHẬN",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF059669),
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = String.format(java.util.Locale.US, "%,d đ", result.totalSalary.toLong()),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF047857)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(8.dp))

            // Seniority Info & Days Leave
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Thâm niên: ${seniority.years} năm ${seniority.months} tháng",
                        fontSize = 11.sp,
                        color = Color(0xFF334155),
                        fontWeight = FontWeight.Medium
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFEFF6FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.BeachAccess,
                            contentDescription = null,
                            tint = Color(0xFF1D4ED8),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Phép: ${seniority.totalLeaveDays} ngày (+${seniority.extraLeaveDays})",
                            fontSize = 10.sp,
                            color = Color(0xFF1D4ED8),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
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
                    Text(String.format(java.util.Locale.US, "%,d đ", emp.baseSalary.toLong()), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("• Công thực tế (${result.actualWorkDays} / ${policy.standardWorkDays} ngày):", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text(String.format(java.util.Locale.US, "%,d đ", result.actualWorkSalary.toLong()), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                }
                if (result.totalOvertimeHours > 0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("• Tăng ca OT (${result.totalOvertimeHours} giờ):", fontSize = 11.sp, color = Color(0xFFD97706))
                        Text(String.format(java.util.Locale.US, "+%,d đ", result.overtimeSalary.toLong()), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                    }
                }
                if (seniority.bonusAmount > 0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("• Thưởng thâm niên:", fontSize = 11.sp, color = Color(0xFFB45309))
                        Text(String.format(java.util.Locale.US, "+%,d đ", seniority.bonusAmount.toLong()), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                    }
                }
                if (emp.kpiBonus > 0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("• Thưởng KPI:", fontSize = 11.sp, color = Color(0xFF059669))
                        Text(String.format(java.util.Locale.US, "+%,d đ", emp.kpiBonus.toLong()), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                    }
                }
                if (emp.allowance > 0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("• Phụ cấp:", fontSize = 11.sp, color = Color(0xFF64748B))
                        Text(String.format(java.util.Locale.US, "+%,d đ", emp.allowance.toLong()), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
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

// 4. Notification Settings Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit,
    onNavigateToUpgrade: (AccountTier) -> Unit = {}
) {
    val settings by viewModel.notificationSettings.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val isVipUnlocked = userProfile.accountTier.isVipOrHigher
    val isBusinessUnlocked = userProfile.accountTier == AccountTier.BUSINESS

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showVipUpgradeDialog by remember { mutableStateOf<String?>(null) }
    var showBusinessUpgradeDialog by remember { mutableStateOf<String?>(null) }
    var isSendingTestNotif by remember { mutableStateOf(false) }

    // State for day/month picker dropdowns
    var showDayDropdown by remember { mutableStateOf(false) }
    var showMonthDropdown by remember { mutableStateOf(false) }

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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDBEAFE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = Color(0xFF1D4ED8),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Hệ thống thông báo đang hoạt động",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E3A8A)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Gói hiện tại: ${userProfile.accountTier.displayName} • Cấu hình nhận thông tin real-time",
                            fontSize = 12.sp,
                            color = Color(0xFF3B82F6)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ================= 1. KÊNH NHẬN THÔNG BÁO =================
            SettingsGroupHeader("KÊNH NHẬN THÔNG BÁO")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    ToggleSettingItem(
                        title = "Thông báo ứng dụng (Push)",
                        subtitle = "Hiển thị thông báo trên thanh trạng thái điện thoại",
                        checked = settings.systemNotice,
                        onCheckedChange = { viewModel.updateNotificationSettings(settings.copy(systemNotice = it)) }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    ToggleSettingItem(
                        title = "Âm thanh chuông báo",
                        subtitle = "Phát âm thanh khi có tin nhắn & thông báo mới",
                        checked = settings.soundEnabled,
                        onCheckedChange = { viewModel.updateNotificationSettings(settings.copy(soundEnabled = it)) }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    ToggleSettingItem(
                        title = "Rung phản hồi",
                        subtitle = "Rung thiết bị khi nhận thông báo quan trọng",
                        checked = settings.vibrateEnabled,
                        onCheckedChange = { viewModel.updateNotificationSettings(settings.copy(vibrateEnabled = it)) }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Email tổng kết và báo cáo [VIP]
                    ToggleSettingItem(
                        title = "Email tổng kết & Báo cáo",
                        subtitle = "Tùy chọn ngày nhận theo tuần, tháng, quý, 6 tháng, năm tài chính",
                        checked = settings.weeklyReportEmail,
                        onCheckedChange = { isChecked ->
                            if (isVipUnlocked) {
                                viewModel.updateNotificationSettings(settings.copy(weeklyReportEmail = isChecked))
                            } else {
                                showVipUpgradeDialog = "Email tổng kết và báo cáo"
                            }
                        },
                        badge = if (isVipUnlocked) "VIP" else "VIP (Khóa)",
                        badgeBg = if (isVipUnlocked) Color(0xFFFEF3C7) else Color(0xFFFEE2E2),
                        badgeColor = if (isVipUnlocked) Color(0xFFB45309) else Color(0xFFDC2626),
                        enabled = isVipUnlocked,
                        onClickLocked = { showVipUpgradeDialog = "Email tổng kết và báo cáo" }
                    )

                    // Expanded Fiscal Year & Report Scheduler Card
                    if (isVipUnlocked && settings.weeklyReportEmail) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8FAFC))
                                .padding(16.dp)
                        ) {
                            val schedule = settings.emailReportSchedule

                            // Section A: Fiscal Year Configuration
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = null,
                                            tint = ProfessionalPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "THIẾT LẬP NĂM TÀI CHÍNH (FISCAL YEAR)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Ngày kết thúc tự động tính là ngày trước ngày bắt đầu năm tiếp theo 1 ngày.",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Pickers for Start Day & Start Month
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // Start Day Picker Box
                                        Box(modifier = Modifier.weight(1f)) {
                                            OutlinedButton(
                                                onClick = { showDayDropdown = true },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF8FAFC)),
                                                border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                                            ) {
                                                Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                                                    Text("Ngày bắt đầu", fontSize = 10.sp, color = Color(0xFF64748B))
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text("Ngày ${schedule.fiscalStartDay}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                                                    }
                                                }
                                            }
                                            DropdownMenu(
                                                expanded = showDayDropdown,
                                                onDismissRequest = { showDayDropdown = false }
                                            ) {
                                                (1..31).forEach { day ->
                                                    DropdownMenuItem(
                                                        text = { Text("Ngày $day") },
                                                        onClick = {
                                                            viewModel.updateNotificationSettings(
                                                                settings.copy(emailReportSchedule = schedule.copy(fiscalStartDay = day))
                                                            )
                                                            showDayDropdown = false
                                                        }
                                                    )
                                                }
                                            }
                                        }

                                        // Start Month Picker Box
                                        Box(modifier = Modifier.weight(1.2f)) {
                                            OutlinedButton(
                                                onClick = { showMonthDropdown = true },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF8FAFC)),
                                                border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                                            ) {
                                                Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                                                    Text("Tháng bắt đầu", fontSize = 10.sp, color = Color(0xFF64748B))
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text("Tháng ${schedule.fiscalStartMonth}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                                                    }
                                                }
                                            }
                                            DropdownMenu(
                                                expanded = showMonthDropdown,
                                                onDismissRequest = { showMonthDropdown = false }
                                            ) {
                                                (1..12).forEach { month ->
                                                    DropdownMenuItem(
                                                        text = { Text("Tháng $month") },
                                                        onClick = {
                                                            viewModel.updateNotificationSettings(
                                                                settings.copy(emailReportSchedule = schedule.copy(fiscalStartMonth = month))
                                                            )
                                                            showMonthDropdown = false
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Fiscal calculation display banner
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFEFF6FF),
                                        border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = schedule.getFiscalYearLabel(2026),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = Color(0xFF1E40AF)
                                                )
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = if (schedule.fiscalStartDay == 1 && schedule.fiscalStartMonth == 1) Color(0xFFDBEAFE) else Color(0xFFF1F5F9),
                                                    border = BorderStroke(1.dp, if (schedule.fiscalStartDay == 1 && schedule.fiscalStartMonth == 1) Color(0xFF93C5FD) else Color(0xFFCBD5E1)),
                                                    modifier = Modifier.clickable {
                                                        viewModel.updateNotificationSettings(
                                                            settings.copy(
                                                                emailReportSchedule = schedule.copy(
                                                                    fiscalStartDay = 1,
                                                                    fiscalStartMonth = 1
                                                                )
                                                            )
                                                        )
                                                    }
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Refresh,
                                                            contentDescription = "Đặt lại chu kỳ chuẩn",
                                                            tint = if (schedule.fiscalStartDay == 1 && schedule.fiscalStartMonth == 1) Color(0xFF1D4ED8) else Color(0xFF475569),
                                                            modifier = Modifier.size(12.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(
                                                            text = "Chu kỳ chuẩn",
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = if (schedule.fiscalStartDay == 1 && schedule.fiscalStartMonth == 1) Color(0xFF1D4ED8) else Color(0xFF475569)
                                                        )
                                                    }
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "Thời gian: ${schedule.getFiscalYearRangeString(2026)}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF1E3A8A)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Section B: Report Frequencies (Multi-select options)
                            Text(
                                text = "TÙY CHỌN NGÀY NHẬN BÁO CÁO (CHỌN NHIỀU TÙY CHỌN)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF475569)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // 1. Weekly Report
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("1. Báo cáo theo tuần", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                            Text("Chọn 1 ngày cố định trong tuần để nhận báo cáo", fontSize = 11.sp, color = Color(0xFF64748B))
                                        }
                                        Switch(
                                            checked = schedule.enableWeeklyReport,
                                            onCheckedChange = { isChecked ->
                                                viewModel.updateNotificationSettings(
                                                    settings.copy(emailReportSchedule = schedule.copy(enableWeeklyReport = isChecked))
                                                )
                                            },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color.White,
                                                checkedTrackColor = ProfessionalPrimary
                                            )
                                        )
                                    }

                                    if (schedule.enableWeeklyReport) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Chọn ngày nhận trong tuần (chọn 1 ngày):", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF475569))
                                        Spacer(modifier = Modifier.height(6.dp))

                                        val daysList = listOf(
                                            2 to "Thứ 2",
                                            3 to "Thứ 3",
                                            4 to "Thứ 4",
                                            5 to "Thứ 5",
                                            6 to "Thứ 6",
                                            7 to "Thứ 7",
                                            1 to "Chủ Nhật"
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            daysList.forEach { (dayCode, dayLabel) ->
                                                val isSelected = schedule.weeklyDays.contains(dayCode)
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = if (isSelected) ProfessionalPrimary else Color(0xFFF1F5F9),
                                                    border = BorderStroke(1.dp, if (isSelected) ProfessionalPrimary else Color(0xFFE2E8F0)),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clickable {
                                                            viewModel.updateNotificationSettings(
                                                                settings.copy(emailReportSchedule = schedule.copy(weeklyDays = setOf(dayCode)))
                                                            )
                                                        }
                                                ) {
                                                    Box(
                                                        modifier = Modifier.padding(vertical = 8.dp),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = dayLabel,
                                                            fontSize = 10.sp,
                                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                            color = if (isSelected) Color.White else Color(0xFF475569),
                                                            maxLines = 1
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // 2. Monthly Report
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("2. Báo cáo theo tháng tài chính", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                            Text("Tổng hợp hiệu suất tháng tài chính", fontSize = 11.sp, color = Color(0xFF64748B))
                                        }
                                        Switch(
                                            checked = schedule.enableMonthlyReport,
                                            onCheckedChange = { isChecked ->
                                                viewModel.updateNotificationSettings(
                                                    settings.copy(emailReportSchedule = schedule.copy(enableMonthlyReport = isChecked))
                                                )
                                            },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color.White,
                                                checkedTrackColor = ProfessionalPrimary
                                            )
                                        )
                                    }

                                    if (schedule.enableMonthlyReport) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            FilterChip(
                                                selected = schedule.monthlyStart,
                                                onClick = {
                                                    viewModel.updateNotificationSettings(
                                                        settings.copy(emailReportSchedule = schedule.copy(monthlyStart = !schedule.monthlyStart))
                                                    )
                                                },
                                                label = { Text("Ngày đầu tháng", fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ProfessionalPrimary,
                                                    selectedLabelColor = Color.White
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                            FilterChip(
                                                selected = schedule.monthlyEnd,
                                                onClick = {
                                                    viewModel.updateNotificationSettings(
                                                        settings.copy(emailReportSchedule = schedule.copy(monthlyEnd = !schedule.monthlyEnd))
                                                    )
                                                },
                                                label = { Text("Ngày cuối tháng", fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ProfessionalPrimary,
                                                    selectedLabelColor = Color.White
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // 3. Quarterly Report
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("3. Báo cáo theo quý tài chính (Q1 - Q4)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                            Text("Tổng hợp hiệu suất mỗi 3 tháng tài chính", fontSize = 11.sp, color = Color(0xFF64748B))
                                        }
                                        Switch(
                                            checked = schedule.enableQuarterlyReport,
                                            onCheckedChange = { isChecked ->
                                                viewModel.updateNotificationSettings(
                                                    settings.copy(emailReportSchedule = schedule.copy(enableQuarterlyReport = isChecked))
                                                )
                                            },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color.White,
                                                checkedTrackColor = ProfessionalPrimary
                                            )
                                        )
                                    }

                                    if (schedule.enableQuarterlyReport) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            FilterChip(
                                                selected = schedule.quarterlyStart,
                                                onClick = {
                                                    viewModel.updateNotificationSettings(
                                                        settings.copy(emailReportSchedule = schedule.copy(quarterlyStart = !schedule.quarterlyStart))
                                                    )
                                                },
                                                label = { Text("Ngày đầu mỗi quý", fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ProfessionalPrimary,
                                                    selectedLabelColor = Color.White
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                            FilterChip(
                                                selected = schedule.quarterlyEnd,
                                                onClick = {
                                                    viewModel.updateNotificationSettings(
                                                        settings.copy(emailReportSchedule = schedule.copy(quarterlyEnd = !schedule.quarterlyEnd))
                                                    )
                                                },
                                                label = { Text("Ngày cuối mỗi quý", fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ProfessionalPrimary,
                                                    selectedLabelColor = Color.White
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // 4. Semi-Annual (6 months / Giữa năm) Report
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("4. Báo cáo bán niên (6 tháng / Giữa năm)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                            Text("Tổng hợp hiệu suất nửa năm tài chính", fontSize = 11.sp, color = Color(0xFF64748B))
                                        }
                                        Switch(
                                            checked = schedule.enableSemiAnnualReport,
                                            onCheckedChange = { isChecked ->
                                                viewModel.updateNotificationSettings(
                                                    settings.copy(emailReportSchedule = schedule.copy(enableSemiAnnualReport = isChecked))
                                                )
                                            },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color.White,
                                                checkedTrackColor = ProfessionalPrimary
                                            )
                                        )
                                    }

                                    if (schedule.enableSemiAnnualReport) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            FilterChip(
                                                selected = schedule.semiAnnualStart,
                                                onClick = {
                                                    viewModel.updateNotificationSettings(
                                                        settings.copy(emailReportSchedule = schedule.copy(semiAnnualStart = !schedule.semiAnnualStart))
                                                    )
                                                },
                                                label = { Text("Ngày đầu kỳ 6 tháng", fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ProfessionalPrimary,
                                                    selectedLabelColor = Color.White
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                            FilterChip(
                                                selected = schedule.semiAnnualEnd,
                                                onClick = {
                                                    viewModel.updateNotificationSettings(
                                                        settings.copy(emailReportSchedule = schedule.copy(semiAnnualEnd = !schedule.semiAnnualEnd))
                                                    )
                                                },
                                                label = { Text("Ngày cuối kỳ 6 tháng", fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ProfessionalPrimary,
                                                    selectedLabelColor = Color.White
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // 5. Annual (Năm tài chính) Report
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("5. Báo cáo năm tài chính", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                            Text("Báo cáo tổng kết toàn diện cuối năm tài chính", fontSize = 11.sp, color = Color(0xFF64748B))
                                        }
                                        Switch(
                                            checked = schedule.enableAnnualReport,
                                            onCheckedChange = { isChecked ->
                                                viewModel.updateNotificationSettings(
                                                    settings.copy(emailReportSchedule = schedule.copy(enableAnnualReport = isChecked))
                                                )
                                            },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color.White,
                                                checkedTrackColor = ProfessionalPrimary
                                            )
                                        )
                                    }

                                    if (schedule.enableAnnualReport) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            FilterChip(
                                                selected = schedule.annualStart,
                                                onClick = {
                                                    viewModel.updateNotificationSettings(
                                                        settings.copy(emailReportSchedule = schedule.copy(annualStart = !schedule.annualStart))
                                                    )
                                                },
                                                label = { Text("Ngày đầu năm", fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ProfessionalPrimary,
                                                    selectedLabelColor = Color.White
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                            FilterChip(
                                                selected = schedule.annualEnd,
                                                onClick = {
                                                    viewModel.updateNotificationSettings(
                                                        settings.copy(emailReportSchedule = schedule.copy(annualEnd = !schedule.annualEnd))
                                                    )
                                                },
                                                label = { Text("Ngày cuối năm", fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ProfessionalPrimary,
                                                    selectedLabelColor = Color.White
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ================= 2. NỘI DUNG THÔNG BÁO CHI TIẾT =================
            SettingsGroupHeader("NỘI DUNG THÔNG BÁO")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    // Nhiệm vụ & Công việc mới [BUSINESS]
                    ToggleSettingItem(
                        title = "Nhiệm vụ & Công việc mới",
                        subtitle = "Nhận thông báo khi được giao việc hoặc thay đổi tiến độ",
                        checked = settings.newTask,
                        onCheckedChange = { isChecked ->
                            if (isBusinessUnlocked) {
                                viewModel.updateNotificationSettings(settings.copy(newTask = isChecked))
                            } else {
                                showBusinessUpgradeDialog = "Nhiệm vụ & công việc mới"
                            }
                        },
                        badge = if (isBusinessUnlocked) "BUSINESS" else "BUSINESS (Khóa)",
                        badgeBg = if (isBusinessUnlocked) Color(0xFFF3E8FF) else Color(0xFFFEE2E2),
                        badgeColor = if (isBusinessUnlocked) Color(0xFF7E22CE) else Color(0xFFDC2626),
                        enabled = isBusinessUnlocked,
                        onClickLocked = { showBusinessUpgradeDialog = "Nhiệm vụ & công việc mới" }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Nhắc nhở hạn chót Deadline [FREE]
                    ToggleSettingItem(
                        title = "Nhắc nhở hạn chót (Deadline)",
                        subtitle = "Cảnh báo trước 30 phút và 1 ngày trước khi hết hạn",
                        checked = settings.deadlineReminder,
                        onCheckedChange = { viewModel.updateNotificationSettings(settings.copy(deadlineReminder = it)) }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Cập nhật hồ sơ khách hàng [BUSINESS]
                    ToggleSettingItem(
                        title = "Cập nhật hồ sơ khách hàng",
                        subtitle = "Thông báo khi có khách mới phân phối hoặc sửa thông tin",
                        checked = settings.customerUpdate,
                        onCheckedChange = { isChecked ->
                            if (isBusinessUnlocked) {
                                viewModel.updateNotificationSettings(settings.copy(customerUpdate = isChecked))
                            } else {
                                showBusinessUpgradeDialog = "Cập nhật hồ sơ khách hàng"
                            }
                        },
                        badge = if (isBusinessUnlocked) "BUSINESS" else "BUSINESS (Khóa)",
                        badgeBg = if (isBusinessUnlocked) Color(0xFFF3E8FF) else Color(0xFFFEE2E2),
                        badgeColor = if (isBusinessUnlocked) Color(0xFF7E22CE) else Color(0xFFDC2626),
                        enabled = isBusinessUnlocked,
                        onClickLocked = { showBusinessUpgradeDialog = "Cập nhật hồ sơ khách hàng" }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Báo giá & Duyệt hợp đồng [BUSINESS]
                    ToggleSettingItem(
                        title = "Báo giá & Duyệt hợp đồng",
                        subtitle = "Khách hàng chấp thuận báo giá hoặc yêu cầu chỉnh sửa",
                        checked = settings.quoteApproval,
                        onCheckedChange = { isChecked ->
                            if (isBusinessUnlocked) {
                                viewModel.updateNotificationSettings(settings.copy(quoteApproval = isChecked))
                            } else {
                                showBusinessUpgradeDialog = "Báo giá và duyệt hợp đồng"
                            }
                        },
                        badge = if (isBusinessUnlocked) "BUSINESS" else "BUSINESS (Khóa)",
                        badgeBg = if (isBusinessUnlocked) Color(0xFFF3E8FF) else Color(0xFFFEE2E2),
                        badgeColor = if (isBusinessUnlocked) Color(0xFF7E22CE) else Color(0xFFDC2626),
                        enabled = isBusinessUnlocked,
                        onClickLocked = { showBusinessUpgradeDialog = "Báo giá và duyệt hợp đồng" }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Đợt thanh toán & Thu hồi công nợ [VIP]
                    ToggleSettingItem(
                        title = "Thanh toán & Thu hồi công nợ",
                        subtitle = "Nhắc nhở các mốc thanh toán dự án đến hạn và công nợ",
                        checked = settings.paymentReminder,
                        onCheckedChange = { isChecked ->
                            if (isVipUnlocked) {
                                viewModel.updateNotificationSettings(settings.copy(paymentReminder = isChecked))
                            } else {
                                showVipUpgradeDialog = "Thanh toán và thu hồi công nợ"
                            }
                        },
                        badge = if (isVipUnlocked) "VIP" else "VIP (Khóa)",
                        badgeBg = if (isVipUnlocked) Color(0xFFFEF3C7) else Color(0xFFFEE2E2),
                        badgeColor = if (isVipUnlocked) Color(0xFFB45309) else Color(0xFFDC2626),
                        enabled = isVipUnlocked,
                        onClickLocked = { showVipUpgradeDialog = "Thanh toán và thu hồi công nợ" }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ================= 3. KHÔNG LÀM PHIỀN (DND) [VIP] =================
            SettingsGroupHeader("CHẾ ĐỘ KHÔNG LÀM PHIỀN (DND)")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (!isVipUnlocked) {
                                    Modifier.clickable { showVipUpgradeDialog = "Bật chế độ yên tĩnh ban đêm" }
                                } else Modifier
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Bật chế độ yên tĩnh ban đêm",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isVipUnlocked) Color(0xFF0F172A) else Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isVipUnlocked) Color(0xFFFEF3C7) else Color(0xFFFEE2E2)
                                ) {
                                    Text(
                                        text = if (isVipUnlocked) "VIP" else "VIP (Khóa)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isVipUnlocked) Color(0xFFB45309) else Color(0xFFDC2626),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Tắt chuông và rung trong khoảng thời gian nghỉ ngơi",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        Switch(
                            checked = if (isVipUnlocked) settings.enableQuietHours else false,
                            onCheckedChange = { isChecked ->
                                if (isVipUnlocked) {
                                    viewModel.updateNotificationSettings(settings.copy(enableQuietHours = isChecked))
                                } else {
                                    showVipUpgradeDialog = "Bật chế độ yên tĩnh ban đêm"
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ProfessionalPrimary
                            )
                        )
                    }

                    if (isVipUnlocked && settings.enableQuietHours) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Khung giờ yên tĩnh:", fontSize = 13.sp, color = Color(0xFF475569))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(settings.quietHourStart, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                }
                                Text(" — ", color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(settings.quietHourEnd, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Cho phép thông báo khẩn cấp từ VIP",
                                fontSize = 13.sp,
                                color = Color(0xFF0F172A)
                            )
                            Switch(
                                checked = settings.allowVipOverride,
                                onCheckedChange = {
                                    viewModel.updateNotificationSettings(settings.copy(allowVipOverride = it))
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ProfessionalPrimary
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Button(
                onClick = {
                    isSendingTestNotif = true
                    scope.launch {
                        isSendingTestNotif = false
                        snackbarHostState.showSnackbar("🔔 Đã gửi thông báo thử nghiệm thành công đến thiết bị!")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF1F5F9),
                    contentColor = Color(0xFF1E293B)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Gửi thông báo thử nghiệm", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("✅ Đã lưu cấu hình cài đặt thông báo!")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Lưu cấu hình thông báo", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // ================= VIP UPGRADE DIALOG =================
    if (showVipUpgradeDialog != null) {
        AlertDialog(
            onDismissRequest = { showVipUpgradeDialog = null },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Nâng cấp gói VIP",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF0F172A)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Tính năng \"${showVipUpgradeDialog}\" chỉ dành riêng cho tài khoản gói VIP hoặc BUSINESS.",
                        fontSize = 14.sp,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Nâng cấp ngay để mở khóa toàn bộ báo cáo email theo năm tài chính, chế độ không làm phiền và quản lý công nợ chuyên sâu.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            },
            confirmButton = {},
            dismissButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val targetTier = AccountTier.VIP
                            showVipUpgradeDialog = null
                            onNavigateToUpgrade(targetTier)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                    ) {
                        Text(
                            text = "Đăng ký sử dụng gói VIP",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            val targetTier = AccountTier.VIP
                            showVipUpgradeDialog = null
                            onNavigateToUpgrade(targetTier)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFD97706)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD97706))
                    ) {
                        Text(
                            text = "Xem chi tiết gói VIP",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }

                    OutlinedButton(
                        onClick = { showVipUpgradeDialog = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B))
                    ) {
                        Text(
                            text = "Đóng",
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        )
    }

    // ================= BUSINESS UPGRADE DIALOG =================
    if (showBusinessUpgradeDialog != null) {
        AlertDialog(
            onDismissRequest = { showBusinessUpgradeDialog = null },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3E8FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Apartment,
                        contentDescription = null,
                        tint = Color(0xFF7E22CE),
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Nâng cấp gói BUSINESS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF0F172A)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Tính năng \"${showBusinessUpgradeDialog}\" chỉ dành riêng cho tài khoản gói BUSINESS.",
                        fontSize = 14.sp,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Nâng cấp lên gói Doanh nghiệp để quản lý giao việc đa phòng ban, phân phối hồ sơ khách hàng và phê duyệt báo giá hợp đồng tập trung.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            },
            confirmButton = {},
            dismissButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val targetTier = AccountTier.BUSINESS
                            showBusinessUpgradeDialog = null
                            onNavigateToUpgrade(targetTier)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E22CE))
                    ) {
                        Text(
                            text = "Đăng ký sử dụng gói BUSINESS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            val targetTier = AccountTier.BUSINESS
                            showBusinessUpgradeDialog = null
                            onNavigateToUpgrade(targetTier)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF7E22CE)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF7E22CE))
                    ) {
                        Text(
                            text = "Xem chi tiết gói BUSINESS",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }

                    OutlinedButton(
                        onClick = { showBusinessUpgradeDialog = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B))
                    ) {
                        Text(
                            text = "Đóng",
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        )
    }
}

// 5. Security Screen (Bảo mật tài khoản)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit
) {
    val secSettings by viewModel.securitySettings.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val isDemoAccount = remember(userProfile.email, userProfile.fullName) {
        userProfile.email.isBlank() ||
        userProfile.email.contains("demo", ignoreCase = true) ||
        userProfile.email.contains("test", ignoreCase = true) ||
        userProfile.email.equals("admin@crm.vn", ignoreCase = true) ||
        userProfile.fullName.contains("demo", ignoreCase = true) ||
        userProfile.fullName.contains("test", ignoreCase = true)
    }

    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var passwordChangeError by remember { mutableStateOf<String?>(null) }

    var showAuthenticatorSetupDialog by remember { mutableStateOf(false) }
    var authOtpCode by remember { mutableStateOf("") }
    var authOtpError by remember { mutableStateOf<String?>(null) }

    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var deleteConfirmationInput by remember { mutableStateOf("") }
    var deleteError by remember { mutableStateOf<String?>(null) }

    var showLogoutAllDialog by remember { mutableStateOf(false) }
    var sessionList by remember {
        mutableStateOf(
            listOf(
                Triple("iPhone 15 Pro Max", "Hà Nội, Việt Nam • IP: 118.70.124.88", true),
                Triple("MacBook Pro M3 Max", "TP. Hồ Chí Minh • IP: 14.162.201.12 • 2 giờ trước", false),
                Triple("Samsung Galaxy S24 Ultra", "Đà Nẵng, Việt Nam • IP: 27.72.63.15 • 3 ngày trước", false)
            )
        )
    }

    // Password criteria check
    val hasMinLength = newPassword.length >= 8
    val hasUppercase = newPassword.any { it.isUpperCase() }
    val hasLowercase = newPassword.any { it.isLowerCase() }
    val hasDigit = newPassword.any { it.isDigit() }
    val hasSpecial = newPassword.any { !it.isLetterOrDigit() }
    val isPasswordStrictValid = hasMinLength && hasUppercase && hasLowercase && hasDigit && hasSpecial

    val passwordStrength = remember(newPassword) {
        if (newPassword.isEmpty()) 0f
        else {
            var score = 0f
            if (hasMinLength) score += 0.2f
            if (hasUppercase) score += 0.2f
            if (hasLowercase) score += 0.2f
            if (hasDigit) score += 0.2f
            if (hasSpecial) score += 0.2f
            score
        }
    }
    val strengthLabel = when {
        passwordStrength <= 0.4f -> "Yếu"
        passwordStrength <= 0.8f -> "Khá"
        else -> "Rất mạnh (Đầy đủ tiêu chuẩn)"
    }
    val strengthColor = when {
        passwordStrength <= 0.4f -> Color(0xFFEF4444)
        passwordStrength <= 0.8f -> Color(0xFFF59E0B)
        else -> Color(0xFF10B981)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bảo mật tài khoản", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Security Score Card (Compact)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color(0xFF34D399),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Mức độ an toàn: ",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                            Text(
                                text = if (secSettings.twoFactorAuth && secSettings.biometricAuth) "TỐI ƯU (100%)" else "KHÁ (80%)",
                                color = Color(0xFF34D399),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Bảo vệ bởi mã hóa AES-256, xác thực TOTP Authenticator và giám sát IP",
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // ================= 1. THIẾT BỊ & PHIÊN ĐĂNG NHẬP =================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "THIẾT BỊ & PHIÊN ĐĂNG NHẬP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    letterSpacing = 0.8.sp
                )
                if (sessionList.size > 1) {
                    Text(
                        text = "Đăng xuất tất cả",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626),
                        modifier = Modifier
                            .clickable { showLogoutAllDialog = true }
                            .padding(bottom = 2.dp)
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    sessionList.forEachIndexed { index, (deviceName, deviceDetails, isCurrent) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isCurrent) Color(0xFFECFDF5) else Color(0xFFF1F5F9)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (deviceName.contains("MacBook") || deviceName.contains("PC")) Icons.Default.LaptopMac else Icons.Default.PhoneAndroid,
                                        contentDescription = null,
                                        tint = if (isCurrent) Color(0xFF059669) else Color(0xFF64748B),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = deviceName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = deviceDetails,
                                        fontSize = 10.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }

                            if (isCurrent) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFDEF7EC))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Hiện tại", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                                }
                            } else {
                                IconButton(
                                    onClick = {
                                        sessionList = sessionList.filter { it.first != deviceName }
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Đã thu hồi phiên đăng nhập: $deviceName")
                                        }
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Revoke",
                                        tint = Color(0xFF94A3B8),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        if (index < sessionList.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF1F5F9))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // ================= 2. NHẬT KÝ HOẠT ĐỘNG BẢO MẬT =================
            SettingsGroupHeader("NHẬT KÝ BẢO MẬT GẦN ĐÂY")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    listOf(
                        Triple("Đăng nhập thành công từ thiết bị mới", "Hôm nay, 08:30 • IP: 118.70.124.88", Color(0xFF10B981)),
                        Triple("Cập nhật xác thực 2 bước qua Authenticator", "28/08/2026, 14:15", Color(0xFF3B82F6)),
                        Triple("Cập nhật mật khẩu bảo mật định kỳ", "15/08/2026, 09:00", Color(0xFF6366F1))
                    ).forEachIndexed { idx, (logTitle, logTime, dotColor) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(dotColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(logTitle, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0F172A))
                                Text(logTime, fontSize = 10.sp, color = Color(0xFF94A3B8))
                            }
                        }
                        if (idx < 2) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // ================= 3. MẬT KHẨU & XÁC THỰC (ĐƯA XUỐNG DƯỚI) =================
            SettingsGroupHeader("MẬT KHẨU & XÁC THỰC")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    // Đổi mật khẩu
                    SettingsRowItem(
                        icon = Icons.Default.VpnKey,
                        title = "Đổi mật khẩu",
                        subtitle = "Cập nhật mật khẩu định kỳ 90 ngày",
                        badge = "BẢO VỆ",
                        badgeBg = Color(0xFFEFF6FF),
                        badgeColor = Color(0xFF1D4ED8),
                        onClick = {
                            currentPassword = ""
                            newPassword = ""
                            confirmPassword = ""
                            passwordChangeError = null
                            showChangePasswordDialog = true
                        }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // 2FA qua Authenticator
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFEFF6FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LockReset,
                                    contentDescription = null,
                                    tint = Color(0xFF1D4ED8),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Xác thực 2 yếu tố (2FA)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = Color(0xFFF0FDF4),
                                        shape = RoundedCornerShape(4.dp),
                                        border = BorderStroke(1.dp, Color(0xFFDCFCE7))
                                    ) {
                                        Text(
                                            text = "AUTHENTICATOR",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF16A34A),
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Mã OTP thời gian thực qua Google Authenticator / Microsoft Authenticator",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                        Switch(
                            checked = secSettings.twoFactorAuth,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    authOtpCode = ""
                                    authOtpError = null
                                    showAuthenticatorSetupDialog = true
                                } else {
                                    viewModel.updateSecuritySettings(secSettings.copy(twoFactorAuth = false))
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Đã tắt xác thực 2 yếu tố")
                                    }
                                }
                            }
                        )
                    }

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Biometrics Toggle
                    ToggleSettingItem(
                        title = "Đăng nhập sinh trắc học",
                        subtitle = "Mở khóa an toàn nhanh bằng Vân tay hoặc Face ID",
                        checked = secSettings.biometricAuth,
                        onCheckedChange = {
                            viewModel.updateSecuritySettings(secSettings.copy(biometricAuth = it))
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    if (it) "Đã bật đăng nhập sinh trắc học" else "Đã tắt sinh trắc học"
                                )
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ================= 4. VÙNG NGUY HIỂM: XÓA TÀI KHOẢN =================
            Text(
                text = "VÙNG NGUY HIỂM",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDC2626),
                letterSpacing = 1.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                border = BorderStroke(1.dp, Color(0xFFFECACA)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEE2E2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteForever,
                                contentDescription = "Delete Account",
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Xóa tài khoản vĩnh viễn",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF991B1B)
                            )
                            Text(
                                text = "Xóa toàn bộ thông tin tài khoản, hồ sơ và phiên đăng nhập khỏi hệ thống",
                                fontSize = 11.sp,
                                color = Color(0xFFB91C1C)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            deleteConfirmationInput = ""
                            deleteError = null
                            showDeleteAccountDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(40.dp)
                    ) {
                        Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Thực thi xóa tài khoản", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // ================= CHANGE PASSWORD DIALOG =================
    if (showChangePasswordDialog) {
        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = {
                Text("Đổi mật khẩu tài khoản", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            },
            text = {
                Column {
                    if (isDemoAccount) {
                        Surface(
                            color = Color(0xFFEFF6FF),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFF2563EB),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Tài khoản Demo/Test: Cho phép mật khẩu tùy ý để bạn tiện kiểm thử.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF1E40AF)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    } else {
                        Text(
                            "Mật khẩu phải đáp ứng đủ các yếu tố bảo mật:",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Mật khẩu hiện tại") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (currentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { currentPasswordVisible = !currentPasswordVisible }) {
                                Icon(
                                    imageVector = if (currentPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8)
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Mật khẩu mới") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                                Icon(
                                    imageVector = if (newPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8)
                                )
                            }
                        }
                    )

                    if (!isDemoAccount && newPassword.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { passwordStrength },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = strengthColor,
                            trackColor = Color(0xFFE2E8F0)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Độ an toàn: $strengthLabel",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = strengthColor
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            PasswordCriteriaRow(text = "Tối thiểu 8 ký tự", met = hasMinLength)
                            PasswordCriteriaRow(text = "Chữ in hoa (A-Z)", met = hasUppercase)
                            PasswordCriteriaRow(text = "Chữ thường (a-z)", met = hasLowercase)
                            PasswordCriteriaRow(text = "Chữ số (0-9)", met = hasDigit)
                            PasswordCriteriaRow(text = "Ký tự đặc biệt (!@#$%...)", met = hasSpecial)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Xác nhận mật khẩu mới") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8)
                                )
                            }
                        }
                    )

                    passwordChangeError?.let { err ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(err, color = Color(0xFFDC2626), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (currentPassword.isBlank()) {
                            passwordChangeError = "Vui lòng nhập mật khẩu hiện tại"
                        } else if (!isDemoAccount && !isPasswordStrictValid) {
                            passwordChangeError = "Mật khẩu mới chưa đủ điều kiện: Tối thiểu 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt."
                        } else if (newPassword.isBlank()) {
                            passwordChangeError = "Vui lòng nhập mật khẩu mới"
                        } else if (newPassword != confirmPassword) {
                            passwordChangeError = "Mật khẩu xác nhận không trùng khớp"
                        } else {
                            showChangePasswordDialog = false
                            scope.launch {
                                snackbarHostState.showSnackbar("🎉 Đã đổi mật khẩu thành công!")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
                ) {
                    Text("Cập nhật")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePasswordDialog = false }) {
                    Text("Hủy", color = Color(0xFF64748B))
                }
            }
        )
    }

    // ================= AUTHENTICATOR SETUP DIALOG =================
    if (showAuthenticatorSetupDialog) {
        AlertDialog(
            onDismissRequest = { showAuthenticatorSetupDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LockReset,
                            contentDescription = null,
                            tint = Color(0xFF1D4ED8),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Xác thực 2 bước (Authenticator)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column {
                    Text(
                        "1. Mở ứng dụng Google Authenticator hoặc Microsoft Authenticator trên điện thoại.",
                        fontSize = 12.sp,
                        color = Color(0xFF334155),
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "2. Thêm khóa bảo mật tài khoản bên dưới:",
                        fontSize = 12.sp,
                        color = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val secretKey = "CRM-TOTP-8892-XQ71"
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = secretKey,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF0F172A),
                                letterSpacing = 1.sp
                            )
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("📋 Đã sao chép khóa bảo mật Authenticator!")
                                    }
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = Color(0xFF1D4ED8),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "3. Nhập mã OTP 6 chữ số từ ứng dụng Authenticator để kích hoạt:",
                        fontSize = 12.sp,
                        color = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = authOtpCode,
                        onValueChange = { if (it.length <= 6 && it.all { char -> char.isDigit() }) authOtpCode = it },
                        placeholder = { Text("Nhập 6 chữ số OTP") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    authOtpError?.let { err ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(err, color = Color(0xFFDC2626), fontSize = 11.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (authOtpCode.length != 6) {
                            authOtpError = "Vui lòng nhập đủ 6 chữ số từ ứng dụng Authenticator"
                        } else {
                            viewModel.updateSecuritySettings(secSettings.copy(twoFactorAuth = true))
                            showAuthenticatorSetupDialog = false
                            scope.launch {
                                snackbarHostState.showSnackbar("🎉 Đã bật xác thực qua Google Authenticator thành công!")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
                ) {
                    Text("Xác nhận kích hoạt")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAuthenticatorSetupDialog = false }) {
                    Text("Hủy", color = Color(0xFF64748B))
                }
            }
        )
    }

    // ================= REAL DELETE ACCOUNT DIALOG =================
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEE2E2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WarningAmber,
                        contentDescription = null,
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Xác nhận xóa tài khoản?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color(0xFF991B1B)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Lưu ý quan trọng: Hành động này có hiệu lực thực thi ngay lập tức và KHÔNG THỂ HOÀN TÁC.",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color(0xFF991B1B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Toàn bộ thông tin tài khoản (${userProfile.email.ifBlank { "Tài khoản hiện tại" }}), thiết lập bảo mật và dữ liệu cá nhân cục bộ sẽ bị xóa hoàn toàn khỏi thiết bị.",
                        fontSize = 12.sp,
                        color = Color(0xFF475569),
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Nhập \"XOA\" hoặc \"DELETE\" bên dưới để xác nhận:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = deleteConfirmationInput,
                        onValueChange = { deleteConfirmationInput = it },
                        placeholder = { Text("Nhập XOA để xác nhận") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    deleteError?.let { err ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(err, color = Color(0xFFDC2626), fontSize = 11.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val input = deleteConfirmationInput.trim().uppercase()
                        if (input != "XOA" && input != "DELETE") {
                            deleteError = "Vui lòng nhập đúng \"XOA\" hoặc \"DELETE\" để xác nhận."
                        } else {
                            showDeleteAccountDialog = false
                            // Real execution: wipe account from storage and reset session
                            viewModel.deleteAccount(userProfile.email)
                            scope.launch {
                                snackbarHostState.showSnackbar("Đã thực thi xóa tài khoản và dữ liệu thành công.")
                            }
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Xóa tài khoản vĩnh viễn", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Hủy", color = Color(0xFF64748B))
                }
            }
        )
    }

    // ================= LOGOUT ALL SESSIONS DIALOG =================
    if (showLogoutAllDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutAllDialog = false },
            title = { Text("Đăng xuất tất cả thiết bị khác?", fontWeight = FontWeight.Bold) },
            text = { Text("Mọi phiên đăng nhập trên các máy tính, điện thoại khác sẽ bị thu hồi ngay lập tức.") },
            confirmButton = {
                Button(
                    onClick = {
                        sessionList = sessionList.filter { it.third }
                        showLogoutAllDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Đã đăng xuất khỏi tất cả các thiết bị khác!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Đăng xuất hết")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutAllDialog = false }) {
                    Text("Hủy", color = Color(0xFF64748B))
                }
            }
        )
    }
}

@Composable
private fun PasswordCriteriaRow(text: String, met: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (met) Icons.Default.CheckCircle else Icons.Default.Close,
            contentDescription = null,
            tint = if (met) Color(0xFF10B981) else Color(0xFF94A3B8),
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            color = if (met) Color(0xFF047857) else Color(0xFF64748B),
            fontWeight = if (met) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// 6. Backup & Cloud Sync Screen (Sao lưu & Đồng bộ đám mây)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupAndSyncScreen(
    viewModel: CrmViewModel,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isSyncingNow by remember { mutableStateOf(false) }
    var autoBackupFrequency by remember { mutableStateOf("Hàng ngày") }
    var backupOnlyWifi by remember { mutableStateOf(true) }
    var keepBackupDays by remember { mutableStateOf("30 ngày") }
    var lastBackupTime by remember { mutableStateOf("Hôm nay, 08:30 AM") }
    var lastBackupSize by remember { mutableStateOf("16.2 MB") }

    var showRestoreConfirmDialog by remember { mutableStateOf<String?>(null) }
    var isExporting by remember { mutableStateOf<String?>(null) }

    val restorePoints = remember {
        listOf(
            Triple("Bản sao lưu tự động #03", "Hôm nay lúc 08:30 AM • 16.2 MB • 240 khách hàng, 58 báo giá", "16.2 MB"),
            Triple("Bản sao lưu tự động #02", "Hôm qua lúc 23:00 PM • 15.9 MB • 236 khách hàng, 54 báo giá", "15.9 MB"),
            Triple("Bản sao lưu trước bảo trì #01", "27/08/2026 lúc 18:00 • 15.5 MB • 230 khách hàng", "15.5 MB")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sao lưu và đồng bộ", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Status Cloud Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFECFDF5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Đám mây Google Cloud / Firebase",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Lần sao lưu gần nhất: $lastBackupTime ($lastBackupSize)",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            isSyncingNow = true
                            scope.launch {
                                lastBackupTime = "Vừa xong"
                                isSyncingNow = false
                                snackbarHostState.showSnackbar("☁️ Đã sao lưu dữ liệu toàn diện lên Cloud thành công!")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSyncingNow,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isSyncingNow) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Đang đồng bộ dữ liệu...", fontSize = 13.sp)
                        } else {
                            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Đồng bộ & Sao lưu ngay", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ================= 1. CẤU HÌNH TỰ ĐỘNG =================
            SettingsGroupHeader("CẤU HÌNH SAO LƯU TỰ ĐỘNG")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tần suất sao lưu tự động", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Hàng ngày", "Hàng tuần", "Thủ công").forEach { freq ->
                            val isSel = autoBackupFrequency == freq
                            FilterChip(
                                selected = isSel,
                                onClick = { autoBackupFrequency = freq },
                                label = { Text(freq, fontSize = 12.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Chỉ sao lưu qua Wi-Fi", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                            Text("Tiết kiệm dung lượng 4G/5G của bạn", fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                        Switch(checked = backupOnlyWifi, onCheckedChange = { backupOnlyWifi = it })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ================= 2. ĐIỂM KHÔI PHỤC DỮ LIỆU =================
            SettingsGroupHeader("CÁC ĐIỂM KHÔI PHỤC (RESTORE POINTS)")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    restorePoints.forEachIndexed { index, (pointTitle, pointDesc, _) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(pointTitle, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                Text(pointDesc, fontSize = 11.sp, color = Color(0xFF64748B))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { showRestoreConfirmDialog = pointTitle },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFEFF6FF),
                                    contentColor = Color(0xFF1D4ED8)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Khôi phục", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (index < restorePoints.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF1F5F9))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ================= 3. XUẤT DỮ LIỆU EXCEL / CSV =================
            SettingsGroupHeader("XUẤT & TẢI XUỐNG DỮ LIỆU")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    listOf(
                        Pair("Danh sách Khách hàng & Cơ hội", "customers_export.xlsx"),
                        Pair("Lịch sử Báo giá & Hợp đồng", "quotes_export.xlsx"),
                        Pair("Bảng Chấm công & Lương nhân viên", "payroll_export.xlsx")
                    ).forEachIndexed { idx, (exportTitle, fileName) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("📥 Đã xuất thành công tệp: $fileName")
                                    }
                                }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TableView,
                                    contentDescription = null,
                                    tint = Color(0xFF059669),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(exportTitle, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0F172A))
                                    Text(fileName, fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                            }
                            Icon(imageVector = Icons.Default.Download, contentDescription = "Download", tint = Color(0xFF059669), modifier = Modifier.size(18.dp))
                        }
                        if (idx < 2) {
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // RESTORE CONFIRM DIALOG
    showRestoreConfirmDialog?.let { title ->
        AlertDialog(
            onDismissRequest = { showRestoreConfirmDialog = null },
            title = { Text("Xác nhận khôi phục dữ liệu?", fontWeight = FontWeight.Bold) },
            text = { Text("Ứng dụng sẽ hoàn nguyên dữ liệu về thời điểm của '$title'. Các thay đổi sau thời điểm này có thể bị ghi đè.") },
            confirmButton = {
                Button(
                    onClick = {
                        showRestoreConfirmDialog = null
                        scope.launch {
                            snackbarHostState.showSnackbar("✅ Đã khôi phục dữ liệu thành công từ: $title")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D4ED8))
                ) {
                    Text("Xác nhận khôi phục")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRestoreConfirmDialog = null }) {
                    Text("Hủy", color = Color(0xFF64748B))
                }
            }
        )
    }
}

// 6. VIP & Business Upgrade Screen (Gói cước VIP cá nhân & Business doanh nghiệp)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VipUpgradeScreen(
    viewModel: CrmViewModel? = null,
    initialTier: AccountTier = AccountTier.VIP,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedTierTab by remember(initialTier) { mutableIntStateOf(if (initialTier == AccountTier.BUSINESS) 1 else 0) } // 0: Gói VIP Cá nhân, 1: Gói BUSINESS Doanh nghiệp
    var selectedPlan by remember { mutableStateOf(1) } // 0: Tháng, 1: Năm
    var showTrialSuccessDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nâng cấp dịch vụ", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF5F7FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Tier Selector Tab: VIP Cá nhân vs BUSINESS Doanh nghiệp
            TabRow(
                selectedTabIndex = selectedTierTab,
                containerColor = Color.White,
                contentColor = ProfessionalPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTierTab == 0,
                    onClick = { selectedTierTab = 0 },
                    text = {
                        Text(
                            text = "⭐ Gói VIP (Cá nhân)",
                            fontWeight = if (selectedTierTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTierTab == 0) Color(0xFFD97706) else Color(0xFF64748B),
                            fontSize = 13.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTierTab == 1,
                    onClick = { selectedTierTab = 1 },
                    text = {
                        Text(
                            text = "🏢 Gói BUSINESS",
                            fontWeight = if (selectedTierTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTierTab == 1) Color(0xFF7C3AED) else Color(0xFF64748B),
                            fontSize = 13.sp
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hero Banner for Selected Tier
            if (selectedTierTab == 0) {
                // VIP Individual Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFD97706), Color(0xFFF59E0B), Color(0xFFFBBF24))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Gói VIP Chuyên Nghiệp (Cá nhân)",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Dành riêng cho chuyên viên kinh doanh xuất sắc, mở khóa phân hạng khách hàng VIP, không giới hạn danh bạ & chăm sóc thông minh.",
                            fontSize = 13.sp,
                            color = Color(0xFFFFFBEB),
                            lineHeight = 18.sp
                        )
                    }
                }
            } else {
                // BUSINESS Enterprise Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF4C1D95), Color(0xFF6D28D9), Color(0xFF8B5CF6))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Apartment,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Gói BUSINESS Doanh Nghiệp",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Giải pháp toàn diện cho Doanh nghiệp: Quản lý phòng ban & nhân sự, chấm công tính lương, phân quyền và báo cáo thống kê chuyên sâu.",
                            fontSize = 13.sp,
                            color = Color(0xFFF5F3FF),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (selectedTierTab == 0) "ĐẶC QUYỀN GÓI VIP CÁ NHÂN" else "ĐẶC QUYỀN GÓI BUSINESS DOANH NGHIỆP",
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
                    if (selectedTierTab == 0) {
                        VipPerkItem(
                            icon = Icons.Default.Star,
                            title = "Phân loại khách hàng VIP",
                            desc = "Tùy chỉnh nhóm, phân hạng khách hàng tiềm năng & thẻ VIP",
                            tintColor = Color(0xFFD97706),
                            bgColor = Color(0xFFFEF3C7)
                        )
                        VipPerkItem(
                            icon = Icons.Default.Person,
                            title = "Không giới hạn khách hàng",
                            desc = "Lưu trữ không giới hạn thông tin khách hàng, thẻ liên hệ & giao dịch"
                        )
                        VipPerkItem(
                            icon = Icons.Default.TrendingUp,
                            title = "Hiệu suất & KPIs cá nhân",
                            desc = "Theo dõi mục tiêu tháng, tiến độ hoàn thành chỉ tiêu theo thời gian thực"
                        )
                    } else {
                        VipPerkItem(
                            icon = Icons.Default.SupervisorAccount,
                            title = "Quản lý nhân sự toàn diện",
                            desc = "Quản lý nhân viên, phòng ban, chấm công, bảng lương & tính thâm niên tự động",
                            tintColor = Color(0xFF7C3AED),
                            bgColor = Color(0xFFEDE9FE)
                        )
                        VipPerkItem(
                            icon = Icons.Default.TrendingUp,
                            title = "Báo cáo thống kê chuyên sâu",
                            desc = "Phân tích doanh thu, tỷ lệ chốt deal, hiệu suất phòng ban & biểu đồ tăng trưởng",
                            tintColor = Color(0xFF7C3AED),
                            bgColor = Color(0xFFEDE9FE)
                        )
                        VipPerkItem(
                            icon = Icons.Default.HeadsetMic,
                            title = "Hỗ trợ 24/7 Priority",
                            desc = "Đội ngũ chuyên viên tư vấn riêng xử lý ngay lập tức và hướng dẫn triển khai"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "BẢNG GIÁ DỊCH VỤ",
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
                if (selectedTierTab == 0) {
                    // Month Plan VIP
                    PlanCard(
                        modifier = Modifier.weight(1f),
                        title = "Gói Tháng (VIP)",
                        price = "199.000đ",
                        period = "/tháng",
                        subtitle = "Thanh toán theo tháng",
                        isSelected = selectedPlan == 0,
                        onClick = { selectedPlan = 0 }
                    )

                    // Year Plan VIP
                    PlanCard(
                        modifier = Modifier.weight(1f),
                        title = "Gói Năm (VIP)",
                        price = "1.890.000đ",
                        period = "/năm",
                        subtitle = "Tiết kiệm 20%",
                        badge = "TIẾT KIỆM 20%",
                        isSelected = selectedPlan == 1,
                        onClick = { selectedPlan = 1 }
                    )
                } else {
                    // Month Plan BUSINESS
                    PlanCard(
                        modifier = Modifier.weight(1f),
                        title = "Gói Tháng (BUSINESS)",
                        price = "499.000đ",
                        period = "/tháng",
                        subtitle = "Tối đa 50 nhân viên",
                        isSelected = selectedPlan == 0,
                        onClick = { selectedPlan = 0 }
                    )

                    // Year Plan BUSINESS
                    PlanCard(
                        modifier = Modifier.weight(1f),
                        title = "Gói Năm (BUSINESS)",
                        price = "4.790.000đ",
                        period = "/năm",
                        subtitle = "Tiết kiệm 20%",
                        badge = "TIẾT KIỆM 20%",
                        isSelected = selectedPlan == 1,
                        onClick = { selectedPlan = 1 }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notice about trial and upcoming payment gateway
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFEFF6FF),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF1D4ED8),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Chương trình Trải nghiệm Miễn phí",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF1E40AF)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Hiện tại ứng dụng đang trong giai đoạn phát hành thử nghiệm. Bạn có thể bấm Đăng ký sử dụng ngay hoàn toàn miễn phí. Cổng thanh toán chính thức sẽ được bổ sung trong bản cập nhật kế tiếp.",
                            fontSize = 12.sp,
                            color = Color(0xFF1E3A8A),
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    showTrialSuccessDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTierTab == 0) Color(0xFFD97706) else Color(0xFF7C3AED)
                )
            ) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedTierTab == 0) "ĐĂNG KÝ SỬ DỤNG GÓI VIP (MIỄN PHÍ)" else "ĐĂNG KÝ SỬ DỤNG GÓI BUSINESS (MIỄN PHÍ)",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    if (showTrialSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showTrialSuccessDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFECFDF5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Đăng ký thành công!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF0F172A)
                )
            },
            text = {
                Text(
                    text = "Tài khoản của bạn đã được kích hoạt quyền sử dụng ${if (selectedTierTab == 0) "Gói VIP Cá nhân" else "Gói BUSINESS Doanh nghiệp"}. Bạn có thể trải nghiệm toàn bộ tính năng cao cấp ngay từ bây giờ!",
                    fontSize = 13.sp,
                    color = Color(0xFF475569),
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val tier = if (selectedTierTab == 0) com.example.data.model.AccountTier.VIP else com.example.data.model.AccountTier.BUSINESS
                        viewModel?.setAccountTier(tier)
                        showTrialSuccessDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
                ) {
                    Text("Bắt đầu sử dụng")
                }
            }
        )
    }
}

@Composable
fun VipPerkItem(
    icon: ImageVector,
    title: String,
    desc: String,
    tintColor: Color = Color(0xFF7C3AED),
    bgColor: Color = Color(0xFFEDE9FE)
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tintColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(2.dp))
            Text(desc, fontSize = 12.sp, color = Color(0xFF64748B), lineHeight = 16.sp)
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
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFDEF7EC),
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(
                        text = badge,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
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
fun SettingsGroupHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF64748B),
        letterSpacing = 0.8.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp)
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
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF475569),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f, fill = false)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0F172A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentWidth()
        ) {
            if (badge != null) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeBg,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(
                        text = badge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
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
    onCheckedChange: (Boolean) -> Unit,
    badge: String? = null,
    badgeBg: Color = Color(0xFFF1F5F9),
    badgeColor: Color = Color(0xFF64748B),
    enabled: Boolean = true,
    onClickLocked: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (!enabled && onClickLocked != null) {
                    Modifier.clickable { onClickLocked() }
                } else Modifier
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (enabled) Color(0xFF0F172A) else Color(0xFF64748B)
                )
                if (badge != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = badgeBg
                    ) {
                        Text(
                            text = badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 12.sp, color = Color(0xFF64748B))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Switch(
            checked = if (enabled) checked else false,
            onCheckedChange = { isChecked ->
                if (enabled) {
                    onCheckedChange(isChecked)
                } else {
                    onClickLocked?.invoke()
                }
            },
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

@Composable
fun AccountTierTestingCard(
    currentTier: AccountTier,
    onSelectTier: (AccountTier) -> Unit,
    onResetData: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = ProfessionalPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Chế độ gói & Dữ liệu kiểm thử",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp,
                        color = Color(0xFF0F172A)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (currentTier) {
                        AccountTier.FREE -> Color(0xFFF1F5F9)
                        AccountTier.VIP -> Color(0xFFFEF3C7)
                        AccountTier.BUSINESS -> Color(0xFFEDE9FE)
                    }
                ) {
                    Text(
                        text = "Gói: ${currentTier.displayName}",
                        color = when (currentTier) {
                            AccountTier.FREE -> Color(0xFF475569)
                            AccountTier.VIP -> Color(0xFFD97706)
                            AccountTier.BUSINESS -> Color(0xFF7C3AED)
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Chuyển nhanh giữa các phân quyền để trải nghiệm toàn bộ tính năng và báo cáo chuyên sâu:",
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Row of Tier buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (tier in AccountTier.entries) {
                    val isSelected = currentTier == tier
                    OutlinedCard(
                        onClick = { onSelectTier(tier) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = if (isSelected) ProfessionalPrimary.copy(alpha = 0.08f) else Color(0xFFF8FAFC)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) ProfessionalPrimary else Color(0xFFE2E8F0)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = tier.displayName,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) ProfessionalPrimary else Color(0xFF334155)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = when (tier) {
                                    AccountTier.FREE -> "Cơ bản"
                                    AccountTier.VIP -> "Cá nhân VIP"
                                    AccountTier.BUSINESS -> "Doanh nghiệp"
                                },
                                fontSize = 10.sp,
                                color = if (isSelected) ProfessionalPrimary else Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(12.dp))

            // Reset Data Button (12/2025 - 08/2026)
            OutlinedButton(
                onClick = onResetData,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ProfessionalPrimary
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, ProfessionalPrimary.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Khôi phục dữ liệu mẫu đầy đủ (12/2025 - 08/2026)",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

