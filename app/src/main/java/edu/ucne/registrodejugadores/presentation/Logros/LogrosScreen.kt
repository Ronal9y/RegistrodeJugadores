package edu.ucne.registrodejugadores.presentation.logros

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.registrodejugadores.domain.model.Logro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogrosScreen(
    onNavigateBack: () -> Unit
) {
    val logros = rememberLogrosPredeterminados()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Logros") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(logros) { logro ->
                LogroCard(logro)
            }
        }
    }
}

@Composable
private fun LogroCard(logro: Logro) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Logro",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = logro.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = logro.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun rememberLogrosPredeterminados(): List<Logro> {
    return remember {
        listOf(
            Logro(1, "Racha ganadora", "Gana 10 partidas seguidas"),
            Logro(2, "Maestro del empate", "Evita perder en 10 juegos consecutivos"),
            Logro(3, "Maestro del Tic Tac Toe", "Gana un juego sin usar la esquina"),
            Logro(4, "Derrota sin paliativos", "Pierde la partida"),
            Logro(5, "Juego en el centro", "Empieza la partida colocando tu primer movimiento en el centro"),
            Logro(6, "Juego cerrado", "El juego termina en empate"),
            Logro(7, "Furia del primer movimiento", "Coloca tu primera 'X' en una esquina")
        )
    }
}