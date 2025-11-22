package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.ProfileImageUpdatePolicy
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainee.TraineeDailyRecord
import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import co.kr.tnt.domain.model.trainee.TraineeMealRecordDetail
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.time.LocalDate

interface TraineeRepository {
    suspend fun getMyInfo(): Flow<User.Trainee>
    suspend fun postMealRecord(
        mealImage: File?,
        date: String,
        mealType: String,
        memo: String,
    )
    suspend fun getWeeklyRecordedDate(
        startDate: String,
        endDate: String,
    ): TraineeDailyRecordStatus
    suspend fun getDailyRecord(date: LocalDate): TraineeDailyRecord
    suspend fun getMealRecord(
        dietId: Long,
    ): TraineeMealRecordDetail
    suspend fun updateUserInfo(
        profileImageUpdatePolicy: ProfileImageUpdatePolicy,
        userInfo: User.Trainee,
    )
    suspend fun refreshCachedUserInfo()
    suspend fun clearCachedUserInfo()
}
