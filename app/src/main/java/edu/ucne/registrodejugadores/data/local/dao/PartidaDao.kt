package edu.ucne.registrodejugadores.data.local.dao

import androidx.room.*
import edu.ucne.registrodejugadores.domain.model.Partida
import kotlinx.coroutines.flow.Flow
import edu.ucne.registrodejugadores.data.local.entity.PartidaEntity

@Dao
interface PartidaDao {
    @Query("SELECT * FROM Partida ORDER BY fecha DESC")
    fun getAllPartidas(): Flow<List<PartidaEntity>>

    @Query("SELECT * FROM Partida WHERE partidaId = :id")
    suspend fun getPartidaById(id: Int): PartidaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PartidaEntity): Long

    @Update
    suspend fun update(entity: PartidaEntity)

    @Delete
    suspend fun delete(entity: PartidaEntity)

    @Query("SELECT * FROM Partida WHERE partidaId = :id")
    suspend fun getById(id: Int): PartidaEntity
}