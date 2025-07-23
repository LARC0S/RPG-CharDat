package ies.quevedo.chardat.framework.fragmentShowObjeto

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
class ShowObjetoViewModel @Inject constructor(
    private val objetoRepository: ObjetoRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ShowObjetoContract.State> by lazy {
        MutableStateFlow(ShowObjetoContract.State())
    }
    val uiState: StateFlow<ShowObjetoContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: ShowObjetoContract.Event,
    ) {
        when (event) {
            is ShowObjetoContract.Event.FetchObjeto -> fetchObjeto(event.idObjeto)
            is ShowObjetoContract.Event.PutObjeto -> event.objeto?.let { putEscudo(it) }
        }
    }

    private fun fetchObjeto(idObjeto: Int) {
        viewModelScope.launch {
            objetoRepository.getObjeto(idObjeto)
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
                            ShowObjetoContract.State(objeto = result.data, isLoading = false)
                        }
                    }
                }
        }
    }

    private fun putEscudo(objeto: Objeto) {
        viewModelScope.launch {
            objetoRepository.updateObjeto(objeto)
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
                            ShowObjetoContract.State(
                                objetoActualizado = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }
}