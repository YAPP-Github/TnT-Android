package co.kr.data.repository

import co.kr.data.network.model.ConnectRequest
import co.kr.data.network.model.toDomain
import co.kr.data.network.source.ConnectRemoteDataSource
import co.kr.data.storage.source.ConnectLocalDataSource
import co.kr.tnt.domain.model.ConnectRequestResult
import co.kr.tnt.domain.model.ConnectedResult
import co.kr.tnt.domain.model.InviteCodeResult
import co.kr.tnt.domain.repository.ConnectRepository
import co.kr.tnt.domain.utils.DateFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ConnectRepositoryImpl @Inject constructor(
    private val connectRemoteDataSource: ConnectRemoteDataSource,
    private val connectLocalDataSource: ConnectLocalDataSource,
    private val dateFormatter: DateFormatter,
) : ConnectRepository {
    override suspend fun getInviteCode(): InviteCodeResult {
        val response = connectRemoteDataSource.getInviteCode()

        return response.toDomain()
    }

    override suspend fun regenerateInviteCode(): InviteCodeResult {
        val response = connectRemoteDataSource.regenerateInviteCode()

        return response.toDomain()
    }

    override suspend fun getVerifyInviteCode(code: String): Boolean {
        val response = connectRemoteDataSource.getVerifyInviteCode(code = code)

        return response.isVerified
    }

    override suspend fun connectRequest(
        invitationCode: String,
        startDate: String,
        totalSession: Int,
        completedSession: Int,
    ): ConnectRequestResult {
        val response = connectRemoteDataSource.connectRequest(
            connectRequest = ConnectRequest(
                invitationCode = invitationCode,
                startDate = startDate,
                totalPtCount = totalSession,
                finishedPtCount = completedSession,
            ),
        )
        return response.toDomain()
    }

    override suspend fun getConnectedTraineeInfo(
        trainerId: String,
        traineeId: String,
    ): ConnectedResult {
        val response = connectRemoteDataSource.getConnectedTraineeInfo(
            trainerId = trainerId,
            traineeId = traineeId,
        )
        return response.toDomain()
    }

    override suspend fun isUserConnected(): Flow<Boolean> =
        connectLocalDataSource.isConnected

    override suspend fun setConnectedState(isConnected: Boolean) =
        connectLocalDataSource.updateConnectionStatus(isConnected)

    override suspend fun getHomeDialogHiddenDate(): Flow<LocalDateTime?> =
        connectLocalDataSource.homeDialogHiddenDate.map { dateString ->
            dateString?.let {
                runCatching { dateFormatter.parseDateTime(it) }
                    .getOrDefault(LocalDateTime.MIN)
            }
        }

    override suspend fun updateHomeDialogHiddenDate(date: LocalDateTime) {
        val formattedDate = dateFormatter.format(date)
        connectLocalDataSource.updateHomeDialogHiddenDate(formattedDate)
    }
}
