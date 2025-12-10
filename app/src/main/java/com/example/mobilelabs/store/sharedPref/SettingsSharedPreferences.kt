package com.example.mobilelabs.store.sharedPref

import android.content.Context
import androidx.core.content.edit

class SettingsSharedPreferences(private val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    val password: String
        get() = sharedPreferences.getString(PASSWORD_KEY, "") ?: ""

    fun setPassword(password: String) {
        sharedPreferences.edit {
            putString(PASSWORD_KEY, password)
        }
    }

    companion object {
        private const val PASSWORD_KEY = "user.password"
    }
}