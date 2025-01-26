package co.kr.data.session

import co.kr.data.network.provider.SessionProvider
import co.kr.data.storage.source.SessionLocalDataSource
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SessionProviderImpl @Inject constructor(
    private val sessionLocalDataSource: SessionLocalDataSource,
) : SessionProvider {
    override suspend fun getSessionId(): String =
        sessionLocalDataSource.sessionId.firstOrNull() ?: ""

    override suspend fun setSessionId(sessionId: String) =
        sessionLocalDataSource.updateSessionId(sessionId)
}
