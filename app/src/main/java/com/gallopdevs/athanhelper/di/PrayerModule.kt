package com.gallopdevs.athanhelper.di

import com.gallopdevs.athanhelper.model.PrayerCalculator
import com.gallopdevs.athanhelper.model.PrayerCalculatorIpml
import com.gallopdevs.athanhelper.repository.PrayerRepo
import com.gallopdevs.athanhelper.repository.PrayerRepository
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
    abstract fun bindPrayerCalculator(prayerCalculatorImpl: PrayerCalculatorIpml): PrayerCalculator
}
