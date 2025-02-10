package co.kr.tnt.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    suspend fun isEnablePushNotification(): Flow<Boolean>

    suspend fun setEnablePushNotification(isEnable: Boolean)
}
