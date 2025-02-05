package co.kr.data.network.interceptor

import co.kr.data.network.provider.SessionProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class SessionInterceptor @Inject constructor(
    private val sessionProvider: SessionProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestBuilder = originRequest.newBuilder()

        requestBuilder.addHeader(
            "Authorization",
            "SESSION-ID ${runBlocking { sessionProvider.getSessionId() }}",
        )

        return chain.proceed(requestBuilder.build())
    }
}
