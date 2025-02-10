package co.kr.data.network.source

import co.kr.data.network.service.ApiService
import co.kr.data.network.util.networkHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getMyInfo() = networkHandler {
        apiService.getMyInfo()
    }
}
