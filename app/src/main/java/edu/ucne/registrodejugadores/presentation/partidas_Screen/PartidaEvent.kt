package edu.ucne.registrodejugadores.presentation.partidas_Screen

import edu.ucne.registrodejugadores.domain.model.Partida

sealed interface PartidaEvent {
    data class OnSavePartida(val partida: Partida) : PartidaEvent
    data class OnDeletePartida(val partida: Partida) : PartidaEvent
    data class OnFinalizarPartida(val partida: Partida) : PartidaEvent
    data class OnEditPartida(val partida: Partida) : PartidaEvent
    object OnAddPartida : PartidaEvent

    data class OnJugador1Change(val jugador1Id: Int?) : PartidaEvent
    data class OnJugador2Change(val jugador2Id: Int?) : PartidaEvent
    data class OnGanadorChange(val ganadorId: Int?) : PartidaEvent
    data class OnBoardChange(val board: String) : PartidaEvent
    data class OnFechaChange(val fecha: String) : PartidaEvent

    object Save : PartidaEvent
    object New : PartidaEvent
    data class LoadPartida(val partidaId: Int) : PartidaEvent
}