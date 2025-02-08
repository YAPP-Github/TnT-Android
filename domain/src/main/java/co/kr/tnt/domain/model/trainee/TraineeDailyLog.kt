package co.kr.tnt.domain.model.trainee

import co.kr.tnt.domain.model.RecordType
import java.time.LocalDate

data class TraineeDailyLog(
    val date: LocalDate,
    val ptSession: PtSession?,
    val record: List<DailyRecord>?,
)

data class PtSession(
    val ptSessionId: String,
    val trainerName: String,
    val trainerImage: String?,
    val session: Int,
    val startTime: String,
    val endTime: String,
    val hasRecord: Boolean,
)

// TODO: RecordType 매핑
data class DailyRecord(
    val recordId: String,
    val recordType: RecordType,
    val recordTime: String,
    val recordImage: String?,
    val recordContents: String,
    val feedbackCount: Int,
)
