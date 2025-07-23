package ies.quevedo.chardat.framework.fragmentAddEscudo

import ies.quevedo.chardat.domain.model.Escudo

interface AddEscudoContract {

    sealed class Event {
        class PostEscudo(val escudo: Escudo) : Event()
    }

    data class State(
        val escudo: Escudo? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}