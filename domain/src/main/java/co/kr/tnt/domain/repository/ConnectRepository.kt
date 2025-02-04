package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.InviteCodeResult

interface ConnectRepository {
    suspend fun getInviteCode(): InviteCodeResult

    suspend fun regenerateInviteCode(): InviteCodeResult
}
