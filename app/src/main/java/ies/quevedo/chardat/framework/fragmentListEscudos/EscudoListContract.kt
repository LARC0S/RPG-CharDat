package ies.quevedo.chardat.framework.fragmentListEscudos

import ies.quevedo.chardat.domain.model.Escudo

interface EscudoListContract {

    sealed class Event {
        class FetchEscudos(val idPersonaje: Int) : Event()
        class PostEscudo(val escudo: Escudo) : Event()
        class DeleteEscudo(val idEscudo: Int) : Event()
    }

    data class State(
        val escudoBorrado: Escudo? = null,
        val escudoRecuperado: Escudo? = null,
        val listaEscudos: List<Escudo>? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}