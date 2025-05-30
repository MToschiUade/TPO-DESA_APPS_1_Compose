package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpo_desa_1.data.db.AppDatabase
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.viewmodel.RecetaViewModel
import com.example.tpo_desa_1.viewmodel.RecetaViewModelFactory
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar

@Composable
fun RecetaDetailScreen(recetaId: Long, navController: NavController) {
    val context = LocalContext.current
    val recetaDao = AppDatabase.getDatabase(context).recetaDao()
    val repository = remember { RecetaRepository(recetaDao) }

    val viewModel: RecetaViewModel = viewModel(
        factory = RecetaViewModelFactory(repository)
    )

    val receta by viewModel.obtenerPorId(recetaId)

    ScreenWithBottomBar(navController = navController) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            receta?.let {
                Text(text = "Nombre: ${it.nombre}")
                Text(text = "Autor: ${it.alias}")
                Text(text = "Tiempo: ${it.tiempo} minutos")
                Text(text = "Estado: ${it.estado}")
                Text(text = "Descripción: ${it.descripcion}")
            } ?: Text(text = "Cargando receta...")
        }
    }
}
