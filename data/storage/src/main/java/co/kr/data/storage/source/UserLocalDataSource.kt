package co.kr.data.storage.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(
    @Named("User") private val userPreferences: DataStore<Preferences>,
) {
    private val trainerId: Flow<String> = userPreferences.data.map { preferences ->
        preferences[TRAINER_ID] ?: ""
    }
    private val traineeId: Flow<String> = userPreferences.data.map { preferences ->
        preferences[TRAINEE_ID] ?: ""
    }

    suspend fun updateTrainerId(trainerId: String) {
        userPreferences.edit { preferences ->
            preferences[TRAINER_ID] = trainerId
        }
    }

    suspend fun updateTraineeId(traineeId: String) {
        userPreferences.edit { preferences ->
            preferences[TRAINEE_ID] = traineeId
        }
    }

    suspend fun getTrainerId(): String = trainerId.firstOrNull() ?: ""

    suspend fun getTraineeId(): String = traineeId.firstOrNull() ?: ""

    companion object {
        private val TRAINER_ID = stringPreferencesKey("TRAINER_ID")
        private val TRAINEE_ID = stringPreferencesKey("TRAINEE_ID")
    }
}
