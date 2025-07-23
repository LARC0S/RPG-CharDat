package ies.quevedo.chardat.framework.fragmentShowArma

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
class ShowArmaViewModel @Inject constructor(
    private val armaRepository: ArmaRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ShowArmaContract.State> by lazy {
        MutableStateFlow(ShowArmaContract.State())
    }
    val uiState: StateFlow<ShowArmaContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: ShowArmaContract.Event,
    ) {
        when (event) {
            is ShowArmaContract.Event.FetchArma -> fetchArma(event.idArma)
            is ShowArmaContract.Event.PutArma -> event.arma?.let { putArma(it) }
        }
    }

    private fun fetchArma(idArma: Int) {
        viewModelScope.launch {
            armaRepository.getArma(idArma)
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
                            ShowArmaContract.State(arma = result.data, isLoading = false)
                        }
                    }
                }
        }
    }

    private fun putArma(arma: Arma) {
        viewModelScope.launch {
            armaRepository.updateArma(arma)
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
                            ShowArmaContract.State(armaActualizada = result.data, isLoading = false)
                        }
                    }
                }
        }
    }
}