package co.kr.data.network.di

import co.kr.data.network.repository.TnTRepositoryImpl
import co.kr.tnt.domain.repository.TnTRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class DataModule {
    @Binds
    abstract fun bindsTnTRepository(
        repository: TnTRepositoryImpl,
    ): TnTRepository
}
