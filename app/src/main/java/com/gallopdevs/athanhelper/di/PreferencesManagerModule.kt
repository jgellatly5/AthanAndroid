package com.gallopdevs.athanhelper.di

import android.content.Context
import com.gallopdevs.athanhelper.settings.PreferencesManager
import com.gallopdevs.athanhelper.settings.PreferencesManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PreferencesManagerModule {

    @Provides
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager = PreferencesManagerImpl(context)
}
