package ies.quevedo.chardat.framework.fragmentAddArmadura

import ies.quevedo.chardat.domain.model.Armadura

interface AddArmaduraContract {

    sealed class Event {
        class PostArmadura(val armadura: Armadura) : Event()
    }

    data class State(
        val armadura: Armadura? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}