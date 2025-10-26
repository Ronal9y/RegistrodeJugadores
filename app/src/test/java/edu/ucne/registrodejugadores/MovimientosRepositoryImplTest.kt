package edu.ucne.registrodejugadores

import edu.ucne.registrodejugadores.data.local.dao.MovimientoDao
import edu.ucne.registrodejugadores.data.remote.RemoteDataSource
import edu.ucne.registrodejugadores.data.remote.Resource
import edu.ucne.registrodejugadores.data.remote.dto.PostMovimientoDto
import edu.ucne.registrodejugadores.data.repository.MovimientosRepositoryImpl
import edu.ucne.registrodejugadores.domain.model.Movimiento
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MovimientosRepositoryImplTest {

    private lateinit var repository: MovimientosRepositoryImpl
    private lateinit var localDataSource: MovimientoDao
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        repository = MovimientosRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `postMovimiento convierte correctamente Domain a DTO`() = runTest {

        val movimiento = Movimiento(
            id = "test-id-1",
            partidaId = 1,
            jugador = "X",
            posicionFila = 0,
            posicionColumna = 0
        )

        val expectedDto = PostMovimientoDto(
            partidaId = 1,
            jugador = "X",
            posicionFila = 0,
            posicionColumna = 0
        )

        coEvery { localDataSource.upsert(any()) } returns Unit
        coEvery { remoteDataSource.postMovimiento(any()) } returns Resource.Success(Unit)


        val result = repository.postMovimiento(movimiento)


        assertTrue(result is Resource.Success)
        coVerify {
            remoteDataSource.postMovimiento(
                match { dto ->
                    dto.partidaId == expectedDto.partidaId &&
                            dto.jugador == expectedDto.jugador &&
                            dto.posicionFila == expectedDto.posicionFila &&
                            dto.posicionColumna == expectedDto.posicionColumna
                }
            )
        }
    }
}