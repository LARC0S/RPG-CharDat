package ies.quevedo.chardat.framework.fragmentShowArmadura

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
class ShowArmaduraViewModel @Inject constructor(
    private val armaduraRepository: ArmaduraRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ShowArmaduraContract.State> by lazy {
        MutableStateFlow(ShowArmaduraContract.State())
    }
    val uiState: StateFlow<ShowArmaduraContract.State> = _uiState

    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    fun handleEvent(
        event: ShowArmaduraContract.Event,
    ) {
        when (event) {
            is ShowArmaduraContract.Event.FetchArmadura -> fetchArmadura(event.idArmadura)
            is ShowArmaduraContract.Event.PutArmadura -> event.armadura?.let { putArmadura(it) }
        }
    }

    private fun fetchArmadura(idArmadura: Int) {
        viewModelScope.launch {
            armaduraRepository.getArmadura(idArmadura)
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
                            ShowArmaduraContract.State(armadura = result.data, isLoading = false)
                        }
                    }
                }
        }
    }

    private fun putArmadura(armadura: Armadura) {
        viewModelScope.launch {
            armaduraRepository.updateArmadura(armadura)
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
                            ShowArmaduraContract.State(
                                armaduraActualizada = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }
}