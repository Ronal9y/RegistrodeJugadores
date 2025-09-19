package edu.ucne.registrodejugadores.ui.theme.screen.Juego

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrodejugadores.domain.model.Jugador
import edu.ucne.registrodejugadores.domain.repository.JugadorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: JugadorRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    var lastPlayer: Player? = null
        private set

    fun loadBoard(csv: String) {
        _state.value = GameState(board = csv.toPlayerArray())
    }

    fun getBoardCsv(): String = state.value.board.toCsv()

    fun incrementarPartidas(jugadorId: Int?) {
        if (jugadorId == null || jugadorId <= 0) return

        viewModelScope.launch {
            try {
                val jugador = repository.getJugadorById(jugadorId)
                if (jugador != null) {
                    repository.incrementarPartidas(jugadorId)
                    Log.d("GameViewModel", "Partidas incrementadas para jugador ID: $jugadorId")
                } else {
                    Log.w("GameViewModel", "Jugador con ID $jugadorId no encontrado. No se incrementarán partidas.")
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "Error al incrementar partidas para jugador ID: $jugadorId", e)
            }
        }
    }

    fun onAction(action: GameAction) {
        when (action) {
            is GameAction.BoardTapped -> {
                val currentState = _state.value
                if (currentState.board[action.cell] == null && !currentState.hasWon && !currentState.isDraw) {
                    val newBoard = currentState.board.copyOf()
                    newBoard[action.cell] = currentState.currentPlayer

                    val hasWon = checkWin(newBoard, currentState.currentPlayer)
                    val isDraw = !hasWon && newBoard.all { it != null }

                    lastPlayer = currentState.currentPlayer

                    val newState = currentState.copy(
                        board = newBoard,
                        currentPlayer = if (currentState.currentPlayer == Player.X) Player.O else Player.X,
                        hasWon = hasWon,
                        isDraw = isDraw,
                        winLine = if (hasWon) getWinLine(newBoard, currentState.currentPlayer) else null
                    )

                    _state.value = newState

                    if (hasWon) {
                        _state.value = when (currentState.currentPlayer) {
                            Player.X -> newState.copy(xScore = newState.xScore + 1)
                            Player.O -> newState.copy(oScore = newState.oScore + 1)
                            else -> newState
                        }
                        Log.i("GameViewModel", "¡Jugador ${currentState.currentPlayer} ha ganado!")
                    } else if (isDraw) {
                        _state.value = newState.copy(drawScore = newState.drawScore + 1)
                        Log.i("GameViewModel", "¡Empate!")
                    }
                }
            }
            GameAction.PlayAgain -> {
                val currentState = _state.value
                _state.value = GameState(
                    xScore = currentState.xScore,
                    oScore = currentState.oScore,
                    drawScore = currentState.drawScore
                )
                Log.d("GameViewModel", "Juego reiniciado")
            }
        }

    }

    private fun checkWin(board: Array<Player?>, player: Player): Boolean {
        val winPatterns = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )

        return winPatterns.any { pattern -> pattern.all { board[it] == player } }
    }

    private fun getWinLine(board: Array<Player?>, player: Player): WinLine? {
        val winPatterns = mapOf(
            listOf(0, 1, 2) to WinLine.Horizontal1,
            listOf(3, 4, 5) to WinLine.Horizontal2,
            listOf(6, 7, 8) to WinLine.Horizontal3,
            listOf(0, 3, 6) to WinLine.Vertical1,
            listOf(1, 4, 7) to WinLine.Vertical2,
            listOf(2, 5, 8) to WinLine.Vertical3,
            listOf(0, 4, 8) to WinLine.Diagonal1,
            listOf(2, 4, 6) to WinLine.Diagonal2
        )

        return winPatterns.entries.firstOrNull { (pattern, _) -> pattern.all { board[it] == player } }?.value
    }
}

fun Array<Player?>.toCsv(): String =
    joinToString(",") { it?.name ?: "null" }

fun String.toPlayerArray(): Array<Player?> =
    split(",").map { if (it == "null") null else Player.valueOf(it) }.toTypedArray()



data class GameState(
    val board: Array<Player?> = arrayOfNulls(9),
    val currentPlayer: Player = Player.X,
    val xScore: Int = 0,
    val oScore: Int = 0,
    val drawScore: Int = 0,
    val hasWon: Boolean = false,
    val isDraw: Boolean = false,
    val winLine: WinLine? = null,
    val lastPlayer: Player? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (!board.contentEquals(other.board)) return false
        if (currentPlayer != other.currentPlayer) return false
        if (xScore != other.xScore) return false
        if (oScore != other.oScore) return false
        if (drawScore != other.drawScore) return false
        if (hasWon != other.hasWon) return false
        if (isDraw != other.isDraw) return false
        if (winLine != other.winLine) return false
        if (lastPlayer != other.lastPlayer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = board.contentHashCode()
        result = 31 * result + currentPlayer.hashCode()
        result = 31 * result + xScore
        result = 31 * result + oScore
        result = 31 * result + drawScore
        result = 31 * result + hasWon.hashCode()
        result = 31 * result + isDraw.hashCode()
        result = 31 * result + (winLine?.hashCode() ?: 0)
        result = 31 * result + (lastPlayer?.hashCode() ?: 0)
        return result
    }
}

enum class Player {
    X, O
}

sealed class GameAction {
    data class BoardTapped(val cell: Int) : GameAction()
    object PlayAgain : GameAction()
}

enum class WinLine {
    Horizontal1, Horizontal2, Horizontal3,
    Vertical1, Vertical2, Vertical3,
    Diagonal1, Diagonal2
}