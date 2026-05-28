package be.kdg.filmapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import be.kdg.filmapp.R
import be.kdg.filmapp.ui.EditDirectorDialog
import be.kdg.filmapp.ui.EditFilmDialog
import be.kdg.filmapp.ui.LandscapeLayout
import be.kdg.filmapp.ui.PortraitLayout
import be.kdg.filmapp.ui.viewmodel.DirectorDetailViewModel

@Composable
fun DirectorDetailScreen(
    viewModel: DirectorDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        uiState.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(uiState.error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
        }
        uiState.currentDirector == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.error_director_not_found))
        }
        else -> {
            val director = uiState.currentDirector!!
            val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

            if (isLandscape) {
                LandscapeLayout(
                    director = director,
                    films = uiState.films,
                    currentIndex = uiState.currentIndex,
                    directorCount = uiState.directors.size,
                    onPrevious = viewModel::goToPrevious,
                    onNext = viewModel::goToNext,
                    onEdit = viewModel::showEditDialog,
                    onDelete = viewModel::showDeleteConfirm,
                    onAddFilm = viewModel::showAddFilmDialog
                )
            } else {
                PortraitLayout(
                    director = director,
                    films = uiState.films,
                    currentIndex = uiState.currentIndex,
                    directorCount = uiState.directors.size,
                    onPrevious = viewModel::goToPrevious,
                    onNext = viewModel::goToNext,
                    onEdit = viewModel::showEditDialog,
                    onDelete = viewModel::showDeleteConfirm,
                    onAddFilm = viewModel::showAddFilmDialog
                )
            }

            if (uiState.showEditDialog) {
                EditDirectorDialog(
                    director = director,
                    onDismiss = viewModel::dismissEditDialog,
                    onConfirm = viewModel::updateDirector
                )
            }
            if (uiState.showAddDialog) {
                EditDirectorDialog(
                    director = null,
                    onDismiss = viewModel::dismissAddDialog,
                    onConfirm = viewModel::addDirector
                )
            }
            if (uiState.showDeleteConfirm) {
                AlertDialog(
                    onDismissRequest = viewModel::dismissDeleteConfirm,
                    title = { Text(stringResource(R.string.delete_director)) },
                    text = { Text(stringResource(R.string.delete_confirm, director.name)) },
                    confirmButton = {
                        TextButton(
                            onClick = viewModel::deleteCurrentDirector,
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) { Text(stringResource(R.string.delete)) }
                    },
                    dismissButton = {
                        TextButton(onClick = viewModel::dismissDeleteConfirm) { Text(stringResource(R.string.cancel)) }
                    }
                )
            }
            if (uiState.showAddFilmDialog) {
                EditFilmDialog(
                    film = null,
                    onDismiss = viewModel::dismissAddFilmDialog,
                    onConfirm = viewModel::addFilm
                )
            }
            uiState.filmToEdit?.let { film ->
                EditFilmDialog(
                    film = film,
                    onDismiss = viewModel::dismissEditFilmDialog,
                    onConfirm = viewModel::updateFilm
                )
            }
            uiState.showDeleteFilmConfirm?.let { film ->
                AlertDialog(
                    onDismissRequest = viewModel::dismissDeleteFilmConfirm,
                    title = { Text(stringResource(R.string.delete_film)) },
                    text = { Text(stringResource(R.string.delete_confirm, film.title)) },
                    confirmButton = {
                        TextButton(
                            onClick = { viewModel.deleteFilm(film) },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) { Text(stringResource(R.string.delete)) }
                    },
                    dismissButton = {
                        TextButton(onClick = viewModel::dismissDeleteFilmConfirm) { Text(stringResource(R.string.cancel)) }
                    }
                )
            }
        }
    }
}
