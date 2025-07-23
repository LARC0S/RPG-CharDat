package ies.quevedo.chardat.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "armadura",
    foreignKeys = [
        ForeignKey(
            entity = PersonajeEntity::class,
            parentColumns = ["id"],
            childColumns = ["idPJ"]
        )
    ]
)
data class ArmaduraEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val value: Int,
    val weight: Double,
    val quality: Int,
    val armor: Int,
    val fil: Int,
    val con: Int,
    val pen: Int,
    val cal: Int,
    val ele: Int,
    val fri: Int,
    val ene: Int,
    val description: String,
    val idPJ: Int
)
