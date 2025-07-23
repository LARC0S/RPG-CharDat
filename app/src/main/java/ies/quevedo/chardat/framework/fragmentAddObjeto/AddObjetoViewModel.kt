package ies.quevedo.chardat.framework.fragmentAddObjeto

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
class AddObjetoViewModel @Inject constructor(
    private val objetoRepository: ObjetoRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddObjetoContract.State> by lazy {
        MutableStateFlow(AddObjetoContract.State())
    }
    val uiState: StateFlow<AddObjetoContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: AddObjetoContract.Event,
    ) {
        when (event) {
            is AddObjetoContract.Event.PostObjeto -> postObjeto(event.objeto)
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
                        is NetworkResult.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                        is NetworkResult.Success -> _uiState.update {
                            AddObjetoContract.State(objeto = result.data)
                        }
                    }
                }
        }
    }
}