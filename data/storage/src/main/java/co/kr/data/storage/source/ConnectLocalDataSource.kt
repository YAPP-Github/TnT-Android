package co.kr.data.storage.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
    val isConnected: Flow<Boolean> = connectPreferences.data.map { preferences ->
        preferences[IS_CONNECTED] ?: false
    }

    suspend fun updateConnectionStatus(isConnected: Boolean) {
        connectPreferences.edit { preferences ->
            preferences[IS_CONNECTED] = isConnected
        }
    }

    val homeDialogHiddenDate: Flow<String> = connectPreferences.data.map { preferences ->
        preferences[HOME_DIALOG_HIDDEN_DATE] ?: ""
    }

    suspend fun updateHomeDialogHiddenDate(startDate: String) {
        connectPreferences.edit { preferences ->
            preferences[HOME_DIALOG_HIDDEN_DATE] = startDate
        }
    }

    suspend fun clearHomeDialogPreferences() {
        connectPreferences.edit { preferences ->
            preferences.remove(IS_CONNECTED)
            preferences.remove(HOME_DIALOG_HIDDEN_DATE)
        }
    }

    companion object {
        private val IS_CONNECTED = booleanPreferencesKey("IS_CONNECTED")
        private val HOME_DIALOG_HIDDEN_DATE = stringPreferencesKey("HOME_DIALOG_HIDDEN_DATE")
    }
}
