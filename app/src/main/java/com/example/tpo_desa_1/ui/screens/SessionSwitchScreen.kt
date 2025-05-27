package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar
import com.example.tpo_desa_1.viewmodel.SessionViewModel

@Composable
fun SessionSwitchScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    val isLoggedIn by sessionViewModel.isLoggedIn

    ScreenWithBottomBar(navController = navController) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isLoggedIn) "Bienvenido" else "Para esta funcion debe iniciar sesión",
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = {
                        if (isLoggedIn) {
                            sessionViewModel.logOut()
                        } else {
                            sessionViewModel.logIn()
                        }
                    }
                ) {
                    Text(if (isLoggedIn) "Cerrar sesión" else "Iniciar sesión")
                }
            }
        }
    }
}
