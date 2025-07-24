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
import com.example.tpo_desa_1.ui.screens.CrearRecetaScreen
import com.example.tpo_desa_1.viewmodel.SessionViewModelFactory
import com.example.tpo_desa_1.ui.screens.PasswordResetScreen
import com.example.tpo_desa_1.ui.screens.VerifyCodeScreen
import com.example.tpo_desa_1.ui.screens.NewPasswordScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.tpo_desa_1.ui.screens.*
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.tpo_desa_1.data.source.remote.ApiService
import com.example.tpo_desa_1.viewmodel.CrearRecetaViewModel


sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null,
    val requiresAuth: Boolean = false
) {
    object Splash       : Screen("splash")
    object Home         : Screen("home",     "Inicio",   Icons.Default.Home)
    object Recipes      : Screen("recipes",  "Recetas",  Icons.Default.MenuBook,  true)
    object Saved        : Screen("saved",    "Guardadas",Icons.Default.Bookmark,  true)
    object Profile      : Screen("profile",  "Perfil",   Icons.Default.Person,    true)
    object SessionSwitch: Screen("session",  "Sesión")
}

@Composable
fun AppNavigation(
    sessionViewModel: SessionViewModel,
    crearRecetaViewModel: CrearRecetaViewModel,
    apiService: ApiService,
    navController: NavHostController = rememberNavController()
) {
    val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState(initial = false)
    val alias by sessionViewModel.alias.collectAsState(initial = null)

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash
        composable(Screen.Splash.route) {
            SplashScreen(navController, sessionViewModel)
        }

        // Pública
        composable(Screen.Home.route) {
            HomeScreen(navController, sessionViewModel)
        }

        // Protegidas
        composable(Screen.Recipes.route) {
            if (isLoggedIn && alias != null) {
                ScreenWithBottomBar(navController, sessionViewModel) { innerPadding ->
                    RecipesScreen(navController, sessionViewModel, Modifier.padding(innerPadding))
                }
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.SessionSwitch.route) { launchSingleTop = true }
                }
            }
        }

        composable(Screen.Saved.route) {
            if (isLoggedIn && alias != null) {
                SavedScreen(navController, sessionViewModel)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.SessionSwitch.route) { launchSingleTop = true }
                }
            }
        }

        composable(Screen.Profile.route) {
            if (isLoggedIn && alias != null) {
                ProfileScreen(navController, sessionViewModel)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.SessionSwitch.route) { launchSingleTop = true }
                }
            }
        }

        // Login
        composable(Screen.SessionSwitch.route) {
            LoginSessionScreen(navController, sessionViewModel)
        }

        // Rutas libres (no requieren auth)
        composable("detalle_receta/{recetaId}") { back ->
            back.arguments?.getString("recetaId")?.toIntOrNull()?.let { id ->
                RecetaDetailScreen(id, usuarioActual = alias, navController)
            }
        }

        composable("crear_receta") {
            CrearRecetaScreen(
                navController = navController,
                viewModel = crearRecetaViewModel,
                apiService = apiService
            )
        }

        composable("password_reset") {
            PasswordResetScreen(navController = navController)
        }

        composable("verify_code/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerifyCodeScreen(navController = navController, email = email)
        }

        composable(
            route = "new_password/{email}/{totpCode}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("totpCode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val totpCode = backStackEntry.arguments?.getString("totpCode") ?: ""
            NewPasswordScreen(navController = navController, email = email, totpCode = totpCode)
        }
    }
}