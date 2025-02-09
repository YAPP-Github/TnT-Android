package co.kr.data.repository

import co.kr.data.network.model.LoginRequest
import co.kr.data.network.model.enum.toDomain
import co.kr.data.network.model.toDomain
import co.kr.data.network.source.LoginRemoteDataSource
import co.kr.data.storage.source.SessionLocalDataSource
import co.kr.tnt.domain.model.AuthType
import co.kr.tnt.domain.model.LoginResult
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.domain.repository.LoginRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LoginRepositoryImpl @Inject constructor(
    private val loginRemoteDataSource: LoginRemoteDataSource,
    private val sessionLocalDataSource: SessionLocalDataSource,
) : LoginRepository {
    override suspend fun getUserType(): UserType =
        loginRemoteDataSource.getCheckSession().memberType.toDomain()

    override suspend fun login(
        authType: AuthType,
        accessToken: String,
    ): LoginResult {
        val response = loginRemoteDataSource.postLogin(
            loginRequest = LoginRequest(
                socialType = authType,
                socialAccessToken = accessToken,
            ),
        )

        response.sessionId?.let { sessionId ->
            sessionLocalDataSource.updateSessionId(sessionId)
        }

        return response.toDomain()
    }

    override suspend fun logout() {
        loginRemoteDataSource.postLogout()
        sessionLocalDataSource.removeSessionId()
    }
}
