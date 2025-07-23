package ies.quevedo.chardat.framework.fragmentMainMenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ies.quevedo.chardat.data.repository.PersonajeRepository
import ies.quevedo.chardat.data.utils.NetworkResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val personajeRepository: PersonajeRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainMenuContract.State> by lazy {
        MutableStateFlow(MainMenuContract.State())
    }
    val uiState: StateFlow<MainMenuContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(event: MainMenuContract.Event) {
        when (event) {
            is MainMenuContract.Event.FetchPersonaje -> fetchPersonaje(event.id)
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
                            MainMenuContract.State(personaje = result.data, isLoading = false)
                        }
                    }
                }
        }
    }
}