package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.MemberInfo
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainer.TrainerDailyPtSession
import co.kr.tnt.domain.model.trainer.TrainerDailyPtSessionCount
import java.time.LocalDate
import java.time.YearMonth

interface TrainerRepository {
    suspend fun getMyInfo(): User.Trainer
    suspend fun getMonthlyPtSessionCounts(yearMonth: YearMonth): List<TrainerDailyPtSessionCount>
    suspend fun getDailyPtSessions(day: LocalDate): TrainerDailyPtSession
    suspend fun getActiveMembers(): List<MemberInfo>
}
