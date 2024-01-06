package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.PreferencesManager
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val preferencesManager: PreferencesManager
) : SettingsRepo {

    override fun saveBoolean(key: String, value: Boolean) = preferencesManager.saveBoolean(key, value)

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = preferencesManager.getBoolean(key, defaultValue)

    override fun saveInt(key: String, value: Int) = preferencesManager.saveInt(key, value)

    override fun getInt(key: String, defaultValue: Int): Int = preferencesManager.getInt(key, defaultValue)
}

interface SettingsRepo {
    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun saveInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int): Int
}
