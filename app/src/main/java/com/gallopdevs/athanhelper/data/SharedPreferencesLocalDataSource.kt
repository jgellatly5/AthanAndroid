package com.gallopdevs.athanhelper.data

import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit

class SharedPreferencesLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SettingsLocalDataSource {

    override fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit {
            putBoolean(key, value)
        }
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    override fun saveInt(key: String, value: Int) {
        sharedPreferences.edit {
            putInt(key, value)
        }
    }

    override fun getInt(key: String, defaultValue: Int): Int =
        sharedPreferences.getInt(key, defaultValue)

    override fun saveString(key: String, value: String) {
        sharedPreferences.edit {
            putString(key, value)
        }
    }

    override fun getString(key: String, defaultValue: String): String? =
        sharedPreferences.getString(key, defaultValue)

    companion object {
        const val SETTINGS = "SETTINGS"
        const val ENABLE_NOTIFICATIONS = "ENABLE_NOTIFICATIONS"
        const val CALCULATION_METHOD = "CALCULATION_METHOD"
        const val ASR_METHOD = "ASR_METHOD"
        const val LATITUDES_METHOD = "LATITUDES_METHOD"
        const val TIME_FORMAT = "TIME_FORMAT"
        const val LATITUDE = "LATITUDE"
        const val LONGITUDE = "LONGITUDE"
    }
}

interface SettingsLocalDataSource {
    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun saveInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int): Int
    fun saveString(key: String, value: String)
    fun getString(key: String, defaultValue: String): String?
}
