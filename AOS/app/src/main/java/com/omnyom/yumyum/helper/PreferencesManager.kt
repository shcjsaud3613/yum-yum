package com.omnyom.yumyum.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity


class PreferencesManager {
    companion object {
        private const val PREFERENCES_NAME = "my_preference"
        private const val DEFAULT_VALUE_STRING = ""
        private const val DEFAULT_VALUE_LONG = -1L

        // MainActivity에서 값을 부여함
        var userId : Long = 0
        var eurekaDistance : Long = 4
        var pushOn : Long = 1

        private fun getPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }

        fun setString(context: Context, key: String, value: String) {
            with(getPreferences(context).edit()) {
                putString(key, value)
                apply()
            }
        }

        fun getString(context:Context, key: String): String? {
            val prefs = getPreferences(context)
            return prefs.getString(key, DEFAULT_VALUE_STRING)
        }

        fun setLong(context: Context, key: String, value: Long) {
            with(getPreferences(context).edit()) {
                putLong(key, value)
                apply()
            }
        }

        fun getLong(context: Context, key: String): Long? {
            val prefs = getPreferences(context)
            return prefs.getLong(key, DEFAULT_VALUE_LONG)
        }
    }
}