package ies.quevedo.chardat.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personaje")
data class PersonajeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var clase: String,
    var level: Int,
    var description: String,
    var currentHP: Int,
    var totalHP: Int,
    var currentStamina: Int,
    var totalStamina: Int,
    var attackHability: Int,
    var dodge: Int,
    var parryHability: Int,
    var armor: Int,
    var turn: Int,
    var agility: Int,
    var constitution: Int,
    var dexterity: Int,
    var strength: Int,
    var intelligence: Int,
    var perception: Int,
    var power: Int,
    var will: Int,
    var RF: Int,
    var RM: Int,
    var RP: Int,
    var creationDate: String,
)