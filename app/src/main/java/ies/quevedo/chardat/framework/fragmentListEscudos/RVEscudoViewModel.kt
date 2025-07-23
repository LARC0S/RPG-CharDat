package ies.quevedo.chardat.framework.fragmentListEscudos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ies.quevedo.chardat.data.repository.EscudoRepository
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Escudo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RVEscudoViewModel @Inject constructor(
    private val escudoRepository: EscudoRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<EscudoListContract.State> by lazy {
        MutableStateFlow(EscudoListContract.State())
    }
    val uiState: StateFlow<EscudoListContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: EscudoListContract.Event,
    ) {
        when (event) {
            is EscudoListContract.Event.FetchEscudos -> fetchEscudos(event.idPersonaje)
            is EscudoListContract.Event.PostEscudo -> postEscudo(event.escudo)
            is EscudoListContract.Event.DeleteEscudo -> deleteEscudo(event.idEscudo)
        }
    }

    private fun fetchEscudos(idPersonaje: Int) {
        viewModelScope.launch {
            escudoRepository.getEscudos(idPersonaje)
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
                            EscudoListContract.State(
                                listaEscudos = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun postEscudo(escudo: Escudo) {
        viewModelScope.launch {
            escudoRepository.insertEscudo(escudo)
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
                            EscudoListContract.State(
                                escudoRecuperado = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun deleteEscudo(idEscudo: Int) {
        viewModelScope.launch {
            escudoRepository.deleteEscudo(idEscudo)
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
                            EscudoListContract.State(escudoBorrado = result.data, isLoading = false)
                        }
                    }
                }
        }
    }
}