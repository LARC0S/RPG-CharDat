package ies.quevedo.chardat.data.local

import androidx.room.*
import ies.quevedo.chardat.data.entities.*

@Dao
interface DAOPersonaje {

    @Transaction
    @Query("SELECT * FROM personaje WHERE id = :id")
    fun getPersonaje(id: Int): PersonajeConTodo?

    @Transaction
    @Query("SELECT * FROM personaje")
    fun getPersonajes(): List<PersonajeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPersonaje(personaje: PersonajeEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(personajes: List<PersonajeEntity>)

    @Update
    fun updatePersonaje(personaje: PersonajeEntity)

    @Delete
    fun deletePersonaje(
        personaje: PersonajeEntity,
        armaduras: List<ArmaduraEntity>,
        armas: List<ArmaEntity>,
        escudos: List<EscudoEntity>,
        objetos: List<ObjetoEntity>
    )

    @Delete
    fun deleteAll(personajes: List<PersonajeEntity>)
}