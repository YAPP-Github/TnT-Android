package co.kr.data.network.source

import co.kr.data.network.model.SignUpResponse
import co.kr.data.network.service.ApiService
import co.kr.data.network.util.networkHandler
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun postSignUp(
        profileImage: MultipartBody.Part?,
        request: RequestBody,
    ): SignUpResponse = networkHandler {
        apiService.postSignUp(profileImage, request)
    }
}
