package co.kr.data.network.service

import co.kr.data.network.model.LoginRequest
import co.kr.data.network.model.LoginResponse
import co.kr.data.network.model.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("/login")
    suspend fun postLogin(
        @Body request: LoginRequest,
    ): LoginResponse

    @Multipart
    @POST("/members/sign-up")
    suspend fun postSignUp(
        @Part profileImage: MultipartBody.Part?,
        @Part("request") request: RequestBody,
    ): SignUpResponse
}
