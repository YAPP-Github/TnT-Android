package co.kr.data.repository

import co.kr.data.network.model.trainee.DailyRecordsResponse
import co.kr.data.network.model.trainee.MonthlyRecordedDatesResponse
import co.kr.data.network.model.trainee.PtSessionResponse
import co.kr.data.network.model.trainee.RecordResponse
import co.kr.data.network.model.trainee.RecordedDateResponse
import co.kr.data.network.model.trainee.toDomain
import co.kr.tnt.domain.model.trainee.TraineeDailyRecord
import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.domain.utils.DateFormatter
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TraineeRepositoryImpl @Inject constructor(
    private val dateFormatter: DateFormatter,
) : TraineeRepository {
    // TODO : API에 맞춰 수정
    override suspend fun getDailyDataStatus(yearMonth: YearMonth): List<TraineeDailyRecordStatus> {
        val result = MonthlyRecordedDatesResponse(
            listOf(
                RecordedDateResponse(date = "2025-02-03"),
                RecordedDateResponse(date = "2025-02-08"),
                RecordedDateResponse(date = "2025-02-10"),
                RecordedDateResponse(date = "2025-02-15"),
                RecordedDateResponse(date = "2025-02-23"),
            ),
        ).calendarRecordInfo.map { response ->
            response.toDomain(dateFormatter)
        }

        return result
    }

    override suspend fun getTraineeDailyRecord(day: LocalDate): TraineeDailyRecord {
        val result = listOf(
            DailyRecordsResponse(
                date = "2025-02-03",
                lessons = PtSessionResponse(
                    ptSessionId = "CDE35K32",
                    trainerName = "김헬스",
                    trainerImage = "https://buly.kr/44x6xFN",
                    session = 12,
                    startTime = "2025-02-03T08:00:00.000Z",
                    endTime = "2025-02-03T09:00:00.000Z",
                    hasRecord = true,
                ),
                records = listOf(
                    RecordResponse(
                        recordId = "VDF1D907",
                        recordType = "BREAKFAST",
                        recordTime = "2025-02-03T08:00:00.000Z",
                        recordImage = "https://buly.kr/BpESNP5",
                        recordContents = "아침으로 계란 2개 먹었습니다.",
                        feedbackCount = 1,
                    ),
                    RecordResponse(
                        recordId = "VDF1D907",
                        recordType = "LUNCH",
                        recordTime = "2025-02-03T13:00:00.000Z",
                        recordImage = "https://buly.kr/BpESNP5",
                        recordContents = "점심으로 계란 5개 먹었습니다.",
                        feedbackCount = 0,
                    ),
                ),
            ),
            DailyRecordsResponse(
                date = "2025-02-08",
                lessons = null,
                records = listOf(
                    RecordResponse(
                        recordId = "VDF1D907",
                        recordType = "BREAKFAST",
                        recordTime = "2025-02-08T13:00:00.000Z",
                        recordImage = "https://buly.kr/BpESNP5",
                        recordContents = "계란 2개 먹었습니다.",
                        feedbackCount = 1,
                    ),
                    RecordResponse(
                        recordId = "VDF1D907",
                        recordType = "SNACK",
                        recordTime = "2025-02-08T15:00:00.000Z",
                        recordImage = "https://buly.kr/BpESNP5",
                        recordContents = "계란 반개 먹었습니다.",
                        feedbackCount = 0,
                    ),
                    RecordResponse(
                        recordId = "VDF1D907",
                        recordType = "DINNER",
                        recordTime = "2025-02-08T18:40:00.000Z",
                        recordImage = null,
                        recordContents = "저녁으로 소고기 먹었습니다.",
                        feedbackCount = 2,
                    ),
                ),
            ),
            DailyRecordsResponse(
                date = "2025-02-15",
                lessons = PtSessionResponse(
                    ptSessionId = "OSI93DG1",
                    trainerName = "이강사",
                    trainerImage = null,
                    session = 15,
                    startTime = "2025-02-15T18:00:00.000Z",
                    endTime = "2025-02-15T19:00:00.000Z",
                    hasRecord = true,
                ),
                records = listOf(
                    RecordResponse(
                        recordId = "VDF1D907",
                        recordType = "LUNCH",
                        recordTime = "2025-02-15T13:00:00.000Z",
                        recordImage = null,
                        recordContents = "비빔밥, 바나나 1개",
                        feedbackCount = 1,
                    ),
                    RecordResponse(
                        recordId = "VDF1D907",
                        recordType = "DINNER",
                        recordTime = "2025-02-03T20:00:00.000Z",
                        recordImage = "https://buly.kr/BpESNP5",
                        recordContents = "계란 5개 먹었습니다.",
                        feedbackCount = 0,
                    ),
                ),
            ),
            DailyRecordsResponse(
                date = "2025-02-10",
                lessons = PtSessionResponse(
                    ptSessionId = "CDK392DF",
                    trainerName = "박트레이너",
                    trainerImage = null,
                    session = 10,
                    startTime = "2025-02-10T14:30:00.000Z",
                    endTime = "2025-02-10T15:30:00.000Z",
                    hasRecord = false,
                ),
                records = emptyList(),
            ),
            DailyRecordsResponse(
                date = "2025-02-23",
                lessons = PtSessionResponse(
                    ptSessionId = "CDE35K32",
                    trainerName = "정트레이너",
                    trainerImage = "https://buly.kr/44x6xFN",
                    session = 25,
                    startTime = "2025-02-23T06:00:00.000Z",
                    endTime = "2025-02-23T06:50:00.000Z",
                    hasRecord = true,
                ),
                records = emptyList(),
            ),
        ).map { response ->
            response.toDomain(dateFormatter)
        }.firstOrNull { it.date == day }

        val noData = TraineeDailyRecord(
            date = day,
            ptSession = null,
            record = emptyList(),
        )

        return result ?: noData
    }
}
