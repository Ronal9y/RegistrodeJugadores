package edu.ucne.registrodejugadores.ui.screen.partidas_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.ucne.registrodejugadores.domain.model.Jugador
import edu.ucne.registrodejugadores.domain.model.Partida
import edu.ucne.registrodejugadores.ui.screen.jugador_list.JugadorListViewModel
import edu.ucne.registrodejugadores.ui.theme.screen.partidas_Screen.PartidaEvent
import edu.ucne.registrodejugadores.ui.theme.screen.partidas_Screen.PartidaUiEvent
import edu.ucne.registrodejugadores.ui.theme.screen.partidas_Screen.PartidasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidasScreen(
    navController: NavController,
    viewModel: PartidasViewModel = hiltViewModel(),
    jugadorViewModel: JugadorListViewModel = hiltViewModel(),
    onNavigateToGame: (partidaId: Int) -> Unit = {}
) {
    val partidas by viewModel.partidas.collectAsState(emptyList())
    val jugadores by jugadorViewModel.jugadores.collectAsState(emptyList())

    val partidasFinalizadas = partidas.filter { it.esFinalizada }
    val partidasEnCurso = partidas.filter { !it.esFinalizada }

    var mostrarEnCurso by remember { mutableStateOf(false) }

    val uiEvent by viewModel.uiEvent.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = uiEvent) {
        when (val event = uiEvent) {
            is PartidaUiEvent.ShowMessage -> {
                snackbarHostState.showSnackbar(event.message)
                viewModel.consumeUiEvent()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (mostrarEnCurso) "Partidas en Curso" else "Partidas") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarEnCurso = !mostrarEnCurso },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = if (mostrarEnCurso) "Ver Historial" else "Ver En Curso")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            val lista = if (mostrarEnCurso) partidasEnCurso else partidasFinalizadas

            if (lista.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(lista) { partida ->
                        PartidaCard(
                            partida = partida,
                            jugadores = jugadores,
                            onReanudar = {
                                if (!partida.esFinalizada) {
                                    onNavigateToGame(partida.partidaId)
                                }
                            },
                            onEliminar = { viewModel.onEvent(PartidaEvent.OnDeletePartida(partida)) },
                            esFinalizada = partida.esFinalizada
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.SportsEsports,
                            contentDescription = "Sin partidas",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            if (mostrarEnCurso) "No hay partidas en curso" else "No hay partidas",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PartidaCard(
    partida: Partida,
    jugadores: List<Jugador>,
    onReanudar: (Int) -> Unit,
    onEliminar: () -> Unit,
    esFinalizada: Boolean
) {
    val j1 = jugadores.find { it.id == partida.jugador1Id }?.nombres ?: "Jugador ${partida.jugador1Id}"
    val j2 = jugadores.find { it.id == partida.jugador2Id }?.nombres ?: "Jugador ${partida.jugador2Id}"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (esFinalizada) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            }
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Partida #${partida.partidaId}", style = MaterialTheme.typography.titleMedium)
                    Text(partida.fecha, style = MaterialTheme.typography.bodySmall)
                }
                if (!esFinalizada) {
                    Badge(containerColor = MaterialTheme.colorScheme.primary) {
                        Text("En curso")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Jugadores: $j1 vs $j2", style = MaterialTheme.typography.bodyMedium)

            if (esFinalizada) {
                when {
                    partida.ganadorId != 0 -> {
                        val ganador = jugadores.find { it.id == partida.ganadorId }?.nombres ?: "Jugador ${partida.ganadorId}"
                        Text("Finalizada - Ganador: $ganador", color = MaterialTheme.colorScheme.primary)
                    }
                    else -> {
                        Text("Finalizada", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (!esFinalizada) {
                    Button(
                        onClick = { onReanudar(partida.partidaId) },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Reanudar")
                    }
                }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}