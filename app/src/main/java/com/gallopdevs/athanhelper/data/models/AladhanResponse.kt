package com.gallopdevs.athanhelper.data.models

import com.squareup.moshi.Json

data class AladhanResponse(
    @Json(name = "code") var code: Int? = null,
    @Json(name = "status") var status: String? = null,
    @Json(name = "data") var data: Data? = Data()
)

data class Data(
    @Json(name = "timings") var timings: Timings? = Timings(),
    @Json(name = "date") var date: Date? = Date(),
    @Json(name = "meta") var meta: Meta? = Meta()
)

data class Timings(
    @Json(name = "Fajr") var fajr: String? = null,
    @Json(name = "Sunrise") var sunrise: String? = null,
    @Json(name = "Dhuhr") var dhuhr: String? = null,
    @Json(name = "Asr") var asr: String? = null,
    @Json(name = "Sunset") var sunset: String? = null,
    @Json(name = "Maghrib") var maghrib: String? = null,
    @Json(name = "Isha") var isha: String? = null,
    @Json(name = "Imsak") var imsak: String? = null,
    @Json(name = "Midnight") var midnight: String? = null
) {
    operator fun iterator(): Iterator<Pair<String, String?>> {
        return object : Iterator<Pair<String, String?>> {
            private val properties = listOf(
                "Fajr" to fajr,
                "Sunrise" to sunrise,
                "Dhuhr" to dhuhr,
                "Asr" to asr,
                "Sunset" to sunset,
                "Maghrib" to maghrib,
                "Isha" to isha,
                "Imsak" to imsak,
                "Midnight" to midnight
            )

            private var currentIndex = 0

            override fun hasNext(): Boolean {
                return currentIndex < properties.size
            }

            override fun next(): Pair<String, String?> {
                if (!hasNext()) {
                    throw NoSuchElementException()
                }
                return properties[currentIndex++]
            }
        }
    }
}

data class Date(
    @Json(name = "readable") var readable: String? = null,
    @Json(name = "timestamp") var timestamp: String? = null,
    @Json(name = "gregorian") var gregorian: Gregorian? = Gregorian(),
    @Json(name = "hijri") var hijri: Hijri? = Hijri()
)

data class Gregorian(
    @Json(name = "date") var date: String? = null,
    @Json(name = "format") var format: String? = null,
    @Json(name = "day") var day: String? = null,
    @Json(name = "weekday") var weekday: Weekday? = Weekday(),
    @Json(name = "month") var month: AladhanMonth? = AladhanMonth(),
    @Json(name = "year") var year: String? = null,
    @Json(name = "designation") var designation: Designation? = Designation()
)

data class Weekday(
    @Json(name = "en") var en: String? = null,
    @Json(name = "ar") var ar: String? = null
)

data class AladhanMonth(
    @Json(name = "number") var number: Int? = null,
    @Json(name = "en") var en: String? = null,
    @Json(name = "ar") var ar: String? = null
)

data class Designation(
    @Json(name = "abbreviated") var abbreviated: String? = null,
    @Json(name = "expanded") var expanded: String? = null
)

data class Meta(
    @Json(name = "latitude") var latitude: Double? = null,
    @Json(name = "longitude") var longitude: Double? = null,
    @Json(name = "timezone") var timezone: String? = null,
    @Json(name = "method") var method: Method? = Method(),
    @Json(name = "latitudeAdjustmentMethod") var latitudeAdjustmentMethod: String? = null,
    @Json(name = "midnightMode") var midnightMode: String? = null,
    @Json(name = "school") var school: String? = null,
    @Json(name = "offset") var offset: AladhanOffset? = AladhanOffset()
)

data class Method(
    @Json(name = "id") var id: Int? = null,
    @Json(name = "name") var name: String? = null,
    @Json(name = "params") var params: Params? = Params()
)

data class AladhanOffset(
    @Json(name = "Imsak") var Imsak: Int? = null,
    @Json(name = "Fajr") var Fajr: Int? = null,
    @Json(name = "Sunrise") var Sunrise: Int? = null,
    @Json(name = "Dhuhr") var Dhuhr: Int? = null,
    @Json(name = "Asr") var Asr: Int? = null,
    @Json(name = "Maghrib") var Maghrib: Int? = null,
    @Json(name = "Sunset") var Sunset: Int? = null,
    @Json(name = "Isha") var Isha: Int? = null,
    @Json(name = "Midnight") var Midnight: Int? = null
)

data class Params(
    @Json(name = "Fajr") var Fajr: Int? = null,
    @Json(name = "Isha") var Isha: Int? = null
)

data class Hijri(
    @Json(name = "date") var date: String? = null,
    @Json(name = "format") var format: String? = null,
    @Json(name = "day") var day: String? = null,
    @Json(name = "weekday") var weekday: Weekday? = Weekday(),
    @Json(name = "month") var month: AladhanMonth? = AladhanMonth(),
    @Json(name = "year") var year: String? = null,
    @Json(name = "designation") var designation: Designation? = Designation(),
    @Json(name = "holidays") var holidays: List<String> = listOf()
)
