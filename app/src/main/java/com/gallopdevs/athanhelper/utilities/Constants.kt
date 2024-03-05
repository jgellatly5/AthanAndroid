package com.gallopdevs.athanhelper.utilities

const val BASE_URL = "http://api.aladhan.com/v1/"

const val JAFARI = 0 // Ithna Ashari
const val KARACHI = 1 // University of Islamic Sciences, Karachi
const val ISNA = 2 // Islamic Society of North America (ISNA)
const val MWL = 3 // Muslim World League (MWL)
const val MAKKAH = 4 // Umm al-Qura, Makkah
const val EGYPT = 5 // Egyptian General Authority of Survey
const val TEHRAN = 6 // Institute of Geophysics, University of Tehran
const val CUSTOM = 7 // Custom Setting

// Asr Juristic Methods
const val SHAFII = 0 // Shafii (standard)
const val HANAFI = 1 // Hanafi

// Adjusting Methods for Higher Latitudes
const val NONE = 0
const val MIDNIGHT = 1 // middle of night
const val ONE_SEVENTH = 2 // 1/7th of night
const val ANGLE_BASED = 3 // angle/60th of night

// Time Formats
const val TIME_24 = 0 // 24-hour format
const val TIME_12 = 1 // 12-hour format
const val TIME_12_NS = 2 // 12-hour format with no suffix
const val FLOATING = 3 // floating point number
