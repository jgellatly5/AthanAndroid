package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.ViewModel
import com.gallopdevs.athanhelper.domain.GetSharedPrefsUseCase
import com.gallopdevs.athanhelper.domain.SaveSharedPrefsUseCase
import com.gallopdevs.athanhelper.domain.SharedPrefType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSharedPrefsUseCase: GetSharedPrefsUseCase,
    private val saveSharedPrefsUseCase: SaveSharedPrefsUseCase
) : ViewModel() {

    fun saveBoolean(key: String, value: Boolean) = saveSharedPrefsUseCase(SharedPrefType.BOOLEAN, key, value)

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
        getSharedPrefsUseCase(SharedPrefType.BOOLEAN, key, defaultValue) as Boolean

    fun saveInt(key: String, value: Int) = saveSharedPrefsUseCase(SharedPrefType.INT, key, value)

    fun getInt(key: String, defaultValue: Int = 0): Int =
        getSharedPrefsUseCase(SharedPrefType.INT, key, defaultValue) as Int

    fun saveString(key: String, value: String) = saveSharedPrefsUseCase(SharedPrefType.STRING, key, value)

    fun getString(key: String, defaultValue: String = ""): String =
        getSharedPrefsUseCase(SharedPrefType.STRING, key, defaultValue) as String
}
