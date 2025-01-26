package co.kr.data.network.service

import co.kr.data.network.model.LoginRequest
import co.kr.data.network.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/login")
    suspend fun postLogin(
        @Body request: LoginRequest,
    ): LoginResponse
}
