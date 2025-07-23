package ies.quevedo.chardat.framework.fragmentsAddPersonaje

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ies.quevedo.chardat.data.repository.PersonajeRepository
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Personaje
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddPersonajeViewModel @Inject constructor(
    private val personajeRepository: PersonajeRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddPersonajeContract.State> by lazy {
        MutableStateFlow(AddPersonajeContract.State())
    }
    val uiState: StateFlow<AddPersonajeContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: AddPersonajeContract.Event,
    ) {
        when (event) {
            is AddPersonajeContract.Event.PostPersonaje -> postPersonaje(event.personaje)
        }
    }

    private fun postPersonaje(personaje: Personaje) {
        viewModelScope.launch {
            personajeRepository.insertPersonaje(personaje)
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
                            AddPersonajeContract.State(personaje = result.data)
                        }
                    }
                }
        }
    }
}