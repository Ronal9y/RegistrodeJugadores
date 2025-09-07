package edu.ucne.registrodejugadores.ui.screen.juego

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
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
import edu.ucne.registrodejugadores.ui.screen.jugador_list.JugadorListViewModel
import edu.ucne.registrodejugadores.ui.theme.util.Routes

@Composable
fun SeleccionJugadoresScreen(
    onJugadoresSeleccionados: (jugadorX: Jugador, jugadorO: Jugador) -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: JugadorListViewModel = hiltViewModel()
) {
    val jugadores by viewModel.jugadores.collectAsState(emptyList())
    var jugadorXSeleccionado by remember { mutableStateOf<Jugador?>(null) }
    var jugadorOSeleccionado by remember { mutableStateOf<Jugador?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Seleccionar Jugadores",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text("Jugador X (❌):", fontWeight = FontWeight.Bold)
        LazyColumn(modifier = Modifier.height(150.dp)) {
            items(jugadores) { jugador ->
                JugadorSeleccionItem(
                    jugador = jugador,
                    seleccionado = jugadorXSeleccionado == jugador,
                    onSeleccionar = { jugadorXSeleccionado = jugador }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Jugador O (⭕):", fontWeight = FontWeight.Bold)
        LazyColumn(modifier = Modifier.height(150.dp)) {
            items(jugadores) { jugador ->
                JugadorSeleccionItem(
                    jugador = jugador,
                    seleccionado = jugadorOSeleccionado == jugador,
                    onSeleccionar = { jugadorOSeleccionado = jugador }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { onNavigate(Routes.JUGADOR_LIST) }
            ) {
                Text("Volver")
            }

            Button(
                onClick = {
                    if (jugadorXSeleccionado != null && jugadorOSeleccionado != null) {
                        onJugadoresSeleccionados(jugadorXSeleccionado!!, jugadorOSeleccionado!!)
                    }
                },
                enabled = jugadorXSeleccionado != null && jugadorOSeleccionado != null
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Jugar")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Comenzar Juego")
            }
        }
    }
}

@Composable
fun JugadorSeleccionItem(
    jugador: Jugador,
    seleccionado: Boolean,
    onSeleccionar: () -> Unit
) {
    Card(
        onClick = onSeleccionar,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Person, contentDescription = "Jugador")
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = jugador.nombres,
                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Partidas: ${jugador.partidas}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}