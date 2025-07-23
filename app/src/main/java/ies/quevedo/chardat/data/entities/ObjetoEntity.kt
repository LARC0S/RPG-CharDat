package ies.quevedo.chardat.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "objeto",
    foreignKeys = [
        ForeignKey(
            entity = PersonajeEntity::class,
            parentColumns = ["id"],
            childColumns = ["idPJ"]
        )
    ]
)
data class ObjetoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val value: Int,
    val weight: Double,
    val amount: Int,
    val description: String,
    val idPJ: Int
)
