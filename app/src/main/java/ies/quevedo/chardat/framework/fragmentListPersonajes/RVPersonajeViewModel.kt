package ies.quevedo.chardat.framework.fragmentListPersonajes

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
class RVPersonajeViewModel @Inject constructor(
    private val personajeRepository: PersonajeRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<PersonajeListContract.State> by lazy {
        MutableStateFlow(PersonajeListContract.State())
    }
    val uiState: StateFlow<PersonajeListContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: PersonajeListContract.Event,
    ) {
        when (event) {
            is PersonajeListContract.Event.FetchPersonaje -> fetchPersonaje(event.id)
            PersonajeListContract.Event.FetchPersonajes -> fetchPersonajes()
            is PersonajeListContract.Event.PostPersonaje -> postPersonaje(event.personaje)
            is PersonajeListContract.Event.DeletePersonaje -> deletePersonaje(event.id)
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
                            PersonajeListContract.State(personaje = result.data, isLoading = false)
                        }
                    }
                }
        }
    }

    private fun fetchPersonajes() {
        viewModelScope.launch {
            personajeRepository.getPersonajes()
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
                            PersonajeListContract.State(
                                listaPersonajes = result.data ?: emptyList(), isLoading = false
                            )
                        }
                    }
                }
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
                        is NetworkResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is NetworkResult.Success -> _uiState.update {
                            PersonajeListContract.State(
                                personajeRecuperado = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun deletePersonaje(idPersonaje: Int) {
        viewModelScope.launch {
            personajeRepository.deletePersonaje(idPersonaje)
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
                            PersonajeListContract.State(
                                personajeBorrado = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }
}