package com.gallopdevs.athanhelper.clock

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gallopdevs.athanhelper.model.PrayerRepository

class ClockViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PrayerRepository = PrayerRepository()

    fun getPrayers() = repository.getPrayers()
}