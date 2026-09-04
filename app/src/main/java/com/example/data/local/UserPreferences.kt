package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.SecuritySettings
import com.example.data.model.UserProfile
import org.json.JSONArray
import org.json.JSONObject

data class LocalAccount(
    val email: String,
    val password: String = "",
    val fullName: String,
    var isVerified: Boolean = false,
    val isGoogle: Boolean = false
)

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("crm_user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_FULL_NAME = "full_name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE = "phone"
        private const val KEY_DOB = "dob"
        private const val KEY_ADDRESS = "address"
        private const val KEY_ROLE = "role"
        private const val KEY_AVATAR_URL = "avatar_url"
        private const val KEY_IS_VIP = "is_vip"
        private const val KEY_ACCOUNT_TIER = "account_tier"
        private const val KEY_REGISTERED_ACCOUNTS = "registered_accounts"
        private const val KEY_2FA = "key_two_factor_auth"
        private const val KEY_BIOMETRIC = "key_biometric_auth"

        // Default VIP profile values
        const val DEFAULT_NAME = "Quản Trị Viên VIP"
        const val DEFAULT_EMAIL = "admin@crm.vn"
        const val DEFAULT_PHONE = "901234567"
        const val DEFAULT_DOB = "01/01/1990"
        const val DEFAULT_ADDRESS = "123 Đường Lê Lợi, Quận 1, TP.HCM"
        const val DEFAULT_ROLE = "VIP ENTERPRISE"
    }

    fun getSecuritySettings(): SecuritySettings {
        return SecuritySettings(
            twoFactorAuth = prefs.getBoolean(KEY_2FA, false),
            biometricAuth = prefs.getBoolean(KEY_BIOMETRIC, false)
        )
    }

    fun saveSecuritySettings(settings: SecuritySettings) {
        prefs.edit()
            .putBoolean(KEY_2FA, settings.twoFactorAuth)
            .putBoolean(KEY_BIOMETRIC, settings.biometricAuth)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply()
    }

    fun getUserProfile(): UserProfile {
        val rawPhone = prefs.getString(KEY_PHONE, DEFAULT_PHONE) ?: DEFAULT_PHONE
        val cleanPhone = if (rawPhone.startsWith("0")) rawPhone.dropWhile { it == '0' } else rawPhone
        val isVip = prefs.getBoolean(KEY_IS_VIP, true) // Default is VIP per user request
        val tierStr = prefs.getString(KEY_ACCOUNT_TIER, null)
        val accountTier = when (tierStr) {
            "FREE" -> com.example.data.model.AccountTier.FREE
            "VIP" -> com.example.data.model.AccountTier.VIP
            "BUSINESS" -> com.example.data.model.AccountTier.BUSINESS
            else -> if (isVip) com.example.data.model.AccountTier.VIP else com.example.data.model.AccountTier.FREE
        }

        return UserProfile(
            fullName = prefs.getString(KEY_FULL_NAME, DEFAULT_NAME) ?: DEFAULT_NAME,
            email = prefs.getString(KEY_EMAIL, DEFAULT_EMAIL) ?: DEFAULT_EMAIL,
            phone = cleanPhone,
            dob = prefs.getString(KEY_DOB, DEFAULT_DOB) ?: DEFAULT_DOB,
            address = prefs.getString(KEY_ADDRESS, DEFAULT_ADDRESS) ?: DEFAULT_ADDRESS,
            role = prefs.getString(KEY_ROLE, DEFAULT_ROLE) ?: DEFAULT_ROLE,
            avatarUrl = prefs.getString(KEY_AVATAR_URL, null),
            isVip = accountTier.isVipOrHigher,
            accountTier = accountTier
        )
    }

    fun saveUserProfile(profile: UserProfile) {
        prefs.edit()
            .putString(KEY_FULL_NAME, profile.fullName)
            .putString(KEY_EMAIL, profile.email)
            .putString(KEY_PHONE, profile.phone)
            .putString(KEY_DOB, profile.dob)
            .putString(KEY_ADDRESS, profile.address)
            .putString(KEY_ROLE, profile.role)
            .putString(KEY_AVATAR_URL, profile.avatarUrl)
            .putBoolean(KEY_IS_VIP, profile.accountTier.isVipOrHigher)
            .putString(KEY_ACCOUNT_TIER, profile.accountTier.name)
            .apply()
    }

    fun getRegisteredAccounts(): List<LocalAccount> {
        val jsonStr = prefs.getString(KEY_REGISTERED_ACCOUNTS, null)
        val list = mutableListOf<LocalAccount>()
        if (jsonStr != null) {
            try {
                val jsonArray = JSONArray(jsonStr)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    list.add(
                        LocalAccount(
                            email = obj.getString("email"),
                            password = obj.optString("password", ""),
                            fullName = obj.optString("fullName", ""),
                            isVerified = obj.optBoolean("isVerified", true),
                            isGoogle = obj.optBoolean("isGoogle", false)
                        )
                    )
                }
            } catch (_: Exception) {
                // Ignore parse errors
            }
        }

        var modified = false
        // Purge any previously seeded non-admin test accounts
        val removed = list.removeAll { it.email.equals("hakirotomo@gmail.com", ignoreCase = true) }
        if (removed) {
            modified = true
        }

        // admin@crm.vn is the sole default test account
        if (list.none { it.email.equals("admin@crm.vn", ignoreCase = true) }) {
            list.add(
                LocalAccount(
                    email = "admin@crm.vn",
                    password = "admin", // also supports 123456
                    fullName = "Quản Trị Viên VIP",
                    isVerified = true,
                    isGoogle = false
                )
            )
            modified = true
        }

        if (modified || jsonStr == null) {
            saveAllAccounts(list)
        }
        return list
    }

    fun saveRegisteredAccount(account: LocalAccount) {
        val current = getRegisteredAccounts().filter { !it.email.equals(account.email, ignoreCase = true) }.toMutableList()
        current.add(account)
        saveAllAccounts(current)
    }

    fun updateAccountVerification(email: String, isVerified: Boolean) {
        val current = getRegisteredAccounts().map {
            if (it.email.equals(email, ignoreCase = true)) it.copy(isVerified = isVerified) else it
        }
        saveAllAccounts(current)
    }

    fun deleteAccount(email: String) {
        val current = getRegisteredAccounts().filter { !it.email.equals(email, ignoreCase = true) }
        saveAllAccounts(current)
    }

    fun clearUserData() {
        prefs.edit()
            .remove(KEY_EMAIL)
            .remove(KEY_FULL_NAME)
            .remove(KEY_PHONE)
            .remove(KEY_DOB)
            .remove(KEY_ADDRESS)
            .remove(KEY_ROLE)
            .remove(KEY_AVATAR_URL)
            .remove(KEY_IS_VIP)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
    }

    private fun saveAllAccounts(accounts: List<LocalAccount>) {
        try {
            val jsonArray = JSONArray()
            for (acc in accounts) {
                val obj = JSONObject().apply {
                    put("email", acc.email)
                    put("password", acc.password)
                    put("fullName", acc.fullName)
                    put("isVerified", acc.isVerified)
                    put("isGoogle", acc.isGoogle)
                }
                jsonArray.put(obj)
            }
            prefs.edit().putString(KEY_REGISTERED_ACCOUNTS, jsonArray.toString()).apply()
        } catch (_: Exception) {
            // Ignore
        }
    }
}
