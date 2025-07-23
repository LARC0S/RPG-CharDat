package ies.quevedo.chardat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ies.quevedo.chardat.data.entities.*
import ies.quevedo.chardat.data.utils.Converters

@Database(
    entities = [PersonajeEntity::class,
        ArmaEntity::class,
        ArmaduraEntity::class,
        EscudoEntity::class,
        ObjetoEntity::class],
    version = 13,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class CharDatRoomDatabase : RoomDatabase() {
    abstract fun daoArma(): DAOArma
    abstract fun daoArmadura(): DAOArmadura
    abstract fun daoEscudo(): DAOEscudo
    abstract fun daoObjeto(): DAOObjeto
    abstract fun daoPersonaje(): DAOPersonaje
}