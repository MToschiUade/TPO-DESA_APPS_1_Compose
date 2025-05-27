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
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.tpo_desa_1.ui.screens.RecipesScreen
import com.example.tpo_desa_1.ui.screens.SavedScreen
import com.example.tpo_desa_1.ui.screens.ProfileScreen



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
}


@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
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

        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Recipes.route) { RecipesScreen(navController) }
        composable(Screen.Saved.route) { SavedScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }

    }
}
