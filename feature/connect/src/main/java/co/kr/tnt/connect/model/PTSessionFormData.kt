package co.kr.tnt.connect.model

import java.time.LocalDate

data class PTSessionFormData(
    val completedSession: Int,
    val totalSession: Int,
    val startDate: LocalDate?,
)
