package edu.ucne.registrodejugadores.data.mapper

import edu.ucne.registrodejugadores.data.local.entity.JugadorEntity
import edu.ucne.registrodejugadores.data.local.entity.LogroEntity
import edu.ucne.registrodejugadores.domain.model.Jugador
import edu.ucne.registrodejugadores.domain.model.Logro

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

fun LogroEntity.asExternalModel(): Logro = Logro(
    logroId = logroId,
   titulo = titulo,
    descripcion = descripcion
)

fun Logro.toEntity(): LogroEntity = LogroEntity(
    logroId = logroId ?: 0,
    titulo = titulo,
    descripcion = descripcion
)