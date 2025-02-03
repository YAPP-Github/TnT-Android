package co.kr.tnt.ui.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object NotificationTimeUtil {
    fun formatTime(notificationTime: String): String {
        val now = LocalDateTime.now()
        val time = LocalDateTime.parse(notificationTime, DateTimeFormatter.ISO_DATE_TIME)

        val minutesDiff = ChronoUnit.MINUTES.between(time, now)
        val hoursDiff = ChronoUnit.HOURS.between(time, now)

        return when {
            minutesDiff < 1 -> "방금"
            minutesDiff < 60 -> "${minutesDiff}분 전"
            hoursDiff < 24 -> "${hoursDiff}시간 전"
            else -> {
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.getDefault())
                time.format(dateFormatter)
            }
        }
    }
}
