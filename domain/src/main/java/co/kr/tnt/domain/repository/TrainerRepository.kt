package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.trainer.DailyPtSessionCount
import java.time.YearMonth

interface TrainerRepository {
    suspend fun getMonthlyPtSessionCounts(yearMonth: YearMonth): List<DailyPtSessionCount>
}
