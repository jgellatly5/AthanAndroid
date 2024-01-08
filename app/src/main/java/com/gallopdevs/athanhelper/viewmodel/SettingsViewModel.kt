package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.ViewModel
import com.gallopdevs.athanhelper.repository.SettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo
) : ViewModel() {

    fun saveBoolean(key: String, value: Boolean) = settingsRepo.saveBoolean(key, value)

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
        settingsRepo.getBoolean(key, defaultValue)

    fun saveInt(key: String, value: Int) = settingsRepo.saveInt(key, value)

    fun getInt(key: String, defaultValue: Int = 0): Int = settingsRepo.getInt(key, defaultValue)
}
