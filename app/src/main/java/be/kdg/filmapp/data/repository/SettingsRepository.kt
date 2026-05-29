package be.kdg.filmapp.data.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val language: Flow<String>
    val showOnlyActive: Flow<Boolean>
    val themeMode: Flow<String>
    suspend fun setLanguage(code: String)
    suspend fun setShowOnlyActive(value: Boolean)
    suspend fun setThemeMode(mode: String)
}
