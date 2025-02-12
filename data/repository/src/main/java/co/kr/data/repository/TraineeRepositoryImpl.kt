package co.kr.data.repository

import co.kr.data.network.model.toDomain
import co.kr.data.network.model.trainee.DailyRecordsResponse
import co.kr.data.network.model.trainee.MealRecordRequest
import co.kr.data.network.model.trainee.MonthlyRecordedDatesResponse
import co.kr.data.network.model.trainee.PtSessionResponse
import co.kr.data.network.model.trainee.RecordResponse
import co.kr.data.network.model.trainee.RecordedDateResponse
import co.kr.data.network.model.trainee.toDomain
import co.kr.data.network.source.TraineeRemoteDataSource
import co.kr.data.network.source.UserRemoteDataSource
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainee.TraineeDailyRecord
import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.domain.utils.DateFormatter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TraineeRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val traineeRemoteDataSource: TraineeRemoteDataSource,
    private val dateFormatter: DateFormatter,
    private val json: Json,
) : TraineeRepository {
    override suspend fun getMyInfo(): User.Trainee {
        val user = userRemoteDataSource.getMyInfo().toDomain(dateFormatter)
        require(user is User.Trainee)
        return user
    }

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

    // TODO : API에 맞춰 수정
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

    override suspend fun postMealRecord(
        mealImage: File?,
        date: LocalDateTime,
        mealType: String,
        memo: String,
    ) {
        val imagePart = mealImage?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("dietImage", it.name, requestFile)
        }

        val mealRecordRequest = MealRecordRequest(
            date = date.toString(),
            dietType = mealType,
            memo = memo,
        )
        val requestBody = mealRecordRequest.toRequestBody()

        traineeRemoteDataSource.postMealRecord(
            dietImage = imagePart,
            request = requestBody,
        )
    }

    private fun MealRecordRequest.toRequestBody(): RequestBody {
        val jsonString = json.encodeToString(this)
        return jsonString.toRequestBody("application/json".toMediaTypeOrNull())
    }
}
