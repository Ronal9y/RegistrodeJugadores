package edu.ucne.registrodejugadores.domain.model

data class Jugador(
    val id: Int? = null,
    val nombres: String = "",
    val partidas: Int = 0
){
    fun safeId(): Int = id ?: 0
}
