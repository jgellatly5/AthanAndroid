package com.gallopdevs.athanhelper.di

import com.gallopdevs.athanhelper.settings.PreferencesManager
import com.gallopdevs.athanhelper.settings.PreferencesManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesManagerModule {

    @Binds
    abstract fun bindPreferencesManager(preferencesManagerImpl: PreferencesManagerImpl): PreferencesManager
}
