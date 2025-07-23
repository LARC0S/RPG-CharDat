package ies.quevedo.chardat.framework.fragmentShowObjeto

import ies.quevedo.chardat.domain.model.Objeto

interface ShowObjetoContract {

    sealed class Event {
        class FetchObjeto(val idObjeto: Int) : Event()
        class PutObjeto(val objeto: Objeto?) : Event()
    }

    data class State(
        val objeto: Objeto? = null,
        val objetoActualizado: Objeto? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}