package ies.quevedo.chardat.framework.fragmentAddArma

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
class AddArmaViewModel @Inject constructor(
    private val armaRepository: ArmaRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddArmaContract.State> by lazy {
        MutableStateFlow(AddArmaContract.State())
    }
    val uiState: StateFlow<AddArmaContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: AddArmaContract.Event,
    ) {
        when (event) {
            is AddArmaContract.Event.PostArma -> postArma(event.arma)
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
                        is NetworkResult.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                        is NetworkResult.Success -> _uiState.update {
                            AddArmaContract.State(arma = result.data)
                        }
                    }
                }
        }
    }
}