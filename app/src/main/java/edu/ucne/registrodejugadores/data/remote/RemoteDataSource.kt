package edu.ucne.registrodejugadores.data.remote

import edu.ucne.registrodejugadores.data.remote.dto.PartidaDto
import edu.ucne.registrodejugadores.data.remote.dto.PostMovimientoDto
import edu.ucne.registrodejugadores.data.remote.dto.PostPartidaDto
import edu.ucne.registrodejugadores.data.remote.dto.movimientosDto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: TicTacToeApi
) {
    // MOVIMIENTOS
    suspend fun getMovimientos(partidaId: Int): Resource<List<movimientosDto>> {
        return try {
            val movimientos = api.getMovimientos(partidaId)
            Resource.Success(movimientos)
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun postMovimiento(movimiento: PostMovimientoDto): Resource<Unit> {
        return try {
            api.postMovimiento(movimiento)
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    // PARTIDAS
    suspend fun getPartida(partidaId: Int): Resource<PartidaDto> {
        return try {
            val partida = api.getPartida(partidaId)
            Resource.Success(partida)
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getPartidas(): Resource<List<PartidaDto>> {
        return try {
            val partidas = api.getPartidas()
            Resource.Success(partidas)
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun postPartida(partida: PostPartidaDto): Resource<PartidaDto> {
        return try {
            val nuevaPartida = api.postPartida(partida)
            Resource.Success(nuevaPartida)
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }
}