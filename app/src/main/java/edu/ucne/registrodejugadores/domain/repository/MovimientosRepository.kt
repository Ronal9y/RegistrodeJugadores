package edu.ucne.registrodejugadores.domain.repository

import edu.ucne.registrodejugadores.data.remote.Resource
import edu.ucne.registrodejugadores.domain.model.Movimiento
import kotlinx.coroutines.flow.Flow

interface MovimientosRepository {
    fun getMovimientos(partidaId: Int): Flow<Resource<List<Movimiento>>>
    suspend fun createMovimientoLocal(movimiento: Movimiento): Resource<Movimiento>
    suspend fun postMovimiento(movimiento: Movimiento): Resource<Unit>
    suspend fun deleteMovimiento(id: String): Resource<Unit>
    suspend fun postPendingMovimientos(): Resource<Unit>
    suspend fun cargarMovimientosDesdeAPI(partidaId: Int): Resource<List<Movimiento>>
}