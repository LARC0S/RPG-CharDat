package ies.quevedo.chardat.framework.fragmentListObjetos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ies.quevedo.chardat.data.repository.ObjetoRepository
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Objeto
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RVObjetoViewModel @Inject constructor(
    private val objetoRepository: ObjetoRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ObjetoListContract.State> by lazy {
        MutableStateFlow(ObjetoListContract.State())
    }
    val uiState: StateFlow<ObjetoListContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: ObjetoListContract.Event,
    ) {
        when (event) {
            is ObjetoListContract.Event.FetchObjetos -> fetchObjetos(event.idPersonaje)
            is ObjetoListContract.Event.PostObjeto -> postObjeto(event.objeto)
            is ObjetoListContract.Event.DeleteObjeto -> deleteObjeto(event.idObjeto)
        }
    }

    private fun fetchObjetos(idPersonaje: Int) {
        viewModelScope.launch {
            objetoRepository.getObjetos(idPersonaje)
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
                            ObjetoListContract.State(
                                listaObjetos = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun postObjeto(objeto: Objeto) {
        viewModelScope.launch {
            objetoRepository.insertObjeto(objeto)
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
                            ObjetoListContract.State(
                                objetoRecuperado = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun deleteObjeto(idObjeto: Int) {
        viewModelScope.launch {
            objetoRepository.deleteObjeto(idObjeto)
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
                            ObjetoListContract.State(objetoBorrado = result.data, isLoading = false)
                        }
                    }
                }
        }
    }
}