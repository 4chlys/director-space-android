package be.kdg.filmapp.data.repository

import be.kdg.filmapp.model.Film
import be.kdg.filmapp.model.getDirectors
import be.kdg.filmapp.model.getFilmsForDirector as getFilmsForDirectorFromSource

class InMemoryFilmRepository : FilmRepository {
    private val films = getDirectors()
        .flatMap { getFilmsForDirectorFromSource(it.id) }
        .toMutableList()

    override suspend fun getFilmsForDirector(directorId: Int): List<Film> =
        films.filter { it.directorId == directorId }

    override suspend fun create(film: Film): Film {
        val newId = (films.maxOfOrNull { it.id } ?: 0) + 1
        val saved = film.copy(id = newId)
        films.add(saved)
        return saved
    }

    override suspend fun update(film: Film): Film {
        val index = films.indexOfFirst { it.id == film.id }
        if (index >= 0) films[index] = film
        return film
    }

    override suspend fun delete(id: Int): Boolean = films.removeIf { it.id == id }
}
