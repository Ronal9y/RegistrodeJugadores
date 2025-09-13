package edu.ucne.registrodejugadores.domain.repository

import edu.ucne.registrodejugadores.domain.model.Partida
import kotlinx.coroutines.flow.Flow

interface PartidaRepository {
    fun getAllPartidas(): Flow<List<Partida>>
    suspend fun getPartidaById(id: Int): Partida?
    suspend fun insertPartida(partida: Partida)
    suspend fun updatePartida(partida: Partida)
    suspend fun deletePartida(partida: Partida)
}