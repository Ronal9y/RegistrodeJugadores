package edu.ucne.registrodejugadores.domain.usecases

import edu.ucne.registrodejugadores.domain.model.Movimiento
import edu.ucne.registrodejugadores.domain.repository.MovimientosRepository
import javax.inject.Inject

class PostMovimientosUseCase @Inject constructor(
    private val repository: MovimientosRepository
){
    suspend operator fun invoke(movimiento: Movimiento) =
        repository.postMovimiento(movimiento)
}