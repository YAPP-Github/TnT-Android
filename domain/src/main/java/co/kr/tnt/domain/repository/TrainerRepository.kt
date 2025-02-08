package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.trainer.DailyPtSession
import co.kr.tnt.domain.model.trainer.DailyPtSessionCount
import java.time.LocalDate
import java.time.YearMonth

interface TrainerRepository {
    suspend fun getMonthlyPtSessionCounts(yearMonth: YearMonth): List<DailyPtSessionCount>

    suspend fun getDailyPtSessions(day: LocalDate): DailyPtSession
}
