package com.gallopdevs.athanhelper.di

import com.gallopdevs.athanhelper.data.SettingsLocalDataSource
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource
import com.gallopdevs.athanhelper.repository.SettingsRepository
import com.gallopdevs.athanhelper.repository.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    abstract fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    abstract fun bindLocalDataSource(sharedPreferencesLocalDataSource: SharedPreferencesLocalDataSource): SettingsLocalDataSource
}
