package edu.ucne.registrodejugadores.presentation.partidas_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrodejugadores.domain.model.Partida
import edu.ucne.registrodejugadores.presentation.partidas_Screen.PartidaEvent
import edu.ucne.registrodejugadores.presentation.partidas_Screen.PartidaUiEvent
import edu.ucne.registrodejugadores.presentation.partidas_Screen.PartidasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidaListScreen(
    onNavigateToForm: (Partida?) -> Unit,
    viewModel: PartidasViewModel = hiltViewModel()
) {
    val partidas by viewModel.partidas.collectAsState(emptyList())
    val uiEvent by viewModel.uiEvent.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = uiEvent) {
        when (val event = uiEvent) {
            is PartidaUiEvent.ShowMessage -> {
                snackbarHostState.showSnackbar(event.message)
                viewModel.consumeUiEvent()
            }
            is PartidaUiEvent.NavigateToEdit -> {
                onNavigateToForm(event.partida)
                viewModel.consumeUiEvent()
            }
            is PartidaUiEvent.NavigateToAdd -> {
                onNavigateToForm(null)
                viewModel.consumeUiEvent()
            }
            is PartidaUiEvent.NavigateToGame -> {

                viewModel.consumeUiEvent()
            }
            is PartidaUiEvent.NavigateBack -> {

                viewModel.consumeUiEvent()
            }
            is PartidaUiEvent.ShowError -> {
                snackbarHostState.showSnackbar(event.error)
                viewModel.consumeUiEvent()
            }
            is PartidaUiEvent.ShowSuccess -> {
                snackbarHostState.showSnackbar(event.message)
                viewModel.consumeUiEvent()
            }
            null -> {

            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Historial de Partidas") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(PartidaEvent.OnAddPartida) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar partida")
            }
        }
    ) { innerPadding ->
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
                    PartidaListItem(
                        partida = partida,
                        onEdit = { viewModel.onEvent(PartidaEvent.OnEditPartida(partida)) },
                        onDelete = { viewModel.onEvent(PartidaEvent.OnDeletePartida(partida)) }
                    )
                }
            }
        }
    }
}

@Composable
fun PartidaListItem(
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