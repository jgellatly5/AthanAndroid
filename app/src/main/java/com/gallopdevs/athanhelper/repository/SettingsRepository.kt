package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.LocalDataSource
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) : SettingsRepo {

    override fun saveBoolean(key: String, value: Boolean) = localDataSource.saveBoolean(key, value)

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = localDataSource.getBoolean(key, defaultValue)

    override fun saveInt(key: String, value: Int) = localDataSource.saveInt(key, value)

    override fun getInt(key: String, defaultValue: Int): Int = localDataSource.getInt(key, defaultValue)
}

interface SettingsRepo {
    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun saveInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int): Int
}
