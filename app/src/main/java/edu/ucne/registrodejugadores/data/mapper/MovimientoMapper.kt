package edu.ucne.registrodejugadores.data.mapper

import edu.ucne.registrodejugadores.data.remote.dto.movimientosDto
import edu.ucne.registrodejugadores.domain.model.Movimiento

fun movimientosDto.toDomain(): Movimiento {
    return Movimiento(
        partidaId = this.partidaId,
        jugador = this.jugador,
        posicionFila = this.posicionFila,
        posicionColumna = this.posicionColumna
    )
}

fun Movimiento.toDto(): movimientosDto {
    return movimientosDto(
        partidaId = this.partidaId,
        jugador = this.jugador,
        posicionFila = this.posicionFila,
        posicionColumna = this.posicionColumna
    )
}