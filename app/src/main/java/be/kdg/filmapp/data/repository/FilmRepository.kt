package be.kdg.filmapp.data.repository

import be.kdg.filmapp.model.Film

interface FilmRepository {
    suspend fun getFilmsForDirector(directorId: Int): List<Film>
    suspend fun create(film: Film): Film
    suspend fun update(film: Film): Film
    suspend fun delete(id: Int): Boolean
}
