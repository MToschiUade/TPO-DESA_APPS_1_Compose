package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpo_desa_1.data.db.AppDatabase
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.viewmodel.RecetaViewModel
import com.example.tpo_desa_1.viewmodel.RecetaViewModelFactory
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar
import com.example.tpo_desa_1.ui.components.RecipeListSection

@Composable
fun SavedScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val recetaDao = AppDatabase.getDatabase(context).recetaDao()
    val recetaRepo = RecetaRepository(recetaDao)
    val factory = RecetaViewModelFactory(recetaRepo)
    val recetaViewModel: RecetaViewModel = viewModel(factory = factory)

    val usuario = sessionViewModel.usuarioLogueado.value
    val recetasGuardadas by recetaViewModel.recetasGuardadas
    var search by remember { mutableStateOf("") }

    LaunchedEffect(usuario) {
        usuario?.let {
            recetaViewModel.cargarRecetasGuardadas(it.recetasGuardadas)
        }
    }

    ScreenWithBottomBar(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (usuario == null) {
                Text("Iniciá sesión para ver tus recetas guardadas.")
            } else {
                if (recetasGuardadas.isEmpty()) {
                    Text("Todavía no guardaste recetas.")
                } else {
                    // Título con cantidad
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mis Recetas Guardadas (${recetasGuardadas.size})",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    // Campo de búsqueda con estilo del home
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        placeholder = { Text("Buscar recetas...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar receta"
                            )
                        },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp)
                            .padding(bottom = 12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFF00A86B),
                            focusedBorderColor = Color(0xFF00A86B),
                            unfocusedContainerColor = Color(0xFFF8F8F8),
                            focusedContainerColor = Color(0xFFFFFFFF)

                        )
                    )

                    // Filtrar recetas
                    val recetasFiltradas = recetasGuardadas.filter {
                        it.nombre.contains(search, ignoreCase = true)
                    }

                    // Mostrar recetas
                    RecipeListSection(
                        recetas = recetasFiltradas,
                        titulo = "",
                        mostrarEstado = false,
                        mostrarPuntaje = true,
                        navController = navController
                    )
                }
            }
        }
    }
}