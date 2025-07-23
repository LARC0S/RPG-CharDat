package ies.quevedo.chardat.network

import ies.quevedo.chardat.domain.model.Armadura
import retrofit2.Response
import retrofit2.http.*

interface ArmaduraService {

    @GET("/api/armaduras/one/{id}")
    suspend fun getArmaduraByID(@Path("id") idEscudo: Int): Response<Armadura>

    @GET("/api/armaduras/all/{id}")
    suspend fun getArmaduras(@Path("id") idPersonaje: Int): Response<List<Armadura>>

    @POST("/api/armaduras")
    suspend fun postArmadura(@Body armadura: Armadura): Response<Armadura>

    @PUT("/api/armaduras")
    suspend fun putArmadura(@Body armadura: Armadura): Response<Armadura>

    @DELETE("/api/armaduras")
    suspend fun deleteArmadura(@Query("idArmadura") idArmadura: Int): Response<Armadura>
}