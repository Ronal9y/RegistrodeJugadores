package edu.ucne.registrodejugadores.data.repository

import edu.ucne.registrodejugadores.data.local.dao.PartidaDao
import edu.ucne.registrodejugadores.data.local.entity.PartidaEntity
import edu.ucne.registrodejugadores.domain.model.Partida
import edu.ucne.registrodejugadores.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PartidaRepositoryImpl @Inject constructor(
    private val dao: PartidaDao
) : PartidaRepository {

    override fun getAllPartidas(): Flow<List<Partida>> =
        dao.getAllPartidas().map { list -> list.map { it.toDomain() } }

    override suspend fun getPartidaById(id: Int): Partida? =
        dao.getPartidaById(id)?.toDomain()

    override suspend fun insertPartida(partida: Partida) =
        dao.insert(partida.toEntity())

    override suspend fun updatePartida(partida: Partida) =
        dao.update(partida.toEntity())

    override suspend fun deletePartida(partida: Partida) =
        dao.delete(partida.toEntity())

    private fun PartidaEntity.toDomain() = Partida(
        partidaId, fecha, jugador1Id, jugador2Id, ganadorId, esFinalizada
    )

    private fun Partida.toEntity() = PartidaEntity(
        partidaId, fecha, jugador1Id, jugador2Id, ganadorId, esFinalizada
    )
}

