package edu.ucne.registrodejugadores.domain.usecases

import edu.ucne.registrodejugadores.data.remote.Resource
import edu.ucne.registrodejugadores.domain.repository.MovimientosRepository
import javax.inject.Inject

class TriggerSyncUseCase @Inject constructor(
    private val repository: MovimientosRepository
) {
    suspend operator fun invoke(): Resource<Unit> =
        repository.postPendingMovimientos()
}