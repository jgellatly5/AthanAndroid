package com.gallopdevs.athanhelper.model

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