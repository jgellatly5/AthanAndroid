package com.gallopdevs.athanhelper.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class FormatCurrentTimeUseCase @Inject constructor() {
    operator fun invoke(): String {
        val currentTime = Calendar.getInstance().time
        val currentTimeMillisSdf = SimpleDateFormat("HH:mm:ss", Locale.US)
        return currentTimeMillisSdf.format(currentTime)
    }
}
