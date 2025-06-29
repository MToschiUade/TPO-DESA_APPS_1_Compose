package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar
import com.example.tpo_desa_1.viewmodel.SessionViewModel

@Composable
fun SavedScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    val isLoggedInState = sessionViewModel.isLoggedIn.collectAsState(initial = false)
    val isLoggedIn = isLoggedInState.value

    ScreenWithBottomBar(navController = navController) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoggedIn) {
                Text("Guardadas")
            } else {
                Text("Iniciá sesión para ver tus recetas guardadas")
            }
        }
    }

}
