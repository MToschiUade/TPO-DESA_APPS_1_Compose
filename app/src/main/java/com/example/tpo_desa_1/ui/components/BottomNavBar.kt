package com.example.tpo_desa_1.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tpo_desa_1.navigation.Screen

@Composable
fun BottomNavBar(
    navController: NavController,
    isLoggedIn: Boolean
) {
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
            val isProtected = screen in listOf(Screen.Recipes, Screen.Saved, Screen.Profile)

            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        if (isProtected && !isLoggedIn) {
                            println("🔐 BottomNav: acceso protegido a ${screen.route} → redirigiendo a login")
                            navController.navigate(Screen.SessionSwitch.route) {
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
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

