package edu.ucne.registrodejugadores.data.remote.dto

data class PostMovimientoDto(
    val partidaId: Int,
    val jugador: String,
    val posicionFila: Int,
    val posicionColumna: Int
)
