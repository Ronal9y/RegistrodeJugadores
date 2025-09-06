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

        try {
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
                                    // VERSIÓN SIMPLIFICADA - elimina parámetros temporalmente
                                    navController.navigate(Routes.GAME_SCREEN)
                                },
                                onNavigate = { route ->
                                    if (route == Routes.JUGADOR_LIST) {
                                        navController.popBackStack()
                                    }
                                }
                            )
                        }

                        // VERSIÓN SIMPLIFICADA - sin parámetros
                        composable(Routes.GAME_SCREEN) {
                            // Jugadores de prueba
                            val jugadorX = Jugador(id = 1, nombres = "Jugador 1", partidas = 0)
                            val jugadorO = Jugador(id = 2, nombres = "Jugador 2", partidas = 0)

                            GameScreen(
                                jugadorX = jugadorX,
                                jugadorO = jugadorO,
                                onPartidaTerminada = { navController.popBackStack() },
                                onExitGame = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("CRASH EN MAIN ACTIVITY: ${e.message}")
            e.printStackTrace()
            // Muestra un error simple en lugar de crashear
            setContent {
                Text("Error: ${e.message}")
            }
        }
    }
}