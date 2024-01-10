package com.gallopdevs.athanhelper.di

import com.gallopdevs.athanhelper.data.PrayerCalc
import com.gallopdevs.athanhelper.data.PrayerCalculator
import com.gallopdevs.athanhelper.repository.PrayerRepo
import com.gallopdevs.athanhelper.repository.PrayerRepository
import com.gallopdevs.athanhelper.data.LocalDataSource
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource
import com.gallopdevs.athanhelper.repository.SettingsRepo
import com.gallopdevs.athanhelper.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    abstract fun bindSettingsRepo(settingsRepository: SettingsRepository): SettingsRepo

    @Binds
    abstract fun bindLocalDataSource(sharedPreferencesLocalDataSource: SharedPreferencesLocalDataSource): LocalDataSource
}
