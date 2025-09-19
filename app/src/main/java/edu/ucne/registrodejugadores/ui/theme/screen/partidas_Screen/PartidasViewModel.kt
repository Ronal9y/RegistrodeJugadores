package edu.ucne.registrodejugadores.ui.theme.screen.partidas_Screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrodejugadores.domain.model.Partida
import edu.ucne.registrodejugadores.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartidasViewModel @Inject constructor(
    private val repository: PartidaRepository
) : ViewModel() {

    private val _partidas = MutableStateFlow<List<Partida>>(emptyList())
    val partidas: StateFlow<List<Partida>> = _partidas.asStateFlow()

    private val _uiEvent = MutableStateFlow<PartidaUiEvent?>(null)
    val uiEvent: StateFlow<PartidaUiEvent?> = _uiEvent.asStateFlow()

    private val _lastSavedPartidaId = MutableStateFlow<Int?>(null)
    val lastSavedPartidaId: StateFlow<Int?> = _lastSavedPartidaId.asStateFlow()

    init {
        loadPartidas()
    }

    private fun loadPartidas() {
        viewModelScope.launch {
            repository.getAllPartidas().collect { partidasList ->
                _partidas.value = partidasList
            }
        }
    }

    suspend fun getPartidaById(id: Int): Partida? {
        return repository.getPartidaById(id)
    }

    fun onEvent(event: PartidaEvent) {
        when (event) {
            is PartidaEvent.OnSavePartida -> {
                savePartida(event.partida)
            }
            is PartidaEvent.OnDeletePartida -> {
                deletePartida(event.partida)
            }
            is PartidaEvent.OnFinalizarPartida -> {
                finalizarPartida(event.partida)
            }
            is PartidaEvent.OnEditPartida -> {
                _uiEvent.value = PartidaUiEvent.NavigateToEdit(event.partida)
            }
            PartidaEvent.OnAddPartida -> {
                _uiEvent.value = PartidaUiEvent.NavigateToAdd
            }
        }
    }

    private fun savePartida(partida: Partida) {
        viewModelScope.launch {
            try {
                if (partida.partidaId == 0) {
                    val newId = repository.insertPartida(partida)
                    _lastSavedPartidaId.value = newId.toInt()
                    _uiEvent.value = PartidaUiEvent.ShowMessage("Partida guardada exitosamente")
                } else {
                    repository.updatePartida(partida)
                    _lastSavedPartidaId.value = partida.partidaId
                    _uiEvent.value = PartidaUiEvent.ShowMessage("Partida actualizada exitosamente")
                }
            } catch (e: Exception) {
                _uiEvent.value = PartidaUiEvent.ShowMessage("Error: ${e.message}")
            }
        }
    }

    private fun deletePartida(partida: Partida) {
        viewModelScope.launch {
            try {
                repository.deletePartida(partida)
                _uiEvent.value = PartidaUiEvent.ShowMessage("Partida eliminada exitosamente")
            } catch (e: Exception) {
                _uiEvent.value = PartidaUiEvent.ShowMessage("Error al eliminar: ${e.message}")
            }
        }
    }

    private fun finalizarPartida(partida: Partida) {
        viewModelScope.launch {
            try {
                val partidaFinalizada = partida.copy(esFinalizada = true)
                repository.updatePartida(partidaFinalizada)
                _uiEvent.value = PartidaUiEvent.ShowMessage("Partida finalizada")
            } catch (e: Exception) {
                _uiEvent.value = PartidaUiEvent.ShowMessage("Error al finalizar: ${e.message}")
            }
        }
    }

    fun consumeUiEvent() {
        _uiEvent.value = null
    }
}

sealed class PartidaEvent {
    data class OnSavePartida(val partida: Partida) : PartidaEvent()
    data class OnDeletePartida(val partida: Partida) : PartidaEvent()
    data class OnFinalizarPartida(val partida: Partida) : PartidaEvent()
    data class OnEditPartida(val partida: Partida) : PartidaEvent()
    object OnAddPartida : PartidaEvent()
}

sealed class PartidaUiEvent {
    data class ShowMessage(val message: String) : PartidaUiEvent()
    data class NavigateToEdit(val partida: Partida) : PartidaUiEvent()
    object NavigateToAdd : PartidaUiEvent()
}