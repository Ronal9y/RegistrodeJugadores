package edu.ucne.registrodejugadores.data.repository

import edu.ucne.registrodejugadores.data.local.dao.LogroDao
import edu.ucne.registrodejugadores.data.local.entity.LogroEntity
import edu.ucne.registrodejugadores.domain.model.Logro
import edu.ucne.registrodejugadores.data.mapper.asExternalModel
import edu.ucne.registrodejugadores.data.mapper.toEntity
import edu.ucne.registrodejugadores.domain.repository.LogroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogroRepositoryImpl @Inject constructor(
    private val logroDao: LogroDao
) : LogroRepository {

    override fun getAllLogros(): Flow<List<Logro>> =
        logroDao.observedAll().map { list -> list.map { it.asExternalModel() } }

    override suspend fun getLogroById(id: Int): Logro? =
        logroDao.getById(id)?.asExternalModel()

    override suspend fun insertLogro(logro: Logro) =
        logroDao.upsert(logro.toEntity())

    override suspend fun updateLogro(logro: Logro) =
        logroDao.upsert(logro.toEntity())

    override suspend fun deleteLogro(logro: Logro) =
        logroDao.delete(logro.toEntity())

    override suspend fun deleteLogroById(id: Int) =
        logroDao.deleteById(id)
}