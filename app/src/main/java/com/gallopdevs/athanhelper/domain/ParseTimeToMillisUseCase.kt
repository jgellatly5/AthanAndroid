package com.gallopdevs.athanhelper.domain

import java.text.ParseException
import java.text.SimpleDateFormat
import javax.inject.Inject

class ParseTimeToMillisUseCase @Inject constructor() {
    operator fun invoke(simpleDateFormat: SimpleDateFormat, time: String?): Long {
        return try {
            simpleDateFormat.parse("${time?.replace("(GMT)", "")}")?.time ?: 0
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }
}
