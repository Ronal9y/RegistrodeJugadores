package edu.ucne.registrodejugadores.data.repository

import edu.ucne.registrodejugadores.data.local.dao.MovimientoDao
import edu.ucne.registrodejugadores.data.mapper.toDomain
import edu.ucne.registrodejugadores.data.mapper.toEntity
import edu.ucne.registrodejugadores.data.mapper.toPostDto
import edu.ucne.registrodejugadores.data.remote.RemoteDataSource
import edu.ucne.registrodejugadores.data.remote.Resource
import edu.ucne.registrodejugadores.domain.model.Movimiento
import edu.ucne.registrodejugadores.domain.repository.MovimientosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovimientosRepositoryImpl @Inject constructor(
    private val localDataSource: MovimientoDao,
    private val remoteDataSource: RemoteDataSource
) : MovimientosRepository {

    override fun getMovimientos(partidaId: Int): Flow<Resource<List<Movimiento>>> {
        return localDataSource.observeMovimientos(partidaId).map { entities ->
            Resource.Success(entities.map { it.toDomain() })
        }
    }

    override suspend fun createMovimientoLocal(movimiento: Movimiento): Resource<Movimiento> {
        return try {
            val entity = movimiento.toEntity().copy(isPendingCreate = true)
            localDataSource.upsert(entity)
            Resource.Success(entity.toDomain())
        } catch (e: Exception) {
            Resource.Error("Error al guardar localmente: ${e.message}")
        }
    }

    override suspend fun postMovimiento(movimiento: Movimiento): Resource<Unit> {
        return try {

            val entity = movimiento.toEntity().copy(isPendingCreate = true)
            localDataSource.upsert(entity)

            val movimientoDto = movimiento.toPostDto()
            val result = remoteDataSource.postMovimiento(movimientoDto)

            when (result) {
                is Resource.Success -> {

                    val syncedEntity = entity.copy(isPendingCreate = false)
                    localDataSource.upsert(syncedEntity)
                    Resource.Success(Unit)
                }
                is Resource.Error -> {
                    Resource.Error("Error al sincronizar: ${result.message}")
                }
                is Resource.Loading -> {
                    Resource.Error("Sincronización en progreso")
                }
            }
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    override suspend fun deleteMovimiento(id: String): Resource<Unit> {
        return try {
            localDataSource.delete(id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error al eliminar: ${e.message}")
        }
    }

    override suspend fun postPendingMovimientos(): Resource<Unit> {
        return try {
            val pendingMovimientos = localDataSource.getPendingCreateMovimientos()

            for (entity in pendingMovimientos) {

                val movimiento = entity.toDomain()
                val movimientoDto = movimiento.toPostDto()

                when (val result = remoteDataSource.postMovimiento(movimientoDto)) {
                    is Resource.Success -> {

                        val syncedEntity = entity.copy(isPendingCreate = false)
                        localDataSource.upsert(syncedEntity)
                    }
                    is Resource.Error -> {
                        return Resource.Error("Falló sincronización: ${result.message}")
                    }
                    is Resource.Loading -> {

                        continue
                    }
                }
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error en sincronización: ${e.message}")
        }
    }

    override suspend fun cargarMovimientosDesdeAPI(partidaId: Int): Resource<List<Movimiento>> {
        return try {
            val result = remoteDataSource.getMovimientos(partidaId)
            when (result) {
                is Resource.Success -> {
                    val movimientos = result.data?.map { it.toDomain() } ?: emptyList()

                    movimientos.forEach { movimiento ->
                        val entity = movimiento.toEntity().copy(isPendingCreate = false)
                        localDataSource.upsert(entity)
                    }
                    Resource.Success(movimientos)
                }
                is Resource.Error -> Resource.Error(result.message ?: "Error desconocido")
                is Resource.Loading -> Resource.Loading()
            }
        } catch (e: Exception) {
            Resource.Error("Error al cargar movimientos: ${e.message}")
        }
    }
}