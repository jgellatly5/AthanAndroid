package com.gallopdevs.athanhelper.model.utils

import com.gallopdevs.athanhelper.model.PrayTime

// set the angle for calculating Fajr
fun setFajrAngle(angle: Double) {
    val params = doubleArrayOf(angle, -1.0, -1.0, -1.0, -1.0)
    setCustomParams(params)
}

// set the angle for calculating Maghrib
fun setMaghribAngle(angle: Double) {
    val params = doubleArrayOf(-1.0, 0.0, angle, -1.0, -1.0)
    setCustomParams(params)
}

// set the angle for calculating Isha
fun setIshaAngle(angle: Double) {
    val params = doubleArrayOf(-1.0, -1.0, -1.0, 0.0, angle)
    setCustomParams(params)
}

// set the minutes after Sunset for calculating Maghrib
fun setMaghribMinutes(minutes: Double) {
    val params = doubleArrayOf(-1.0, 1.0, minutes, -1.0, -1.0)
    setCustomParams(params)
}

// set the minutes after Maghrib for calculating Isha
fun setIshaMinutes(minutes: Double) {
    val params = doubleArrayOf(-1.0, -1.0, -1.0, 1.0, minutes)
    setCustomParams(params)
}

// set custom values for calculation parameters
fun setCustomParams(params: DoubleArray) {
    for (i in 0..4) {
        if (params[i].equals(-1.0)) {
            params[i] = PrayTime.methodParams[PrayTime.calcMethod]!![i]
            PrayTime.methodParams[PrayTime.custom] = params
        } else {
            PrayTime.methodParams[PrayTime.custom]!![i] = params[i]
        }
    }
    PrayTime.calcMethod = PrayTime.custom
}