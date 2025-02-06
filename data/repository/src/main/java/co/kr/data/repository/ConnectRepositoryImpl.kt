package co.kr.data.repository

import co.kr.data.network.model.ConnectRequest
import co.kr.data.network.model.toDomain
import co.kr.data.network.source.ConnectRemoteDataSource
import co.kr.data.storage.source.UserLocalDataSource
import co.kr.tnt.domain.model.ConnectRequestResult
import co.kr.tnt.domain.model.ConnectedResult
import co.kr.tnt.domain.model.InviteCodeResult
import co.kr.tnt.domain.repository.ConnectRepository
import javax.inject.Inject

class ConnectRepositoryImpl @Inject constructor(
    private val connectRemoteDataSource: ConnectRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) : ConnectRepository {
    override suspend fun getInviteCode(): InviteCodeResult {
        val response = connectRemoteDataSource.getInviteCode()

        return response.toDomain()
    }

    override suspend fun regenerateInviteCode(): InviteCodeResult {
        val response = connectRemoteDataSource.regenerateInviteCode()

        return response.toDomain()
    }

    override suspend fun verifyInviteCode(code: String): Boolean {
        val response = connectRemoteDataSource.verifyInviteCode(code = code)

        return response.isVerified
    }

    override suspend fun connectRequest(
        invitationCode: String,
        startDate: String,
        totalPtCount: Int,
        finishedPtCount: Int,
    ): ConnectRequestResult {
        val response = connectRemoteDataSource.connectRequest(
            connectRequest = ConnectRequest(
                invitationCode = invitationCode,
                startDate = startDate,
                totalPtCount = totalPtCount,
                finishedPtCount = finishedPtCount,
            ),
        )
        return response.toDomain()
    }

    override suspend fun getConnectedTraineeInfo(): ConnectedResult {
        // TODO trainerId, traineeId 저장하기 (FCM, 알림 화면)
        val trainerId = userLocalDataSource.getTrainerId()
        val traineeId = userLocalDataSource.getTraineeId()

        val response = connectRemoteDataSource.getConnectedTraineeInfo(
            trainerId = trainerId,
            traineeId = traineeId,
        )
        return response.toDomain()
    }
}
