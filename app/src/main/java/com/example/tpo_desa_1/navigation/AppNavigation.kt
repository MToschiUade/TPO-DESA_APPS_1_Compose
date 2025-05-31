package com.example.tpo_desa_1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tpo_desa_1.ui.screens.SplashScreen
import com.example.tpo_desa_1.ui.screens.HomeScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.tpo_desa_1.ui.screens.RecipesScreen
import com.example.tpo_desa_1.ui.screens.SavedScreen
import com.example.tpo_desa_1.ui.screens.ProfileScreen
import com.example.tpo_desa_1.ui.screens.RecetaDetailScreen
import com.example.tpo_desa_1.ui.screens.SessionSwitchScreen
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar
import com.example.tpo_desa_1.ui.screens.CrearRecetaScreen


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
    data object SessionSwitch : Screen("session", "Sesión", null)

}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    sessionViewModel: SessionViewModel
) {
    val usuarioLogueado by sessionViewModel.usuarioLogueado

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.Recipes.route) {
            val usuario = sessionViewModel.usuarioLogueado.value

            if (usuario != null) {
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
            if (usuarioLogueado != null) {
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
            if (usuarioLogueado != null) {
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
            SessionSwitchScreen(navController, sessionViewModel)
        }

        composable("detalle_receta/{recetaId}") { backStackEntry ->
            val recetaId = backStackEntry.arguments?.getString("recetaId")?.toIntOrNull()
            recetaId?.let {
                RecetaDetailScreen(recetaId = it, navController = navController)
            }
        }

        composable("crear_receta") {
            CrearRecetaScreen(navController)
        }
    }
}
