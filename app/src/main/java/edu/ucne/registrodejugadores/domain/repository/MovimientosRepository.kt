package edu.ucne.registrodejugadores.domain.repository

import edu.ucne.registrodejugadores.data.remote.Resource
import edu.ucne.registrodejugadores.domain.model.Movimiento
import kotlinx.coroutines.flow.Flow

interface MovimientosRepository {
    fun getMovimientos(partidaId: Int): Flow<Resource<List<Movimiento>>>
    suspend fun postMovimiento(movimiento: Movimiento): Resource<Unit>
}