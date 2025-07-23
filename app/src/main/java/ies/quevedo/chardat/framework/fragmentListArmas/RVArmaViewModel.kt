package ies.quevedo.chardat.framework.fragmentListArmas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ies.quevedo.chardat.data.repository.ArmaRepository
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Arma
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RVArmaViewModel @Inject constructor(
    private val armaRepository: ArmaRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ArmaListContract.State> by lazy {
        MutableStateFlow(ArmaListContract.State())
    }
    val uiState: StateFlow<ArmaListContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: ArmaListContract.Event,
    ) {
        when (event) {
            is ArmaListContract.Event.FetchArmas -> fetchArmas(event.idPersonaje)
            is ArmaListContract.Event.PostArma -> postArma(event.arma)
            is ArmaListContract.Event.DeleteArma -> deleteArma(event.idArma)
        }
    }

    private fun fetchArmas(id: Int) {
        viewModelScope.launch {
            armaRepository.getArmas(id)
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
                            ArmaListContract.State(
                                listaArmas = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun postArma(arma: Arma) {
        viewModelScope.launch {
            armaRepository.insertArma(arma)
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
                            ArmaListContract.State(
                                armaRecuperada = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun deleteArma(idArma: Int) {
        viewModelScope.launch {
            armaRepository.deleteArma(idArma)
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
                            ArmaListContract.State(armaBorrada = result.data, isLoading = false)
                        }
                    }
                }
        }
    }
}