package ies.quevedo.chardat.framework.fragmentShowPersonaje

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
class ShowPersonajeViewModel @Inject constructor(
    private val personajeRepository: PersonajeRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ShowPersonajeContract.State> by lazy {
        MutableStateFlow(ShowPersonajeContract.State())
    }
    val uiState: StateFlow<ShowPersonajeContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: ShowPersonajeContract.Event,
    ) {
        when (event) {
            is ShowPersonajeContract.Event.FetchPersonaje -> fetchPersonaje(event.id)
            is ShowPersonajeContract.Event.PutPersonaje -> event.personaje?.let { putPersonaje(it) }
        }
    }

    private fun fetchPersonaje(id: Int) {
        viewModelScope.launch {
            personajeRepository.getPersonaje(id)
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
                            ShowPersonajeContract.State(personaje = result.data, isLoading = false)
                        }
                    }
                }
        }
    }

    private fun putPersonaje(personaje: Personaje) {
        viewModelScope.launch {
            personajeRepository.updatePersonaje(personaje)
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
                            ShowPersonajeContract.State(
                                personajeActualizado = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
        }

    }
}