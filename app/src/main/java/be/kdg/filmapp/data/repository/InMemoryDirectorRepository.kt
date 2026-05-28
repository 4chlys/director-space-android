package be.kdg.filmapp.data.repository

import be.kdg.filmapp.model.Director
import be.kdg.filmapp.model.getDirectors

class InMemoryDirectorRepository : DirectorRepository {
    private val directors = getDirectors().toMutableList()

    override suspend fun getAll(): List<Director> = directors.toList()

    override suspend fun getById(id: Int): Director? = directors.find { it.id == id }

    override suspend fun create(director: Director): Director {
        val newId = (directors.maxOfOrNull { it.id } ?: 0) + 1
        val saved = director.copy(id = newId)
        directors.add(saved)
        return saved
    }

    override suspend fun update(director: Director): Director {
        val index = directors.indexOfFirst { it.id == director.id }
        if (index >= 0) directors[index] = director
        return director
    }

    override suspend fun delete(id: Int): Boolean = directors.removeIf { it.id == id }
}
