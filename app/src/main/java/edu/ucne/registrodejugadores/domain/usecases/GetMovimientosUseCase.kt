package edu.ucne.registrodejugadores.domain.usecases

import edu.ucne.registrodejugadores.domain.repository.MovimientosRepository
import javax.inject.Inject

class GetMovimientosUseCase @Inject constructor(
    private val repository: MovimientosRepository
) {
    operator fun invoke(partidaId: Int) =
        repository.getMovimientos(partidaId)
}