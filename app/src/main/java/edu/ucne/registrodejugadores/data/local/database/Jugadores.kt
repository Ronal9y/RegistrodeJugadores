package edu.ucne.registrodejugadores.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrodejugadores.data.local.dao.JugadorDao
import edu.ucne.registrodejugadores.data.local.dao.PartidaDao
import edu.ucne.registrodejugadores.data.local.entity.JugadorEntity
import edu.ucne.registrodejugadores.data.local.entity.PartidaEntity

@Database(
    entities = [JugadorEntity::class, PartidaEntity::class],
    version = 2,
    exportSchema = false
)
abstract class Jugadores : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao
    abstract fun partidaDao(): PartidaDao
}