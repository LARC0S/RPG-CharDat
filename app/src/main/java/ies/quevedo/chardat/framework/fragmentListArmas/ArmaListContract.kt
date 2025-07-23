package ies.quevedo.chardat.framework.fragmentListArmas

import ies.quevedo.chardat.domain.model.Arma

interface ArmaListContract {

    sealed class Event {
        class FetchArmas(val idPersonaje: Int) : Event()
        class PostArma(val arma: Arma) : Event()
        class DeleteArma(val idArma: Int) : Event()
    }

    data class State(
        val armaBorrada: Arma? = null,
        val armaRecuperada: Arma? = null,
        val listaArmas: List<Arma>? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}