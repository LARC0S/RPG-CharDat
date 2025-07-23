package ies.quevedo.chardat.data.hilt

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ies.quevedo.chardat.data.local.*
import ies.quevedo.chardat.data.utils.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, CharDatRoomDatabase::class.java, Constants.DB_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun providesDAOArma(articlesDatabase: CharDatRoomDatabase): DAOArma {
        return articlesDatabase.daoArma()
    }

    @Provides
    fun providesDAOArmadura(articlesDatabase: CharDatRoomDatabase): DAOArmadura {
        return articlesDatabase.daoArmadura()
    }

    @Provides
    fun providesDAOEscudo(articlesDatabase: CharDatRoomDatabase): DAOEscudo {
        return articlesDatabase.daoEscudo()
    }

    @Provides
    fun providesDAOObjeto(articlesDatabase: CharDatRoomDatabase): DAOObjeto {
        return articlesDatabase.daoObjeto()
    }

    @Provides
    fun providesDAOPersonaje(articlesDatabase: CharDatRoomDatabase): DAOPersonaje {
        return articlesDatabase.daoPersonaje()
    }
}