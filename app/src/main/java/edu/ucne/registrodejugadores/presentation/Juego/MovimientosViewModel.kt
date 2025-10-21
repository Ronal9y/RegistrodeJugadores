package edu.ucne.registrodejugadores.presentation.Juego

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrodejugadores.data.remote.Resource
import edu.ucne.registrodejugadores.domain.model.Movimiento
import edu.ucne.registrodejugadores.domain.usecases.GetMovimientosUseCase
import edu.ucne.registrodejugadores.domain.usecases.PostMovimientosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovimientosViewModel @Inject constructor(
    private val getMovimientosUseCase: GetMovimientosUseCase,
    private val postMovimientosUseCase: PostMovimientosUseCase
) : ViewModel() {

    private val _movimientos = MutableStateFlow<Resource<List<Movimiento>>?>(null)
    val movimientos: StateFlow<Resource<List<Movimiento>>?> = _movimientos

    fun cargarMovimientos(partidaId: Int) {
        viewModelScope.launch {
            getMovimientosUseCase(partidaId).collect { resource ->
                _movimientos.value = resource
            }
        }
    }

    suspend fun guardarMovimiento(partidaId: Int, jugador: String, fila: Int, columna: Int) {
        val movimiento = Movimiento(
            partidaId = partidaId,
            jugador = jugador,
            posicionFila = fila,
            posicionColumna = columna
        )
        postMovimientosUseCase(movimiento)
    }

    fun limpiarMovimientos() {
        _movimientos.value = null
    }
}