package co.kr.tnt.domain.monitor

import kotlinx.coroutines.flow.Flow

interface SessionMonitor {
    val onExpired: Flow<Unit>
}
