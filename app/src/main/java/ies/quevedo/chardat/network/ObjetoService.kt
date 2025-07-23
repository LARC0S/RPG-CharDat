package ies.quevedo.chardat.network

import ies.quevedo.chardat.domain.model.Objeto
import retrofit2.Response
import retrofit2.http.*

interface ObjetoService {

    @GET("/api/objetos/one/{id}")
    suspend fun getObjetoByID(@Path("id") idObjeto: Int): Response<Objeto>

    @GET("api/objetos/all/{id}")
    suspend fun getObjetos(@Path("id") idPersonaje: Int): Response<List<Objeto>>

    @POST("api/objetos")
    suspend fun postObjeto(@Body objeto: Objeto): Response<Objeto>

    @PUT("api/objetos")
    suspend fun putObjeto(@Body objeto: Objeto): Response<Objeto>

    @DELETE("api/objetos")
    suspend fun deleteObjeto(@Query("idObjeto") idObjeto: Int): Response<Objeto>
}