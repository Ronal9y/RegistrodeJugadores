package edu.ucne.registrodejugadores.ui.theme.screen.Juego

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrodejugadores.domain.model.Jugador
import edu.ucne.registrodejugadores.domain.model.Partida
import edu.ucne.registrodejugadores.ui.screen.jugador_list.JugadorListViewModel
import edu.ucne.registrodejugadores.ui.theme.screen.partidas_Screen.PartidaEvent
import edu.ucne.registrodejugadores.ui.theme.screen.partidas_Screen.PartidasViewModel
import kotlinx.coroutines.launch

// GameScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    partidaId: Int = -1,
    jugadorXId: Int?,
    jugadorOId: Int?,
    onExitGame: () -> Unit,
    gameViewModel: GameViewModel = hiltViewModel(),
    jugadorViewModel: JugadorListViewModel = hiltViewModel(),
    partidaViewModel: PartidasViewModel = hiltViewModel()
) {
    val state by gameViewModel.state.collectAsState()
    val jugadores by jugadorViewModel.jugadores.collectAsState(emptyList())
    val partidas by partidaViewModel.partidas.collectAsState(emptyList())
    val lastSavedPartidaId by partidaViewModel.lastSavedPartidaId.collectAsState()

    var partidaActual by remember { mutableStateOf<Partida?>(null) }
    var partidaCargada by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val jugadorX = remember(jugadorXId, jugadores) {
        jugadores.find { it.id == jugadorXId }
    }
    val jugadorO = remember(jugadorOId, jugadores) {
        jugadores.find { it.id == jugadorOId }
    }

    // Guardar automáticamente después de cada movimiento
    LaunchedEffect(state.board) {
        if (partidaActual != null && !state.hasWon && !state.isDraw) {
            val partidaActualizada = partidaActual!!.copy(
                board = gameViewModel.getBoardCsv(),
                esFinalizada = false
            )
            partidaViewModel.onEvent(PartidaEvent.OnSavePartida(partidaActualizada))
        }
    }

    // Actualizar partidaActual con el ID correcto después de guardar
    LaunchedEffect(lastSavedPartidaId) {
        lastSavedPartidaId?.let { id ->
            if (partidaActual?.partidaId != id) {
                scope.launch {
                    val partidaGuardada = partidaViewModel.getPartidaById(id)
                    partidaActual = partidaGuardada
                }
            }
        }
    }

    LaunchedEffect(partidaId, jugadorXId, jugadorOId, partidas) {
        if (partidaCargada) return@LaunchedEffect

        when {
            partidaId != -1 -> {
                scope.launch {
                    val partida = partidaViewModel.getPartidaById(partidaId)
                    partida?.let {
                        partidaActual = it
                        gameViewModel.loadBoard(it.board)
                        partidaCargada = true
                    }
                }
            }

            jugadorXId != null && jugadorOId != null -> {
                val existente = partidas.firstOrNull { partida ->
                    !partida.esFinalizada &&
                            ((partida.jugador1Id == jugadorXId && partida.jugador2Id == jugadorOId) ||
                                    (partida.jugador1Id == jugadorOId && partida.jugador2Id == jugadorXId))
                }

                if (existente != null) {
                    partidaActual = existente
                    gameViewModel.loadBoard(existente.board)
                } else {
                    val nuevaPartida = Partida(
                        partidaId = 0, // Se asignará el ID real al guardar
                        fecha = Partida.getFechaActual(),
                        jugador1Id = jugadorXId,
                        jugador2Id = jugadorOId,
                        ganadorId = 0,
                        esFinalizada = false,
                        board = gameViewModel.getBoardCsv()
                    )
                    partidaViewModel.onEvent(PartidaEvent.OnSavePartida(nuevaPartida))
                    // partidaActual se actualizará con el ID correcto mediante el efecto de lastSavedPartidaId
                }
                partidaCargada = true
            }
        }
    }

    LaunchedEffect(state.hasWon, state.isDraw) {
        if ((state.hasWon || state.isDraw) && partidaActual != null && !partidaActual!!.esFinalizada) {
            val ganadorIdValor = when {
                state.hasWon && state.lastPlayer == Player.X -> jugadorX?.id ?: 0
                state.hasWon && state.lastPlayer == Player.O -> jugadorO?.id ?: 0
                else -> 0
            }

            val partidaFinalizada = partidaActual!!.copy(
                ganadorId = ganadorIdValor,
                esFinalizada = true,
                board = gameViewModel.getBoardCsv()
            )

            partidaViewModel.onEvent(PartidaEvent.OnSavePartida(partidaFinalizada))

            jugadorX?.id?.let { gameViewModel.incrementarPartidas(it) }
            jugadorO?.id?.let { gameViewModel.incrementarPartidas(it) }
            if (ganadorIdValor != 0) {
                gameViewModel.incrementarPartidas(ganadorIdValor)
            }

            partidaActual = partidaFinalizada
        }
    }

    if (jugadorX == null || jugadorO == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Jugadores no encontrados",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Button(
                    onClick = onExitGame,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Volver al menú")
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Tic Tac Toe",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            PuntuacionRow(state, jugadorX, jugadorO)
            TableroJuego(state, gameViewModel)
            MensajeTurno(state, jugadorX, jugadorO)
            BotonesJuego(
                onExitGame = {
                    if (partidaActual != null && !state.hasWon && !state.isDraw) {
                        val partidaActualizada = partidaActual!!.copy(
                            board = gameViewModel.getBoardCsv(),
                            esFinalizada = false
                        )
                        partidaViewModel.onEvent(PartidaEvent.OnSavePartida(partidaActualizada))
                    }
                    onExitGame()
                },
                gameViewModel = gameViewModel
            )
        }
    }
}


@Composable
private fun PuntuacionRow(state: GameState, jugadorX: Jugador, jugadorO: Jugador) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScoreItem(
                label = jugadorO.nombres,
                score = state.oScore,
                color = MaterialTheme.colorScheme.secondary
            )
            ScoreItem(
                label = "Empates",
                score = state.drawScore,
                color = MaterialTheme.colorScheme.outline
            )
            ScoreItem(
                label = jugadorX.nombres,
                score = state.xScore,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ScoreItem(label: String, score: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Text(
            text = score.toString(),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )
    }
}

@Composable
private fun TableroJuego(state: GameState, gameViewModel: GameViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            GameBoard(
                board = state.board,
                onCellClicked = { cell ->
                    if (!state.hasWon && !state.isDraw) {
                        gameViewModel.onAction(GameAction.BoardTapped(cell))
                    }
                },
                modifier = Modifier.fillMaxSize(0.9f)
            )

            when (state.winLine) {
                WinLine.Horizontal1 -> WinHorizontalLine1()
                WinLine.Horizontal2 -> WinHorizontalLine2()
                WinLine.Horizontal3 -> WinHorizontalLine3()
                WinLine.Vertical1 -> WinVerticalLine1()
                WinLine.Vertical2 -> WinVerticalLine2()
                WinLine.Vertical3 -> WinVerticalLine3()
                WinLine.Diagonal1 -> WinDiagonalLine1()
                WinLine.Diagonal2 -> WinDiagonalLine2()
                null -> {}
            }
        }
    }
}

@Composable
private fun MensajeTurno(state: GameState, jugadorX: Jugador, jugadorO: Jugador) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                state.hasWon -> MaterialTheme.colorScheme.primaryContainer
                state.isDraw -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.secondaryContainer
            },
            contentColor = when {
                state.hasWon -> MaterialTheme.colorScheme.onPrimaryContainer
                state.isDraw -> MaterialTheme.colorScheme.onTertiaryContainer
                else -> MaterialTheme.colorScheme.onSecondaryContainer
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = obtenerMensajeEstado(state, jugadorX, jugadorO),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
    }
}

private fun obtenerMensajeEstado(state: GameState, jugadorX: Jugador, jugadorO: Jugador): String {
    return when {
        state.hasWon -> {
            val ganador = when (state.lastPlayer) {
                Player.X -> jugadorX.nombres
                Player.O -> jugadorO.nombres
                else -> "Felicidades"
            }
            "🎉 ¡$ganador Ganaste!"
        }
        state.isDraw -> "🤝 ¡Es un empate!"
        else -> "▶ Turno de: ${if (state.currentPlayer == Player.X) jugadorX.nombres else jugadorO.nombres}"
    }
}

@Composable
private fun BotonesJuego(
    onExitGame: () -> Unit,
    gameViewModel: GameViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onExitGame,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            )
        ) {
            Icon(
                Icons.Default.ExitToApp,
                contentDescription = "Salir",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Salir", fontWeight = FontWeight.SemiBold)
        }


        Button(
            onClick = { gameViewModel.onAction(GameAction.PlayAgain) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            )
        ) {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = "Volver a jugar",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reiniciar", fontWeight = FontWeight.SemiBold)
        }
    }
}