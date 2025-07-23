package ies.quevedo.chardat.data.repository

import ies.quevedo.chardat.data.remote.dataSources.ArmaduraRemoteDataSource
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Armadura
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ArmaduraRepository @Inject constructor(
    private val armaduraRemoteDataSource: ArmaduraRemoteDataSource
) {

    fun getArmadura(idArmadura: Int): Flow<NetworkResult<Armadura>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(armaduraRemoteDataSource.fetchArmadura(idArmadura))
        }.flowOn(Dispatchers.IO)
    }

    fun getArmaduras(idPJ: Int): Flow<NetworkResult<List<Armadura>>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(armaduraRemoteDataSource.fetchArmaduras(idPJ))
        }.flowOn(Dispatchers.IO)
    }

    fun insertArmadura(armadura: Armadura): Flow<NetworkResult<Armadura>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(armaduraRemoteDataSource.postArmadura(armadura))
        }.flowOn(Dispatchers.IO)
    }

    fun updateArmadura(armadura: Armadura): Flow<NetworkResult<Armadura>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(armaduraRemoteDataSource.putArmadura(armadura))
        }.flowOn(Dispatchers.IO)
    }

    fun deleteArmadura(idArmadura: Int): Flow<NetworkResult<Armadura>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(armaduraRemoteDataSource.deleteArmadura(idArmadura))
        }.flowOn(Dispatchers.IO)
    }
}