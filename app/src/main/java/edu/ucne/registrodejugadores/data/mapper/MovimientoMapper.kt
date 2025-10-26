package edu.ucne.registrodejugadores.data.mapper

import edu.ucne.registrodejugadores.data.local.entity.MovimientoEntity
import edu.ucne.registrodejugadores.data.remote.dto.movimientosDto
import edu.ucne.registrodejugadores.data.remote.dto.PostMovimientoDto
import edu.ucne.registrodejugadores.domain.model.Movimiento


fun MovimientoEntity.toDomain(): Movimiento {
    return Movimiento(
        id = this.id,
        partidaId = this.partidaId,
        jugador = this.jugador,
        posicionFila = this.posicionFila,
        posicionColumna = this.posicionColumna
    )
}

fun Movimiento.toEntity(): MovimientoEntity {
    return MovimientoEntity(
        id = this.id,
        partidaId = this.partidaId,
        jugador = this.jugador,
        posicionFila = this.posicionFila,
        posicionColumna = this.posicionColumna
    )
}

fun movimientosDto.toDomain(): Movimiento {
    return Movimiento(
        id = this.movimientoId.toString(),
        partidaId = null,
        jugador = this.jugador,
        posicionFila = this.posicionFila,
        posicionColumna = this.posicionColumna
    )
}

fun Movimiento.toPostDto(): PostMovimientoDto {
    return PostMovimientoDto(
        partidaId = this.partidaId ?: 0,
        jugador = this.jugador,
        posicionFila = this.posicionFila,
        posicionColumna = this.posicionColumna
    )
}


fun MovimientoEntity.toPostDto(): PostMovimientoDto {
    return PostMovimientoDto(
        partidaId = this.partidaId ?: 0,
        jugador = this.jugador,
        posicionFila = this.posicionFila,
        posicionColumna = this.posicionColumna
    )
}