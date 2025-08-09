package co.kr.data.network.source

import co.kr.data.network.model.UpdateUserInfoRequest
import co.kr.data.network.model.trainer.ActiveTraineesResponse
import co.kr.data.network.model.trainer.DailyPtSessionsResponse
import co.kr.data.network.model.trainer.MonthlyPtSessionCountsResponse
import co.kr.data.network.model.trainer.PtSessionRequest
import co.kr.data.network.service.ApiService
import co.kr.data.network.util.networkHandler
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainerRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val json: Json,
) {
    suspend fun getMonthlyPtSessionCounts(
        year: Int,
        month: Int,
    ): MonthlyPtSessionCountsResponse = networkHandler {
        apiService.getMonthlyPtSessionCounts(
            year = year,
            month = month,
        )
    }

    suspend fun getDailyPtSessions(
        date: String,
    ): DailyPtSessionsResponse = networkHandler {
        apiService.getDailyPtSessions(date)
    }

    suspend fun getActiveTrainees(): ActiveTraineesResponse = networkHandler {
        apiService.getActiveTrainees()
    }

    suspend fun postPtSession(request: PtSessionRequest) = networkHandler {
        apiService.postPtSession(request)
    }

    suspend fun putCompletePtSession(ptSessionId: String) = networkHandler {
        apiService.putCompletePtSession(ptSessionId)
    }

    suspend fun putUserInfo(
        profileImage: File?,
        request: UpdateUserInfoRequest,
    ) {
        // TODO 공통 로직 추출
        val profileImagePart = profileImage?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profileImage", it.name, requestFile)
        }

        val requestBody = json
            .encodeToString(request)
            .toRequestBody("application/json".toMediaTypeOrNull())

        networkHandler {
            apiService.putMyInfo(
                profileImage = profileImagePart,
                request = requestBody,
            )
        }
    }
}
