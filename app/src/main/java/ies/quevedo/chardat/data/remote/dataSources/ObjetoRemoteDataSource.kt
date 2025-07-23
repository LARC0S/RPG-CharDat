package ies.quevedo.chardat.data.remote.dataSources

import ies.quevedo.chardat.data.remote.BaseApiResponse
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Objeto
import ies.quevedo.chardat.network.ObjetoService
import javax.inject.Inject

class ObjetoRemoteDataSource @Inject constructor(
    private val objetoService: ObjetoService
) : BaseApiResponse() {

    suspend fun fetchObjeto(idObjeto: Int): NetworkResult<Objeto> {
        return safeApiCall(
            apiCall = { objetoService.getObjetoByID(idObjeto) }
        )
    }

    suspend fun fetchObjetos(idPJ: Int): NetworkResult<List<Objeto>> {
        return safeApiCall(
            apiCall = { objetoService.getObjetos(idPJ) }
        )
    }

    suspend fun postObjeto(objeto: Objeto): NetworkResult<Objeto> {
        return safeApiCall(
            apiCall = { objetoService.postObjeto(objeto) }
        )
    }

    suspend fun putObjeto(objeto: Objeto): NetworkResult<Objeto> {
        return safeApiCall(
            apiCall = { objetoService.putObjeto(objeto) }
        )
    }

    suspend fun deleteObjeto(idObjeto: Int): NetworkResult<Objeto> {
        return safeApiCall(
            apiCall = { objetoService.deleteObjeto(idObjeto) }
        )
    }
}