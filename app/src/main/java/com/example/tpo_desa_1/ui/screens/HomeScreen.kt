package com.example.tpo_desa_1.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.tpo_desa_1.ui.components.BottomNavBar
import com.example.tpo_desa_1.ui.components.RecommendationCarousel
import com.example.tpo_desa_1.ui.components.RecipeListSection
import com.example.tpo_desa_1.viewmodel.RecetaViewModel
import com.example.tpo_desa_1.viewmodel.RecetaViewModelFactory
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.data.db.AppDatabase

import androidx.compose.runtime.getValue



@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val recetaDao = AppDatabase.getDatabase(context).recetaDao()
    val repository = remember { RecetaRepository(recetaDao) }
    val viewModel: RecetaViewModel = viewModel(
        factory = RecetaViewModelFactory(repository)
    )

    val recetasAprobadasRecientes by viewModel.recetasAprobadasRecientes
    val recetasAprobadas by viewModel.recetasAprobadas


    LaunchedEffect(Unit) {
        viewModel.cargarRecientesAprobadas()
        viewModel.cargarRecetasAprobadas()
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController) },
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Busca las mejores recetas...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            RecommendationCarousel(recetas = recetasAprobadasRecientes)
            Spacer(modifier = Modifier.height(8.dp))
            RecipeListSection(recetas = recetasAprobadas.shuffled())

        }
    }
}

