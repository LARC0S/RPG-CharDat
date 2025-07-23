package ies.quevedo.chardat.framework.fragmentShowEscudo

import ies.quevedo.chardat.domain.model.Escudo

interface ShowEscudoContract {

    sealed class Event {
        class FetchEscudo(val idEscudo: Int) : Event()
        class PutEscudo(val escudo: Escudo?) : Event()
    }

    data class State(
        val escudo: Escudo? = null,
        val escudoActualizado: Escudo? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}