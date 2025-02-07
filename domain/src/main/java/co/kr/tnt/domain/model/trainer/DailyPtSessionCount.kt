package co.kr.tnt.domain.model.trainer

import java.time.LocalDate

data class DailyPtSessionCount(
    val date: LocalDate,
    val count: Int,
)
