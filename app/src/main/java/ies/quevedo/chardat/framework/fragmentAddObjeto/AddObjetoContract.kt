package ies.quevedo.chardat.framework.fragmentAddObjeto

import ies.quevedo.chardat.domain.model.Objeto

interface AddObjetoContract {

    sealed class Event {
        class PostObjeto(val objeto: Objeto) : Event()
    }

    data class State(
        val objeto: Objeto? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}