package be.kdg.filmapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import be.kdg.filmapp.R

@Composable
fun DisplayController(
    currentIndex: Int,
    directorCount: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onAddFilm: () -> Unit,
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
                Text(stringResource(R.string.edit_director))
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
        OutlinedButton(
            onClick = onDelete,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(stringResource(R.string.delete_director))
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedButton(
            onClick = onAddFilm,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.add_film))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.counter, currentIndex + 1, directorCount),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
