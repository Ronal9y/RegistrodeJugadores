package edu.ucne.registrodejugadores.data.local.dao

import androidx.room.*
import edu.ucne.registrodejugadores.data.local.entity.LogroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogroDao {
    @Query("SELECT * FROM logros ORDER BY logroId DESC")
    fun observedAll(): Flow<List<LogroEntity>>

    @Query("SELECT * FROM logros WHERE logroId = :id")
    suspend fun getById(id: Int): LogroEntity?

    @Upsert
    suspend fun upsert(entity: LogroEntity)

    @Delete
    suspend fun delete(entity: LogroEntity)

    @Query("DELETE FROM logros WHERE logroId = :id")
    suspend fun deleteById(id: Int)
}