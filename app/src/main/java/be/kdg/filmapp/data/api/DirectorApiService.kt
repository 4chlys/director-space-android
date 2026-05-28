package be.kdg.filmapp.data.api

import be.kdg.filmapp.model.Director
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DirectorApiService {
    @GET("directors")
    suspend fun getDirectors(): List<Director>

    @POST("directors")
    suspend fun createDirector(@Body director: Director): Response<Director>

    @PUT("directors/{id}")
    suspend fun updateDirector(@Path("id") id: Int, @Body director: Director): Response<Director>

    @DELETE("directors/{id}")
    suspend fun deleteDirector(@Path("id") id: Int): Response<Unit>
}
