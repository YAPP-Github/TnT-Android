package co.kr.data.repository

import co.kr.data.network.model.toDomain
import co.kr.data.network.model.trainee.DailyRecordsResponse
import co.kr.data.network.model.trainee.DietRecordResponse
import co.kr.data.network.model.trainee.MealRecordRequest
import co.kr.data.network.model.trainee.PtSessionResponse
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

    override suspend fun getWeeklyRecordedDate(
        startDate: String,
        endDate: String,
    ): TraineeDailyRecordStatus {
        val response = traineeRemoteDataSource.getWeeklyRecordedDates(startDate, endDate)
        return response.toDomain(dateFormatter)
    }

    override suspend fun getTraineeDailyRecord(day: LocalDate): TraineeDailyRecord {
        val result = listOf(
            DailyRecordsResponse(
                date = "2025-02-03",
                ptInfo = PtSessionResponse(
                    trainerName = "김헬스",
                    trainerProfileImage = "https://buly.kr/44x6xFN",
                    session = 12,
                    lessonStart = "2025-02-03T08:00:00",
                    lessonEnd = "2025-02-03T09:00:00",
                ),
                diets = listOf(
                    DietRecordResponse(
                        dietId = 1001L,
                        date = "2025-02-03T08:00:00",
                        dietImageUrl = "https://buly.kr/BpESNP5",
                        dietType = "BREAKFAST",
                        memo = "아침으로 계란 2개 먹었습니다.",
                    ),
                    DietRecordResponse(
                        dietId = 1002L,
                        date = "2025-02-03T13:00:00",
                        dietImageUrl = "https://buly.kr/BpESNP5",
                        dietType = "LUNCH",
                        memo = "점심으로 계란 5개 먹었습니다.",
                    ),
                ),
            ),
            DailyRecordsResponse(
                date = "2025-02-08",
                ptInfo = null,
                diets = listOf(
                    DietRecordResponse(
                        dietId = 2001L,
                        date = "2025-02-08T13:00:00",
                        dietImageUrl = "https://buly.kr/BpESNP5",
                        dietType = "BREAKFAST",
                        memo = "계란 2개 먹었습니다.",
                    ),
                    DietRecordResponse(
                        dietId = 2002L,
                        date = "2025-02-08T15:00:00",
                        dietImageUrl = "https://buly.kr/BpESNP5",
                        dietType = "SNACK",
                        memo = "계란 반개 먹었습니다.",
                    ),
                    DietRecordResponse(
                        dietId = 2003L,
                        date = "2025-02-08T18:40:00",
                        dietImageUrl = null,
                        dietType = "DINNER",
                        memo = "저녁으로 소고기 먹었습니다.",
                    ),
                ),
            ),
            DailyRecordsResponse(
                date = "2025-02-15",
                ptInfo = PtSessionResponse(
                    trainerName = "이강사",
                    trainerProfileImage = null,
                    session = 15,
                    lessonStart = "2025-02-15T18:00:00",
                    lessonEnd = "2025-02-15T19:00:00",
                ),
                diets = listOf(
                    DietRecordResponse(
                        dietId = 3001L,
                        date = "2025-02-15T13:00:00",
                        dietImageUrl = null,
                        dietType = "LUNCH",
                        memo = "비빔밥, 바나나 1개",
                    ),
                    DietRecordResponse(
                        dietId = 3002L,
                        date = "2025-02-15T20:00:00",
                        dietImageUrl = "https://buly.kr/BpESNP5",
                        dietType = "DINNER",
                        memo = "계란 5개 먹었습니다.",
                    ),
                ),
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
