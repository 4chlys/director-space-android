package be.kdg.filmapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.kdg.filmapp.data.repository.DirectorRepository
import be.kdg.filmapp.data.repository.FilmRepository
import be.kdg.filmapp.model.Director
import be.kdg.filmapp.model.Film
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DirectorDetailUiState(
    val directors: List<Director> = emptyList(),
    val currentIndex: Int = 0,
    val films: List<Film> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val showEditDialog: Boolean = false,
    val showAddDialog: Boolean = false,
    val showDeleteConfirm: Boolean = false,
    val showAddFilmDialog: Boolean = false,
    val filmToEdit: Film? = null,
    val showDeleteFilmConfirm: Film? = null
) {
    val currentDirector: Director? get() = directors.getOrNull(currentIndex)
}

class DirectorDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val directorRepository: DirectorRepository,
    private val filmRepository: FilmRepository
) : ViewModel() {

    private val directorId: Int = checkNotNull(savedStateHandle["directorId"])

    private val _uiState = MutableStateFlow(DirectorDetailUiState())
    val uiState: StateFlow<DirectorDetailUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "DirectorDetailViewModel"
    }

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val directors = directorRepository.getAll()
                val index = directors.indexOfFirst { it.id == directorId }.coerceAtLeast(0)
                val films = filmRepository.getFilmsForDirector(directors[index].id)
                Log.i(TAG, "Loaded director at index $index with ${films.size} films")
                _uiState.update { it.copy(directors = directors, currentIndex = index, films = films, isLoading = false) }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load", e)
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun reloadFilms() {
        viewModelScope.launch {
            _uiState.value.currentDirector?.let { director ->
                val films = filmRepository.getFilmsForDirector(director.id)
                _uiState.update { it.copy(films = films) }
            }
        }
    }

    fun goToPrevious() {
        val state = _uiState.value
        if (state.currentIndex > 0) {
            val newIndex = state.currentIndex - 1
            _uiState.update { it.copy(currentIndex = newIndex, films = emptyList()) }
            viewModelScope.launch {
                val films = filmRepository.getFilmsForDirector(state.directors[newIndex].id)
                _uiState.update { it.copy(films = films) }
            }
            Log.d(TAG, "Previous: ${state.directors[newIndex].name}")
        }
    }

    fun goToNext() {
        val state = _uiState.value
        if (state.currentIndex < state.directors.lastIndex) {
            val newIndex = state.currentIndex + 1
            _uiState.update { it.copy(currentIndex = newIndex, films = emptyList()) }
            viewModelScope.launch {
                val films = filmRepository.getFilmsForDirector(state.directors[newIndex].id)
                _uiState.update { it.copy(films = films) }
            }
            Log.d(TAG, "Next: ${state.directors[newIndex].name}")
        }
    }

    fun showEditDialog() = _uiState.update { it.copy(showEditDialog = true) }
    fun dismissEditDialog() = _uiState.update { it.copy(showEditDialog = false) }
    fun showAddDialog() = _uiState.update { it.copy(showAddDialog = true) }
    fun dismissAddDialog() = _uiState.update { it.copy(showAddDialog = false) }
    fun showDeleteConfirm() = _uiState.update { it.copy(showDeleteConfirm = true) }
    fun dismissDeleteConfirm() = _uiState.update { it.copy(showDeleteConfirm = false) }
    fun showAddFilmDialog() = _uiState.update { it.copy(showAddFilmDialog = true) }
    fun dismissAddFilmDialog() = _uiState.update { it.copy(showAddFilmDialog = false) }
    fun showEditFilmDialog(film: Film) = _uiState.update { it.copy(filmToEdit = film) }
    fun dismissEditFilmDialog() = _uiState.update { it.copy(filmToEdit = null) }
    fun showDeleteFilmConfirm(film: Film) = _uiState.update { it.copy(showDeleteFilmConfirm = film) }
    fun dismissDeleteFilmConfirm() = _uiState.update { it.copy(showDeleteFilmConfirm = null) }

    fun updateDirector(updated: Director) {
        viewModelScope.launch {
            try {
                val saved = directorRepository.update(updated)
                Log.i(TAG, "Updated director: ${saved.name}")
                val newList = _uiState.value.directors.map { if (it.id == saved.id) saved else it }
                _uiState.update { it.copy(directors = newList, showEditDialog = false) }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to update director", e)
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun addDirector(director: Director) {
        viewModelScope.launch {
            try {
                val saved = directorRepository.create(director)
                Log.i(TAG, "Added director: ${saved.name}")
                val newList = _uiState.value.directors + saved
                _uiState.update { it.copy(directors = newList, currentIndex = newList.lastIndex, showAddDialog = false) }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to add director", e)
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun deleteCurrentDirector() {
        val state = _uiState.value
        val director = state.currentDirector ?: return
        if (state.directors.size <= 1) {
            Log.w(TAG, "Cannot delete last director")
            _uiState.update { it.copy(showDeleteConfirm = false) }
            return
        }
        viewModelScope.launch {
            try {
                directorRepository.delete(director.id)
                Log.i(TAG, "Deleted director: ${director.name}")
                val newList = state.directors.filter { it.id != director.id }
                val newIndex = minOf(state.currentIndex, newList.lastIndex)
                _uiState.update { it.copy(directors = newList, currentIndex = newIndex, showDeleteConfirm = false) }
                reloadFilms()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete director", e)
                _uiState.update { it.copy(error = e.message, showDeleteConfirm = false) }
            }
        }
    }

    fun addFilm(film: Film) {
        val director = _uiState.value.currentDirector ?: return
        viewModelScope.launch {
            try {
                val saved = filmRepository.create(film.copy(directorId = director.id))
                Log.i(TAG, "Added film: ${saved.title}")
                _uiState.update { it.copy(films = it.films + saved, showAddFilmDialog = false) }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to add film", e)
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun updateFilm(film: Film) {
        viewModelScope.launch {
            try {
                val saved = filmRepository.update(film)
                Log.i(TAG, "Updated film: ${saved.title}")
                val newFilms = _uiState.value.films.map { if (it.id == saved.id) saved else it }
                _uiState.update { it.copy(films = newFilms, filmToEdit = null) }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to update film", e)
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun deleteFilm(film: Film) {
        viewModelScope.launch {
            try {
                filmRepository.delete(film.id)
                Log.i(TAG, "Deleted film: ${film.title}")
                _uiState.update { it.copy(films = it.films.filter { f -> f.id != film.id }, showDeleteFilmConfirm = null) }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete film", e)
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}
