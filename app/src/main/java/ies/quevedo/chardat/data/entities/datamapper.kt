package ies.quevedo.chardat.data.entities

import ies.quevedo.chardat.domain.model.*
import java.util.*

fun PersonajeConTodo.toPersonaje(): Personaje {
    return Personaje(
        id = this.personajeEntity.id,
        name = this.personajeEntity.name,
        clase = this.personajeEntity.clase,
        level = this.personajeEntity.level,
        description = this.personajeEntity.description,
        currentHP = this.personajeEntity.currentHP,
        totalHP = this.personajeEntity.totalHP,
        currentStamina = this.personajeEntity.currentStamina,
        totalStamina = this.personajeEntity.totalStamina,
        attackHability = this.personajeEntity.attackHability,
        dodge = this.personajeEntity.dodge,
        parryHability = this.personajeEntity.parryHability,
        armor = this.personajeEntity.armor,
        turn = this.personajeEntity.turn,
        agility = this.personajeEntity.agility,
        constitution = this.personajeEntity.constitution,
        dexterity = this.personajeEntity.dexterity,
        strength = this.personajeEntity.strength,
        intelligence = this.personajeEntity.intelligence,
        perception = this.personajeEntity.perception,
        power = this.personajeEntity.power,
        will = this.personajeEntity.will,
        RF = this.personajeEntity.RF,
        RM = this.personajeEntity.RM,
        RP = this.personajeEntity.RP,
        creationDate = this.personajeEntity.creationDate,
        armas = this.armas?.map { it.toArma() },
        armaduras = this.armaduras?.map { it.toArmadura() },
        escudos = this.escudos?.map { it.toEscudo() },
        objetos = this.objetos?.map { it.toObjeto() }
    )
}

fun Personaje.toPersonajeConTodo(): PersonajeConTodo {
    return PersonajeConTodo(
        personajeEntity = this.toPersonajeEntity(),
        armas = this.armas?.map { it.toArmaEntity(this.id) },
        armaduras = this.armaduras?.map { it.toArmaduraEntity(this.id) },
        escudos = this.escudos?.map { it.toEscudoEntity(this.id) },
        objetos = this.objetos?.map { it.toObjetoEntity(this.id) }
    )
}

fun PersonajeEntity.toPersonaje(): Personaje {
    return Personaje(
        id = this.id,
        name = this.name,
        level = this.level,
        clase = this.clase,
        description = this.description,
        currentHP = this.currentHP,
        totalHP = this.totalHP,
        currentStamina = this.currentStamina,
        totalStamina = this.totalStamina,
        attackHability = this.attackHability,
        dodge = this.dodge,
        parryHability = this.parryHability,
        armor = this.armor,
        turn = this.turn,
        agility = this.agility,
        constitution = this.constitution,
        dexterity = this.dexterity,
        strength = this.strength,
        intelligence = this.intelligence,
        perception = this.perception,
        power = this.power,
        will = this.will,
        RF = this.RF,
        RM = this.RM,
        RP = this.RP,
        creationDate = this.creationDate,
        armas = Collections.emptyList(),
        armaduras = Collections.emptyList(),
        escudos = Collections.emptyList(),
        objetos = Collections.emptyList()
    )
}

fun ArmaEntity.toArma(): Arma {
    return Arma(
        id = this.id,
        name = this.name,
        value = this.value,
        weight = this.weight,
        quality = this.quality,
        turn = this.turn,
        attackHability = this.attackHability,
        damage = this.damage,
        parry = this.parry,
        description = this.description,
        idPJ = this.idPJ
    )
}

fun ArmaduraEntity.toArmadura(): Armadura {
    return Armadura(
        id = this.id,
        name = this.name,
        value = this.value,
        weight = this.weight,
        quality = this.quality,
        armor = this.armor,
        fil = this.fil,
        con = this.con,
        pen = this.pen,
        cal = this.cal,
        ele = this.ele,
        fri = this.fri,
        ene = this.ene,
        description = this.description,
        idPJ = this.idPJ
    )
}

fun EscudoEntity.toEscudo(): Escudo {
    return Escudo(
        id = this.id,
        name = this.name,
        value = this.value,
        weight = this.weight,
        quality = this.quality,
        attackHability = this.attackHability,
        damage = this.damage,
        parry = this.parry,
        description = this.description,
        idPJ = this.idPJ
    )
}

fun ObjetoEntity.toObjeto(): Objeto {
    return Objeto(
        id = this.id,
        name = this.name,
        value = this.value,
        weight = this.weight,
        amount = this.amount,
        description = this.description,
        idPJ = this.idPJ
    )
}

fun Personaje.toPersonajeEntity(): PersonajeEntity {
    return PersonajeEntity(
        id = this.id,
        name = this.name,
        clase = this.clase,
        level = this.level,
        description = this.description,
        currentHP = this.currentHP,
        totalHP = this.totalHP,
        currentStamina = this.currentStamina,
        totalStamina = this.totalStamina,
        attackHability = this.attackHability,
        dodge = this.dodge,
        parryHability = this.parryHability,
        armor = this.armor,
        turn = this.turn,
        agility = this.agility,
        constitution = this.constitution,
        dexterity = this.dexterity,
        strength = this.strength,
        intelligence = this.intelligence,
        perception = this.perception,
        power = this.power,
        will = this.will,
        RF = this.RF,
        RM = this.RM,
        RP = this.RP,
        creationDate = this.creationDate
    )
}

fun Arma.toArmaEntity(idPJ: Int): ArmaEntity {
    return ArmaEntity(
        id = this.id,
        name = this.name,
        value = this.value,
        weight = this.weight,
        quality = this.quality,
        turn = this.turn,
        attackHability = this.attackHability,
        damage = this.damage,
        parry = this.parry,
        description = this.description,
        idPJ = idPJ
    )
}

fun Armadura.toArmaduraEntity(idPJ: Int): ArmaduraEntity {
    return ArmaduraEntity(
        id = this.id,
        name = this.name,
        value = this.value,
        weight = this.weight,
        quality = this.quality,
        armor = this.armor,
        fil = this.fil,
        con = this.con,
        pen = this.pen,
        cal = this.cal,
        ele = this.ele,
        fri = this.fri,
        ene = this.ene,
        description = this.description,
        idPJ = idPJ
    )
}

fun Escudo.toEscudoEntity(idPJ: Int): EscudoEntity {
    return EscudoEntity(
        id = this.id,
        name = this.name,
        value = this.value,
        weight = this.weight,
        quality = this.quality,
        attackHability = this.attackHability,
        damage = this.damage,
        parry = this.parry,
        description = this.description,
        idPJ = idPJ
    )
}

fun Objeto.toObjetoEntity(idPJ: Int): ObjetoEntity {
    return ObjetoEntity(
        id = this.id,
        name = this.name,
        value = this.value,
        weight = this.weight,
        amount = this.amount,
        description = this.description,
        idPJ = idPJ
    )
}