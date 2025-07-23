package ies.quevedo.chardat.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class PersonajeConTodo(
    @Embedded val personajeEntity: PersonajeEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "idPJ"
    )
    val armas: List<ArmaEntity>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "idPJ"
    )
    val armaduras: List<ArmaduraEntity>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "idPJ"
    )
    val escudos: List<EscudoEntity>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "idPJ"
    )
    val objetos: List<ObjetoEntity>?
)