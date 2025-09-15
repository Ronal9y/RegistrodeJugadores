package edu.ucne.registrodejugadores.ui.screen.partidas_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import edu.ucne.registrodejugadores.ui.theme.screen.partidas_Screen.PartidaEvent
import edu.ucne.registrodejugadores.ui.theme.screen.partidas_Screen.PartidaUiEvent
import edu.ucne.registrodejugadores.ui.theme.screen.partidas_Screen.PartidasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidasScreen(
    viewModel: PartidasViewModel = hiltViewModel()
) {
    val partidas by viewModel.partidas.collectAsState(emptyList())
    val uiEvent by viewModel.uiEvent.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showForm by remember { mutableStateOf(false) }
    var editingPartida by remember { mutableStateOf<Partida?>(null) }

    var partidaId by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var jugador1Id by remember { mutableStateOf("") }
    var jugador2Id by remember { mutableStateOf("") }
    var ganadorId by remember { mutableStateOf("") }
    var esFinalizada by remember { mutableStateOf(false) }


    fun resetForm() {
        partidaId = ""
        fecha = Partida.getFechaActual()
        jugador1Id = ""
        jugador2Id = ""
        ganadorId = ""
        esFinalizada = false
    }

    LaunchedEffect(key1 = uiEvent) {
        when (val event = uiEvent) {
            is PartidaUiEvent.ShowMessage -> {
                snackbarHostState.showSnackbar(event.message)
                viewModel.consumeUiEvent()
            }
            else -> {}
        }
    }

    LaunchedEffect(key1 = editingPartida) {
        if (editingPartida != null) {
            partidaId = editingPartida!!.partidaId.toString()
            fecha = editingPartida!!.fecha
            jugador1Id = editingPartida!!.jugador1Id.toString()
            jugador2Id = editingPartida!!.jugador2Id.toString()
            ganadorId = editingPartida!!.ganadorId.toString()
            esFinalizada = editingPartida!!.esFinalizada
            showForm = true
        } else {
            resetForm()
        }
    }

    fun savePartida() {
        val partida = Partida(
            partidaId = partidaId.toIntOrNull() ?: 0,
            fecha = fecha,
            jugador1Id = jugador1Id.toIntOrNull() ?: 0,
            jugador2Id = jugador2Id.toIntOrNull() ?: 0,
            ganadorId = ganadorId.toIntOrNull() ?: 0,
            esFinalizada = esFinalizada
        )
        viewModel.onEvent(PartidaEvent.OnSavePartida(partida))
        showForm = false
        editingPartida = null
        resetForm()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(if (showForm) "Formulario de Partida" else "Historial de Partidas")
                }
            )
        },
        floatingActionButton = {
            if (!showForm) {
                FloatingActionButton(
                    onClick = { showForm = true }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar partida")
                }
            }
        }
    ) { innerPadding ->
        if (showForm) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (editingPartida != null) {
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            showForm = false
                            editingPartida = null
                            resetForm()
                        }
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = { savePartida() }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Guardar")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (editingPartida == null) "Crear" else "Actualizar")
                    }
                }
            }
        } else {
            if (partidas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay partidas registradas")
                }
            } else {
                LazyColumn(modifier = Modifier.padding(innerPadding)) {
                    items(partidas) { partida ->
                        partidaListBody(
                            partida = partida,
                            onEdit = { editingPartida = partida },
                            onDelete = { viewModel.onEvent(PartidaEvent.OnDeletePartida(partida)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun partidaListBody(
    partida: Partida,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Partida #${partida.partidaId}", fontWeight = FontWeight.Bold)
                Text("Fecha: ${partida.fecha}")
                Text("Jugadores: ${partida.jugador1Id} vs ${partida.jugador2Id}")
                Text("Ganador: ${partida.ganadorId}")
                Text("Finalizada: ${if (partida.esFinalizada) "Sí" else "No"}")
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PartidasScreenPreview() {
    PartidasScreen()
}

@Preview(showBackground = true)
@Composable
fun PartidaListBodyPreview() {
    val partida = Partida(
        partidaId = 1,
        fecha = "15/12/2023",
        jugador1Id = 1,
        jugador2Id = 2,
        ganadorId = 1,
        esFinalizada = true
    )
    partidaListBody(
        partida = partida,
        onEdit = {},
        onDelete = {}
    )
}