package be.kdg.filmapp.data.repository

import be.kdg.filmapp.data.api.FilmApiService
import be.kdg.filmapp.model.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestFilmRepository(private val api: FilmApiService) : FilmRepository {

    override suspend fun getFilmsForDirector(directorId: Int): List<Film> = withContext(Dispatchers.IO) {
        api.getFilmsForDirector(directorId)
    }

    override suspend fun create(film: Film): Film = withContext(Dispatchers.IO) {
        val response = api.createFilm(film)
        response.body() ?: throw Exception("Failed to create film: ${response.code()}")
    }

    override suspend fun update(film: Film): Film = withContext(Dispatchers.IO) {
        val response = api.updateFilm(film.id, film)
        response.body() ?: throw Exception("Failed to update film: ${response.code()}")
    }

    override suspend fun delete(id: Int): Boolean = withContext(Dispatchers.IO) {
        api.deleteFilm(id).isSuccessful
    }
}
