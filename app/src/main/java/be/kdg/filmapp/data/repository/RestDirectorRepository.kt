package be.kdg.filmapp.data.repository

import be.kdg.filmapp.data.api.DirectorApiService
import be.kdg.filmapp.model.Director
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestDirectorRepository(private val api: DirectorApiService) : DirectorRepository {

    override suspend fun getAll(): List<Director> = withContext(Dispatchers.IO) {
        api.getDirectors()
    }

    override suspend fun getById(id: Int): Director? = withContext(Dispatchers.IO) {
        getAll().find { it.id == id }
    }

    override suspend fun create(director: Director): Director = withContext(Dispatchers.IO) {
        val response = api.createDirector(director)
        response.body() ?: throw Exception("Failed to create director: ${response.code()}")
    }

    override suspend fun update(director: Director): Director = withContext(Dispatchers.IO) {
        val response = api.updateDirector(director.id, director)
        response.body() ?: throw Exception("Failed to update director: ${response.code()}")
    }

    override suspend fun delete(id: Int): Boolean = withContext(Dispatchers.IO) {
        api.deleteDirector(id).isSuccessful
    }
}
