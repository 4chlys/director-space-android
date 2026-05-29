package be.kdg.filmapp.data.di

import android.content.Context
import be.kdg.filmapp.data.network.DirectorApiService
import be.kdg.filmapp.data.network.FilmApiService
import be.kdg.filmapp.data.repository.DataStoreSettingsRepository
import be.kdg.filmapp.data.repository.DirectorRepository
import be.kdg.filmapp.data.repository.FilmRepository
import be.kdg.filmapp.data.repository.RestDirectorRepository
import be.kdg.filmapp.data.repository.RestFilmRepository
import be.kdg.filmapp.data.repository.SettingsRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit

@Module
@ComponentScan("be.kdg.filmapp")
class AppModule {

    @Single
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Single
    fun provideRetrofit(json: Json): Retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Single
    fun provideDirectorApiService(retrofit: Retrofit): DirectorApiService =
        retrofit.create(DirectorApiService::class.java)

    @Single
    fun provideFilmApiService(retrofit: Retrofit): FilmApiService =
        retrofit.create(FilmApiService::class.java)

    @Single
    fun provideDirectorRepository(api: DirectorApiService): DirectorRepository =
        RestDirectorRepository(api)

    @Single
    fun provideFilmRepository(api: FilmApiService): FilmRepository =
        RestFilmRepository(api)

    @Single
    fun provideSettingsRepository(context: Context): SettingsRepository =
        DataStoreSettingsRepository(context)
}
