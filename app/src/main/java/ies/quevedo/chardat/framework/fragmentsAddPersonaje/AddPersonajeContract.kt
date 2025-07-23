package ies.quevedo.chardat.framework.fragmentsAddPersonaje

import ies.quevedo.chardat.domain.model.Personaje

interface AddPersonajeContract {

    sealed class Event {
        class PostPersonaje(val personaje: Personaje) : Event()
    }

    data class State(
        var personaje: Personaje? = null,
        var isLoading: Boolean = false,
        val error: String? = null
    )
}