package com.gallopdevs.athanhelper.settings

import android.content.Context

class PreferencesManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(ENABLE_NOTIFICATIONS, Context.MODE_PRIVATE)

    fun saveData(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    companion object {
        const val ENABLE_NOTIFICATIONS = "ENABLE_NOTIFICATIONS"
    }
}
