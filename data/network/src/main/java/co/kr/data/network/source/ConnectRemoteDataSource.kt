package co.kr.data.network.source

import co.kr.data.network.model.ConnectRequest
import co.kr.data.network.model.ConnectRequestResponse
import co.kr.data.network.model.ConnectedTraineeResponse
import co.kr.data.network.model.InviteCodeResponse
import co.kr.data.network.model.VerifyCodeResponse
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

    suspend fun getVerifyInviteCode(code: String): VerifyCodeResponse = networkHandler {
        apiService.getVerifyInviteCode(code = code)
    }

    suspend fun connectRequest(connectRequest: ConnectRequest): ConnectRequestResponse =
        networkHandler {
            apiService.postConnectRequest(request = connectRequest)
        }

    suspend fun getConnectedTraineeInfo(
        trainerId: String,
        traineeId: String,
    ): ConnectedTraineeResponse = networkHandler {
        apiService.getConnectedTraineeInfo(
            trainerId = trainerId,
            traineeId = traineeId,
        )
    }
}
