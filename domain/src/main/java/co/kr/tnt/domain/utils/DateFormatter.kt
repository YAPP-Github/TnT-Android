package co.kr.tnt.domain.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateFormatter @Inject constructor() {
    fun parse(
        rawDate: String,
        formatter: DateTimeFormatter = DEFAULT_DATE_FORMAT,
    ): LocalDate {
        require(rawDate.isNotBlank())

        return LocalDate.parse(rawDate, formatter)
    }

    fun format(
        date: LocalDate,
        formatter: DateTimeFormatter = DEFAULT_DATE_FORMAT,
    ): String = formatter.format(date)

    companion object {
        private val DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }
}
