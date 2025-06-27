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
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.data.db.AppDatabase
import androidx.compose.ui.graphics.Color
import com.example.tpo_desa_1.repository.UsuarioRepository
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import com.example.tpo_desa_1.viewmodel.SessionViewModelFactory

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val recetaDao = AppDatabase.getDatabase(context).recetaDao()
    val recetaRepository = remember { RecetaRepository(recetaDao) }
    val viewModel: RecetaViewModel = viewModel(factory = RecetaViewModelFactory(recetaRepository))
    val recetasAprobadas by viewModel.recetasAprobadas
    val recetasAprobadasRecientes by viewModel.recetasAprobadasRecientes

    val usuarioDao = AppDatabase.getDatabase(context).usuarioDao()
    val usuarioRepository = remember { UsuarioRepository(usuarioDao) }
    val sessionViewModel: SessionViewModel = viewModel(factory = SessionViewModelFactory(usuarioRepository))

    var search by remember { mutableStateOf("") }

    // Orden
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Nombre") }
    val opcionesOrden = listOf("Nombre", "Más nuevas", "Usuario")

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

                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { expanded = true }
                    ) {
                        Text(
                            text = "Ordenar por: $selectedOption",
                            modifier = Modifier.padding(end = 8.dp)
                        )
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
                modifier = Modifier.weight(1f),
                navController = navController
            )
        }
    }
}