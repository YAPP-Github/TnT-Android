package co.kr.data.session

import co.kr.data.network.provider.SessionProvider
import co.kr.data.storage.source.SessionDataSource
import kotlinx.coroutines.flow.lastOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SessionProviderImpl @Inject constructor(
    private val sessionDataSource: SessionDataSource,
) : SessionProvider {
    override suspend fun getSessionId(): String =
        sessionDataSource.sessionId.lastOrNull() ?: ""

    override suspend fun setSessionId(sessionId: String) =
        sessionDataSource.updateSessionId(sessionId)
}
