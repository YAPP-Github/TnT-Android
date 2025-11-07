package co.kr.data.repository

import co.kr.data.network.model.UpdateUserInfoRequest
import co.kr.data.network.model.enum.MemberType
import co.kr.data.network.model.toDomain
import co.kr.data.network.model.trainee.MealRecordRequest
import co.kr.data.network.model.trainee.toDomain
import co.kr.data.network.source.TraineeRemoteDataSource
import co.kr.data.network.source.UserRemoteDataSource
import co.kr.tnt.domain.model.ProfileImageUpdatePolicy
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainee.TraineeDailyRecord
import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import co.kr.tnt.domain.model.trainee.TraineeMealRecordDetail
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

    override suspend fun getDailyRecord(date: LocalDate): TraineeDailyRecord {
        val selectedDate = dateFormatter.format(date, "yyyy-MM-dd")
        val response = traineeRemoteDataSource.getDailyRecord(selectedDate)

        return response.toDomain(dateFormatter)
    }

    override suspend fun postMealRecord(
        mealImage: File?,
        date: String,
        mealType: String,
        memo: String,
    ) {
        val imagePart = mealImage?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("dietImage", it.name, requestFile)
        }

        val mealRecordRequest = MealRecordRequest(
            date = date,
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

    override suspend fun getMealRecord(dietId: Long): TraineeMealRecordDetail =
        traineeRemoteDataSource.getMealRecord(dietId).toDomain(dateFormatter)

    override suspend fun updateUserInfo(
        profileImageUpdatePolicy: ProfileImageUpdatePolicy,
        userInfo: User.Trainee,
    ) {
        val (profileImage, isRemoveProfileImage) = when (profileImageUpdatePolicy) {
            is ProfileImageUpdatePolicy.Change -> profileImageUpdatePolicy.newProfileImage to false
            ProfileImageUpdatePolicy.Keep -> null to false
            ProfileImageUpdatePolicy.Remove -> null to true
        }
        val imagePart = profileImage?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("dietImage", it.name, requestFile)
        }
        val selectedDate = userInfo.birthday?.let { dateFormatter.format(it, "yyyy-MM-dd") }

        val request = UpdateUserInfoRequest(
            removeImage = isRemoveProfileImage,
            memberType = MemberType.TRAINEE,
            name = userInfo.name,
            birthDay = selectedDate,
            height = userInfo.height?.toDouble(),
            weight = userInfo.weight,
            cautionNote = userInfo.caution,
            ptGoals = userInfo.ptPurpose,
        )
        val requestBody = json
            .encodeToString(request)
            .toRequestBody("application/json".toMediaTypeOrNull())

        traineeRemoteDataSource.putUserInfo(
            profileImage = imagePart,
            request = requestBody,
        )
    }
}
