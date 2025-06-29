package com.example.tpo_desa_1.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
    val icon: ImageVector? = null
) {
    data object Splash : Screen("splash")
    data object Home : Screen("home", "Inicio", Icons.Default.Home)
    data object Recipes : Screen("recipes", "Recetas", Icons.Default.MenuBook)
    data object Saved : Screen("saved", "Guardadas", Icons.Default.Bookmark)
    data object Profile : Screen("profile", "Perfil", Icons.Default.Person)
    data object SessionSwitch : Screen("session", "Sesión")
}

@Composable
fun AppNavigation(
    sessionViewModel: SessionViewModel,
    navController: NavHostController = rememberNavController()
) {
    val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState(initial = false)
    val alias by sessionViewModel.alias.collectAsState(initial = null)

    LaunchedEffect(isLoggedIn, alias) {
        println("🔍 AppNavigation -> isLoggedIn: $isLoggedIn | alias: $alias")
        if (!isLoggedIn || alias == null) {
            navController.navigate(Screen.Splash.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController, sessionViewModel)
        }

        composable(Screen.Recipes.route) {
            if (isLoggedIn && alias != null) {
                ScreenWithBottomBar(navController) { innerPadding ->
                    RecipesScreen(
                        navController = navController,
                        sessionViewModel = sessionViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.SessionSwitch.route) {
                        popUpTo(Screen.Recipes.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Screen.Saved.route) {
            if (isLoggedIn && alias != null) {
                SavedScreen(navController, sessionViewModel)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.SessionSwitch.route) {
                        popUpTo(Screen.Saved.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Screen.Profile.route) {
            if (isLoggedIn && alias != null) {
                ProfileScreen(navController, sessionViewModel)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.SessionSwitch.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Screen.SessionSwitch.route) {
            LoginSessionScreen(navController, sessionViewModel)
        }

        composable("detalle_receta/{recetaId}") { backStackEntry ->
            val recetaId = backStackEntry.arguments?.getString("recetaId")?.toIntOrNull()
            recetaId?.let {
                RecetaDetailScreen(
                    recetaId = it,
                    usuarioActual = alias,
                    navController = navController
                )
            }
        }

        composable("crear_receta") {
            CrearRecetaScreen(navController)
        }
    }

}

