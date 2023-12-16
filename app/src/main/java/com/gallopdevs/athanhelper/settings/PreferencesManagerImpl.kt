package com.gallopdevs.athanhelper.settings

import android.content.Context

class PreferencesManagerImpl(context: Context) : PreferencesManager {
    private val sharedPreferences =
        context.getSharedPreferences(ENABLE_NOTIFICATIONS, Context.MODE_PRIVATE)

    override fun saveData(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    override fun getData(key: String, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    companion object {
        const val ENABLE_NOTIFICATIONS = "ENABLE_NOTIFICATIONS"
    }
}

interface PreferencesManager {
    fun saveData(key: String, value: Boolean)
    fun getData(key: String, defaultValue: Boolean): Boolean
}
