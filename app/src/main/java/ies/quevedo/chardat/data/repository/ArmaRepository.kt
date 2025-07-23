package ies.quevedo.chardat.data.repository

import ies.quevedo.chardat.data.remote.dataSources.ArmaRemoteDataSource
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Arma
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ArmaRepository @Inject constructor(
    private val armaRemoteDataSource: ArmaRemoteDataSource
) {

    fun getArma(idArma: Int): Flow<NetworkResult<Arma>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(armaRemoteDataSource.fetchArma(idArma))
        }.flowOn(Dispatchers.IO)
    }

    fun getArmas(idPJ: Int): Flow<NetworkResult<List<Arma>>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(armaRemoteDataSource.fetchArmas(idPJ))
        }.flowOn(Dispatchers.IO)
    }

    fun insertArma(arma: Arma): Flow<NetworkResult<Arma>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(armaRemoteDataSource.postArma(arma))
        }.flowOn(Dispatchers.IO)
    }


    fun updateArma(arma: Arma): Flow<NetworkResult<Arma>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(armaRemoteDataSource.putArma(arma))
        }.flowOn(Dispatchers.IO)
    }

    fun deleteArma(idArma: Int): Flow<NetworkResult<Arma>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(armaRemoteDataSource.deleteArma(idArma))
        }.flowOn(Dispatchers.IO)
    }
}