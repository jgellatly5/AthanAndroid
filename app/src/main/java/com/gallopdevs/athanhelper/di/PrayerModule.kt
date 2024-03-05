package com.gallopdevs.athanhelper.di

import com.gallopdevs.athanhelper.data.NetworkRemoteDataSource
import com.gallopdevs.athanhelper.data.PrayerLocalDataSource
import com.gallopdevs.athanhelper.data.RemoteDataSource
import com.gallopdevs.athanhelper.data.RoomLocalDataSource
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
    abstract fun bindRemoteDataSource(networkRemoteDataSource: NetworkRemoteDataSource): RemoteDataSource

    @Binds
    abstract fun bindPrayerLocalDataSource(roomLocalDataSource: RoomLocalDataSource): PrayerLocalDataSource
}
