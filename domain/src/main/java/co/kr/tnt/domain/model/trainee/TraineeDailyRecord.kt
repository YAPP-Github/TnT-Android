package co.kr.tnt.domain.model.trainee

import co.kr.tnt.domain.model.DailyRecord
import java.time.LocalDate
import java.time.LocalDateTime

data class TraineeDailyRecord(
    val date: LocalDate,
    val ptSession: TraineePtSession?,
    val record: List<DailyRecord>,
)

data class TraineePtSession(
    val ptSessionId: Long,
    val trainerName: String,
    val trainerImage: String?,
    val session: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val hasRecord: Boolean,
)
