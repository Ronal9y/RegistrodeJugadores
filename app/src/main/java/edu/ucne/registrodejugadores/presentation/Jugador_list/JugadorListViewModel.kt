package edu.ucne.registrodejugadores.presentation.jugador_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrodejugadores.domain.model.Jugador
import edu.ucne.registrodejugadores.domain.repository.JugadorRepository
import edu.ucne.registrodejugadores.ui.theme.util.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JugadorListViewModel @Inject constructor(
    private val repository: JugadorRepository
) : ViewModel() {

    private val _jugadores = MutableStateFlow<List<Jugador>>(emptyList())
    val jugadores: StateFlow<List<Jugador>> = _jugadores

    private val _uiEvent = MutableStateFlow<UiEvent?>(null)
    val uiEvent: StateFlow<UiEvent?> = _uiEvent

    init {
        loadJugadores()
    }

    private fun loadJugadores() {
        viewModelScope.launch {
            repository.getAllJugadores().collectLatest { jugadoresList ->
                _jugadores.value = jugadoresList
            }
        }
    }

    fun onEvent(event: JugadorListEvent) {
        when (event) {
            is JugadorListEvent.OnDeleteJugador -> {
                deleteJugador(event.jugador)
            }
            is JugadorListEvent.OnAddJugador -> {
                _uiEvent.value = UiEvent.NavigateTo("jugador_form")
            }
            is JugadorListEvent.OnSelectJugador -> {
                _uiEvent.value = UiEvent.ShowMessage("Jugador seleccionado: ${event.jugador.nombres}")
            }
        }
    }

    fun consumeUiEvent() {
        _uiEvent.value = null
    }

    private fun deleteJugador(jugador: Jugador) {
        viewModelScope.launch {
            try {
                repository.deleteJugador(jugador)
                _uiEvent.value = UiEvent.ShowMessage("Jugador eliminado exitosamente")
            } catch (e: Exception) {
                _uiEvent.value = UiEvent.ShowMessage("Error al eliminar jugador: ${e.message}")
            }
        }
    }
}

sealed class JugadorListEvent {
    data class OnDeleteJugador(val jugador: Jugador) : JugadorListEvent()
    data class OnSelectJugador(val jugador: Jugador) : JugadorListEvent()
    object OnAddJugador : JugadorListEvent()
}