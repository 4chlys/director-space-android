package be.kdg.filmapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreSettingsRepository(private val context: Context) : SettingsRepository {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
        val SHOW_ONLY_ACTIVE = booleanPreferencesKey("show_only_active")
    }

    override val language: Flow<String> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.LANGUAGE] ?: "en" }

    override val showOnlyActive: Flow<Boolean> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.SHOW_ONLY_ACTIVE] ?: false }

    override suspend fun setLanguage(code: String) {
        context.dataStore.edit { it[Keys.LANGUAGE] = code }
    }

    override suspend fun setShowOnlyActive(value: Boolean) {
        context.dataStore.edit { it[Keys.SHOW_ONLY_ACTIVE] = value }
    }
}
