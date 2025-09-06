package edu.ucne.registrodejugadores.ui.theme.util

sealed class UiEvent {
    data class ShowMessage(val message: String) : UiEvent()
    object NavigateBack : UiEvent()
    data class NavigateTo(val route: String) : UiEvent()
}