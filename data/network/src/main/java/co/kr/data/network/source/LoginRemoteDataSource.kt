package co.kr.data.network.source

import co.kr.data.network.model.LoginRequest
import co.kr.data.network.model.LoginResponse
import co.kr.data.network.service.ApiService
import co.kr.data.network.util.networkHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getCheckSession() = networkHandler {
        apiService.getCheckSession()
    }

    suspend fun postLogin(loginRequest: LoginRequest): LoginResponse = networkHandler {
        apiService.postLogin(loginRequest)
    }
}
