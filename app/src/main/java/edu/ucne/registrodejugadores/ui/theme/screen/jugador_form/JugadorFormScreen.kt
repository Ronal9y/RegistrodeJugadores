package edu.ucne.registrodejugadores.ui.screen.jugador_form

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrodejugadores.ui.theme.screen.jugador_form.JugadorFormEvent
import edu.ucne.registrodejugadores.ui.theme.screen.jugador_form.JugadorFormViewModel
import edu.ucne.registrodejugadores.ui.theme.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadorFormScreen(
    onNavigateBack: () -> Unit,
    onNavigateToList: () -> Unit,
    viewModel: JugadorFormViewModel = hiltViewModel()
) {
    val nombres by viewModel.nombres.collectAsState()
    val uiEvent by viewModel.uiEvent.collectAsState()

    LaunchedEffect(key1 = uiEvent) {
        when (uiEvent) {
            is UiEvent.NavigateBack -> {
                onNavigateBack()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Jugador") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToList) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nombres,
                onValueChange = { viewModel.onEvent(JugadorFormEvent.OnNombresChange(it)) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onEvent(JugadorFormEvent.OnSaveJugador) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }

            Button(
                onClick = onNavigateToList,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Lista")
            }
        }
    }
}