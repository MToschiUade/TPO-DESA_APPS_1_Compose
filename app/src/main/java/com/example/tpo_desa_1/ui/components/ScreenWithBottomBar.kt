package com.example.tpo_desa_1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.navigation.NavController

@Composable
fun ScreenWithBottomBar(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    androidx.compose.material3.Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController) },
        contentWindowInsets = WindowInsets.systemBars,
        content = content
    )
}
