package co.kr.data.repository

import co.kr.data.network.model.toDomain
import co.kr.data.network.source.ConnectRemoteDataSource
import co.kr.tnt.domain.model.InviteCodeResult
import co.kr.tnt.domain.repository.ConnectRepository
import javax.inject.Inject

class ConnectRepositoryImpl @Inject constructor(
    private val connectRemoteDataSource: ConnectRemoteDataSource,
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
}
