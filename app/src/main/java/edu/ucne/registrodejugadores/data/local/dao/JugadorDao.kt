package edu.ucne.registrodejugadores.data.local.dao

import androidx.room.*
import edu.ucne.registrodejugadores.data.local.entity.JugadorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JugadorDao {
    @Query("SELECT * FROM Jugadores ORDER BY nombres")
    fun getAllJugadores(): Flow<List<JugadorEntity>>

    @Query("SELECT * FROM Jugadores WHERE jugadorId = :id")
    suspend fun getJugadorById(id: Int): JugadorEntity?

    @Query("SELECT * FROM Jugadores WHERE nombres = :nombres")
    suspend fun getJugadorByNombre(nombres: String): JugadorEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertJugador(jugador: JugadorEntity)

    @Delete
    suspend fun deleteJugador(jugador: JugadorEntity)

    @Update
    suspend fun updateJugador(jugador: JugadorEntity)

    @Query("UPDATE Jugadores SET partidas = partidas + 1 WHERE jugadorId = :jugadorId")
    suspend fun incrementarPartidas(jugadorId: Int)
}
