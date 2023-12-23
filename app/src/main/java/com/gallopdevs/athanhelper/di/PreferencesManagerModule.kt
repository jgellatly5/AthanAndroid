package com.gallopdevs.athanhelper.di

import com.gallopdevs.athanhelper.ui.settings.PreferencesManager
import com.gallopdevs.athanhelper.ui.settings.PreferencesManagerImpl
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
