package co.kr.data.session.di

import co.kr.data.network.provider.SessionProvider
import co.kr.data.session.SessionProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class SessionModule {
    @Binds
    abstract fun bindsSessionProvider(
        provider: SessionProviderImpl,
    ): SessionProvider
}
