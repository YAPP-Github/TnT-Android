package co.kr.tnt.ui.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class NotificationTimeFormatter {
    fun formatTime(notificationTime: LocalDateTime): String {
        val now = LocalDateTime.now()

        val minutesDiff = ChronoUnit.MINUTES.between(notificationTime, now)
        val hoursDiff = ChronoUnit.HOURS.between(notificationTime, now)

        return when {
            minutesDiff < 1 -> "방금"
            minutesDiff < 60 -> "${minutesDiff}분 전"
            hoursDiff < 24 -> "${hoursDiff}시간 전"
            else -> {
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.getDefault())
                notificationTime.format(dateFormatter)
            }
        }
    }
}
