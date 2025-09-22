package edu.ucne.registrodejugadores.presentation.partidas_Screen

import edu.ucne.registrodejugadores.domain.model.Partida

sealed class PartidaUiEvent {
    data class ShowMessage(val message: String) : PartidaUiEvent()
    data class NavigateToEdit(val partida: Partida) : PartidaUiEvent()
    data class NavigateToGame(val partidaId: Int) : PartidaUiEvent()
    object NavigateToAdd : PartidaUiEvent()
    object NavigateBack : PartidaUiEvent()
    data class ShowError(val error: String) : PartidaUiEvent()
    data class ShowSuccess(val message: String) : PartidaUiEvent()
}