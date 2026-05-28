package be.kdg.filmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import be.kdg.filmapp.ui.DirectorSpaceApp
import be.kdg.filmapp.ui.theme.FilmAppTheme

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
