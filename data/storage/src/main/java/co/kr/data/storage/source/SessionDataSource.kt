package co.kr.data.storage.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionDataSource @Inject constructor(
    private val sessionPreferences: DataStore<Preferences>,
) {
    val sessionId: Flow<String> = sessionPreferences.data.map { preferences ->
        preferences[SESSION_ID] ?: ""
    }

    suspend fun updateSessionId(sessionId: String) {
        sessionPreferences.edit { preferences ->
            preferences[SESSION_ID] = sessionId
        }
    }

    companion object {
        private val SESSION_ID = stringPreferencesKey("SESSION_ID")
    }
}
