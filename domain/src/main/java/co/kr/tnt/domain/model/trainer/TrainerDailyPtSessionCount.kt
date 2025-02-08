package co.kr.tnt.domain.model.trainer

import java.time.LocalDate

data class TrainerDailyPtSessionCount(
    val date: LocalDate,
    val count: Int,
)
