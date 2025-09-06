package edu.ucne.registrodejugadores.ui.screen.jugador_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrodejugadores.domain.model.Jugador
import edu.ucne.registrodejugadores.ui.theme.util.Routes
import edu.ucne.registrodejugadores.ui.theme.util.UiEvent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadorListScreen(
    onNavigate: (String) -> Unit,
    viewModel: JugadorListViewModel = hiltViewModel()
) {
    val jugadores by viewModel.jugadores.collectAsState(emptyList())
    val uiEvent by viewModel.uiEvent.collectAsState()

    LaunchedEffect(key1 = uiEvent) {
        when (uiEvent) {
            is UiEvent.NavigateTo -> {
                onNavigate((uiEvent as UiEvent.NavigateTo).route)
            }
            is UiEvent.ShowMessage -> {

            }
            else -> {}
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                    onNavigate(Routes.SELECCION_JUGADORES)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar jugador")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Jugadores Registrados",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (jugadores.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay jugadores registrados")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(jugadores) { jugador ->
                        JugadorItem(
                            jugador = jugador,
                            onDelete = { viewModel.onEvent(JugadorListEvent.OnDeleteJugador(jugador)) },
                            onSelect = { viewModel.onEvent(JugadorListEvent.OnSelectJugador(jugador)) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadorItem(
    jugador: Jugador,
    onDelete: () -> Unit,
    onSelect: () -> Unit
) {
    Card(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Jugador",
                    modifier = Modifier.padding(end = 16.dp)
                )
                Column {
                    Text(
                        text = jugador.nombres,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Partidas: ${jugador.partidas}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}