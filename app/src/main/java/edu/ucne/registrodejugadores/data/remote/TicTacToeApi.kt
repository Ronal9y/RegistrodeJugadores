package edu.ucne.registrodejugadores.data.remote

import edu.ucne.registrodejugadores.data.remote.dto.movimientosDto
import edu.ucne.registrodejugadores.data.remote.dto.PartidaDto
import edu.ucne.registrodejugadores.data.remote.dto.PostMovimientoDto
import edu.ucne.registrodejugadores.data.remote.dto.PostPartidaDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TicTacToeApi {

    // MOVIMIENTOS
    @GET("api/Movimientos/{partidaId}")
    suspend fun getMovimientos(@Path("partidaId") partidaId: Int): List<movimientosDto>

    @POST("api/Movimientos")
    suspend fun postMovimiento(@Body movimiento: PostMovimientoDto)

    // PARTIDAS
    @GET("api/Partidas/{partidaId}")
    suspend fun getPartida(@Path("partidaId") partidaId: Int): PartidaDto

    @GET("api/Partidas")
    suspend fun getPartidas(): List<PartidaDto>

    @POST("api/Partidas")
    suspend fun postPartida(@Body partida: PostPartidaDto): PartidaDto
}