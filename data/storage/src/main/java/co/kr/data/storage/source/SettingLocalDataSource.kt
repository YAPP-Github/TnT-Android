package co.kr.data.storage.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import co.kr.data.storage.di.StorageModule.SETTING_STORAGE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SettingLocalDataSource @Inject constructor(
    @Named(SETTING_STORAGE_NAME) private val settingPreferences: DataStore<Preferences>,
) {
    val isEnablePushNotification: Flow<Boolean> = settingPreferences.data.map { preferences ->
        preferences[IS_ENABLE_PUSH_NOTIFICATION_ID] ?: false
    }

    suspend fun updateIsEnablePushNotification(isEnable: Boolean) {
        settingPreferences.edit { preferences ->
            preferences[IS_ENABLE_PUSH_NOTIFICATION_ID] = isEnable
        }
    }

    suspend fun clearSetting() {
        settingPreferences.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val IS_ENABLE_PUSH_NOTIFICATION_ID = booleanPreferencesKey("IS_ENABLE_PUSH_NOTIFICATION_ID")
    }
}
