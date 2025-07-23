package ies.quevedo.chardat.framework.fragmentListObjetos

import ies.quevedo.chardat.domain.model.Objeto

interface ObjetoListContract {

    sealed class Event {
        class FetchObjetos(val idPersonaje: Int) : Event()
        class PostObjeto(val objeto: Objeto) : Event()
        class DeleteObjeto(val idObjeto: Int) : Event()
    }

    data class State(
        val objetoBorrado: Objeto? = null,
        val objetoRecuperado: Objeto? = null,
        val listaObjetos: List<Objeto>? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}