package co.kr.data.repository.di

import co.kr.data.repository.ConnectRepositoryImpl
import co.kr.data.repository.LoginRepositoryImpl
import co.kr.data.repository.SignUpRepositoryImpl
import co.kr.data.repository.TraineeRepositoryImpl
import co.kr.data.repository.TrainerRepositoryImpl
import co.kr.tnt.domain.repository.ConnectRepository
import co.kr.tnt.domain.repository.LoginRepository
import co.kr.tnt.domain.repository.SignUpRepository
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.domain.repository.TrainerRepository
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

    @Binds
    abstract fun bindsSignUpRepository(
        repository: SignUpRepositoryImpl,
    ): SignUpRepository

    @Binds
    abstract fun bindConnectRepository(
        repository: ConnectRepositoryImpl,
    ): ConnectRepository

    @Binds
    abstract fun bindTrainerRepository(
        repository: TrainerRepositoryImpl,
    ): TrainerRepository

    @Binds
    abstract fun bindTraineeRepository(
        repository: TraineeRepositoryImpl,
    ): TraineeRepository
}
