package ies.quevedo.chardat.data.remote.dataSources

import ies.quevedo.chardat.data.remote.BaseApiResponse
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Personaje
import ies.quevedo.chardat.network.PersonajeService
import javax.inject.Inject

class PersonajeRemoteDataSource @Inject constructor(
    private val personajeService: PersonajeService
) : BaseApiResponse() {

    suspend fun fetchPersonaje(id: Int): NetworkResult<Personaje> {
        return safeApiCall(
            apiCall = { personajeService.getPersonajeByID(id) }
        )
    }

    suspend fun fetchPersonajes(): NetworkResult<List<Personaje>> {
        return safeApiCall(
            apiCall = { personajeService.getPersonajes() }
        )
    }

    suspend fun postPersonaje(personaje: Personaje): NetworkResult<Personaje> {
        return safeApiCall(
            apiCall = { personajeService.postPersonaje(personaje) }
        )
    }

    suspend fun putPersonaje(personaje: Personaje): NetworkResult<Personaje> {
        return safeApiCall(
            apiCall = { personajeService.putPersonaje(personaje) }
        )
    }

    suspend fun deletePersonaje(idPersonaje: Int): NetworkResult<Personaje> {
        return safeApiCall(
            apiCall = { personajeService.deletePersonaje(idPersonaje) }
        )
    }
}