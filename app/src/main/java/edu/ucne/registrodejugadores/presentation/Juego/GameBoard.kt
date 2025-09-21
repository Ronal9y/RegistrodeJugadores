package edu.ucne.registrodejugadores.presentation.Juego

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun GameBoard(
    board: Array<Player?>,
    onCellClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        BoardBase()


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            for (row in 0 until 3) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0 until 3) {
                        val index = row * 3 + col
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .clickable { onCellClicked(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            when (board[index]) {
                                Player.X -> Cross()
                                Player.O -> Circle()
                                null -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BoardBase(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .size(300.dp)
            .padding(10.dp),
    ) {
        drawLine(
            color = Color.Gray,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width / 3, y = 0f),
            end = Offset(x = size.width / 3, y = size.height)
        )
        drawLine(
            color = Color.Gray,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width * 2 / 3, y = 0f),
            end = Offset(x = size.width * 2 / 3, y = size.height)
        )
        drawLine(
            color = Color.Gray,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height / 3),
            end = Offset(x = size.width, y = size.height / 3)
        )
        drawLine(
            color = Color.Gray,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height * 2 / 3),
            end = Offset(x = size.width, y = size.height * 2 / 3)
        )
    }
}