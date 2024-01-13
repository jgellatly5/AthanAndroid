package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.SettingsLocalDataSource
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : SettingsRepo {

    override fun saveBoolean(key: String, value: Boolean) = settingsLocalDataSource.saveBoolean(key, value)

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = settingsLocalDataSource.getBoolean(key, defaultValue)

    override fun saveInt(key: String, value: Int) = settingsLocalDataSource.saveInt(key, value)

    override fun getInt(key: String, defaultValue: Int): Int = settingsLocalDataSource.getInt(key, defaultValue)

    override fun saveString(key: String, value: String) = settingsLocalDataSource.saveString(key, value)

    override fun getString(key: String, defaultValue: String): String? = settingsLocalDataSource.getString(key, defaultValue)
}

interface SettingsRepo {
    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun saveInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int): Int
    fun saveString(key: String, value: String)
    fun getString(key: String, defaultValue: String): String?
}
