package edu.ucne.registrodejugadores.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.ucne.registrodejugadores.domain.model.Partida
import edu.ucne.registrodejugadores.ui.theme.screen.Juego.Player

@Entity(tableName = "Partida")
data class PartidaEntity(
    @PrimaryKey(autoGenerate = true)
    val partidaId: Int = 0,
    val fecha: String,
    val jugador1Id: Int,
    val jugador2Id: Int,
    val ganadorId: Int,
    val esFinalizada: Boolean,
    val board: String
)

fun Array<Player?>.toCsv(): String =
    joinToString(",") { it?.name ?: "null" }

fun String.toPlayerArray(): Array<Player?> =
    split(",").map { if (it == "null") null else Player.valueOf(it) }.toTypedArray()