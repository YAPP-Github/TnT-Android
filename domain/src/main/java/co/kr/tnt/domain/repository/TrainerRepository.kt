package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.MemberInfo
import co.kr.tnt.domain.model.ProfileImageUpdatePolicy
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainer.TrainerDailyPtSession
import co.kr.tnt.domain.model.trainer.TrainerDailyPtSessionCount
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

interface TrainerRepository {
    suspend fun getMyInfo(): Flow<User.Trainer>
    suspend fun getMonthlyPtSessionCounts(yearMonth: YearMonth): List<TrainerDailyPtSessionCount>
    suspend fun getDailyPtSessions(day: LocalDate): TrainerDailyPtSession
    suspend fun getActiveMembers(): List<MemberInfo>
    suspend fun postPtSession(
        startDateTime: LocalDateTime,
        endLocalDateTime: LocalDateTime,
        memo: String,
        traineeId: Long,
    )

    suspend fun postCompleteSession(ptSessionId: String)
    suspend fun updateUserInfo(
        profileImageUpdatePolicy: ProfileImageUpdatePolicy,
        name: String,
    )
}
