package edu.ucne.registrodejugadores.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "movimientos")
data class MovimientoEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val partidaId: Int?,
    val jugador: String,
    val posicionFila: Int,
    val posicionColumna: Int,
    val isPendingCreate: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)