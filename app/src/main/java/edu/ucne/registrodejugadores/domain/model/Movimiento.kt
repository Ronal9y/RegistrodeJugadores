package edu.ucne.registrodejugadores.domain.model

import java.util.UUID

data class Movimiento(
    val id: String = UUID.randomUUID().toString(),
    val partidaId: Int?,
    val jugador: String,
    val posicionFila: Int,
    val posicionColumna: Int
)
