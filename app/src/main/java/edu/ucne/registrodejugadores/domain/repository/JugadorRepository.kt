package edu.ucne.registrodejugadores.domain.repository

import edu.ucne.registrodejugadores.domain.model.Jugador
import kotlinx.coroutines.flow.Flow

interface JugadorRepository {
    fun getAllJugadores(): Flow<List<Jugador>>
    suspend fun getJugadorById(id: Int): Jugador?
    suspend fun getJugadorByNombre(nombres: String): Jugador?
    suspend fun insertJugador(jugador: Jugador)
    suspend fun deleteJugador(jugador: Jugador)
    suspend fun updateJugador(jugador: Jugador)
    suspend fun incrementarPartidas(jugadorId: Int)
}