package edu.ucne.registrodejugadores.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logros")
data class LogroEntity(
    @PrimaryKey(autoGenerate = true)
    val logroId: Int = 0,
    val titulo: String,
    val descripcion: String
)