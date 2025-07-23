package ies.quevedo.chardat.data.local

import androidx.room.*
import ies.quevedo.chardat.data.entities.ArmaduraEntity

@Dao
interface DAOArmadura {

    @Query("SELECT * FROM armadura WHERE id = :id")
    fun getArmadura(id: Int): ArmaduraEntity

    @Query("SELECT * FROM armadura WHERE idPJ = :idPJ")
    fun getArmaduras(idPJ: Int): List<ArmaduraEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertArmadura(armadura: ArmaduraEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(armaduras: List<ArmaduraEntity>)

    @Update
    fun updateArmadura(armadura: ArmaduraEntity)

    @Delete
    fun deleteArmadura(armadura: ArmaduraEntity)

    @Delete
    fun deleteAll(armaduras: List<ArmaduraEntity>)
}