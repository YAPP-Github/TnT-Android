package co.kr.tnt.domain.repository

import co.kr.tnt.domain.model.AuthType
import co.kr.tnt.domain.model.LoginResult

interface LoginRepository {
    suspend fun isNeedLogin(): Boolean

    suspend fun login(
        authType: AuthType,
        accessToken: String,
    ): LoginResult
}
