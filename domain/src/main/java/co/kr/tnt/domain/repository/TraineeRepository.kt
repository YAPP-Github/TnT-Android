package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainee.TraineeDailyRecord
import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

interface TraineeRepository {
    suspend fun getMyInfo(): User.Trainee
    suspend fun getDailyDataStatus(yearMonth: YearMonth): List<TraineeDailyRecordStatus>
    suspend fun getTraineeDailyRecord(day: LocalDate): TraineeDailyRecord
    suspend fun postMealRecord(
        mealImage: File?,
        date: LocalDateTime,
        mealType: String,
        memo: String,
    )
}
