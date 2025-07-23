package ies.quevedo.chardat.framework.fragmentShowArmadura

import ies.quevedo.chardat.domain.model.Armadura

interface ShowArmaduraContract {

    sealed class Event {
        class FetchArmadura(val idArmadura: Int) : Event()
        class PutArmadura(val armadura: Armadura?) : Event()
    }

    data class State(
        val armadura: Armadura? = null,
        val armaduraActualizada: Armadura? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}