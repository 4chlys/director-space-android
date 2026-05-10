package be.kdg.filmapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary             = VelvetPrimary,
    onPrimary           = VelvetOnPrimary,
    primaryContainer    = VelvetContainer,
    onPrimaryContainer  = VelvetAccent,
    secondary           = VelvetSecondary,
    onSecondary         = VelvetOnPrimary,
    background          = VelvetBackground,
    onBackground        = VelvetOnSurface,
    surface             = VelvetBackground,
    onSurface           = VelvetOnSurface,
    surfaceVariant      = VelvetContainer,
    onSurfaceVariant    = VelvetMuted,
    error               = VelvetError,
    onError             = VelvetOnError,
)

@Composable
fun FilmAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography  = Typography,
        content     = content,
    )
}
