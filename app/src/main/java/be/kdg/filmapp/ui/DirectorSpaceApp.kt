package be.kdg.filmapp.ui

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import be.kdg.filmapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectorSpaceApp(viewModel: DirectorViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val director = uiState.currentDirector
    val films = uiState.currentFilms
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = viewModel::showAddDialog) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_director)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val layoutModifier = Modifier.padding(innerPadding)
        if (isLandscape) {
            LandscapeLayout(
                director = director,
                films = films,
                currentIndex = uiState.currentIndex,
                directorCount = uiState.directors.size,
                onPrevious = viewModel::goToPrevious,
                onNext = viewModel::goToNext,
                onEdit = viewModel::showEditDialog,
                onDelete = viewModel::showDeleteConfirm,
                modifier = layoutModifier
            )
        } else {
            PortraitLayout(
                director = director,
                films = films,
                currentIndex = uiState.currentIndex,
                directorCount = uiState.directors.size,
                onPrevious = viewModel::goToPrevious,
                onNext = viewModel::goToNext,
                onEdit = viewModel::showEditDialog,
                onDelete = viewModel::showDeleteConfirm,
                modifier = layoutModifier
            )
        }
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
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissDeleteConfirm) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
