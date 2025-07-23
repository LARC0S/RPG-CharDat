package ies.quevedo.chardat.framework.fragmentAddArma

import ies.quevedo.chardat.domain.model.Arma

interface AddArmaContract {

    sealed class Event {
        class PostArma(val arma: Arma) : Event()
    }

    data class State(
        val arma: Arma? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}