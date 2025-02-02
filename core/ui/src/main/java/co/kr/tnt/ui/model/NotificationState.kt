package co.kr.tnt.ui.model

import co.kr.tnt.designsystem.component.notification.model.NotificationIcon
import co.kr.tnt.domain.model.NotificationInfo
import co.kr.tnt.domain.model.NotificationType

data class NotificationState(
    val type: NotificationIcon,
    val title: String,
    val contents: String,
    val time: String,
    val isChecked: Boolean = true,
) {
    companion object {
        fun fromDomain(domain: NotificationInfo): NotificationState {
            return NotificationState(
                type = when (domain.type) {
                    NotificationType.LINK -> NotificationIcon.LINK
                    NotificationType.SCHEDULE -> NotificationIcon.SCHEDULE
                },
                title = domain.title,
                contents = domain.contents,
                time = domain.time,
                isChecked = domain.isChecked,
            )
        }
    }
}

fun List<NotificationInfo>.toUiStateList(): List<NotificationState> {
    return this.map { NotificationState.fromDomain(it) }
}
