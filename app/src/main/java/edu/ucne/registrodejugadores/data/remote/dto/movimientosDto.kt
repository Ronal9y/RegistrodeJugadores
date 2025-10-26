package edu.ucne.registrodejugadores.data.remote.dto

data class movimientosDto (
    val movimientoId: Int = 0,
    val jugador: String,
    val posicionFila: Int,
    val posicionColumna: Int
)
