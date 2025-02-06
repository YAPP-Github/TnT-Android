package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.ConnectRequestResult
import co.kr.tnt.domain.model.InviteCodeResult

interface ConnectRepository {
    suspend fun getInviteCode(): InviteCodeResult

    suspend fun regenerateInviteCode(): InviteCodeResult

    suspend fun verifyInviteCode(code: String): Boolean

    suspend fun connectRequest(
        invitationCode: String,
        startDate: String,
        totalPtCount: Int,
        finishedPtCount: Int,
    ): ConnectRequestResult
}
