package co.kr.data.repository

import co.kr.data.network.model.toDomain
import co.kr.data.network.model.trainer.toDomain
import co.kr.data.network.source.TrainerRemoteDataSource
import co.kr.data.network.source.UserRemoteDataSource
import co.kr.tnt.domain.model.MemberInfo
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainer.TrainerDailyPtSession
import co.kr.tnt.domain.model.trainer.TrainerDailyPtSessionCount
import co.kr.tnt.domain.repository.TrainerRepository
import co.kr.tnt.domain.utils.DateFormatter
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TrainerRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val trainerRemoteDataSource: TrainerRemoteDataSource,
    private val dateFormatter: DateFormatter,
) : TrainerRepository {
    override suspend fun getMonthlyPtSessionCounts(yearMonth: YearMonth): List<TrainerDailyPtSessionCount> =
        trainerRemoteDataSource.getMonthlyPtSessionCounts(
            year = yearMonth.year,
            month = yearMonth.monthValue,
        ).calendarPtLessonCounts.map { response ->
            response.toDomain(dateFormatter)
        }

    override suspend fun getMyInfo(): User.Trainer {
        val user = userRemoteDataSource.getMyInfo().toDomain(dateFormatter)
        require(user is User.Trainer)
        return user
    }

    override suspend fun getDailyPtSessions(day: LocalDate): TrainerDailyPtSession =
        trainerRemoteDataSource.getDailyPtSessions(
            date = dateFormatter.format(day, "yyyy-MM-dd"),
        ).toDomain(dateFormatter)

    override suspend fun getMemberList(): List<MemberInfo> =
        trainerRemoteDataSource.getActiveTraineeList().memberList.map { response ->
            response.toDomain()
        }
}
