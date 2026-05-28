package be.kdg.filmapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.kdg.filmapp.data.repository.DirectorRepository
import be.kdg.filmapp.data.repository.SettingsRepository
import be.kdg.filmapp.model.Director
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DirectorListUiState(
    val directors: List<Director> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class DirectorListViewModel(
    private val directorRepository: DirectorRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DirectorListUiState())
    val uiState: StateFlow<DirectorListUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "DirectorListViewModel"
    }

    init {
        viewModelScope.launch {
            settingsRepository.showOnlyActive.collect { filterActive ->
                loadDirectors(filterActive)
            }
        }
    }

    fun loadDirectors(filterActive: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val all = directorRepository.getAll()
                val list = if (filterActive) all.filter { it.active } else all
                Log.i(TAG, "Loaded ${list.size} directors (filterActive=$filterActive)")
                _uiState.update { it.copy(directors = list, isLoading = false) }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load directors", e)
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
