package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
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
        private const val KEY_REGISTERED_ACCOUNTS = "registered_accounts"

        // Default VIP profile values
        const val DEFAULT_NAME = "Quản Trị Viên VIP"
        const val DEFAULT_EMAIL = "admin@crm.vn"
        const val DEFAULT_PHONE = "901234567"
        const val DEFAULT_DOB = "01/01/1990"
        const val DEFAULT_ADDRESS = "123 Đường Lê Lợi, Quận 1, TP.HCM"
        const val DEFAULT_ROLE = "VIP ENTERPRISE"
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
        return UserProfile(
            fullName = prefs.getString(KEY_FULL_NAME, DEFAULT_NAME) ?: DEFAULT_NAME,
            email = prefs.getString(KEY_EMAIL, DEFAULT_EMAIL) ?: DEFAULT_EMAIL,
            phone = cleanPhone,
            dob = prefs.getString(KEY_DOB, DEFAULT_DOB) ?: DEFAULT_DOB,
            address = prefs.getString(KEY_ADDRESS, DEFAULT_ADDRESS) ?: DEFAULT_ADDRESS,
            role = prefs.getString(KEY_ROLE, DEFAULT_ROLE) ?: DEFAULT_ROLE,
            avatarUrl = prefs.getString(KEY_AVATAR_URL, null),
            isVip = prefs.getBoolean(KEY_IS_VIP, true) // Default is VIP per user request
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
            .putBoolean(KEY_IS_VIP, profile.isVip)
            .apply()
    }

    fun getRegisteredAccounts(): List<LocalAccount> {
        val jsonStr = prefs.getString(KEY_REGISTERED_ACCOUNTS, null) ?: return emptyList()
        val list = mutableListOf<LocalAccount>()
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
