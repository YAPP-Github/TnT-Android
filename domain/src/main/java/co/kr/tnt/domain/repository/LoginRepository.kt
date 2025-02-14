package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.AuthType
import co.kr.tnt.domain.model.LoginResult
import co.kr.tnt.domain.model.UserType

interface LoginRepository {
    suspend fun getUserType(): UserType

    suspend fun login(
        authType: AuthType,
        messagingToken: String,
        accessToken: String,
    ): LoginResult

    suspend fun logout()

    suspend fun withdraw()
}
