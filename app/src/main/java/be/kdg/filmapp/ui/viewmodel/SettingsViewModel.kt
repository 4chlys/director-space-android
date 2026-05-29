package be.kdg.filmapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.kdg.filmapp.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    companion object {
        private const val TAG = "SettingsViewModel"
    }

    val language: StateFlow<String> = settingsRepository.language
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "en")

    val showOnlyActive: StateFlow<Boolean> = settingsRepository.showOnlyActive
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val themeMode: StateFlow<String> = settingsRepository.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "system")

    fun setLanguage(code: String) {
        viewModelScope.launch {
            Log.i(TAG, "Setting language: $code")
            settingsRepository.setLanguage(code)
        }
    }

    fun setShowOnlyActive(value: Boolean) {
        viewModelScope.launch {
            Log.i(TAG, "Setting showOnlyActive: $value")
            settingsRepository.setShowOnlyActive(value)
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            Log.i(TAG, "Setting themeMode: $mode")
            settingsRepository.setThemeMode(mode)
        }
    }
}
