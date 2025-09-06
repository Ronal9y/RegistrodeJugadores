package edu.ucne.registrodejugadores.ui.theme.screen.jugador_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrodejugadores.domain.model.Jugador
import edu.ucne.registrodejugadores.domain.repository.JugadorRepository
import edu.ucne.registrodejugadores.ui.theme.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JugadorFormViewModel @Inject constructor(
    private val repository: JugadorRepository
) : ViewModel() {

    private val _nombres = MutableStateFlow("")
    val nombres: StateFlow<String> = _nombres

    private val _uiEvent = MutableStateFlow<UiEvent?>(null)
    val uiEvent: StateFlow<UiEvent?> = _uiEvent

    fun onEvent(event: JugadorFormEvent) {
        when (event) {
            is JugadorFormEvent.OnNombresChange -> {
                _nombres.value = event.nombres
            }
            is JugadorFormEvent.OnSaveJugador -> {
                saveJugador()
            }
        }
    }

    private fun saveJugador() {
        viewModelScope.launch {
            if (_nombres.value.isBlank()) {
                _uiEvent.value = UiEvent.ShowMessage("El nombre es obligatorio")
                return@launch
            }

            try {
                val jugador = Jugador(
                    nombres = _nombres.value.trim(),
                    partidas = 0
                )
                repository.insertJugador(jugador)
                _uiEvent.value = UiEvent.NavigateBack
            } catch (e: IllegalArgumentException) {
                _uiEvent.value = UiEvent.ShowMessage(e.message ?: "Error al guardar jugador")
            } catch (e: Exception) {
                _uiEvent.value = UiEvent.ShowMessage("Error al guardar jugador: ${e.message}")
            }
        }
    }
}

sealed class JugadorFormEvent {
    data class OnNombresChange(val nombres: String) : JugadorFormEvent()
    object OnSaveJugador : JugadorFormEvent()
}