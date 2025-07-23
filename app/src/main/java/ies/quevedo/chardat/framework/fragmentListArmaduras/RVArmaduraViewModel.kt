package ies.quevedo.chardat.framework.fragmentListArmaduras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ies.quevedo.chardat.data.repository.ArmaduraRepository
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Armadura
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RVArmaduraViewModel @Inject constructor(
    private val armaduraRepository: ArmaduraRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ArmaduraListContract.State> by lazy {
        MutableStateFlow(ArmaduraListContract.State())
    }
    val uiState: StateFlow<ArmaduraListContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: ArmaduraListContract.Event,
    ) {
        when (event) {
            is ArmaduraListContract.Event.FetchArmaduras -> fetchArmaduras(event.idPersonaje)
            is ArmaduraListContract.Event.PostArmadura -> postArmadura(event.armadura)
            is ArmaduraListContract.Event.DeleteArmadura -> deleteArmadura(event.idArmadura)
        }
    }

    private fun fetchArmaduras(id: Int) {
        viewModelScope.launch {
            armaduraRepository.getArmaduras(id)
                .catch(action = { cause ->
                    _uiError.send(cause.message ?: "Error")
                    Timber.tag("Error").e(cause)
                })
                .collect { result ->
                    when (result) {
                        is NetworkResult.Error -> {
                            _uiState.update { it.copy(error = result.message) }
                            Timber.tag("Error").e(result.message)
                        }
                        is NetworkResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is NetworkResult.Success -> _uiState.update {
                            ArmaduraListContract.State(
                                listaArmaduras = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun postArmadura(armadura: Armadura) {
        viewModelScope.launch {
            armaduraRepository.insertArmadura(armadura)
                .catch(action = { cause ->
                    _uiError.send(cause.message ?: "Error")
                    Timber.tag("Error").e(cause)
                })
                .collect { result ->
                    when (result) {
                        is NetworkResult.Error -> {
                            _uiState.update { it.copy(error = result.message) }
                            Timber.tag("Error").e(result.message)
                        }
                        is NetworkResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is NetworkResult.Success -> _uiState.update {
                            ArmaduraListContract.State(
                                armaduraRecuperada = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun deleteArmadura(idArmadura: Int) {
        viewModelScope.launch {
            armaduraRepository.deleteArmadura(idArmadura)
                .catch(action = { cause ->
                    _uiError.send(cause.message ?: "Error")
                    Timber.tag("Error").e(cause)
                })
                .collect { result ->
                    when (result) {
                        is NetworkResult.Error -> {
                            _uiState.update { it.copy(error = result.message) }
                            Timber.tag("Error").e(result.message)
                        }
                        is NetworkResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is NetworkResult.Success -> _uiState.update {
                            ArmaduraListContract.State(
                                armaduraBorrada = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }
}