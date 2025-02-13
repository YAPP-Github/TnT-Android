package co.kr.data.network.service

import co.kr.data.network.model.CheckSessionResponse
import co.kr.data.network.model.ConnectRequest
import co.kr.data.network.model.ConnectRequestResponse
import co.kr.data.network.model.ConnectedTraineeResponse
import co.kr.data.network.model.InviteCodeResponse
import co.kr.data.network.model.LoginRequest
import co.kr.data.network.model.LoginResponse
import co.kr.data.network.model.SignUpResponse
import co.kr.data.network.model.UserResponse
import co.kr.data.network.model.VerifyCodeResponse
import co.kr.data.network.model.trainer.ActiveTraineesResponse
import co.kr.data.network.model.trainer.DailyPtSessionsResponse
import co.kr.data.network.model.trainer.MonthlyPtSessionCountsResponse
import co.kr.data.network.model.trainer.PtSessionRequest
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

    @POST("/logout")
    suspend fun postLogout()

    @POST("/members/withdraw")
    suspend fun postWithdraw()

    // SignUp
    @Multipart
    @POST("/members/sign-up")
    suspend fun postSignUp(
        @Part profileImage: MultipartBody.Part?,
        @Part("request") request: RequestBody,
    ): SignUpResponse

    // User Info
    @GET("/members")
    suspend fun getMyInfo(): UserResponse

    // Connect
    @GET("/trainers/invitation-code")
    suspend fun getInviteCode(): InviteCodeResponse

    @PUT("/trainers/invitation-code/reissue")
    suspend fun regenerateInviteCode(): InviteCodeResponse

    @GET("/trainers/invitation-code/verify/{code}")
    suspend fun getVerifyInviteCode(
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

    // Trainer
    @GET("/trainers/lessons/calendar")
    suspend fun getMonthlyPtSessionCounts(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): MonthlyPtSessionCountsResponse

    @GET("/trainers/lessons/{date}")
    suspend fun getDailyPtSessions(
        @Path("date") date: String,
    ): DailyPtSessionsResponse

    @GET("/trainers/active-trainees")
    suspend fun getActiveTrainees(): ActiveTraineesResponse

    @POST("/trainers/lessons")
    suspend fun postPtSession(
        @Body request: PtSessionRequest,
    )

    // Trainee
    @Multipart
    @POST("/trainees/diets")
    suspend fun postMealRecord(
        @Part dietImage: MultipartBody.Part?,
        @Part("request") request: RequestBody,
    )
}
