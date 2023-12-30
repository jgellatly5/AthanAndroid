package com.gallopdevs.athanhelper.model.utils

import com.gallopdevs.athanhelper.model.PrayerCalculatorIpml
import com.gallopdevs.athanhelper.model.PrayerCalculatorIpml.Companion.CUSTOM
import com.gallopdevs.athanhelper.model.PrayerCalculatorIpml.Companion.methodParams

// set the angle for calculating Fajr
fun PrayerCalculatorIpml.setFajrAngle(angle: Double) {
    val params = doubleArrayOf(angle, -1.0, -1.0, -1.0, -1.0)
    setCustomParams(params)
}

// set the angle for calculating Maghrib
fun PrayerCalculatorIpml.setMaghribAngle(angle: Double) {
    val params = doubleArrayOf(-1.0, 0.0, angle, -1.0, -1.0)
    setCustomParams(params)
}

// set the angle for calculating Isha
fun PrayerCalculatorIpml.setIshaAngle(angle: Double) {
    val params = doubleArrayOf(-1.0, -1.0, -1.0, 0.0, angle)
    setCustomParams(params)
}

// set the minutes after Sunset for calculating Maghrib
fun PrayerCalculatorIpml.setMaghribMinutes(minutes: Double) {
    val params = doubleArrayOf(-1.0, 1.0, minutes, -1.0, -1.0)
    setCustomParams(params)
}

// set the minutes after Maghrib for calculating Isha
fun PrayerCalculatorIpml.setIshaMinutes(minutes: Double) {
    val params = doubleArrayOf(-1.0, -1.0, -1.0, 1.0, minutes)
    setCustomParams(params)
}

// set custom values for calculation parameters
fun PrayerCalculatorIpml.setCustomParams(params: DoubleArray) {
    for (i in 0..4) {
        if (params[i].equals(-1.0)) {
            params[i] = methodParams[calcMethod]!![i]
            methodParams[CUSTOM] = params
        } else {
            methodParams[CUSTOM]!![i] = params[i]
        }
    }
    calcMethod = CUSTOM
}
