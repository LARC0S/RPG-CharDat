package ies.quevedo.chardat.network

import ies.quevedo.chardat.domain.model.Escudo
import retrofit2.Response
import retrofit2.http.*

interface EscudoService {

    @GET("/api/escudos/one/{id}")
    suspend fun getEscudoByID(@Path("id") idEscudo: Int): Response<Escudo>

    @GET("api/escudos/all/{id}")
    suspend fun getEscudos(@Path("id") idPersonaje: Int): Response<List<Escudo>>

    @POST("api/escudos")
    suspend fun postEscudo(@Body escudo: Escudo): Response<Escudo>

    @PUT("api/escudos")
    suspend fun putEscudo(@Body escudo: Escudo): Response<Escudo>

    @DELETE("api/escudos")
    suspend fun deleteEscudo(@Query("idEscudo") idEscudo: Int): Response<Escudo>
}