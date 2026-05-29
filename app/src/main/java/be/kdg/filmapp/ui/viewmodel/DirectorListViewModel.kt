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
import org.koin.android.annotation.KoinViewModel

data class DirectorListUiState(
    val directors: List<Director> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val showAddDialog: Boolean = false
)

@KoinViewModel
class DirectorListViewModel(
    private val directorRepository: DirectorRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DirectorListUiState())
    val uiState: StateFlow<DirectorListUiState> = _uiState.asStateFlow()

    private var currentFilterActive = false

    companion object {
        private const val TAG = "DirectorListViewModel"
    }

    init {
        viewModelScope.launch {
            settingsRepository.showOnlyActive.collect { filterActive ->
                currentFilterActive = filterActive
                loadDirectors(filterActive)
            }
        }
    }

    fun loadDirectors(filterActive: Boolean = currentFilterActive) {
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

    fun showAddDialog() = _uiState.update { it.copy(showAddDialog = true) }
    fun dismissAddDialog() = _uiState.update { it.copy(showAddDialog = false) }

    fun addDirector(director: Director) {
        viewModelScope.launch {
            try {
                val saved = directorRepository.create(director)
                Log.i(TAG, "Added director: ${saved.name}")
                _uiState.update { it.copy(showAddDialog = false) }
                loadDirectors()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to add director", e)
                _uiState.update { it.copy(error = e.message, showAddDialog = false) }
            }
        }
    }
}
