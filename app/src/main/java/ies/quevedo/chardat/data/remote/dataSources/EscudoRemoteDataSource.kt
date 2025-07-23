package ies.quevedo.chardat.data.remote.dataSources

import ies.quevedo.chardat.data.remote.BaseApiResponse
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Escudo
import ies.quevedo.chardat.network.EscudoService
import javax.inject.Inject

class EscudoRemoteDataSource @Inject constructor(
    private val escudoService: EscudoService
) : BaseApiResponse() {

    suspend fun fetchEscudo(idEscudo: Int): NetworkResult<Escudo> {
        return safeApiCall(
            apiCall = { escudoService.getEscudoByID(idEscudo) }
        )
    }

    suspend fun fetchEscudos(idPJ: Int): NetworkResult<List<Escudo>> {
        return safeApiCall(
            apiCall = { escudoService.getEscudos(idPJ) }
        )
    }

    suspend fun postEscudo(escudo: Escudo): NetworkResult<Escudo> {
        return safeApiCall(
            apiCall = { escudoService.postEscudo(escudo) }
        )
    }

    suspend fun putEscudo(escudo: Escudo): NetworkResult<Escudo> {
        return safeApiCall(
            apiCall = { escudoService.putEscudo(escudo) }
        )
    }

    suspend fun deleteEscudo(idEscudo: Int): NetworkResult<Escudo> {
        return safeApiCall(
            apiCall = { escudoService.deleteEscudo(idEscudo) }
        )
    }
}