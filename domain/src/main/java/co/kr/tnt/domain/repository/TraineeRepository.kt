package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.trainee.DailyDataStatus
import co.kr.tnt.domain.model.trainee.TraineeDailyLog
import java.time.LocalDate
import java.time.YearMonth

interface TraineeRepository {
    suspend fun getDailyDataStatus(yearMonth: YearMonth): DailyDataStatus

    suspend fun getTraineeDailyLog(date: LocalDate): TraineeDailyLog
}
