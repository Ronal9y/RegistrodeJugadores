package edu.ucne.registrodejugadores

import android.R.attr.type
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

                                navController.navigate("${Routes.GAME_SCREEN}?jugadorXId=${jugadorX.id}&jugadorOId=${jugadorO.id}")
                            },
                            onNavigate = { route ->
                                if (route == Routes.JUGADOR_LIST) {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }

                    composable(
                        route = "${Routes.GAME_SCREEN}?jugadorXId={jugadorXId}&jugadorOId={jugadorOId}",
                        arguments = listOf(
                            navArgument("jugadorXId") { type = NavType.IntType },
                            navArgument("jugadorOId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val jugadorXId = backStackEntry.arguments?.getInt("jugadorXId")
                        val jugadorOId = backStackEntry.arguments?.getInt("jugadorOId")

                        GameScreen(
                            jugadorXId = jugadorXId,
                            jugadorOId = jugadorOId,
                            onPartidaTerminada = { navController.popBackStack() },
                            onExitGame = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}