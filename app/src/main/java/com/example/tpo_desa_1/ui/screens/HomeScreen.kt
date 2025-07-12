package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpo_desa_1.ui.components.BottomNavBar
import com.example.tpo_desa_1.ui.components.RecommendationCarousel
import com.example.tpo_desa_1.ui.components.RecipeListSection
import com.example.tpo_desa_1.viewmodel.RecetaViewModel
import com.example.tpo_desa_1.viewmodel.RecetaViewModelFactory
import com.example.tpo_desa_1.data.db.AppDatabase
import androidx.compose.ui.graphics.Color
import com.example.tpo_desa_1.repository.UsuarioRepository
import com.example.tpo_desa_1.viewmodel.PuntajeViewModel
import com.example.tpo_desa_1.viewmodel.PuntajeViewModelFactory
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import com.example.tpo_desa_1.viewmodel.SessionViewModelFactory
import kotlinx.coroutines.flow.map


@Composable
fun HomeScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    val context = LocalContext.current
    val viewModel: RecetaViewModel = viewModel(
        factory = RecetaViewModelFactory(context)
    )

    val recetasAprobadasRecientes by viewModel.recetasAprobadasRecientes
    val recetasAprobadas by viewModel.recetasAprobadas

    val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState(initial = false)

    var search by remember { mutableStateOf("") }

    // Ordenamiento
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Nombre") }
    val opcionesOrden = listOf("Nombre", "Más nuevas", "Usuario")

    val puntajeViewModel: PuntajeViewModel = viewModel(
        factory = PuntajeViewModelFactory(context)
    )

// Dispara la carga de recetas aprobadas al iniciar
    LaunchedEffect(Unit) {
        viewModel.cargarRecientesAprobadas()
        viewModel.cargarRecetasAprobadas()
    }

// Cuando recetasAprobadas se actualiza y tiene data, cargamos los puntajes
    LaunchedEffect(recetasAprobadas) {
        if (recetasAprobadas.isNotEmpty()) {
            puntajeViewModel.cargarComentariosDe(recetasAprobadas.map { it.id })
        }
    }


    val puntajesPorReceta by puntajeViewModel.mapaComentarios
        .map { mapa ->
            mapa.mapValues { (_, comentarios) ->
                val puntuaciones = comentarios.mapNotNull { it.puntaje }
                if (puntuaciones.isNotEmpty()) puntuaciones.average().toInt() else 0
            }
        }.collectAsState(initial = emptyMap())


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(navController = navController, isLoggedIn = isLoggedIn)
        },
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    placeholder = { Text("Busca las mejores recetas...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { expanded = true }
                    ) {
                        Text("Ordenar por: $selectedOption", modifier = Modifier.padding(end = 8.dp))
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Expandir menú")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        opcionesOrden.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    selectedOption = opcion
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            RecommendationCarousel(
                recetas = recetasAprobadasRecientes,
                navController = navController
            )

            Spacer(modifier = Modifier.height(8.dp))

            val recetasFiltradas = recetasAprobadas
                .filter {
                    it.nombre.contains(search, ignoreCase = true) ||
                            it.autor.contains(search, ignoreCase = true)
                }
                .let {
                    when (selectedOption) {
                        "Más nuevas" -> it.sortedByDescending { receta -> receta.fechaRevision ?: 0L }
                        "Usuario" -> it.sortedBy { receta -> receta.autor }
                        else -> it.sortedBy { receta -> receta.nombre }
                    }
                }

            RecipeListSection(
                recetas = recetasFiltradas,
                titulo = "Explorá recetas de la comunidad",
                maxItems = 4,
                mostrarEstado = false,
                mostrarPuntaje = true,
                mostrarAutor = true,
                puntajes = puntajesPorReceta, // 👈 nuevo
                modifier = Modifier.weight(1f),
                navController = navController
            )

        }
    }
}
