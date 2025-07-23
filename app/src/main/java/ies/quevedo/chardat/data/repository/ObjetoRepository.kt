package ies.quevedo.chardat.data.repository

import ies.quevedo.chardat.data.remote.dataSources.ObjetoRemoteDataSource
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Objeto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ObjetoRepository @Inject constructor(
    private val objetoRemoteDataSource: ObjetoRemoteDataSource
) {

    fun getObjeto(idObjeto: Int): Flow<NetworkResult<Objeto>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(objetoRemoteDataSource.fetchObjeto(idObjeto))
        }.flowOn(Dispatchers.IO)
    }

    fun getObjetos(idPJ: Int): Flow<NetworkResult<List<Objeto>>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(objetoRemoteDataSource.fetchObjetos(idPJ))
        }.flowOn(Dispatchers.IO)
    }

    fun insertObjeto(objeto: Objeto): Flow<NetworkResult<Objeto>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(objetoRemoteDataSource.postObjeto(objeto))
        }.flowOn(Dispatchers.IO)
    }

    fun updateObjeto(objeto: Objeto): Flow<NetworkResult<Objeto>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(objetoRemoteDataSource.putObjeto(objeto))
        }.flowOn(Dispatchers.IO)
    }

    fun deleteObjeto(idObjeto: Int): Flow<NetworkResult<Objeto>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(objetoRemoteDataSource.deleteObjeto(idObjeto))
        }.flowOn(Dispatchers.IO)
    }
}