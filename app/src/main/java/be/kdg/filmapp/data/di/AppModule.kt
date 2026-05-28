package be.kdg.filmapp.data.di

import be.kdg.filmapp.data.repository.DirectorRepository
import be.kdg.filmapp.data.repository.FilmRepository
import be.kdg.filmapp.data.repository.InMemoryDirectorRepository
import be.kdg.filmapp.data.repository.InMemoryFilmRepository

object AppModule {
    val directorRepository: DirectorRepository = InMemoryDirectorRepository()
    val filmRepository: FilmRepository = InMemoryFilmRepository()
}
