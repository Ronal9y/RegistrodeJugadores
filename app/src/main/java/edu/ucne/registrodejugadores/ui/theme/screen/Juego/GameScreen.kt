package edu.ucne.registrodejugadores.ui.theme.screen.Juego

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrodejugadores.domain.model.Jugador
import edu.ucne.registrodejugadores.ui.screen.jugador_list.JugadorListViewModel
import edu.ucne.registrodejugadores.ui.theme.BlueCustom
import edu.ucne.registrodejugadores.ui.theme.GrayBackground
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    jugadorXId: Int?,
    jugadorOId: Int?,
    onPartidaTerminada: () -> Unit,
    onExitGame: () -> Unit,
    gameViewModel: GameViewModel = hiltViewModel(),
    jugadorViewModel: JugadorListViewModel = hiltViewModel()
) {
    val state by gameViewModel.state.collectAsState()
    val jugadores by jugadorViewModel.jugadores.collectAsState(emptyList())

    val jugadorX = remember(jugadorXId, jugadores) {
        jugadores.find { it.id == jugadorXId }
    }
    val jugadorO = remember(jugadorOId, jugadores) {
        jugadores.find { it.id == jugadorOId }
    }
    if (jugadorX == null || jugadorO == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Error: Jugadores no encontrados")
            Button(onClick = onExitGame) {
                Text("Volver")
            }
        }
        return
    }

    LaunchedEffect(key1 = state.hasWon, key2 = state.isDraw) {
        if (state.hasWon || state.isDraw) {
            val ganador = determinarGanador(state, jugadorX, jugadorO)
            launch {
                incrementarPartidas(gameViewModel, jugadorX, jugadorO, ganador)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        PuntuacionRow(state, jugadorX, jugadorO)
        TituloJuego()
        TableroJuego(state, gameViewModel)
        MensajeTurno(state, jugadorX, jugadorO)
        BotonesJuego(onExitGame, gameViewModel)
    }
}

private fun determinarGanador(state: GameState, jugadorX: Jugador, jugadorO: Jugador): Jugador? {
    if (!state.hasWon) return null

    return when (state.lastPlayer) {
        Player.X -> jugadorX
        Player.O -> jugadorO
        else -> null
    }
}
private suspend fun incrementarPartidas(
    gameViewModel: GameViewModel,
    jugadorX: Jugador,
    jugadorO: Jugador,
    ganador: Jugador?
) {
    gameViewModel.incrementarPartidas(jugadorX.id)
    gameViewModel.incrementarPartidas(jugadorO.id)
    ganador?.let { gameViewModel.incrementarPartidas(it.id) }
}

@Composable
private fun PuntuacionRow(state: GameState, jugadorX: Jugador, jugadorO: Jugador) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${jugadorO.nombres} : ${state.oScore}", fontSize = 16.sp)
        Text(text = "Empates : ${state.drawScore}", fontSize = 16.sp)
        Text(text = "${jugadorX.nombres} : ${state.xScore}", fontSize = 16.sp)
    }
}

@Composable
private fun TituloJuego() {
    Text(
        text = "Tic Tac Toe",
        fontSize = 50.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Cursive,
        color = BlueCustom
    )
}

@Composable
private fun TableroJuego(state: GameState, gameViewModel: GameViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(GrayBackground),
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

@Composable
private fun MensajeTurno(state: GameState, jugadorX: Jugador, jugadorO: Jugador) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = obtenerMensajeEstado(state, jugadorX, jugadorO),
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic
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
            "¡$ganador Ganaste!"
        }
        state.isDraw -> "¡Es un empate!"
        else -> "Turno de: ${if (state.currentPlayer == Player.X) jugadorX.nombres else jugadorO.nombres}"
    }
}

@Composable
private fun BotonesJuego(onExitGame: () -> Unit, gameViewModel: GameViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onExitGame,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Salir")
        }

        Button(
            onClick = { gameViewModel.onAction(GameAction.PlayAgain) },
            colors = ButtonDefaults.buttonColors(containerColor = BlueCustom)
        ) {
            Text("Volver a Jugar")
        }
    }
}