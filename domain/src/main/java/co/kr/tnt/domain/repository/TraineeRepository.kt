package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainee.TraineeDailyRecord
import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime

interface TraineeRepository {
    suspend fun getMyInfo(): User.Trainee
    suspend fun postMealRecord(
        mealImage: File?,
        date: LocalDateTime,
        mealType: String,
        memo: String,
    )
    suspend fun getWeeklyRecordedDate(
        startDate: String,
        endDate: String,
    ): TraineeDailyRecordStatus
    suspend fun getDailyRecord(date: LocalDate): TraineeDailyRecord
}
