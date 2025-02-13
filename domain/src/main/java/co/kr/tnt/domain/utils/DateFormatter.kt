package co.kr.tnt.domain.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateFormatter @Inject constructor() {
    fun parse(
        rawDate: String,
        formatter: DateTimeFormatter = DEFAULT_DATE_FORMATTER,
    ): LocalDate {
        require(rawDate.isNotBlank())
        return LocalDate.parse(rawDate, formatter)
    }

    fun parseDateTime(
        rawDate: String,
        pattern: String = "yyyy-MM-dd'T'HH:mm:ss",
    ): LocalDateTime {
        require(rawDate.isNotBlank())
        return LocalDateTime.parse(rawDate, DateTimeFormatter.ofPattern(pattern))
    }

    fun format(
        date: LocalDate,
        formatter: DateTimeFormatter = DEFAULT_DATE_FORMATTER,
    ): String = formatter.format(date)

    fun format(
        date: LocalDate,
        pattern: String = "yyyy-MM-dd",
    ): String = DateTimeFormatter.ofPattern(pattern).format(date)

    fun format(
        date: LocalDateTime,
        pattern: String = "yyyy-MM-dd'T'HH:mm:ss",
    ): String = DateTimeFormatter.ofPattern(pattern).format(date)

    fun format(
        date: LocalTime,
        pattern: String = "HH:mm",
    ): String = DateTimeFormatter.ofPattern(pattern).format(date)

    companion object {
        private val DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }
}
