package co.kr.data.repository

import co.kr.data.network.model.UpdateUserInfoRequest
import co.kr.data.network.model.enum.MemberType
import co.kr.data.network.model.toDomain
import co.kr.data.network.model.trainer.PtSessionRequest
import co.kr.data.network.model.trainer.toDomain
import co.kr.data.network.source.TrainerRemoteDataSource
import co.kr.data.network.source.UserRemoteDataSource
import co.kr.tnt.domain.model.MemberInfo
import co.kr.tnt.domain.model.ProfileImageUpdatePolicy
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.domain.model.trainer.TrainerDailyPtSession
import co.kr.tnt.domain.model.trainer.TrainerDailyPtSessionCount
import co.kr.tnt.domain.repository.TrainerRepository
import co.kr.tnt.domain.utils.DateFormatter
import java.time.LocalDate
import java.time.LocalDateTime
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

    override suspend fun getActiveMembers(): List<MemberInfo> =
        trainerRemoteDataSource.getActiveTrainees().trainees.map { response ->
            response.toDomain()
        }

    override suspend fun postPtSession(
        startDateTime: LocalDateTime,
        endLocalDateTime: LocalDateTime,
        memo: String,
        traineeId: Long,
    ) {
        trainerRemoteDataSource.postPtSession(
            PtSessionRequest(
                start = dateFormatter.format(startDateTime),
                end = dateFormatter.format(endLocalDateTime),
                memo = memo,
                traineeId = traineeId,
            ),
        )
    }

    override suspend fun postCompleteSession(ptSessionId: String) =
        trainerRemoteDataSource.putCompletePtSession(ptSessionId)

    override suspend fun updateUserInfo(
        profileImageUpdatePolicy: ProfileImageUpdatePolicy,
        name: String,
    ) {
        val (profileImage, isRemoveProfileImage) = when (profileImageUpdatePolicy) {
            is ProfileImageUpdatePolicy.Change -> profileImageUpdatePolicy.newProfileImage to false
            ProfileImageUpdatePolicy.Keep -> null to false
            ProfileImageUpdatePolicy.Remove -> null to true
        }

        trainerRemoteDataSource.putUserInfo(
            profileImage = profileImage,
            request = UpdateUserInfoRequest(
                removeImage = isRemoveProfileImage,
                memberType = MemberType.from(UserType.TRAINER),
                name = name,
            ),
        )
    }
}
