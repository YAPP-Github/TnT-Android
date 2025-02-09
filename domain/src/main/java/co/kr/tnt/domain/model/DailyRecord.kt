package co.kr.tnt.domain.model

import java.time.LocalDateTime

data class DailyRecord(
    val recordId: String,
    val recordType: RecordType,
    val recordTime: LocalDateTime,
    val recordImage: String?,
    val recordContents: String,
    val feedbackCount: Int,
)
