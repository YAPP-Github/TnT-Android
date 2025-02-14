package co.kr.data.storage.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import co.kr.data.storage.di.StorageModule.CONNECT_STORAGE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ConnectLocalDataSource @Inject constructor(
    @Named(CONNECT_STORAGE_NAME) private val connectPreferences: DataStore<Preferences>,
) {
    val explicitDeniedConnectDate: Flow<String?> = connectPreferences.data.map { preferences ->
        preferences[EXPLICIT_DENIED_CONNECT_DATE] ?: ""
    }

    suspend fun updateExplicitDeniedConnectDate(startDate: String) {
        connectPreferences.edit { preferences ->
            preferences[EXPLICIT_DENIED_CONNECT_DATE] = startDate
        }
    }

    suspend fun clearExplicitDeniedConnectDate() {
        connectPreferences.edit { preferences ->
            preferences.remove(EXPLICIT_DENIED_CONNECT_DATE)
        }
    }

    companion object {
        private val EXPLICIT_DENIED_CONNECT_DATE = stringPreferencesKey("EXPLICIT_DENIED_CONNECT_DATE")
    }
}
