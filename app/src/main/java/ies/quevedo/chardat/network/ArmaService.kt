package ies.quevedo.chardat.network

import ies.quevedo.chardat.domain.model.Arma
import retrofit2.Response
import retrofit2.http.*

interface ArmaService {

    @GET("/api/armas/one/{id}")
    suspend fun getArmaByID(@Path("id") idArma: Int): Response<Arma>

    @GET("api/armas/all/{id}")
    suspend fun getArmas(@Path("id") idPersonaje: Int): Response<List<Arma>>

    @POST("api/armas")
    suspend fun postArma(@Body arma: Arma): Response<Arma>

    @PUT("api/armas")
    suspend fun putArma(@Body arma: Arma): Response<Arma>

    @DELETE("api/armas")
    suspend fun deleteArma(@Query("idArma") idArma: Int): Response<Arma>
}