// data/di/AppModule.kt
package edu.ucne.registrodejugadores.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrodejugadores.data.local.Jugadores
import edu.ucne.registrodejugadores.data.local.dao.JugadorDao
import edu.ucne.registrodejugadores.data.local.dao.PartidaDao
import edu.ucne.registrodejugadores.data.repository.JugadorRepositoryImpl
import edu.ucne.registrodejugadores.data.repository.PartidaRepositoryImpl
import edu.ucne.registrodejugadores.domain.repository.JugadorRepository
import edu.ucne.registrodejugadores.domain.repository.PartidaRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJugadorDatabase(@ApplicationContext context: Context): Jugadores {
        return Room.databaseBuilder(
            context,
            Jugadores::class.java,
            "jugadoresDb"
        )
            .fallbackToDestructiveMigration() // ✅ Importante para cambios de versión
            .build()
    }

    @Provides
    @Singleton
    fun provideJugadorDao(database: Jugadores): JugadorDao {
        return database.jugadorDao()
    }

    @Provides
    @Singleton
    fun providePartidaDao(database: Jugadores): PartidaDao {
        return database.partidaDao()
    }

    @Provides
    @Singleton
    fun provideJugadorRepository(dao: JugadorDao): JugadorRepository {
        return JugadorRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun providePartidaRepository(dao: PartidaDao): PartidaRepository {
        return PartidaRepositoryImpl(dao)
    }
}