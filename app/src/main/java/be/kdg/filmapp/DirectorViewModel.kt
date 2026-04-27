package be.kdg.filmapp

import androidx.lifecycle.ViewModel
import be.kdg.filmapp.model.Director
import be.kdg.filmapp.model.getDirectors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DirectorUiState(
    val directors: List<Director> = getDirectors().toList(),
    val currentIndex: Int = 0,
    val showEditDialog: Boolean = false
) {
    val currentDirector: Director get() = directors[currentIndex]
}

class DirectorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DirectorUiState())
    val uiState: StateFlow<DirectorUiState> = _uiState.asStateFlow()

    fun goToPrevious() {
        _uiState.update { it.copy(currentIndex = it.currentIndex - 1) }
    }

    fun goToNext() {
        _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
    }

    fun showEditDialog() {
        _uiState.update { it.copy(showEditDialog = true) }
    }

    fun dismissEditDialog() {
        _uiState.update { it.copy(showEditDialog = false) }
    }

    fun updateRating(newRating: Double) {
        _uiState.update { state ->
            val updated = state.directors.toMutableList()
            updated[state.currentIndex] = state.currentDirector.copy(rating = newRating)
            state.copy(directors = updated, showEditDialog = false)
        }
    }
}
