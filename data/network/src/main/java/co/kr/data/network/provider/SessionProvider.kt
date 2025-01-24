package co.kr.data.network.provider

interface SessionProvider {
    suspend fun getSessionId(): String
    suspend fun setSessionId(sessionId: String)
}
