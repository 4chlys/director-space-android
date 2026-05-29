package be.kdg.filmapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import be.kdg.filmapp.R
import be.kdg.filmapp.model.Director
import be.kdg.filmapp.model.Genre
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
private fun RequiredLabel(text: String) {
    Text(
        buildAnnotatedString {
            append(text)
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) { append(" *") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditDirectorDialog(
    director: Director?,
    onDismiss: () -> Unit,
    onConfirm: (Director) -> Unit
) {
    val isCreate = director == null
    var name by remember { mutableStateOf(director?.name ?: "") }
    var nationality by remember { mutableStateOf(director?.nationality ?: "") }
    var country by remember { mutableStateOf(director?.country ?: "") }
    var birthDate by remember { mutableStateOf(director?.birthDate ?: "") }
    var careerStart by remember { mutableStateOf(director?.careerStart?.toString() ?: "") }
    var active by remember { mutableStateOf(director?.active ?: true) }
    var careerEnd by remember { mutableStateOf(director?.careerEnd?.toString() ?: "") }
    var rating by remember { mutableStateOf(director?.rating?.toString() ?: "5.0") }
    var selectedGenres by remember { mutableStateOf((director?.genres ?: emptyList()).toSet()) }
    var imageUrl by remember { mutableStateOf(director?.imageUrl ?: "") }
    var bio by remember { mutableStateOf(director?.bio ?: "") }
    var showDatePicker by remember { mutableStateOf(false) }

    val nameValid = name.isNotBlank()
    val careerStartValid = careerStart.toIntOrNull()?.let { it > 0 } == true
    val careerEndValid = active || careerEnd.toIntOrNull()?.let { it > 0 } == true
    val ratingValid = rating.toDoubleOrNull()?.let { it in 0.0..10.0 } == true
    val genresValid = selectedGenres.isNotEmpty()
    val formValid = nameValid && careerStartValid && careerEndValid && ratingValid && genresValid

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (isCreate) stringResource(R.string.add_director)
                    else stringResource(R.string.edit_director),
                    style = MaterialTheme.typography.headlineSmall
                )
                val requiredHint = stringResource(R.string.required_field)
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) { append("* ") }
                        append(requiredHint)
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { RequiredLabel(stringResource(R.string.field_name)) },
                    isError = name.isNotEmpty() && !nameValid,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nationality,
                    onValueChange = { nationality = it },
                    label = { Text(stringResource(R.string.field_nationality)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = country,
                    onValueChange = { country = it },
                    label = { Text(stringResource(R.string.field_country)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    label = { Text(stringResource(R.string.field_birth_date)) },
                    placeholder = { Text(stringResource(R.string.field_birth_date_hint)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = stringResource(R.string.field_birth_date)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (showDatePicker) {
                    val initialMillis = birthDate.takeIf { it.isNotBlank() }
                        ?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
                        ?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
                    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    birthDate = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneOffset.UTC).toLocalDate().toString()
                                }
                                showDatePicker = false
                            }) { Text(stringResource(R.string.ok)) }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text(stringResource(R.string.cancel))
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                OutlinedTextField(
                    value = careerStart,
                    onValueChange = { careerStart = it },
                    label = { RequiredLabel(stringResource(R.string.field_career_start)) },
                    isError = careerStart.isNotEmpty() && !careerStartValid,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.field_active),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(checked = active, onCheckedChange = { active = it })
                }

                if (!active) {
                    OutlinedTextField(
                        value = careerEnd,
                        onValueChange = { careerEnd = it },
                        label = { RequiredLabel(stringResource(R.string.field_career_end)) },
                        isError = careerEnd.isNotEmpty() && !careerEndValid,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = rating,
                    onValueChange = { rating = it },
                    label = { RequiredLabel(stringResource(R.string.field_rating)) },
                    isError = rating.isNotEmpty() && !ratingValid,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Column {
                    val genresLabel = stringResource(R.string.field_genres)
                    Text(
                        text = buildAnnotatedString {
                            append(genresLabel)
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) { append(" *") }
                        },
                        style = MaterialTheme.typography.labelLarge,
                        color = if (!genresValid && selectedGenres.isEmpty())
                            MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Genre.entries.forEach { genre ->
                            FilterChip(
                                selected = genre in selectedGenres,
                                onClick = {
                                    selectedGenres = if (genre in selectedGenres)
                                        selectedGenres - genre
                                    else
                                        selectedGenres + genre
                                },
                                label = { Text(genre.name) }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text(stringResource(R.string.field_image_url)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text(stringResource(R.string.field_bio)) },
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onConfirm(
                                Director(
                                    id = director?.id ?: 0,
                                    name = name.trim(),
                                    nationality = nationality.trim(),
                                    country = country.trim(),
                                    birthDate = birthDate.trim(),
                                    careerStart = careerStart.toInt(),
                                    careerEnd = if (active) null else careerEnd.toIntOrNull(),
                                    active = active,
                                    rating = rating.toDouble(),
                                    genres = selectedGenres.toList(),
                                    imageUrl = imageUrl.trim(),
                                    bio = bio.trim()
                                )
                            )
                        },
                        enabled = formValid
                    ) {
                        Text(
                            if (isCreate) stringResource(R.string.add)
                            else stringResource(R.string.save)
                        )
                    }
                }
            }
        }
    }
}
