package edu.ucne.registrodejugadores.presentation.partidas_Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrodejugadores.domain.model.Partida
import edu.ucne.registrodejugadores.presentation.partidas_Screen.PartidaEvent
import edu.ucne.registrodejugadores.presentation.partidas_Screen.PartidasViewModel

@Composable
fun PartidaFormScreen(
    partida: Partida? = null,
    onNavigateBack: () -> Unit,
    viewModel: PartidasViewModel = hiltViewModel()
) {
    var partidaId by remember { mutableStateOf(partida?.partidaId?.toString() ?: "") }
    var fecha by remember { mutableStateOf(partida?.fecha ?: Partida.getFechaActual()) }
    var jugador1Id by remember { mutableStateOf(partida?.jugador1Id?.toString() ?: "") }
    var jugador2Id by remember { mutableStateOf(partida?.jugador2Id?.toString() ?: "") }
    var ganadorId by remember { mutableStateOf(partida?.ganadorId?.toString() ?: "") }
    var esFinalizada by remember { mutableStateOf(partida?.esFinalizada ?: false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (partida == null) "Nueva Partida" else "Editar Partida",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (partida != null) {
            OutlinedTextField(
                value = partidaId,
                onValueChange = { partidaId = it },
                label = { Text("Partida ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha (dd/MM/yyyy)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = jugador1Id,
            onValueChange = { jugador1Id = it },
            label = { Text("Jugador 1 ID") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = jugador2Id,
            onValueChange = { jugador2Id = it },
            label = { Text("Jugador 2 ID") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = ganadorId,
            onValueChange = { ganadorId = it },
            label = { Text("Ganador ID") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = esFinalizada,
                onCheckedChange = { esFinalizada = it }
            )
            Text("¿Partida finalizada?")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val nuevaPartida = Partida(
                    partidaId = partidaId.toIntOrNull() ?: 0,
                    fecha = fecha,
                    jugador1Id = jugador1Id.toIntOrNull() ?: 0,
                    jugador2Id = jugador2Id.toIntOrNull() ?: 0,
                    ganadorId = ganadorId.toIntOrNull() ?: 0,
                    esFinalizada = esFinalizada
                )
                viewModel.onEvent(PartidaEvent.OnSavePartida(nuevaPartida))
                onNavigateBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = "Guardar")
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (partida == null) "Crear Partida" else "Actualizar Partida")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PartidaFormScreenPreview() {
    PartidaFormScreen(
        partida = Partida(
            partidaId = 1,
            fecha = "15/12/2023",
            jugador1Id = 1,
            jugador2Id = 2,
            ganadorId = 1,
            esFinalizada = true
        ),
        onNavigateBack = {}
    )
}

@Preview(showBackground = true)
@Composable
fun NuevaPartidaFormScreenPreview() {
    PartidaFormScreen(
        partida = null,
        onNavigateBack = {}
    )
}