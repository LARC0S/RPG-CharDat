package ies.quevedo.chardat.framework.fragmentListArmaduras

import ies.quevedo.chardat.domain.model.Armadura

interface ArmaduraListContract {

    sealed class Event {
        class FetchArmaduras(val idPersonaje: Int) : Event()
        class PostArmadura(val armadura: Armadura) : Event()
        class DeleteArmadura(val idArmadura: Int) : Event()
    }

    data class State(
        val armaduraBorrada: Armadura? = null,
        val armaduraRecuperada: Armadura? = null,
        val listaArmaduras: List<Armadura>? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}