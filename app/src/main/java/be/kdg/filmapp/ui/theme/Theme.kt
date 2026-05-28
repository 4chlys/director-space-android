package be.kdg.filmapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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
    errorContainer      = VelvetContainer,
    onErrorContainer    = VelvetError,
)

private val LightColorScheme = lightColorScheme(
    primary             = VelvetPrimary,
    onPrimary           = VelvetOnPrimary,
    primaryContainer    = CinemaLightPrimaryContainer,
    onPrimaryContainer  = CinemaLightOnPrimaryContainer,
    secondary           = VelvetSecondary,
    onSecondary         = VelvetOnPrimary,
    background          = CinemaLightBackground,
    onBackground        = CinemaLightOnBackground,
    surface             = CinemaLightSurface,
    onSurface           = CinemaLightOnSurface,
    surfaceVariant      = CinemaLightContainer,
    onSurfaceVariant    = CinemaLightOnSurfaceVariant,
    error               = CinemaLightError,
    onError             = VelvetOnPrimary,
    errorContainer      = CinemaLightErrorContainer,
    onErrorContainer    = CinemaLightOnErrorContainer,
)

@Composable
fun FilmAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content,
    )
}
