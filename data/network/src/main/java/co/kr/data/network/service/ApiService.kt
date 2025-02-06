package co.kr.data.network.service

import co.kr.data.network.model.CheckSessionResponse
import co.kr.data.network.model.ConnectRequest
import co.kr.data.network.model.ConnectRequestResponse
import co.kr.data.network.model.ConnectedTraineeResponse
import co.kr.data.network.model.InviteCodeResponse
import co.kr.data.network.model.LoginRequest
import co.kr.data.network.model.LoginResponse
import co.kr.data.network.model.SignUpResponse
import co.kr.data.network.model.VerifyCodeResponse
import co.kr.data.network.util.WithoutSessionCheckPath.CHECK_SESSION_PATH
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(CHECK_SESSION_PATH)
    suspend fun getCheckSession(): CheckSessionResponse

    // Login
    @POST("/login")
    suspend fun postLogin(
        @Body request: LoginRequest,
    ): LoginResponse

    // SignUp
    @Multipart
    @POST("/members/sign-up")
    suspend fun postSignUp(
        @Part profileImage: MultipartBody.Part?,
        @Part("request") request: RequestBody,
    ): SignUpResponse

    // Connect
    @GET("/trainers/invitation-code")
    suspend fun getInviteCode(): InviteCodeResponse

    @PUT("/trainers/invitation-code/reissue")
    suspend fun regenerateInviteCode(): InviteCodeResponse

    @GET("/trainers/invitation-code/verify/{code}")
    suspend fun verifyInviteCode(
        @Path("code") code: String,
    ): VerifyCodeResponse

    @POST("/trainees/connect-trainer")
    suspend fun postConnectRequest(
        @Body request: ConnectRequest,
    ): ConnectRequestResponse

    @GET("/trainers/first-connected-trainee")
    suspend fun getConnectedTraineeInfo(
        @Query("trainerId") trainerId: String,
        @Query("traineeId") traineeId: String,
    ): ConnectedTraineeResponse
}
