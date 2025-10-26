package edu.ucne.registrodejugadores.data.local.dao

import androidx.room.*
import edu.ucne.registrodejugadores.data.local.entity.MovimientoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {
    @Query("SELECT * FROM movimientos WHERE partidaId = :partidaId ORDER BY timestamp ASC")
    fun observeMovimientos(partidaId: Int): Flow<List<MovimientoEntity>>

    @Query("SELECT * FROM movimientos WHERE id = :id")
    suspend fun getMovimiento(id: String): MovimientoEntity?

    @Upsert
    suspend fun upsert(movimiento: MovimientoEntity)

    @Query("DELETE FROM movimientos WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM movimientos WHERE isPendingCreate = 1")
    suspend fun getPendingCreateMovimientos(): List<MovimientoEntity>

    @Query("SELECT * FROM movimientos WHERE partidaId = :partidaId AND isPendingCreate = 1")
    suspend fun getPendingCreateMovimientosByPartida(partidaId: Int): List<MovimientoEntity>
}