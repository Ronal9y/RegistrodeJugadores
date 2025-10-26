package edu.ucne.registrodejugadores

import edu.ucne.registrodejugadores.data.remote.RemoteDataSource
import edu.ucne.registrodejugadores.data.remote.Resource
import edu.ucne.registrodejugadores.data.remote.TicTacToeApi
import edu.ucne.registrodejugadores.data.remote.dto.movimientosDto
import edu.ucne.registrodejugadores.data.remote.dto.PostMovimientoDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RemoteDataSourceTest {

    private lateinit var dataSource: RemoteDataSource
    private lateinit var api: TicTacToeApi

    @Before
    fun setup() {
        api = mockk()
        dataSource = RemoteDataSource(api)
    }

    @Test
    fun `getMovimientos retorna lista cuando API responde exitosamente`() = runTest {

        val partidaId = 1
        val expectedMovimientos = listOf(
            movimientosDto(0, "X", 0, 0),
            movimientosDto(0, "O", 1, 1),
            movimientosDto(0, "X", 2, 2)
        )
        coEvery { api.getMovimientos(partidaId) } returns expectedMovimientos


        val result = dataSource.getMovimientos(partidaId)


        assertTrue(result is Resource.Success)
        val successResult = result as Resource.Success
        assertNotNull(successResult.data)
        assertEquals(expectedMovimientos, successResult.data)
        coVerify { api.getMovimientos(partidaId) }
    }

}