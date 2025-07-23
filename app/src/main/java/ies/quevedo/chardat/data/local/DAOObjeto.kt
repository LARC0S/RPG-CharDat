package ies.quevedo.chardat.data.local

import androidx.room.*
import ies.quevedo.chardat.data.entities.ObjetoEntity

@Dao
interface DAOObjeto {

    @Query("SELECT * FROM objeto WHERE id = :id")
    fun getObjeto(id: Int): ObjetoEntity

    @Query("SELECT * FROM objeto WHERE idPJ = :idPJ")
    fun getObjetos(idPJ: Int): List<ObjetoEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertObjeto(objeto: ObjetoEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(objetos: List<ObjetoEntity>)

    @Update
    fun updateObjeto(objeto: ObjetoEntity)

    @Delete
    fun deleteObjeto(objeto: ObjetoEntity)

    @Delete
    fun deleteAll(objetos: List<ObjetoEntity>)
}