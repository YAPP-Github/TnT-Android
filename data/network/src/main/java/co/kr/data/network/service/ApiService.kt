package co.kr.data.network.service

import co.kr.data.network.model.CheckSessionResponse
import co.kr.data.network.model.LoginRequest
import co.kr.data.network.model.LoginResponse
import co.kr.data.network.model.SignUpResponse
import co.kr.data.network.util.WithoutSessionCheckPath.CHECK_SESSION_PATH
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET(CHECK_SESSION_PATH)
    suspend fun getCheckSession(): CheckSessionResponse

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
