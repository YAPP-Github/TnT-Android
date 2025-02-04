package co.kr.data.network.source

import co.kr.data.network.model.InviteCodeResponse
import co.kr.data.network.service.ApiService
import co.kr.data.network.util.networkHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getInviteCode(): InviteCodeResponse = networkHandler {
        apiService.getInviteCode()
    }

    suspend fun regenerateInviteCode(): InviteCodeResponse = networkHandler {
        apiService.regenerateInviteCode()
    }
}
