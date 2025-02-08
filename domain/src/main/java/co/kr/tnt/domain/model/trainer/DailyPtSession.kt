package co.kr.tnt.domain.model.trainer

import java.time.LocalDate
import java.time.LocalDateTime

data class DailyPtSession(
    val date: LocalDate,
    val sessions: List<PtSession>,
)

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
