package com.gallopdevs.athanhelper.data.utils

import com.gallopdevs.athanhelper.data.PrayerCalculator
import kotlin.math.abs

// References:
// http://www.ummah.net/astronomy/saltime
// http://aa.usno.navy.mil/faq/docs/SunApprox.html
// compute declination angle of sun_icon and equation of time
fun sunPosition(jd: Double): DoubleArray {
    val D = jd - 2451545
    val g = fixAngle(357.529 + 0.98560028 * D)
    val q = fixAngle(280.459 + 0.98564736 * D)
    val L = fixAngle(q + 1.915 * degreeSin(g) + 0.020 * degreeSin(2 * g))

    // double R = 1.00014 - 0.01671 * [self dcos:g] - 0.00014 * [self dcos:
    // (2*g)];
    val e = 23.439 - 0.00000036 * D
    val d = degreeArcsin(degreeSin(e) * degreeSin(L))
    var RA = degreeArctan2(degreeCos(e) * degreeSin(L), degreeCos(L)) / 15.0
    RA = fixHour(RA)
    val EqT = q / 15.0 - RA
    val sPosition = DoubleArray(2)
    sPosition[0] = d
    sPosition[1] = EqT
    return sPosition
}

// compute equation of time
fun equationOfTime(jd: Double): Double = sunPosition(jd)[1]

// compute declination angle of sun_icon
fun sunDeclination(jd: Double): Double = sunPosition(jd)[0]

// compute mid-day (Dhuhr, Zawal) time
fun PrayerCalculator.computeMidDay(t: Double): Double {
    val time = equationOfTime(jDate + t)
    return fixHour(12 - time)
}

// compute time for a given angle G
fun PrayerCalculator.computeTime(G: Double, t: Double): Double {
    val D = sunDeclination(jDate + t)
    val Z = computeMidDay(t)
    val Beg = -degreeSin(G) - degreeSin(D) * degreeSin(lat)
    val Mid = degreeCos(D) * degreeCos(lat)
    val V = degreeArccos(Beg / Mid) / 15.0
    return Z + if (G > 90) -V else V
}

// compute the time of Asr
// Shafii: step=1, Hanafi: step=2
fun PrayerCalculator.computeAsr(step: Double, t: Double): Double {
    val D = sunDeclination(jDate + t)
    val G = -degreeArccot(step + degreeTan(abs(lat - D)))
    return computeTime(G, t)
}
