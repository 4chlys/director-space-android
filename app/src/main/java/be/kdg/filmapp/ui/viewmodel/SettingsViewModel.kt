package be.kdg.filmapp.ui.viewmodel

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.kdg.filmapp.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    companion object {
        private const val TAG = "SettingsViewModel"
    }

    val language: StateFlow<String> = settingsRepository.language
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "en")

    val showOnlyActive: StateFlow<Boolean> = settingsRepository.showOnlyActive
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun setLanguage(code: String) {
        viewModelScope.launch {
            Log.i(TAG, "Setting language: $code")
            settingsRepository.setLanguage(code)
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(code))
        }
    }

    fun setShowOnlyActive(value: Boolean) {
        viewModelScope.launch {
            Log.i(TAG, "Setting showOnlyActive: $value")
            settingsRepository.setShowOnlyActive(value)
        }
    }
}
