package ies.quevedo.chardat.framework.fragmentAddEscudo

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
class AddEscudoViewModel @Inject constructor(
    private val escudoRepository: EscudoRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddEscudoContract.State> by lazy {
        MutableStateFlow(AddEscudoContract.State())
    }
    val uiState: StateFlow<AddEscudoContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: AddEscudoContract.Event,
    ) {
        when (event) {
            is AddEscudoContract.Event.PostEscudo -> postEscudo(event.escudo)
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
                        is NetworkResult.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                        is NetworkResult.Success -> _uiState.update {
                            AddEscudoContract.State(escudo = result.data)
                        }
                    }
                }
        }
    }
}