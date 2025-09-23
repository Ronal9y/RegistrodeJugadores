package edu.ucne.registrodejugadores.domain.repository

import edu.ucne.registrodejugadores.domain.model.Logro
import kotlinx.coroutines.flow.Flow

interface LogroRepository {
    fun getAllLogros(): Flow<List<Logro>>


    suspend fun getLogroById(id: Int): Logro?


    suspend fun insertLogro(logro: Logro)


    suspend fun updateLogro(logro: Logro)


    suspend fun deleteLogro(logro: Logro)


    suspend fun deleteLogroById(id: Int)
}