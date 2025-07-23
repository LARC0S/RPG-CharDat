package ies.quevedo.chardat.framework.fragmentListPersonajes

import ies.quevedo.chardat.domain.model.Personaje

interface PersonajeListContract {

    sealed class Event {
        class FetchPersonaje(val id: Int) : Event()
        object FetchPersonajes : Event()
        class PostPersonaje(val personaje: Personaje) : Event()
        class DeletePersonaje(val id: Int) : Event()
    }

    data class State(
        val personaje: Personaje? = null,
        val personajeBorrado: Personaje? = null,
        val personajeRecuperado: Personaje? = null,
        val listaPersonajes: List<Personaje>? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}