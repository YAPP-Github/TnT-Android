package co.kr.data.network.source

import co.kr.data.network.service.ApiService
import co.kr.data.network.util.networkHandler
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraineeRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun postMealRecord(
        dietImage: MultipartBody.Part?,
        request: RequestBody,
    ) = networkHandler {
        apiService.postMealRecord(dietImage, request)
    }

    suspend fun getWeeklyRecordedDates(
        startDate: String,
        endDate: String,
    ) = networkHandler {
        apiService.getWeeklyRecordedDates(startDate, endDate)
    }
}
