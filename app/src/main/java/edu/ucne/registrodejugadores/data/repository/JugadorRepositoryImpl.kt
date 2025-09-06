package edu.ucne.registrodejugadores.data.repository

import edu.ucne.registrodejugadores.data.local.dao.JugadorDao
import edu.ucne.registrodejugadores.data.mapper.toJugador
import edu.ucne.registrodejugadores.data.mapper.toEntity
import edu.ucne.registrodejugadores.domain.model.Jugador
import edu.ucne.registrodejugadores.domain.repository.JugadorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JugadorRepositoryImpl @Inject constructor(
    private val dao: JugadorDao
) : JugadorRepository {

    override fun getAllJugadores(): Flow<List<Jugador>> {
        return dao.getAllJugadores().map { jugadores ->
            jugadores.map { it.toJugador() }
        }
    }

    override suspend fun getJugadorById(id: Int): Jugador? {
        return dao.getJugadorById(id)?.toJugador()
    }

    override suspend fun getJugadorByNombre(nombres: String): Jugador? {
        return dao.getJugadorByNombre(nombres)?.toJugador()
    }

    override suspend fun insertJugador(jugador: Jugador) {

        val existingJugador = dao.getJugadorByNombre(jugador.nombres)
        if (existingJugador != null) {
            throw IllegalArgumentException("Ya existe un jugador con el nombre: ${jugador.nombres}")
        }
        dao.insertJugador(jugador.toEntity())
    }

    override suspend fun deleteJugador(jugador: Jugador) {
        dao.deleteJugador(jugador.toEntity())
    }

    override suspend fun updateJugador(jugador: Jugador) {
        dao.updateJugador(jugador.toEntity())
    }

    override suspend fun incrementarPartidas(jugadorId: Int) {
        dao.incrementarPartidas(jugadorId)
    }
}