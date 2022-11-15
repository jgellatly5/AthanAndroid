package com.gallopdevs.athanhelper.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gallopdevs.athanhelper.model.PrayerRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PrayerRepository = PrayerRepository()

    fun getPrayers() = repository.getPrayers()
}