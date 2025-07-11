package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpo_desa_1.ui.components.RecipeListSection
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar
import com.example.tpo_desa_1.viewmodel.PuntajeViewModel
import com.example.tpo_desa_1.viewmodel.PuntajeViewModelFactory
import com.example.tpo_desa_1.viewmodel.RecetaViewModel
import com.example.tpo_desa_1.viewmodel.RecetaViewModelFactory
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import kotlinx.coroutines.flow.map

@Composable
fun RecipesScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val recetaViewModel: RecetaViewModel = viewModel(
        factory = RecetaViewModelFactory(context)
    )

    val alias by sessionViewModel.alias.collectAsState(initial = null)
    val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState(initial = false)
    val recetas = recetaViewModel.recetasDelUsuario.value

    val token by sessionViewModel.accessToken.collectAsState(initial = null)

    val puntajeViewModel: PuntajeViewModel = viewModel(
        factory = PuntajeViewModelFactory(context)
    )


    LaunchedEffect(token) {
        token?.let { recetaViewModel.cargarMisRecetas(it) }
    }

    LaunchedEffect(recetas) {
        if (recetas.isNotEmpty()) {
            puntajeViewModel.cargarComentariosDe(recetas.map { it.id })
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Título y botón
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mis Recetas", style = MaterialTheme.typography.headlineSmall)

                IconButton(
                    onClick = { navController.navigate("crear_receta") },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color = Color(0xFF00A86B))
                        .shadow(2.dp, RoundedCornerShape(50))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar receta",
                        tint = Color.White
                    )
                }
            }

            // Contenido
            when {
                !isLoggedIn || alias == null -> {
                    Text("Iniciá sesión para ver tus recetas.")
                }

                recetas.isEmpty() -> {
                    Text("Todavía no tenés recetas creadas.")
                }

                else -> {
                    RecipeListSection(
                        recetas = recetas,
                        titulo = "Tus recetas creadas",
                        mostrarEstado = true,
                        mostrarPuntaje = true,
                        puntajes = puntajesPorReceta, // 👈 nuevo
                        navController = navController
                    )
                }
            }
        }
    }
}


