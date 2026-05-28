package be.kdg.filmapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import be.kdg.filmapp.R
import be.kdg.filmapp.model.Film
import be.kdg.filmapp.model.Genre

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFilmDialog(
    film: Film?,
    onDismiss: () -> Unit,
    onConfirm: (Film) -> Unit
) {
    val isCreate = film == null
    var title by remember { mutableStateOf(film?.title ?: "") }
    var releaseYear by remember { mutableStateOf(film?.releaseYear?.toString() ?: "") }
    var rating by remember { mutableStateOf(film?.rating?.toString() ?: "5.0") }
    var durationMinutes by remember { mutableStateOf(film?.durationMinutes?.toString() ?: "") }
    var selectedGenre by remember { mutableStateOf(film?.genre ?: Genre.Drama) }
    var genreExpanded by remember { mutableStateOf(false) }
    var inColor by remember { mutableStateOf(film?.inColor ?: true) }
    var awards by remember { mutableStateOf(film?.awards?.joinToString(", ") ?: "") }
    var imageUrl by remember { mutableStateOf(film?.imageUrl ?: "") }

    val titleValid = title.isNotBlank()
    val yearValid = releaseYear.toIntOrNull()?.let { it > 1888 } == true
    val ratingValid = rating.toDoubleOrNull()?.let { it in 0.0..10.0 } == true
    val durationValid = durationMinutes.toIntOrNull()?.let { it > 0 } == true
    val formValid = titleValid && yearValid && ratingValid && durationValid

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(modifier = Modifier.fillMaxWidth(0.95f).fillMaxHeight(0.90f), shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    if (isCreate) stringResource(R.string.add_film) else stringResource(R.string.edit_film),
                    style = MaterialTheme.typography.headlineSmall
                )
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    label = { Text(stringResource(R.string.field_title)) },
                    isError = title.isNotEmpty() && !titleValid, singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = releaseYear, onValueChange = { releaseYear = it },
                    label = { Text(stringResource(R.string.field_release_year)) },
                    isError = releaseYear.isNotEmpty() && !yearValid, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = rating, onValueChange = { rating = it },
                    label = { Text(stringResource(R.string.field_rating)) },
                    isError = rating.isNotEmpty() && !ratingValid, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = durationMinutes, onValueChange = { durationMinutes = it },
                    label = { Text(stringResource(R.string.field_duration)) },
                    isError = durationMinutes.isNotEmpty() && !durationValid, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenuBox(expanded = genreExpanded, onExpandedChange = { genreExpanded = it }) {
                    OutlinedTextField(
                        value = selectedGenre.name, onValueChange = {}, readOnly = true,
                        label = { Text(stringResource(R.string.field_genres)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(genreExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = genreExpanded, onDismissRequest = { genreExpanded = false }) {
                        Genre.entries.forEach { genre ->
                            DropdownMenuItem(
                                text = { Text(genre.name) },
                                onClick = { selectedGenre = genre; genreExpanded = false }
                            )
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.field_in_color), style = MaterialTheme.typography.bodyLarge)
                    Switch(checked = inColor, onCheckedChange = { inColor = it })
                }
                OutlinedTextField(
                    value = awards, onValueChange = { awards = it },
                    label = { Text(stringResource(R.string.field_awards)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = imageUrl, onValueChange = { imageUrl = it },
                    label = { Text(stringResource(R.string.field_image_url)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onConfirm(Film(
                                id = film?.id ?: 0,
                                directorId = film?.directorId ?: 0,
                                title = title.trim(),
                                releaseYear = releaseYear.toInt(),
                                rating = rating.toDouble(),
                                durationMinutes = durationMinutes.toInt(),
                                genre = selectedGenre,
                                inColor = inColor,
                                awards = awards.split(",").map { it.trim() }.filter { it.isNotBlank() },
                                imageUrl = imageUrl.trim()
                            ))
                        },
                        enabled = formValid
                    ) {
                        Text(if (isCreate) stringResource(R.string.add) else stringResource(R.string.save))
                    }
                }
            }
        }
    }
}
