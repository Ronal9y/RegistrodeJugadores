package edu.ucne.registrodejugadores

import android.R.attr.type
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
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
import edu.ucne.registrodejugadores.ui.screen.partidas_screen.PartidasScreen
import edu.ucne.registrodejugadores.ui.theme.RegistrodeJugadoresTheme
import edu.ucne.registrodejugadores.ui.theme.screen.Juego.GameScreen
import edu.ucne.registrodejugadores.ui.theme.util.Routes

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RegistrodeJugadoresTheme {
                val navController = rememberNavController()
                val currentDestination = navController.currentDestination?.route ?: Routes.JUGADOR_FORM

                Scaffold(
                    bottomBar = {
                        if (currentDestination != Routes.GAME_SCREEN
                           ) {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.PersonAdd, contentDescription = "Registro") },
                                    label = { Text("Registro") },
                                    selected = currentDestination == Routes.JUGADOR_FORM,
                                    onClick = { navController.navigate(Routes.JUGADOR_FORM) }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.List, contentDescription = "Jugadores") },
                                    label = { Text("Jugadores") },
                                    selected = currentDestination == Routes.JUGADOR_LIST,
                                    onClick = { navController.navigate(Routes.JUGADOR_LIST) }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.SportsEsports, contentDescription = "Jugar") },
                                    label = { Text("Jugar") },
                                    selected = currentDestination == Routes.SELECCION_JUGADORES,
                                    onClick = { navController.navigate(Routes.SELECCION_JUGADORES) }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
                                    label = { Text("Historial") },
                                    selected = currentDestination == Routes.PARTIDAS_SCREEN,
                                    onClick = { navController.navigate(Routes.PARTIDAS_SCREEN) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.JUGADOR_FORM,
                        modifier = Modifier.padding(innerPadding)
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
                            route = "${Routes.GAME_SCREEN}?partidaId={partidaId}&jugadorXId={jugadorXId}&jugadorOId={jugadorOId}",
                            arguments = listOf(
                                navArgument("partidaId") { type = NavType.IntType; defaultValue = -1 },
                                navArgument("jugadorXId") { type = NavType.IntType; defaultValue = -1 },
                                navArgument("jugadorOId") { type = NavType.IntType; defaultValue = -1 }
                            )
                        ) { backStackEntry ->
                            val partidaId = backStackEntry.arguments?.getInt("partidaId") ?: -1
                            val jugadorXId = backStackEntry.arguments?.getInt("jugadorXId").takeIf { it != -1 }
                            val jugadorOId = backStackEntry.arguments?.getInt("jugadorOId").takeIf { it != -1 }

                            GameScreen(
                                partidaId = partidaId,
                                jugadorXId = jugadorXId,
                                jugadorOId = jugadorOId,
                                onExitGame = { navController.popBackStack() }
                            )
                        }

                        composable(Routes.PARTIDAS_SCREEN) {
                            PartidasScreen(
                                navController = navController,
                                onNavigateToGame = { partidaId ->
                                    navController.navigate("${Routes.GAME_SCREEN}?partidaId=$partidaId")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}