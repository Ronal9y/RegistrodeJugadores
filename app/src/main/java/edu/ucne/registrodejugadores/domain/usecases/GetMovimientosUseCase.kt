package edu.ucne.registrodejugadores.domain.usecases

import edu.ucne.registrodejugadores.data.remote.Resource
import edu.ucne.registrodejugadores.domain.model.Movimiento
import edu.ucne.registrodejugadores.domain.repository.MovimientosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovimientosUseCase @Inject constructor(
    private val repository: MovimientosRepository
) {
    operator fun invoke(partidaId: Int): Flow<Resource<List<Movimiento>>> =
        repository.getMovimientos(partidaId)


    suspend fun cargarMovimientosDesdeAPI(partidaId: Int): Resource<List<Movimiento>> =
        repository.cargarMovimientosDesdeAPI(partidaId)
}