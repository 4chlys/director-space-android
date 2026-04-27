package be.kdg.filmapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.kdg.filmapp.model.Director
import be.kdg.filmapp.model.getFilmCountForDirector
import be.kdg.filmapp.ui.theme.FilmAppTheme
import coil3.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmAppTheme {
                DirectorSpaceApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectorSpaceApp(viewModel: DirectorViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val director = uiState.currentDirector
    val filmCount = remember(director.id) { getFilmCountForDirector(director.id) }
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        if (isLandscape) {
            LandscapeLayout(
                director = director,
                filmCount = filmCount,
                currentIndex = uiState.currentIndex,
                directorCount = uiState.directors.size,
                onPrevious = viewModel::goToPrevious,
                onNext = viewModel::goToNext,
                onEdit = viewModel::showEditDialog,
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            PortraitLayout(
                director = director,
                filmCount = filmCount,
                currentIndex = uiState.currentIndex,
                directorCount = uiState.directors.size,
                onPrevious = viewModel::goToPrevious,
                onNext = viewModel::goToNext,
                onEdit = viewModel::showEditDialog,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    if (uiState.showEditDialog) {
        EditRatingDialog(
            director = director,
            onDismiss = viewModel::dismissEditDialog,
            onConfirm = viewModel::updateRating
        )
    }
}

@Composable
fun ArtworkWall(director: Director, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        tonalElevation = 4.dp,
        shadowElevation = 16.dp,
        shape = RoundedCornerShape(4.dp)
    ) {
        AsyncImage(
            model = director.imageUrl,
            contentDescription = stringResource(R.string.director_image_description, director.name),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ArtworkDescriptor(director: Director, filmCount: Int, modifier: Modifier = Modifier) {
    val careerString = if (director.active) {
        stringResource(R.string.career_active, director.careerStart)
    } else {
        stringResource(R.string.career_ended, director.careerStart, director.careerEnd!!)
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = director.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = director.nationality,
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.born_info, director.birthDate, director.country),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = careerString,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.rating_display, director.rating),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(12.dp))
                Surface(
                    color = if (director.active) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (director.active) stringResource(R.string.active)
                        else stringResource(R.string.inactive),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = if (director.active) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pluralStringResource(R.plurals.film_count, filmCount, filmCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun DisplayController(
    currentIndex: Int,
    directorCount: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onPrevious,
                enabled = currentIndex > 0,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.previous))
            }
            OutlinedButton(
                onClick = onEdit,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.edit_rating))
            }
            Button(
                onClick = onNext,
                enabled = currentIndex < directorCount - 1,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.next))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.counter, currentIndex + 1, directorCount),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PortraitLayout(
    director: Director,
    filmCount: Int,
    currentIndex: Int,
    directorCount: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onEdit: () -> Unit,
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
            filmCount = filmCount,
            modifier = Modifier.fillMaxWidth()
        )
        DisplayController(
            currentIndex = currentIndex,
            directorCount = directorCount,
            onPrevious = onPrevious,
            onNext = onNext,
            onEdit = onEdit,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LandscapeLayout(
    director: Director,
    filmCount: Int,
    currentIndex: Int,
    directorCount: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onEdit: () -> Unit,
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
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ArtworkDescriptor(
                director = director,
                filmCount = filmCount,
                modifier = Modifier.fillMaxWidth()
            )
            DisplayController(
                currentIndex = currentIndex,
                directorCount = directorCount,
                onPrevious = onPrevious,
                onNext = onNext,
                onEdit = onEdit,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun EditRatingDialog(
    director: Director,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var input by remember { mutableStateOf(director.rating.toString()) }
    val isValid = input.toDoubleOrNull()?.let { it in 0.0..10.0 } == true

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.edit_rating)) },
        text = {
            Column {
                Text(stringResource(R.string.edit_rating_message, director.name))
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text(stringResource(R.string.rating_range_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = !isValid,
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(input.toDouble()) },
                enabled = isValid
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
