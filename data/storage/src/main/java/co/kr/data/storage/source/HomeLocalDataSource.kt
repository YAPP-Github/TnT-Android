package co.kr.data.storage.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class HomeLocalDataSource @Inject constructor(
    @Named("home") private val homePreferences: DataStore<Preferences>,
) {
    val hideDialogStartDate: Flow<String> = homePreferences.data.map { preferences ->
        preferences[HIDE_DIALOG_START_DATE] ?: ""
    }

    suspend fun updateHideDialogStartDate(startDate: String) {
        homePreferences.edit { preferences ->
            preferences[HIDE_DIALOG_START_DATE] = startDate
        }
    }

    companion object {
        private val HIDE_DIALOG_START_DATE = stringPreferencesKey("HIDE_DIALOG_START_DATE")
    }
}
