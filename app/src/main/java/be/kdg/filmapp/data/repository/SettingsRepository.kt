package be.kdg.filmapp.data.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val language: Flow<String>
    val showOnlyActive: Flow<Boolean>
    suspend fun setLanguage(code: String)
    suspend fun setShowOnlyActive(value: Boolean)
}
