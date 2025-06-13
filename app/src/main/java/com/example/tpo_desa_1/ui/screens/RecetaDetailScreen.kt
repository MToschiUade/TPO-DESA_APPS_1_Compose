package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tpo_desa_1.data.db.AppDatabase
import com.example.tpo_desa_1.repository.DetallesRecetaRepository
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.ui.components.PasoRecetaCard
import com.example.tpo_desa_1.viewmodel.RecetaViewModel
import com.example.tpo_desa_1.viewmodel.RecetaViewModelFactory
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar
import com.example.tpo_desa_1.ui.components.formatTiempo
import com.example.tpo_desa_1.viewmodel.DetallesRecetaViewModel
import com.example.tpo_desa_1.viewmodel.DetallesRecetaViewModelFactory
import com.example.tpo_desa_1.ui.components.ComentarioCard

@Composable
fun RecetaDetailScreen(
    recetaId: Int,
    usuarioActual: String?, // ✅ ahora puede ser null
    navController: NavController
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val recetaDao = db.recetaDao()
    val comentarioDao = db.comentarioDao()
    val pasoDao = db.pasoRecetaDao()

    val recetaRepository = remember { RecetaRepository(recetaDao) }
    val detallesRepository = remember {
        DetallesRecetaRepository(comentarioDao, pasoDao)
    }

    val recetaViewModel: RecetaViewModel = viewModel(
        factory = RecetaViewModelFactory(recetaRepository)
    )

    val detallesViewModel: DetallesRecetaViewModel = viewModel(
        factory = DetallesRecetaViewModelFactory(detallesRepository)
    )

    val receta by recetaViewModel.obtenerPorId(recetaId)
    val comentarios = detallesViewModel.comentarios
    val pasos = detallesViewModel.pasos

    LaunchedEffect(recetaId) {
        detallesViewModel.cargarDatos(recetaId)
    }

    ScreenWithBottomBar(navController = navController) { padding ->
        receta?.let { r ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                item {
                    AsyncImage(
                        model = r.imagenPortadaUrl,
                        contentDescription = r.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Nombre: ${r.nombre}", style = MaterialTheme.typography.titleLarge)
                    Text("Autor: ${r.autor}")
                    Text("Tiempo: ${formatTiempo(r.tiempo)}")
                    Text("Estado: ${r.estado}")
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    Text("Pasos", style = MaterialTheme.typography.titleMedium)
                }
                items(pasos) { paso ->
                    PasoRecetaCard(paso)
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    Text("Comentarios", style = MaterialTheme.typography.titleMedium)
                }
                items(comentarios) { comentario ->
                    ComentarioCard(
                        comentario = comentario,
                        usuarioActual = usuarioActual,
                        onEliminar = { id ->
                            detallesViewModel.eliminarComentario(id, usuarioActual) {}
                        }
                    )
                }
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Cargando receta...")
        }
    }
}

