package ies.quevedo.chardat.framework.fragmentShowArma

import ies.quevedo.chardat.domain.model.Arma

interface ShowArmaContract {

    sealed class Event {
        class FetchArma(val idArma: Int) : Event()
        class PutArma(val arma: Arma?) : Event()
    }

    data class State(
        val arma: Arma? = null,
        val armaActualizada: Arma? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}