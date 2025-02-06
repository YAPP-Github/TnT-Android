package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.ConnectRequestResult
import co.kr.tnt.domain.model.ConnectedResult
import co.kr.tnt.domain.model.InviteCodeResult

interface ConnectRepository {
    suspend fun getInviteCode(): InviteCodeResult

    suspend fun regenerateInviteCode(): InviteCodeResult

    suspend fun getVerifyInviteCode(code: String): Boolean

    suspend fun connectRequest(
        invitationCode: String,
        startDate: String,
        totalSession: Int,
        completedSession: Int,
    ): ConnectRequestResult

    suspend fun getConnectedTraineeInfo(
        trainerId: String,
        traineeId: String,
    ): ConnectedResult
}
