package edu.ucne.registrodejugadores.presentation.Logros

import edu.ucne.registrodejugadores.domain.model.Logro

interface LogrosEvent {
    // Eventos de cambio de campos
    data class LogroNombreChanged(val value: String) : LogrosEvent
    data class DescripcionChanged(val value: String) : LogrosEvent

    // Eventos de acciones sobre logros
    data class Save(val logro: Logro) : LogrosEvent
    data class Delete(val logro: Logro) : LogrosEvent
    data class Select(val logro: Logro) : LogrosEvent
}