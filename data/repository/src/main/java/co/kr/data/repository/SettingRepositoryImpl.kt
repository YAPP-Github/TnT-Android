package co.kr.data.repository

import co.kr.data.storage.source.SettingLocalDataSource
import co.kr.tnt.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SettingRepositoryImpl @Inject constructor(
    private val settingLocalDataSource: SettingLocalDataSource,
) : SettingRepository {
    override suspend fun isEnablePushNotification(): Flow<Boolean> =
        settingLocalDataSource.isEnablePushNotification

    override suspend fun setEnablePushNotification(isEnable: Boolean) =
        settingLocalDataSource.updateIsEnablePushNotification(isEnable)
}
