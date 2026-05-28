package be.kdg.filmapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import be.kdg.filmapp.data.di.AppModule
import be.kdg.filmapp.data.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.get
import org.koin.ksp.generated.module

class FilmApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FilmApplication)
            modules(AppModule().module)
        }
        val savedLanguage = runBlocking {
            get<SettingsRepository>(SettingsRepository::class.java).language.first()
        }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(savedLanguage))
    }
}
