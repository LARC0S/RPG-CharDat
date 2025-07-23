package ies.quevedo.chardat.framework.fragmentShowPersonaje

import ies.quevedo.chardat.domain.model.Personaje

interface ShowPersonajeContract {

    sealed class Event {
        class FetchPersonaje(val id: Int) : Event()
        class PutPersonaje(val personaje: Personaje?) : Event()
    }

    data class State(
        val personaje: Personaje? = null,
        val personajeActualizado: Personaje? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}