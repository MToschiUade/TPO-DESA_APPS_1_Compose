package com.example.tpo_desa_1.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tpo_desa_1.navigation.Screen

@Composable
fun BottomNavBar(navController: NavController) {
    val screens = listOf(
        Screen.Home,
        Screen.Recipes,
        Screen.Saved,
        Screen.Profile
    )

    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = Color.White
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        // TODO Revisar que vamos a querer hacer con esta navegación la comentada es la que mostre en el video
//                        navController.navigate(screen.route) {
//                            popUpTo(Screen.Home.route) { saveState = true }
//                            launchSingleTop = true
//                            restoreState = true
//                        }

                        // Esta es que si tocas inicio u otra pantalla no te mantiene la receta que estabas viendo
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }

                    }
                },
                icon = {
                    screen.icon?.let {
                        Icon(it, contentDescription = screen.title ?: "")
                    }
                },
                label = {
                    Text(screen.title ?: "")
                }
            )
        }
    }
}
