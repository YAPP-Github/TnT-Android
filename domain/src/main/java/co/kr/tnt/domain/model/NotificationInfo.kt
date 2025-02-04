package co.kr.tnt.domain.model

import java.time.LocalDateTime

data class NotificationInfo(
    val type: NotificationType,
    val title: String,
    val contents: String,
    val time: LocalDateTime,
    val isChecked: Boolean,
)

// TODO API 나오면 수정 필요
enum class NotificationType {
    CONNECT_COMPLETE,
    DISCONNECT,
    SCHEDULE,
    ;

    companion object {
        fun from(type: String): NotificationType {
            return when (type) {
                "CONNECT_COMPLETE" -> CONNECT_COMPLETE
                "DISCONNECT" -> DISCONNECT
                "SCHEDULE" -> SCHEDULE
                else -> throw IllegalArgumentException("지원하지 않는 $type 입니다.")
            }
        }
    }
}
