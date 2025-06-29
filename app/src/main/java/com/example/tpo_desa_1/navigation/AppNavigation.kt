package com.example.tpo_desa_1.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar
import com.example.tpo_desa_1.ui.screens.*
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    object Splash       : Screen("splash")
    object Home         : Screen("home",     "Inicio",   Icons.Default.Home)
    object Recipes      : Screen("recipes",  "Recetas",  Icons.Default.MenuBook)
    object Saved        : Screen("saved",    "Guardadas",Icons.Default.Bookmark)
    object Profile      : Screen("profile",  "Perfil",   Icons.Default.Person)
    object SessionSwitch: Screen("session",  "Sesión")
}

@Composable
fun AppNavigation(
    sessionViewModel: SessionViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // 1) Siempre ciego → Home
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        // 2) Home siempre accesible
        composable(Screen.Home.route) {
            HomeScreen(navController, sessionViewModel)
        }

        // 3) Pantallas protegidas
        composable(Screen.Recipes.route) {
            RequireLogin(sessionViewModel, navController) {
                ScreenWithBottomBar(navController) { inner ->
                    RecipesScreen(
                        navController = navController,
                        sessionViewModel = sessionViewModel,
                        modifier = Modifier.padding(inner)
                    )
                }
            }
        }
        composable(Screen.Saved.route) {
            RequireLogin(sessionViewModel, navController) {
                SavedScreen(navController, sessionViewModel)
            }
        }
        composable(Screen.Profile.route) {
            RequireLogin(sessionViewModel, navController) {
                ProfileScreen(navController, sessionViewModel)
            }
        }

        // 4) Login / registro
        composable(Screen.SessionSwitch.route) {
            LoginSessionScreen(navController, sessionViewModel)
        }

        // 5) Rutas libres
        composable("detalle_receta/{recetaId}") { back ->
            back.arguments?.getString("recetaId")?.toIntOrNull()?.let { id ->
                RecetaDetailScreen(id, usuarioActual = null, navController)
            }
        }
        composable("crear_receta") {
            CrearRecetaScreen(navController)
        }
    }
}

@Composable
fun RequireLogin(
    sessionViewModel: SessionViewModel,
    navController: NavController,
    content: @Composable () -> Unit
) {
    // 1) Convertir Flow→State<T>
    val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState(initial = false)
    val alias      by sessionViewModel.alias     .collectAsState(initial = null)
    // 2) Redirigir si NO hay sesión y no estamos en Login/Splash
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    LaunchedEffect(isLoggedIn, alias, currentRoute) {
        val needsLogin = !isLoggedIn || alias == null
        val onAuthScreen = currentRoute == Screen.SessionSwitch.route
                || currentRoute == Screen.Splash.route
        if (needsLogin && !onAuthScreen) {
            navController.navigate(Screen.SessionSwitch.route) {
                popUpTo(Screen.Home.route)
                launchSingleTop = true
            }
        }
    }
    // 3) Solo mostrar contenido si hay sesión
    if (isLoggedIn && alias != null) {
        content()
    }
}
