package edu.ucne.registrodejugadores.data.remote

import edu.ucne.registrodejugadores.data.remote.dto.movimientosDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TicTacToeApi {
    @GET("api/Movimientos/{partidaId}")
    suspend fun getMovimientos(@Path("partidaId") id: Int): List<movimientosDto>

    @POST("api/Movimientos")
    suspend fun postMovimiento(@Body movimiento: movimientosDto)
}