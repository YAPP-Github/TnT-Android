package co.kr.data.repository.di

import co.kr.data.repository.LoginRepositoryImpl
import co.kr.tnt.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {
    @Binds
    abstract fun bindsLoginRepository(
        repository: LoginRepositoryImpl,
    ): LoginRepository
}
