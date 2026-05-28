package be.kdg.filmapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.kdg.filmapp.data.repository.DirectorRepository
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
    private val directorRepository: DirectorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DirectorListUiState())
    val uiState: StateFlow<DirectorListUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "DirectorListViewModel"
    }

    init { loadDirectors() }

    fun loadDirectors() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val list = directorRepository.getAll()
                Log.i(TAG, "Loaded ${list.size} directors")
                _uiState.update { it.copy(directors = list, isLoading = false) }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load directors", e)
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
