package co.kr.data.network.di

import co.kr.data.network.monitor.NetworkSessionMonitor
import co.kr.tnt.domain.monitor.SessionMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MonitorModule {
    @Binds
    abstract fun bindsSessionMonitor(
        monitor: NetworkSessionMonitor,
    ): SessionMonitor
}
