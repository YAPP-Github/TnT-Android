package co.kr.tnt.trainee.connect.model

import java.time.LocalDate

data class PTSessionFormData(
    val completedSession: Int,
    val totalSession: Int,
    val selectedStartDate: LocalDate,
)
