package be.kdg.filmapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import be.kdg.filmapp.model.Director
import be.kdg.filmapp.model.Film
import be.kdg.filmapp.model.getDirectors
import be.kdg.filmapp.model.getFilmsForDirector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DirectorUiState(
    val directors: List<Director> = getDirectors().toList(),
    val currentIndex: Int = 0,
    val showEditDialog: Boolean = false,
    val showAddDialog: Boolean = false,
    val showDeleteConfirm: Boolean = false
) {
    val currentDirector: Director get() = directors[currentIndex]
    val currentFilms: List<Film> get() = getFilmsForDirector(currentDirector.id)
}

class DirectorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DirectorUiState())
    val uiState: StateFlow<DirectorUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "DirectorViewModel"
    }

    init {
        Log.d(TAG, "ViewModel initialized with ${_uiState.value.directors.size} directors")
    }

    private fun navigate(delta: Int) {
        _uiState.update { it.copy(currentIndex = it.currentIndex + delta) }
        Log.d(TAG, "Navigated to: ${_uiState.value.currentDirector.name}")
    }

    fun goToPrevious() = navigate(-1)
    fun goToNext() = navigate(1)

    fun showEditDialog() {
        Log.d(TAG, "Opening edit dialog for: ${_uiState.value.currentDirector.name}")
        _uiState.update { it.copy(showEditDialog = true) }
    }

    fun dismissEditDialog() {
        _uiState.update { it.copy(showEditDialog = false) }
    }

    fun showAddDialog() {
        Log.d(TAG, "Opening add director dialog")
        _uiState.update { it.copy(showAddDialog = true) }
    }

    fun dismissAddDialog() {
        _uiState.update { it.copy(showAddDialog = false) }
    }

    fun showDeleteConfirm() {
        Log.d(TAG, "Showing delete confirm for: ${_uiState.value.currentDirector.name}")
        _uiState.update { it.copy(showDeleteConfirm = true) }
    }

    fun dismissDeleteConfirm() {
        _uiState.update { it.copy(showDeleteConfirm = false) }
    }

    fun updateDirector(updated: Director) {
        Log.i(TAG, "Updating director: ${updated.name}")
        _uiState.update { state ->
            val updatedList = state.directors.mapIndexed { i, d ->
                if (i == state.currentIndex) updated else d
            }
            state.copy(directors = updatedList, showEditDialog = false)
        }
    }

    fun addDirector(director: Director) {
        _uiState.update { state ->
            val newId = (state.directors.maxOfOrNull { it.id } ?: 0) + 1
            val newDirector = director.copy(id = newId)
            val newList = state.directors + newDirector
            Log.i(TAG, "Added director: ${newDirector.name} (id=$newId)")
            state.copy(directors = newList, currentIndex = newList.lastIndex, showAddDialog = false)
        }
    }

    fun deleteCurrentDirector() {
        _uiState.update { state ->
            if (state.directors.size <= 1) {
                Log.w(TAG, "Cannot delete the last director")
                return@update state.copy(showDeleteConfirm = false)
            }
            Log.i(TAG, "Deleting director: ${state.currentDirector.name}")
            val newList = state.directors.filterIndexed { i, _ -> i != state.currentIndex }
            val newIndex = minOf(state.currentIndex, newList.lastIndex)
            state.copy(directors = newList, currentIndex = newIndex, showDeleteConfirm = false)
        }
    }
}
