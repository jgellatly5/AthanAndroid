package com.gallopdevs.athanhelper.di

import android.content.Context
import com.gallopdevs.athanhelper.data.TimingsDao
import com.gallopdevs.athanhelper.data.TimingsResponsesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideTimingsResponsesDatabase(@ApplicationContext context: Context): TimingsResponsesDatabase {
        return TimingsResponsesDatabase.getDatabase(context)
    }

    @Provides
    fun provideTimingsDao(timingsResponsesDatabase: TimingsResponsesDatabase): TimingsDao {
        return timingsResponsesDatabase.timingsDao()
    }
}