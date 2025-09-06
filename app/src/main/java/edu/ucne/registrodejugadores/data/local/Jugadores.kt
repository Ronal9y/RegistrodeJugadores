package edu.ucne.registrodejugadores.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrodejugadores.data.local.dao.JugadorDao
import edu.ucne.registrodejugadores.data.local.entity.JugadorEntity

@Database(
    entities = [JugadorEntity::class],
    version = 1,
    exportSchema = false
)
abstract class Jugadores : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao
}