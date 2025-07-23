package ies.quevedo.chardat.framework.fragmentShowEscudo

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
class ShowEscudoViewModel @Inject constructor(
    private val escudoRepository: EscudoRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ShowEscudoContract.State> by lazy {
        MutableStateFlow(ShowEscudoContract.State())
    }
    val uiState: StateFlow<ShowEscudoContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: ShowEscudoContract.Event,
    ) {
        when (event) {
            is ShowEscudoContract.Event.FetchEscudo -> fetchEscudo(event.idEscudo)
            is ShowEscudoContract.Event.PutEscudo -> event.escudo?.let { putEscudo(it) }
        }
    }

    private fun fetchEscudo(idEscudo: Int) {
        viewModelScope.launch {
            escudoRepository.getEscudo(idEscudo)
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
                            ShowEscudoContract.State(escudo = result.data, isLoading = false)
                        }
                    }
                }
        }
    }

    private fun putEscudo(escudo: Escudo) {
        viewModelScope.launch {
            escudoRepository.updateEscudo(escudo)
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
                            ShowEscudoContract.State(
                                escudoActualizado = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }
}