package com.gallopdevs.athanhelper.di

import android.content.Context
import android.content.SharedPreferences
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.SETTINGS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
}
