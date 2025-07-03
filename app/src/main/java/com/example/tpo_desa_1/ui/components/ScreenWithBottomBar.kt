package com.example.tpo_desa_1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.navigation.NavController
import androidx.compose.material3.Scaffold
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import androidx.compose.runtime.*

// Versión 1: para pantallas que ya tienen el viewModel
@Composable
fun ScreenWithBottomBar(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    content: @Composable (PaddingValues) -> Unit
) {
    val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState(initial = false)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController, isLoggedIn) },
        contentWindowInsets = WindowInsets.systemBars,
        content = content
    )
}
// Versión 2: para pantallas públicas
@Composable
fun ScreenWithBottomBar(
    navController: NavController,
    isLoggedIn: Boolean,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController, isLoggedIn) },
        contentWindowInsets = WindowInsets.systemBars,
        content = content
    )
}

