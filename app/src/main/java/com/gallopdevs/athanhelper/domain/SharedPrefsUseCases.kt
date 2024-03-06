package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.repository.SettingsRepo
import javax.inject.Inject

class GetSharedPrefsUseCase @Inject constructor(
    private val settingsRepo: SettingsRepo
) {
    operator fun invoke(type: SharedPrefType, key: String, defaultValue: Any): Comparable<*>? {
        return when (type) {
            SharedPrefType.BOOLEAN -> settingsRepo.getBoolean(key, defaultValue as Boolean)
            SharedPrefType.INT -> settingsRepo.getInt(key, defaultValue as Int)
            SharedPrefType.STRING -> settingsRepo.getString(key, defaultValue as String)
        }
    }
}

class SaveSharedPrefsUseCase @Inject constructor(
    private val settingsRepo: SettingsRepo
) {
    operator fun invoke(type: SharedPrefType, key: String, value: Any) {
        return when (type) {
            SharedPrefType.BOOLEAN -> settingsRepo.saveBoolean(key, value as Boolean)
            SharedPrefType.INT -> settingsRepo.saveInt(key, value as Int)
            SharedPrefType.STRING -> settingsRepo.saveString(key, value as String)
        }
    }
}

enum class SharedPrefType {
    BOOLEAN,
    INT,
    STRING
}
