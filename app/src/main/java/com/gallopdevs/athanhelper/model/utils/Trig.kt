package com.gallopdevs.athanhelper.model.utils

import kotlin.math.*

// range reduce angle in degrees.
fun fixAngle(a: Double): Double {
    var angle = a
    angle -= 360 * floor(angle / 360.0)
    angle = if (angle < 0) angle + 360 else angle
    return angle
}

// range reduce hours to 0..23
fun fixHour(h: Double): Double {
    var hour = h
    hour -= 24.0 * floor(hour / 24.0)
    hour = if (hour < 0) hour + 24 else hour
    return hour
}

// radians to degrees
fun radiansToDegrees(alpha: Double): Double = alpha * 180.0 / Math.PI

// degrees to radians
fun degreesToRadians(alpha: Double): Double = alpha * Math.PI / 180.0

// degree sin
fun degreeSin(d: Double): Double = sin(degreesToRadians(d))

// degree cos
fun degreeCos(d: Double): Double = cos(degreesToRadians(d))

// degree tan
fun degreeTan(d: Double): Double = tan(degreesToRadians(d))

// degree arcsin
fun degreeArcsin(x: Double): Double = radiansToDegrees(asin(x))

// degree arccos
fun degreeArccos(x: Double): Double = radiansToDegrees(acos(x))

// degree arctan
fun degreeArctan(x: Double): Double = radiansToDegrees(atan(x))

// degree arctan2
fun degreeArctan2(y: Double, x: Double): Double = radiansToDegrees(atan2(y, x))

// degree arccot
fun degreeArccot(x: Double): Double = radiansToDegrees(atan2(1.0, x))