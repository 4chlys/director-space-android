package be.kdg.filmapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import be.kdg.filmapp.model.Director
import be.kdg.filmapp.model.Film

@Composable
fun PortraitLayout(
    director: Director,
    films: List<Film>,
    currentIndex: Int,
    directorCount: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onAddFilm: () -> Unit,
    onAddDirector: () -> Unit,
    onEditFilm: (Film) -> Unit,
    onDeleteFilm: (Film) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ArtworkWall(
            director = director,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        )
        ArtworkDescriptor(
            director = director,
            filmCount = films.size,
            modifier = Modifier.fillMaxWidth()
        )
        FilmListSection(
            films = films,
            onEditFilm = onEditFilm,
            onDeleteFilm = onDeleteFilm,
            modifier = Modifier.fillMaxWidth()
        )
        DisplayController(
            currentIndex = currentIndex,
            directorCount = directorCount,
            onPrevious = onPrevious,
            onNext = onNext,
            onEdit = onEdit,
            onDelete = onDelete,
            onAddFilm = onAddFilm,
            onAddDirector = onAddDirector,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LandscapeLayout(
    director: Director,
    films: List<Film>,
    currentIndex: Int,
    directorCount: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onAddFilm: () -> Unit,
    onAddDirector: () -> Unit,
    onEditFilm: (Film) -> Unit,
    onDeleteFilm: (Film) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ArtworkWall(
            director = director,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ArtworkDescriptor(
                director = director,
                filmCount = films.size,
                modifier = Modifier.fillMaxWidth()
            )
            FilmListSection(
                films = films,
                onEditFilm = onEditFilm,
                onDeleteFilm = onDeleteFilm,
                modifier = Modifier.fillMaxWidth()
            )
            DisplayController(
                currentIndex = currentIndex,
                directorCount = directorCount,
                onPrevious = onPrevious,
                onNext = onNext,
                onEdit = onEdit,
                onDelete = onDelete,
                onAddFilm = onAddFilm,
                onAddDirector = onAddDirector,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
