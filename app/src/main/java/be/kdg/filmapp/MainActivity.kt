package be.kdg.filmapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import be.kdg.filmapp.data.repository.SettingsRepository
import be.kdg.filmapp.ui.navigation.AppNavigation
import be.kdg.filmapp.ui.theme.FilmAppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.compose.koinInject
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsRepository = koinInject<SettingsRepository>()

            // Seed initial values synchronously so the very first frame already
            // uses the persisted language/theme (avoids a flash on cold start).
            val initial = remember {
                runBlocking {
                    settingsRepository.language.first() to settingsRepository.themeMode.first()
                }
            }
            val language by settingsRepository.language.collectAsState(initial = initial.first)
            val themeMode by settingsRepository.themeMode.collectAsState(initial = initial.second)

            val darkTheme = when (themeMode) {
                "light" -> false
                "dark" -> true
                else -> isSystemInDarkTheme()
            }

            // Override the locale config so stringResource() resolves against the
            // selected language's strings.xml (values/ vs values-nl/) and re-renders
            // live when the user switches language in Settings.
            val baseContext = LocalContext.current
            val localizedContext = remember(language) {
                val config = Configuration(baseContext.resources.configuration)
                config.setLocale(Locale.forLanguageTag(language))
                baseContext.createConfigurationContext(config)
            }

            CompositionLocalProvider(
                LocalConfiguration provides localizedContext.resources.configuration,
                LocalContext provides localizedContext
            ) {
                FilmAppTheme(darkTheme = darkTheme) {
                    AppNavigation()
                }
            }
        }
    }
}
