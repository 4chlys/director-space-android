package be.kdg.filmapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import be.kdg.filmapp.R
import be.kdg.filmapp.model.Director
import be.kdg.filmapp.ui.EditDirectorDialog
import be.kdg.filmapp.ui.viewmodel.DirectorListViewModel
import coil3.compose.AsyncImage

@Composable
fun DirectorListScreen(
    onDirectorClick: (Int) -> Unit,
    viewModel: DirectorListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            uiState.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = viewModel::loadDirectors) {
                        Text(stringResource(R.string.retry))
                    }
                }
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(uiState.directors, key = { it.id }) { director ->
                    DirectorListItem(director = director, onClick = { onDirectorClick(director.id) })
                }
            }
        }

        FloatingActionButton(
            onClick = viewModel::showAddDialog,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_director))
        }
    }

    if (uiState.showAddDialog) {
        EditDirectorDialog(
            director = null,
            onDismiss = viewModel::dismissAddDialog,
            onConfirm = viewModel::addDirector
        )
    }
}

@Composable
fun DirectorListItem(director: Director, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = director.imageUrl,
                contentDescription = director.name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_image_placeholder),
                error = painterResource(R.drawable.ic_broken_image),
                modifier = Modifier.size(72.dp).clip(RoundedCornerShape(4.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(director.name, style = MaterialTheme.typography.titleSmall)
                Text(director.nationality, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.rating_display, director.rating),
                        style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.width(8.dp))
                    val badgeColor = if (director.active) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.errorContainer
                    val badgeTextColor = if (director.active) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onErrorContainer
                    val badgeText = if (director.active) stringResource(R.string.active)
                        else stringResource(R.string.inactive)
                    Surface(color = badgeColor, shape = RoundedCornerShape(4.dp)) {
                        Text(badgeText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = badgeTextColor,
                            style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
