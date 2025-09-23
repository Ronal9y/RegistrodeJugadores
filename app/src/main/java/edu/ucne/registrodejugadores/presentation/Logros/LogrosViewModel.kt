package edu.ucne.registrodejugadores.presentation.Logros

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrodejugadores.domain.model.Logro
import edu.ucne.registrodejugadores.domain.repository.LogroRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogrosViewModel @Inject constructor(
    private val repository: LogroRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(LogrosState())
    val state: StateFlow<LogrosState> = _state.asStateFlow()

    // Lista de logros expuesta como StateFlow
    val logros: StateFlow<List<Logro>> = repository.getAllLogros().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _event = Channel<String>()
    val event = _event.receiveAsFlow()

    private fun sendEvent(message: String) {
        viewModelScope.launch {
            _event.send(message)
        }
    }

    fun onEvent(event: LogrosEvent) {
        when (event) {
            is LogrosEvent.LogroNombreChanged -> {
                _state.value = _state.value.copy(titulo = event.value)
            }
            is LogrosEvent.DescripcionChanged -> {
                _state.value = _state.value.copy(descripcion = event.value)
            }
            is LogrosEvent.Save -> {
                insertarLogro(event.logro)
            }
            is LogrosEvent.Delete -> {
                eliminarLogro(event.logro)
            }
            is LogrosEvent.Select -> {
                _state.value = _state.value.copy(
                    logroId = event.logro.logroId,
                    titulo = event.logro.titulo,
                    descripcion = event.logro.descripcion
                )
            }
        }
    }

    private fun insertarLogro(logro: Logro) {
        viewModelScope.launch {
            repository.getAllLogros().collect { lista ->
                if (lista.any { it.titulo.equals(logro.titulo, ignoreCase = true) }) {
                    sendEvent("No se puede agregar un logro con el mismo nombre")
                } else {
                    repository.insertLogro(logro)
                }
            }
        }
    }

    private fun eliminarLogro(logro: Logro) {
        viewModelScope.launch {
            repository.deleteLogro(logro)
        }
    }
}