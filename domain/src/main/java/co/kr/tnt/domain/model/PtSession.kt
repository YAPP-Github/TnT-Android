package co.kr.tnt.domain.model

import java.time.LocalDateTime

data class PtSession(
    val id: String,
    val round: Int,
    val traineeId: String,
    val traineeName: String,
    val traineeProfileUrl: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val isCompleted: Boolean,
)
