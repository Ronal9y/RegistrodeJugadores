package edu.ucne.registrodejugadores.data.repository

import edu.ucne.registrodejugadores.data.mapper.toDomain
import edu.ucne.registrodejugadores.data.mapper.toDto
import edu.ucne.registrodejugadores.data.remote.RemoteDataSource
import edu.ucne.registrodejugadores.data.remote.Resource
import edu.ucne.registrodejugadores.domain.model.Movimiento
import edu.ucne.registrodejugadores.domain.repository.MovimientosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class MovimientosRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): MovimientosRepository {
    override fun getMovimientos(partidaId: Int): Flow<Resource<List<Movimiento>>> = flow {
        try {
            emit(Resource.Loading())
            val movimientosDto = remoteDataSource.getMovimientos(partidaId)
            val movimientos = movimientosDto.map { dto -> dto.toDomain() }
            emit(Resource.Success(movimientos))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexion: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Ocurrio un error inesperado: ${e.message}"))
        }
    }

    override suspend fun postMovimiento(movimiento: Movimiento): Resource<Unit> {
        return try {
            remoteDataSource.postMovimiento(movimiento.toDto())
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexion: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Ocurrio un error inesperado: ${e.message}")
        }
    }
}