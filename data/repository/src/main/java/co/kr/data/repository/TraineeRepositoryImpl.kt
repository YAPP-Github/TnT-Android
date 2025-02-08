package co.kr.data.repository

import co.kr.tnt.domain.model.RecordType
import co.kr.tnt.domain.model.trainee.DailyDataStatus
import co.kr.tnt.domain.model.trainee.DailyRecord
import co.kr.tnt.domain.model.trainee.PtSession
import co.kr.tnt.domain.model.trainee.TraineeDailyLog
import co.kr.tnt.domain.repository.TraineeRepository
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TraineeRepositoryImpl @Inject constructor() : TraineeRepository {
    // TODO : API에 맞춰 수정
    override suspend fun getDailyDataStatus(yearMonth: YearMonth): DailyDataStatus {
        val result = DailyDataStatus(
            date = listOf(
                LocalDate.of(2025, 2, 3),
                LocalDate.of(2025, 2, 8),
                LocalDate.of(2025, 2, 10),
                LocalDate.of(2025, 2, 15),
                LocalDate.of(2025, 2, 23),
            ),
        )

        return result
    }

    override suspend fun getTraineeDailyLog(day: LocalDate): TraineeDailyLog {
        val result = listOf(
            TraineeDailyLog(
                date = LocalDate.of(2025, 2, 3),
                ptSession = PtSession(
                    ptLessonId = "CDE35K32",
                    trainerName = "김헬스",
                    trainerImage = "https://buly.kr/44x6xFN",
                    session = 12,
                    startTime = "2025-02-03T08:00:00.000Z",
                    endTime = "2025-02-03T09:00:00.000Z",
                    hasRecord = true,
                ),
                record = listOf(
                    DailyRecord(
                        recordId = "VDF1D907",
                        recordType = RecordType.MealType.BREAKFAST,
                        recordTime = "2025-02-03T08:00:00.000Z",
                        recordImage = "https://buly.kr/BpESNP5",
                        recordContents = "아침으로 계란 2개 먹었습니다.",
                        feedbackCount = 1,
                    ),
                    DailyRecord(
                        recordId = "VDF1D907",
                        recordType = RecordType.MealType.LUNCH,
                        recordTime = "2025-02-03T13:00:00.000Z",
                        recordImage = "https://buly.kr/BpESNP5",
                        recordContents = "점심으로 계란 5개 먹었습니다.",
                        feedbackCount = 0,
                    ),
                ),
            ),
            TraineeDailyLog(
                date = LocalDate.of(2025, 2, 8),
                ptSession = null,
                record = listOf(
                    DailyRecord(
                        recordId = "VDF1D907",
                        recordType = RecordType.MealType.BREAKFAST,
                        recordTime = "2025-02-08T13:00:00.000Z",
                        recordImage = "https://buly.kr/BpESNP5",
                        recordContents = "계란 2개 먹었습니다.",
                        feedbackCount = 1,
                    ),
                    DailyRecord(
                        recordId = "VDF1D907",
                        recordType = RecordType.MealType.SNACK,
                        recordTime = "2025-02-08T15:00:00.000Z",
                        recordImage = "https://buly.kr/BpESNP5",
                        recordContents = "계란 반개 먹었습니다.",
                        feedbackCount = 0,
                    ),
                    DailyRecord(
                        recordId = "VDF1D907",
                        recordType = RecordType.MealType.DINNER,
                        recordTime = "2025-02-08T18:40:00.000Z",
                        recordImage = null,
                        recordContents = "저녁으로 소고기 먹었습니다.",
                        feedbackCount = 2,
                    ),
                ),
            ),
            TraineeDailyLog(
                date = LocalDate.of(2025, 2, 15),
                ptSession = PtSession(
                    ptLessonId = "OSI93DG1",
                    trainerName = "이강사",
                    trainerImage = null,
                    session = 15,
                    startTime = "2025-02-15T18:00:00.000Z",
                    endTime = "2025-02-15T19:00:00.000Z",
                    hasRecord = true,
                ),
                record = listOf(
                    DailyRecord(
                        recordId = "VDF1D907",
                        recordType = RecordType.MealType.LUNCH,
                        recordTime = "2025-02-15T13:00:00.000Z",
                        recordImage = null,
                        recordContents = "비빔밥, 바나나 1개",
                        feedbackCount = 1,
                    ),
                    DailyRecord(
                        recordId = "VDF1D907",
                        recordType = RecordType.MealType.DINNER,
                        recordTime = "2025-02-03T20:00:00.000Z",
                        recordImage = "https://buly.kr/BpESNP5",
                        recordContents = "계란 5개 먹었습니다.",
                        feedbackCount = 0,
                    ),
                ),
            ),
            TraineeDailyLog(
                date = LocalDate.of(2025, 2, 10),
                ptSession = PtSession(
                    ptLessonId = "CDK392DF",
                    trainerName = "박트레이너",
                    trainerImage = null,
                    session = 10,
                    startTime = "2025-02-10T14:30:00.000Z",
                    endTime = "2025-02-10T15:30:00.000Z",
                    hasRecord = false,
                ),
                record = null,
            ),
            TraineeDailyLog(
                date = LocalDate.of(2025, 2, 23),
                ptSession = PtSession(
                    ptLessonId = "CDE35K32",
                    trainerName = "정트레이너",
                    trainerImage = "https://buly.kr/44x6xFN",
                    session = 25,
                    startTime = "2025-02-23T06:00:00.000Z",
                    endTime = "2025-02-23T06:50:00.000Z",
                    hasRecord = true,
                ),
                record = null,
            ),
        )

        val filteredRecord = result.find { it.date == day }
        val noData = TraineeDailyLog(
            date = day,
            ptSession = null,
            record = null,
        )

        return filteredRecord ?: noData
    }
}
