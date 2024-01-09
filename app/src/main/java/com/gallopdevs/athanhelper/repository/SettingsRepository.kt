package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.PreferencesMgr
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val preferencesMgr: PreferencesMgr
) : SettingsRepo {

    override fun saveBoolean(key: String, value: Boolean) = preferencesMgr.saveBoolean(key, value)

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = preferencesMgr.getBoolean(key, defaultValue)

    override fun saveInt(key: String, value: Int) = preferencesMgr.saveInt(key, value)

    override fun getInt(key: String, defaultValue: Int): Int = preferencesMgr.getInt(key, defaultValue)
}

interface SettingsRepo {
    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun saveInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int): Int
}
