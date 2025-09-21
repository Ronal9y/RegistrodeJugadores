package edu.ucne.registrodejugadores.presentation.partidas_Screen

import edu.ucne.registrodejugadores.domain.model.Jugador
import edu.ucne.registrodejugadores.domain.model.Partida

data class PartidaUiState(

    val partidaId: Int = 0,
    val fecha: String = "",
    val jugador1Id: Int = 0,
    val jugador2Id: Int = 0,
    val ganadorId: Int = 0,
    val esFinalizada: Boolean = false,
    val board: String = "",

    val nombreJugador1: String = "",
    val nombreJugador2: String = "",
    val nombreGanador: String = "",


    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,


    val jugadoresDisponibles: List<Jugador> = emptyList(),
    val partidas: List<Partida> = emptyList(),


    val mostrarSoloEnCurso: Boolean = false,
    val searchQuery: String = "",


    val isJugador1Valid: Boolean = true,
    val isJugador2Valid: Boolean = true,
    val areJugadoresDiferentes: Boolean = true
) {

    fun toPartida(): Partida = Partida(
        partidaId = partidaId,
        fecha = if (fecha.isBlank()) Partida.getFechaActual() else fecha,
        jugador1Id = jugador1Id,
        jugador2Id = jugador2Id,
        ganadorId = ganadorId,
        esFinalizada = esFinalizada,
        board = board
    )

    companion object {
        fun fromPartida(partida: Partida, jugadores: List<Jugador> = emptyList()): PartidaUiState {
            val jugador1 = jugadores.find { it.id == partida.jugador1Id }
            val jugador2 = jugadores.find { it.id == partida.jugador2Id }
            val ganador = jugadores.find { it.id == partida.ganadorId }

            return PartidaUiState(
                partidaId = partida.partidaId,
                fecha = partida.fecha,
                jugador1Id = partida.jugador1Id,
                jugador2Id = partida.jugador2Id,
                ganadorId = partida.ganadorId,
                esFinalizada = partida.esFinalizada,
                board = partida.board,
                nombreJugador1 = jugador1?.nombres ?: "Jugador ${partida.jugador1Id}",
                nombreJugador2 = jugador2?.nombres ?: "Jugador ${partida.jugador2Id}",
                nombreGanador = ganador?.nombres ?: if (partida.ganadorId == 0) "Empate" else "Jugador ${partida.ganadorId}"
            )
        }


        fun empty(): PartidaUiState = PartidaUiState(
            fecha = Partida.getFechaActual()
        )
    }
}