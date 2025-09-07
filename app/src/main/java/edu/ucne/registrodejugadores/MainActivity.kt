package edu.ucne.registrodejugadores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registrodejugadores.domain.model.Jugador
import edu.ucne.registrodejugadores.ui.screen.juego.SeleccionJugadoresScreen
import edu.ucne.registrodejugadores.ui.screen.jugador_form.JugadorFormScreen
import edu.ucne.registrodejugadores.ui.screen.jugador_list.JugadorListScreen
import edu.ucne.registrodejugadores.ui.theme.RegistrodeJugadoresTheme
import edu.ucne.registrodejugadores.ui.theme.screen.Juego.GameScreen
import edu.ucne.registrodejugadores.ui.theme.util.Routes

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            RegistrodeJugadoresTheme {
                NavHost(
                    navController = navController,
                    startDestination = Routes.JUGADOR_FORM
                ) {
                    composable(Routes.JUGADOR_FORM) {
                        JugadorFormScreen(
                            onNavigateBack = { navController.navigate(Routes.JUGADOR_LIST) },
                            onNavigateToList = { navController.navigate(Routes.JUGADOR_LIST) }
                        )
                    }

                    composable(Routes.JUGADOR_LIST) {
                        JugadorListScreen(
                            onNavigate = { route ->
                                when (route) {
                                    Routes.JUGADOR_FORM -> navController.navigate(route)
                                    Routes.SELECCION_JUGADORES -> navController.navigate(route)
                                }
                            }
                        )
                    }

                    composable(Routes.SELECCION_JUGADORES) {
                        SeleccionJugadoresScreen(
                            onJugadoresSeleccionados = { jugadorX, jugadorO ->
                                navController.currentBackStackEntry?.savedStateHandle?.set("jugadorX", jugadorX)
                                navController.currentBackStackEntry?.savedStateHandle?.set("jugadorO", jugadorO)
                                navController.navigate(Routes.GAME_SCREEN)
                            },
                            onNavigate = { route ->
                                if (route == Routes.JUGADOR_LIST) {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }

                    composable(Routes.GAME_SCREEN) { backStackEntry ->
                        val jugadorX = backStackEntry.savedStateHandle?.get<Jugador>("jugadorX")
                        val jugadorO = backStackEntry.savedStateHandle?.get<Jugador>("jugadorO")

                        if (jugadorX != null && jugadorO != null) {
                            GameScreen(
                                jugadorX = jugadorX,
                                jugadorO = jugadorO,
                                onPartidaTerminada = { navController.popBackStack() },
                                onExitGame = { navController.popBackStack() }
                            )
                        } else {
                            Text("Error: no se seleccionaron jugadores")
                        }
                    }
                }
            }
        }
    }
}