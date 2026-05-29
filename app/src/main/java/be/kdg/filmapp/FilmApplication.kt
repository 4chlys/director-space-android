package be.kdg.filmapp

import android.app.Application
import be.kdg.filmapp.data.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class FilmApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FilmApplication)
            modules(AppModule().module)
        }
    }
}
