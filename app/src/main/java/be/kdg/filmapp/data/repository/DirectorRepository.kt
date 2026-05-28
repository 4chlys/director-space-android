package be.kdg.filmapp.data.repository

import be.kdg.filmapp.model.Director

interface DirectorRepository {
    suspend fun getAll(): List<Director>
    suspend fun getById(id: Int): Director?
    suspend fun create(director: Director): Director
    suspend fun update(director: Director): Director
    suspend fun delete(id: Int): Boolean
}
