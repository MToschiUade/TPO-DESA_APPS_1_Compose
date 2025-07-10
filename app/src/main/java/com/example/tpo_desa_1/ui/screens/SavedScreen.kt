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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.tpo_desa_1.viewmodel.PuntajeViewModel
import com.example.tpo_desa_1.viewmodel.PuntajeViewModelFactory
import kotlinx.coroutines.flow.map


@Composable
fun SavedScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val recetaViewModel: RecetaViewModel = viewModel(
        factory = RecetaViewModelFactory(context)
    )

    val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState(initial = false)

    val recetasGuardadas by recetaViewModel.recetasGuardadas
    var search by remember { mutableStateOf("") }


    // ❗ Pendiente: se asume que el ViewModel puede cargar recetas guardadas por alias
    val token by sessionViewModel.accessToken.collectAsState()

    val puntajeViewModel: PuntajeViewModel = viewModel(
        factory = PuntajeViewModelFactory(context)
    )

    LaunchedEffect(isLoggedIn, token) {
        if (isLoggedIn && !token.isNullOrBlank()) {
            recetaViewModel.cargarRecetasGuardadas(token!!)
        }
    }

    LaunchedEffect(recetasGuardadas) {
        if (recetasGuardadas.isNotEmpty()) {
            puntajeViewModel.cargarComentariosDe(recetasGuardadas.map { it.id })
        }
    }

    val puntajesPorReceta by puntajeViewModel.mapaComentarios
        .map { mapa ->
            mapa.mapValues { (_, comentarios) ->
                val puntajes = comentarios.mapNotNull { it.puntaje }
                if (puntajes.isNotEmpty()) puntajes.average().toInt() else 0
            }
        }.collectAsState(initial = emptyMap())


    ScreenWithBottomBar(navController = navController, sessionViewModel = sessionViewModel) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (!isLoggedIn) {
                Text("Iniciá sesión para ver tus recetas guardadas.")
            } else {
                if (recetasGuardadas.isEmpty()) {
                    Text("Todavía no guardaste recetas.") // ❗ Pendiente: ver cómo obtener recetas guardadas reales desde backend
                } else {
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

                    val recetasFiltradas = recetasGuardadas.filter {
                        it.nombre.contains(search, ignoreCase = true)
                    }

                    RecipeListSection(
                        recetas = recetasFiltradas,
                        titulo = "",
                        mostrarEstado = false,
                        mostrarPuntaje = true,
                        puntajes = puntajesPorReceta, // 👈 NUEVO
                        navController = navController
                    )
                }
            }
        }
    }
}
