package edu.ucne.registrodejugadores.data.remote

import edu.ucne.registrodejugadores.data.remote.dto.movimientosDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: TicTacToeApi
) {
    suspend fun getMovimientos(partidaId: Int): List<movimientosDto> =
        api.getMovimientos(partidaId)

    suspend fun postMovimiento(movimiento: movimientosDto) =
        api.postMovimiento(movimiento)
}