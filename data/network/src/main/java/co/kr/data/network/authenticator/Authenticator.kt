package co.kr.data.network.authenticator

import co.kr.data.network.monitor.NetworkSessionMonitor
import co.kr.data.network.util.WithoutSessionCheckPath
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

// 구현체 주입 여부 재검토 필요.
internal class Authenticator @Inject constructor(
    private val networkSessionMonitor: NetworkSessionMonitor,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (WithoutSessionCheckPath.paths.contains(response.request.url.encodedPath)) {
            return null
        }

        networkSessionMonitor.sendExpired()
        return null
    }
}
