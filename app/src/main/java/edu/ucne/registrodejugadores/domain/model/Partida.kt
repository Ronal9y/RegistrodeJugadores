package edu.ucne.registrodejugadores.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Partida(
    val partidaId: Int = 0,
    val fecha: String = "",
    val jugador1Id: Int = 0,
    val jugador2Id: Int = 0,
    val ganadorId: Int = 0,
    val esFinalizada: Boolean,
    val board: String = "null,null,null,null,null,null,null,null,null"
) : Parcelable {
    companion object {
        fun getFechaActual(): String {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
            return sdf.format(java.util.Date())
        }
    }
}