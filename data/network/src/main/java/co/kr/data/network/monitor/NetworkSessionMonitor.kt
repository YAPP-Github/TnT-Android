package co.kr.data.network.monitor

import co.kr.tnt.domain.monitor.SessionMonitor
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkSessionMonitor @Inject constructor() : SessionMonitor {
    private val _onExpired = Channel<Unit>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val onExpired: Flow<Unit>
        get() = _onExpired.receiveAsFlow()

    fun sendExpired() = _onExpired.trySend(Unit)
}
