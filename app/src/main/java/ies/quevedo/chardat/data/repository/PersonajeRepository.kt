package ies.quevedo.chardat.data.repository

import ies.quevedo.chardat.data.remote.dataSources.PersonajeRemoteDataSource
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Personaje
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PersonajeRepository @Inject constructor(
    private val personajeRemoteDataSource: PersonajeRemoteDataSource
) {

    fun getPersonaje(id: Int): Flow<NetworkResult<Personaje>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(personajeRemoteDataSource.fetchPersonaje(id))
        }.flowOn(Dispatchers.IO)
    }

    fun getPersonajes(): Flow<NetworkResult<List<Personaje>>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(personajeRemoteDataSource.fetchPersonajes())
        }.flowOn(Dispatchers.IO)
    }

    fun insertPersonaje(personaje: Personaje): Flow<NetworkResult<Personaje>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(personajeRemoteDataSource.postPersonaje(personaje))
        }.flowOn(Dispatchers.IO)
    }

    fun updatePersonaje(personaje: Personaje): Flow<NetworkResult<Personaje>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(personajeRemoteDataSource.putPersonaje(personaje))
        }.flowOn(Dispatchers.IO)
    }

    fun deletePersonaje(idPersonaje: Int): Flow<NetworkResult<Personaje>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(personajeRemoteDataSource.deletePersonaje(idPersonaje))
        }.flowOn(Dispatchers.IO)
    }
}