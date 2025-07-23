package ies.quevedo.chardat.framework.fragmentAddArmadura

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ies.quevedo.chardat.data.repository.ArmaduraRepository
import ies.quevedo.chardat.data.utils.NetworkResult
import ies.quevedo.chardat.domain.model.Armadura
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddArmaduraViewModel @Inject constructor(
    private val armaduraRepository: ArmaduraRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddArmaduraContract.State> by lazy {
        MutableStateFlow(AddArmaduraContract.State())
    }
    val uiState: StateFlow<AddArmaduraContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: AddArmaduraContract.Event,
    ) {
        when (event) {
            is AddArmaduraContract.Event.PostArmadura -> postArma(event.armadura)
        }
    }

    private fun postArma(armadura: Armadura) {
        viewModelScope.launch {
            armaduraRepository.insertArmadura(armadura)
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
                            AddArmaduraContract.State(armadura = result.data)
                        }
                    }
                }
        }
    }

}