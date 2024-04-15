package dev.crmodders.versionsgen.utils

import java.time.LocalDate
import java.time.Month
import java.time.ZoneOffset

class TimeUtils private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        fun getTodayUnixTime(
            year: Int = LocalDate.now().year,
            month: Month = LocalDate.now().month,
            day: Int = LocalDate.now().dayOfMonth
        ): Long {
            return LocalDate.of(year, month, day).atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        }
    }
}