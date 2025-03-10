package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.repository.SettingsRepository
import javax.inject.Inject

class GetSharedPrefsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(type: SharedPrefType, key: String, defaultValue: Any): Comparable<*>? {
        return when (type) {
            SharedPrefType.BOOLEAN -> settingsRepository.getBoolean(key, defaultValue as Boolean)
            SharedPrefType.INT -> settingsRepository.getInt(key, defaultValue as Int)
            SharedPrefType.STRING -> settingsRepository.getString(key, defaultValue as String)
        }
    }
}

class SaveSharedPrefsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(type: SharedPrefType, key: String, value: Any) {
        return when (type) {
            SharedPrefType.BOOLEAN -> settingsRepository.saveBoolean(key, value as Boolean)
            SharedPrefType.INT -> settingsRepository.saveInt(key, value as Int)
            SharedPrefType.STRING -> settingsRepository.saveString(key, value as String)
        }
    }
}

enum class SharedPrefType {
    BOOLEAN,
    INT,
    STRING
}
