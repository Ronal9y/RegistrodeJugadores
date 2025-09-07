package edu.ucne.registrodejugadores.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Jugador(
    val id: Int? = null,
    val nombres: String = "",
    val partidas: Int = 0
): Parcelable
{
    fun safeId(): Int = id ?: 0
}
