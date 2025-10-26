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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovimientosViewModel @Inject constructor(
    private val getMovimientosUseCase: GetMovimientosUseCase,
    private val postMovimientosUseCase: PostMovimientosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MovimientosState())
    val state: StateFlow<MovimientosState> = _state.asStateFlow()

    fun cargarMovimientos(partidaId: Int) {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {

            when (val apiResult = getMovimientosUseCase.cargarMovimientosDesdeAPI(partidaId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            movimientos = apiResult.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    getMovimientosUseCase(partidaId).collect { localResource ->
                        _state.update {
                            when (localResource) {
                                is Resource.Success -> it.copy(
                                    movimientos = localResource.data ?: emptyList(),
                                    isLoading = false,
                                    error = null
                                )
                                is Resource.Error -> it.copy(
                                    isLoading = false,
                                    error = localResource.message
                                )
                                is Resource.Loading -> it.copy(isLoading = true)
                            }
                        }
                    }
                }
                is Resource.Loading -> {

                }
            }
        }
    }

    fun guardarMovimiento(partidaId: Int, jugador: String, fila: Int, columna: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val movimiento = Movimiento(
                partidaId = partidaId,
                jugador = jugador,
                posicionFila = fila,
                posicionColumna = columna
            )

            when (val result = postMovimientosUseCase(movimiento)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Resource.Loading -> {

                }
            }
        }
    }

    fun limpiarMovimientos() {
        _state.update {
            MovimientosState(
                movimientos = emptyList(),
                isLoading = false,
                error = null
            )
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

data class MovimientosState(
    val movimientos: List<Movimiento> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)