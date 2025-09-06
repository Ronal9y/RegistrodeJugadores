package edu.ucne.registrodejugadores.data.mapper

import edu.ucne.registrodejugadores.data.local.entity.JugadorEntity
import edu.ucne.registrodejugadores.domain.model.Jugador

fun JugadorEntity.toJugador(): Jugador = Jugador(
    id = jugadorId,
    nombres = nombres,
    partidas = partidas
)

fun Jugador.toEntity(): JugadorEntity = JugadorEntity(
    jugadorId = id ?: 0,
    nombres = nombres,
    partidas = partidas
)