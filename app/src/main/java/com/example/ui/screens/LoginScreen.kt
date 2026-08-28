package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ProfessionalPrimary
import com.example.ui.theme.ProfessionalPrimaryNavy

enum class AuthMode {
    LOGIN,
    REGISTER
}

enum class RegisterOption {
    GMAIL,
    PERSONAL_EMAIL
}

data class RegisteredAccount(
    val email: String,
    val password: String = "",
    val fullName: String,
    var isVerified: Boolean = false,
    val isGoogle: Boolean = false
)

@Composable
fun LoginScreen(
    onLoginSuccess: (email: String, name: String) -> Unit
) {
    var authMode by remember { mutableStateOf(AuthMode.LOGIN) }
    var registerOption by remember { mutableStateOf(RegisterOption.GMAIL) }

    // Account Store in-memory for testing both flows
    val accounts = remember {
        mutableStateListOf(
            RegisteredAccount(
                email = "admin@nexus.crm",
                password = "admin",
                fullName = "Quản Trị Viên CRM",
                isVerified = true,
                isGoogle = false
            ),
            RegisteredAccount(
                email = "hakirotomo@gmail.com",
                password = "",
                fullName = "Tomo Nguyen",
                isVerified = true,
                isGoogle = true
            )
        )
    }

    // Login Form State
    var loginEmail by remember { mutableStateOf("admin@nexus.crm") }
    var loginPassword by remember { mutableStateOf("admin") }
    var isLoginPasswordVisible by remember { mutableStateOf(false) }
    var loginErrorMessage by remember { mutableStateOf<String?>(null) }

    // Register Form State
    var regFullName by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var regConfirmPassword by remember { mutableStateOf("") }
    var isRegPasswordVisible by remember { mutableStateOf(false) }
    var registerErrorMessage by remember { mutableStateOf<String?>(null) }

    // Dialogs
    var showVerificationEmailDialog by remember { mutableStateOf(false) }
    var pendingVerificationAccount by remember { mutableStateOf<RegisteredAccount?>(null) }
    var showGoogleAccountSelectorDialog by remember { mutableStateOf(false) }
    var showActivationSuccessDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FB))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Navy Square Logo with Building Icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(ProfessionalPrimaryNavy, ProfessionalPrimary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Apartment,
                        contentDescription = "CRM Logo",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Kinetic CRM Enterprise",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Text(
                    text = if (authMode == AuthMode.LOGIN)
                        "Chào mừng trở lại! Vui lòng đăng nhập để tiếp tục."
                    else
                        "Tạo tài khoản mới để trải nghiệm hệ sinh thái CRM.",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Tab Switcher: Đăng nhập vs Đăng ký
                TabRow(
                    selectedTabIndex = if (authMode == AuthMode.LOGIN) 0 else 1,
                    containerColor = Color(0xFFF1F5F9),
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[if (authMode == AuthMode.LOGIN) 0 else 1]),
                            color = ProfessionalPrimary,
                            height = 3.dp
                        )
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = authMode == AuthMode.LOGIN,
                        onClick = {
                            authMode = AuthMode.LOGIN
                            loginErrorMessage = null
                        },
                        text = {
                            Text(
                                "Đăng nhập",
                                fontWeight = if (authMode == AuthMode.LOGIN) FontWeight.Bold else FontWeight.Normal,
                                color = if (authMode == AuthMode.LOGIN) ProfessionalPrimary else Color(0xFF64748B)
                            )
                        }
                    )
                    Tab(
                        selected = authMode == AuthMode.REGISTER,
                        onClick = {
                            authMode = AuthMode.REGISTER
                            registerErrorMessage = null
                        },
                        text = {
                            Text(
                                "Đăng ký",
                                fontWeight = if (authMode == AuthMode.REGISTER) FontWeight.Bold else FontWeight.Normal,
                                color = if (authMode == AuthMode.REGISTER) ProfessionalPrimary else Color(0xFF64748B)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedContent(
                    targetState = authMode,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "auth_form_transition"
                ) { mode ->
                    when (mode) {
                        AuthMode.LOGIN -> {
                            // ==================== LOGIN FORM ====================
                            Column(modifier = Modifier.fillMaxWidth()) {
                                if (loginErrorMessage != null) {
                                    Surface(
                                        color = Color(0xFFFEF2F2),
                                        shape = RoundedCornerShape(8.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 14.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Warning,
                                                contentDescription = "Error",
                                                tint = Color(0xFFDC2626),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = loginErrorMessage ?: "",
                                                color = Color(0xFFB91C1C),
                                                fontSize = 12.sp,
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }
                                }

                                // Email Field
                                Text(
                                    text = "Email / Tài khoản",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF334155),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )

                                OutlinedTextField(
                                    value = loginEmail,
                                    onValueChange = {
                                        loginEmail = it
                                        loginErrorMessage = null
                                    },
                                    placeholder = { Text("Nhập địa chỉ email", color = Color(0xFF94A3B8)) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Email,
                                            contentDescription = "Email",
                                            tint = Color(0xFF94A3B8)
                                        )
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Next
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ProfessionalPrimary,
                                        unfocusedBorderColor = Color(0xFFCBD5E1)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("login_email_input")
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Password Field
                                Text(
                                    text = "Mật khẩu",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF334155),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )

                                OutlinedTextField(
                                    value = loginPassword,
                                    onValueChange = {
                                        loginPassword = it
                                        loginErrorMessage = null
                                    },
                                    placeholder = { Text("Nhập mật khẩu", color = Color(0xFF94A3B8)) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Password",
                                            tint = Color(0xFF94A3B8)
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { isLoginPasswordVisible = !isLoginPasswordVisible }) {
                                            Icon(
                                                imageVector = if (isLoginPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = if (isLoginPasswordVisible) "Hide password" else "Show password",
                                                tint = Color(0xFF94A3B8)
                                            )
                                        }
                                    },
                                    visualTransformation = if (isLoginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Password,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(onDone = {
                                        performLogin(
                                            email = loginEmail,
                                            password = loginPassword,
                                            accounts = accounts,
                                            onError = { loginErrorMessage = it },
                                            onUnverified = {
                                                pendingVerificationAccount = it
                                                showVerificationEmailDialog = true
                                            },
                                            onSuccess = onLoginSuccess
                                        )
                                    }),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ProfessionalPrimary,
                                        unfocusedBorderColor = Color(0xFFCBD5E1)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("login_password_input")
                                )

                                // Forgot Password Link
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "Quên mật khẩu?",
                                        fontSize = 12.sp,
                                        color = ProfessionalPrimary,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier
                                            .clickable { /* mock */ }
                                            .padding(vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                // Login Button
                                Button(
                                    onClick = {
                                        performLogin(
                                            email = loginEmail,
                                            password = loginPassword,
                                            accounts = accounts,
                                            onError = { loginErrorMessage = it },
                                            onUnverified = {
                                                pendingVerificationAccount = it
                                                showVerificationEmailDialog = true
                                            },
                                            onSuccess = onLoginSuccess
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("login_submit_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Đăng nhập",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                // OR Divider
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                                    Text(
                                        text = "HOẶC",
                                        fontSize = 11.sp,
                                        color = Color(0xFF94A3B8),
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )
                                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Fast Google Sign-in
                                OutlinedButton(
                                    onClick = { showGoogleAccountSelectorDialog = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                                    border = ButtonDefaults.outlinedButtonBorder(true)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        GoogleGIcon()
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Đăng nhập nhanh với Google",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF1E293B)
                                        )
                                    }
                                }
                            }
                        }

                        AuthMode.REGISTER -> {
                            // ==================== REGISTER FORM ====================
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Sub-option selector: Gmail vs Personal Email
                                Text(
                                    text = "Phương thức đăng ký:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF334155),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Option 1: Gmail (Tự động liên kết)
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable {
                                                registerOption = RegisterOption.GMAIL
                                                registerErrorMessage = null
                                            },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (registerOption == RegisterOption.GMAIL) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                                        border = androidx.compose.foundation.BorderStroke(
                                            width = if (registerOption == RegisterOption.GMAIL) 1.5.dp else 1.dp,
                                            color = if (registerOption == RegisterOption.GMAIL) ProfessionalPrimary else Color(0xFFE2E8F0)
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                GoogleGIcon(size = 18)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Tài khoản Gmail",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (registerOption == RegisterOption.GMAIL) ProfessionalPrimary else Color(0xFF475569)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Liên kết tự động 1-chạm",
                                                fontSize = 10.sp,
                                                color = Color(0xFF059669),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    // Option 2: Personal Email (Xác nhận qua link)
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable {
                                                registerOption = RegisterOption.PERSONAL_EMAIL
                                                registerErrorMessage = null
                                            },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (registerOption == RegisterOption.PERSONAL_EMAIL) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                                        border = androidx.compose.foundation.BorderStroke(
                                            width = if (registerOption == RegisterOption.PERSONAL_EMAIL) 1.5.dp else 1.dp,
                                            color = if (registerOption == RegisterOption.PERSONAL_EMAIL) ProfessionalPrimary else Color(0xFFE2E8F0)
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Email,
                                                    contentDescription = "Email",
                                                    tint = if (registerOption == RegisterOption.PERSONAL_EMAIL) ProfessionalPrimary else Color(0xFF64748B),
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Email cá nhân",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (registerOption == RegisterOption.PERSONAL_EMAIL) ProfessionalPrimary else Color(0xFF475569)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Xác nhận qua link mail",
                                                fontSize = 10.sp,
                                                color = Color(0xFFD97706),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                if (registerOption == RegisterOption.GMAIL) {
                                    // GMAIL AUTO-LINK CARD
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Link,
                                                contentDescription = "Link",
                                                tint = ProfessionalPrimary,
                                                modifier = Modifier.size(36.dp)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Đăng ký & Liên kết tự động qua Gmail",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = Color(0xFF0F172A),
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Tài khoản Gmail của bạn sẽ được tự động kích hoạt và liên kết ngay lập tức mà không cần qua bước xác thực email thủ công.",
                                                fontSize = 12.sp,
                                                color = Color(0xFF64748B),
                                                textAlign = TextAlign.Center,
                                                lineHeight = 16.sp
                                            )

                                            Spacer(modifier = Modifier.height(16.dp))

                                            Button(
                                                onClick = { showGoogleAccountSelectorDialog = true },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(46.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimaryNavy)
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    GoogleGIcon()
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        "Chọn tài khoản Gmail để liên kết",
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // PERSONAL EMAIL REGISTRATION FORM
                                    if (registerErrorMessage != null) {
                                        Surface(
                                            color = Color(0xFFFEF2F2),
                                            shape = RoundedCornerShape(8.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 14.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Warning,
                                                    contentDescription = "Error",
                                                    tint = Color(0xFFDC2626),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = registerErrorMessage ?: "",
                                                    color = Color(0xFFB91C1C),
                                                    fontSize = 12.sp,
                                                    lineHeight = 16.sp
                                                )
                                            }
                                        }
                                    }

                                    // Full Name Field
                                    Text(
                                        text = "Họ và tên",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF334155),
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )

                                    OutlinedTextField(
                                        value = regFullName,
                                        onValueChange = {
                                            regFullName = it
                                            registerErrorMessage = null
                                        },
                                        placeholder = { Text("Ví dụ: Nguyễn Văn B", color = Color(0xFF94A3B8)) },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = "Full Name",
                                                tint = Color(0xFF94A3B8)
                                            )
                                        },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = ProfessionalPrimary,
                                            unfocusedBorderColor = Color(0xFFCBD5E1)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Personal Email Field
                                    Text(
                                        text = "Email cá nhân (Nhận link xác thực)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF334155),
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )

                                    OutlinedTextField(
                                        value = regEmail,
                                        onValueChange = {
                                            regEmail = it
                                            registerErrorMessage = null
                                        },
                                        placeholder = { Text("user@company.com", color = Color(0xFF94A3B8)) },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Email,
                                                contentDescription = "Email",
                                                tint = Color(0xFF94A3B8)
                                            )
                                        },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Email,
                                            imeAction = ImeAction.Next
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = ProfessionalPrimary,
                                            unfocusedBorderColor = Color(0xFFCBD5E1)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Password Field
                                    Text(
                                        text = "Mật khẩu",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF334155),
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )

                                    OutlinedTextField(
                                        value = regPassword,
                                        onValueChange = {
                                            regPassword = it
                                            registerErrorMessage = null
                                        },
                                        placeholder = { Text("Tối thiểu 6 ký tự", color = Color(0xFF94A3B8)) },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Lock,
                                                contentDescription = "Password",
                                                tint = Color(0xFF94A3B8)
                                            )
                                        },
                                        trailingIcon = {
                                            IconButton(onClick = { isRegPasswordVisible = !isRegPasswordVisible }) {
                                                Icon(
                                                    imageVector = if (isRegPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                    contentDescription = if (isRegPasswordVisible) "Hide password" else "Show password",
                                                    tint = Color(0xFF94A3B8)
                                                )
                                            }
                                        },
                                        visualTransformation = if (isRegPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Password,
                                            imeAction = ImeAction.Next
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = ProfessionalPrimary,
                                            unfocusedBorderColor = Color(0xFFCBD5E1)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Confirm Password Field
                                    Text(
                                        text = "Xác nhận mật khẩu",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF334155),
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )

                                    OutlinedTextField(
                                        value = regConfirmPassword,
                                        onValueChange = {
                                            regConfirmPassword = it
                                            registerErrorMessage = null
                                        },
                                        placeholder = { Text("Nhập lại mật khẩu", color = Color(0xFF94A3B8)) },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Lock,
                                                contentDescription = "Confirm Password",
                                                tint = Color(0xFF94A3B8)
                                            )
                                        },
                                        visualTransformation = if (isRegPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Password,
                                            imeAction = ImeAction.Done
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = ProfessionalPrimary,
                                            unfocusedBorderColor = Color(0xFFCBD5E1)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Registration Info Notice
                                    Surface(
                                        color = Color(0xFFFFFBEB),
                                        shape = RoundedCornerShape(8.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = "Info",
                                                tint = Color(0xFFD97706),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Hệ thống sẽ tự động gửi email xác thực kèm link kích hoạt. Bạn cần bấm vào link để kích hoạt tài khoản trước khi đăng nhập.",
                                                fontSize = 11.sp,
                                                color = Color(0xFF92400E),
                                                lineHeight = 15.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(18.dp))

                                    // Submit Register Button
                                    Button(
                                        onClick = {
                                            if (regFullName.isBlank()) {
                                                registerErrorMessage = "Vui lòng nhập họ và tên của bạn."
                                                return@Button
                                            }
                                            if (regEmail.isBlank() || !regEmail.contains("@")) {
                                                registerErrorMessage = "Vui lòng nhập địa chỉ email hợp lệ."
                                                return@Button
                                            }
                                            if (regPassword.length < 6) {
                                                registerErrorMessage = "Mật khẩu phải có ít nhất 6 ký tự."
                                                return@Button
                                            }
                                            if (regPassword != regConfirmPassword) {
                                                registerErrorMessage = "Mật khẩu xác nhận không khớp."
                                                return@Button
                                            }

                                            // Check if email exists
                                            val existing = accounts.find { it.email.equals(regEmail.trim(), ignoreCase = true) }
                                            if (existing != null) {
                                                registerErrorMessage = "Email này đã được đăng ký trên hệ thống."
                                                return@Button
                                            }

                                            // Create pending unverified account
                                            val newAccount = RegisteredAccount(
                                                email = regEmail.trim(),
                                                password = regPassword,
                                                fullName = regFullName.trim(),
                                                isVerified = false,
                                                isGoogle = false
                                            )
                                            accounts.add(newAccount)
                                            pendingVerificationAccount = newAccount
                                            showVerificationEmailDialog = true
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary)
                                    ) {
                                        Text(
                                            text = "Đăng ký & Nhận Email Xác Thực",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
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

    // ==================== GOOGLE ACCOUNT SELECTOR DIALOG (AUTO LINK) ====================
    if (showGoogleAccountSelectorDialog) {
        Dialog(onDismissRequest = { showGoogleAccountSelectorDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GoogleGIcon(size = 28)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Đăng nhập với Google",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Chọn tài khoản Google để liên kết tự động",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Option 1: Current Google User
                    GoogleAccountItem(
                        name = "Tomo Nguyen",
                        email = "hakirotomo@gmail.com",
                        avatarChar = "T",
                        onClick = {
                            showGoogleAccountSelectorDialog = false
                            // Auto link and login
                            onLoginSuccess("hakirotomo@gmail.com", "Tomo Nguyen")
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Option 2: Kinetic Enterprise Admin
                    GoogleAccountItem(
                        name = "Kinetic Enterprise",
                        email = "admin.kinetic@gmail.com",
                        avatarChar = "K",
                        onClick = {
                            showGoogleAccountSelectorDialog = false
                            // Auto link and login
                            onLoginSuccess("admin.kinetic@gmail.com", "Kinetic Enterprise")
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { showGoogleAccountSelectorDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Hủy bỏ", color = Color(0xFF64748B))
                    }
                }
            }
        }
    }

    // ==================== MOCK INCOMING EMAIL VERIFICATION MODAL ====================
    if (showVerificationEmailDialog && pendingVerificationAccount != null) {
        val account = pendingVerificationAccount!!

        Dialog(onDismissRequest = { showVerificationEmailDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    // Header simulating email client inbox
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEFF6FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MarkEmailRead,
                                contentDescription = "Mail",
                                tint = ProfessionalPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Hộp thư điện tử (Mô phỏng)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Email tự động gửi từ hệ thống CRM",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email Message Container
                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Từ: no-reply@nexus-crm.com",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "Đến: ${account.email}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF334155)
                            )
                            Text(
                                text = "Tiêu đề: [Nexus CRM] Xác nhận kích hoạt tài khoản của bạn",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            HorizontalDivider(color = Color(0xFFE2E8F0), modifier = Modifier.padding(vertical = 8.dp))

                            Text(
                                text = "Kính chào ${account.fullName},",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Cảm ơn bạn đã đăng ký tài khoản tại Kinetic CRM Enterprise. Để bảo mật và hoàn tất kích hoạt tài khoản của bạn, vui lòng bấm vào nút liên kết xác thực bên dưới:",
                                fontSize = 12.sp,
                                color = Color(0xFF475569),
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Clickable Activation Link Button
                            Button(
                                onClick = {
                                    // Verify account
                                    account.isVerified = true
                                    showVerificationEmailDialog = false
                                    showActivationSuccessDialog = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "👉 BẤM VÀO ĐÂY ĐỂ XÁC NHẬN TÀI KHOẢN",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Liên kết này sẽ hết hạn sau 24 giờ kể từ khi yêu cầu.",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showVerificationEmailDialog = false }) {
                            Text("Đóng hộp thư", color = Color(0xFF64748B))
                        }
                    }
                }
            }
        }
    }

    // ==================== ACTIVATION SUCCESS DIALOG ====================
    if (showActivationSuccessDialog && pendingVerificationAccount != null) {
        val account = pendingVerificationAccount!!

        AlertDialog(
            onDismissRequest = { showActivationSuccessDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(44.dp)
                )
            },
            title = {
                Text(
                    text = "Xác nhận thành công!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = "Tài khoản (${account.email}) của bạn đã được kích hoạt thành công. Bạn có thể đăng nhập vào hệ thống ngay bây giờ.",
                    fontSize = 13.sp,
                    color = Color(0xFF475569),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showActivationSuccessDialog = false
                        // Automatically log user in
                        onLoginSuccess(account.email, account.fullName)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfessionalPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Đăng nhập vào hệ thống ngay", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showActivationSuccessDialog = false
                        authMode = AuthMode.LOGIN
                        loginEmail = account.email
                        loginPassword = account.password
                    }
                ) {
                    Text("Về trang đăng nhập")
                }
            }
        )
    }
}

private fun performLogin(
    email: String,
    password: String,
    accounts: List<RegisteredAccount>,
    onError: (String) -> Unit,
    onUnverified: (RegisteredAccount) -> Unit,
    onSuccess: (email: String, name: String) -> Unit
) {
    if (email.isBlank()) {
        onError("Vui lòng nhập địa chỉ email.")
        return
    }

    val trimmedEmail = email.trim()
    val account = accounts.find { it.email.equals(trimmedEmail, ignoreCase = true) }

    if (account == null) {
        onError("Tài khoản không tồn tại. Vui lòng kiểm tra lại hoặc chuyển sang tab Đăng ký.")
        return
    }

    if (!account.isGoogle && account.password.isNotBlank() && account.password != password) {
        onError("Mật khẩu không chính xác. Vui lòng thử lại.")
        return
    }

    // CHECK IF ACCOUNT IS VERIFIED
    if (!account.isVerified) {
        onError("⚠️ Tài khoản chưa được kích hoạt qua email! Vui lòng bấm vào liên kết xác nhận đã gửi đến hộp thư ${account.email}.")
        onUnverified(account)
        return
    }

    // Login success
    onSuccess(account.email, account.fullName)
}

@Composable
fun GoogleAccountItem(
    name: String,
    email: String,
    avatarChar: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3B82F6)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = avatarChar,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = email,
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Select",
                tint = Color(0xFF059669),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun GoogleGIcon(size: Int = 20) {
    Surface(
        modifier = Modifier.size(size.dp),
        shape = CircleShape,
        color = Color(0xFF4285F4)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "G",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = (size * 0.65).sp
            )
        }
    }
}
