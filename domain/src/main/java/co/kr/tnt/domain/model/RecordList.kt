package co.kr.tnt.domain.model

import java.time.LocalDate

data class RecordList(
    val recordDate: LocalDate,
    val recordType: RecordType,
    val recordTime: String,
    val recordImage: String?,
    val recordContents: String,
    val hasFeedback: Boolean,
)
