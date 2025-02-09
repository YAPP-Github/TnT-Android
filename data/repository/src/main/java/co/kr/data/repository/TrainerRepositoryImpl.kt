package co.kr.data.repository

import co.kr.data.network.model.trainer.MemberListResponse
import co.kr.data.network.model.trainer.MemberResponse
import co.kr.data.network.model.trainer.toDomain
import co.kr.data.network.source.TrainerRemoteDataSource
import co.kr.tnt.domain.model.MemberInfo
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

    override suspend fun getDailyPtSessions(day: LocalDate): TrainerDailyPtSession =
        trainerRemoteDataSource.getDailyPtSessions(
            date = dateFormatter.format(day, "yyyy-MM-dd"),
        ).toDomain(dateFormatter)

    // TODO : API 연동
    override suspend fun getMemberList(): List<MemberInfo> {
        val result = MemberListResponse(
            memberList = listOf(
                MemberResponse(
                    traineeName = "김선정",
                    profileUrl = "https://buly.kr/BpESNP5",
                    ptPurpose = "체중 감량, 근력 향상, 건강 관리, 자세 교정, 유연성 향상",
                    memo = "발목 안 좋고 식단 관리 원함. 하체가 약한 편임. 손목도 약함",
                    finishedPtCount = 4,
                    totalPtCount = 20,
                ),
                MemberResponse(
                    traineeName = "김영명",
                    profileUrl = "https://buly.kr/BpESNP5",
                    ptPurpose = "근력 향상",
                    memo = "식단 관리 원함.",
                    finishedPtCount = 10,
                    totalPtCount = 16,
                ),
                MemberResponse(
                    traineeName = "김정호",
                    profileUrl = "https://buly.kr/BpESNP5",
                    ptPurpose = "근력 향상, 건강 관리",
                    memo = "직장인. 6시 퇴근이라 이후에 수업 일정 잡아야 함.",
                    finishedPtCount = 35,
                    totalPtCount = 50,
                ),
                MemberResponse(
                    traineeName = "박민서",
                    profileUrl = "https://buly.kr/BpESNP5",
                    ptPurpose = "근력 향상, 건강 관리",
                    memo = "수면 부족임. 잠을 잘 잤는지 확인 필요",
                    finishedPtCount = 98,
                    totalPtCount = 99,
                ),
                MemberResponse(
                    traineeName = "김혜림",
                    profileUrl = "https://buly.kr/BpESNP5",
                    ptPurpose = "건강 관리",
                    memo = "3시 이후 수업 가능",
                    finishedPtCount = 5,
                    totalPtCount = 50,
                ),
                MemberResponse(
                    traineeName = "김혜린",
                    profileUrl = "https://buly.kr/BpESNP5",
                    ptPurpose = "근력 향상",
                    memo = "식단 원하심. 3시 이후 수업 가능",
                    finishedPtCount = 43,
                    totalPtCount = 60,
                ),
            ),
        ).memberList.map { response ->
            response.toDomain()
        }

        return result
    }
}
