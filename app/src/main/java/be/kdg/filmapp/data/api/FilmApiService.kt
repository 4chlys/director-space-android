package be.kdg.filmapp.data.api

import be.kdg.filmapp.model.Film
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmApiService {
    @GET("films")
    suspend fun getFilmsForDirector(@Query("directorId") directorId: Int): List<Film>

    @POST("films")
    suspend fun createFilm(@Body film: Film): Response<Film>

    @PUT("films/{id}")
    suspend fun updateFilm(@Path("id") id: Int, @Body film: Film): Response<Film>

    @DELETE("films/{id}")
    suspend fun deleteFilm(@Path("id") id: Int): Response<Unit>
}
