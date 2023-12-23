package com.gallopdevs.athanhelper.ui.settings

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesManagerImpl @Inject constructor(
    @ApplicationContext context: Context
) : PreferencesManager {
    private val sharedPreferences =
        context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    override fun saveBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    override fun saveInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int =
        sharedPreferences.getInt(key, defaultValue)

    companion object {
        const val SETTINGS = "SETTINGS"
        const val CALCULATION_METHOD = "CALCULATION_METHOD"
        const val ASR_METHOD = "ASR_METHOD"
        const val LATITUDES = "LATITUDES"
    }
}

interface PreferencesManager {
    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun saveInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int): Int
}
