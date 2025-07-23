package ies.quevedo.chardat.data.local

import androidx.room.*
import ies.quevedo.chardat.data.entities.ArmaEntity

@Dao
interface DAOArma {

    @Query("SELECT * FROM arma WHERE id = :id")
    fun getArma(id: Int): ArmaEntity

    @Query("SELECT * FROM arma WHERE idPJ = :idPJ")
    fun getArmas(idPJ: Int): List<ArmaEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertArma(arma: ArmaEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(armas: List<ArmaEntity>)

    @Update
    fun updateArma(arma: ArmaEntity)

    @Delete
    fun deleteArma(arma: ArmaEntity)

    @Delete
    fun deleteAll(armas: List<ArmaEntity>)
}