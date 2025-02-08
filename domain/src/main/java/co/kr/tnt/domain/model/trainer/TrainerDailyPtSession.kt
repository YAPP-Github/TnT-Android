package co.kr.tnt.domain.model.trainer

import co.kr.tnt.domain.model.PtSession
import java.time.LocalDate

data class TrainerDailyPtSession(
    val date: LocalDate,
    val sessions: List<PtSession>,
)
