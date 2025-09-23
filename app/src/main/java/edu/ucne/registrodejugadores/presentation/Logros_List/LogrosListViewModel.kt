package edu.ucne.registrodejugadores.presentation.Logros_List

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrodejugadores.domain.model.Logro
import edu.ucne.registrodejugadores.domain.repository.LogroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogrosListViewModel @Inject constructor(
    private val repository: LogroRepository
) : ViewModel() {

    val logros: StateFlow<List<Logro>> = repository.getAllLogros()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        insertarLogrosPredeterminados()
    }

    private fun insertarLogrosPredeterminados() = viewModelScope.launch {
        val count = logros.value.size
        if (count > 0) return@launch

        val predeterminados = listOf(
            Logro(1, "Racha ganadora", "Gana 10 partidas seguidas"),
            Logro(2, "Maestro del empate", "Evita perder en 10 juegos consecutivos"),
            Logro(3, "Maestro del Tic Tac Toe", "Gana un juego sin usar la esquina"),
            Logro(4, "Derrota sin paliativos", "Pierde la partida"),
            Logro(5, "Juego en el centro", "Empieza la partida colocando tu primer movimiento en el centro"),
            Logro(6, "Juego cerrado", "El juego termina en empate"),
            Logro(7, "Furia del primer movimiento", "Coloca tu primera 'X' en una esquina")
        )

        predeterminados.forEach { repository.insertLogro(it) }
    }
}