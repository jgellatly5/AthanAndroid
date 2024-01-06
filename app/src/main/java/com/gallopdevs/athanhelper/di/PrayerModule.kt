package com.gallopdevs.athanhelper.di

import com.gallopdevs.athanhelper.data.PrayerCalculator
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml
import com.gallopdevs.athanhelper.repository.PrayerRepo
import com.gallopdevs.athanhelper.repository.PrayerRepository
import com.gallopdevs.athanhelper.data.PreferencesManager
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl
import com.gallopdevs.athanhelper.repository.SettingsRepo
import com.gallopdevs.athanhelper.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PrayerModule {

    @Binds
    abstract fun bindPrayerRepo(prayerRepository: PrayerRepository): PrayerRepo

    @Binds
    abstract fun bindSettingsRepo(settingsRepository: SettingsRepository): SettingsRepo

    @Binds
    abstract fun bindPrayerCalculator(prayerCalculatorImpl: PrayerCalculatorIpml): PrayerCalculator

    @Binds
    abstract fun bindPreferencesManager(preferencesManagerImpl: PreferencesManagerImpl): PreferencesManager
}
