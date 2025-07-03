package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tpo_desa_1.data.db.AppDatabase
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.repository.DetallesRecetaRepository
// TODO limpiar código import
import com.example.tpo_desa_1.viewmodel.RecetaViewModel
import com.example.tpo_desa_1.viewmodel.RecetaViewModelFactory
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar
import com.example.tpo_desa_1.ui.components.formatTiempo
import com.example.tpo_desa_1.viewmodel.DetallesRecetaViewModel
import com.example.tpo_desa_1.viewmodel.DetallesRecetaViewModelFactory
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontWeight
import com.example.tpo_desa_1.data.model.PasoReceta
// TODO limpiar código import

import androidx.compose.ui.graphics.Brush


import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material.icons.filled.Star

import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.tpo_desa_1.R
import com.example.tpo_desa_1.data.model.Comentario
import com.example.tpo_desa_1.data.persistence.UserPreferences
import com.example.tpo_desa_1.viewmodel.DestacarRecetaViewModel
import com.example.tpo_desa_1.viewmodel.DestacarRecetaViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RecetaDetailScreen(
    recetaId: Int,
    usuarioActual: String?, // ✅ ahora puede ser null
    navController: NavController
) {
    val context = LocalContext.current
    val recetaViewModel: RecetaViewModel = viewModel(
        factory = RecetaViewModelFactory(context)
    )

    val userPreferences = UserPreferences(context)
    val destacarViewModel: DestacarRecetaViewModel = viewModel(
        factory = DestacarRecetaViewModelFactory(context, userPreferences)
    )


    val db = AppDatabase.getDatabase(context)

    val comentarioDao = db.comentarioDao()
    val pasoDao = db.pasoRecetaDao()

    val detallesRepository = remember {
        DetallesRecetaRepository(comentarioDao, pasoDao)
    }


    val detallesViewModel: DetallesRecetaViewModel = viewModel(
        factory = DetallesRecetaViewModelFactory(detallesRepository)
    )

    val receta by recetaViewModel.obtenerPorId(recetaId)
    val comentarios = detallesViewModel.comentarios
    val pasos = detallesViewModel.pasos

    LaunchedEffect(recetaId) {
        detallesViewModel.cargarDatos(recetaId)
    }

    ScreenWithBottomBar(
        navController = navController,
        isLoggedIn = usuarioActual != null
    ) { padding ->
        receta?.let { r ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                item {
                    EncabezadoReceta(
                        receta = r,
                        usuarioLogueado = usuarioActual != null,
                        onBack = { navController.popBackStack() },
                        destacarViewModel = destacarViewModel
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    IngredientesSection()
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    Text("Pasos", style = MaterialTheme.typography.titleMedium)
                }
                items(pasos) { paso ->
                    PasoCard(paso)
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    PuntajeResumenSection(puntaje = r.puntaje, usuarioActual = usuarioActual)
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    ComentariosSection(
                        comentarios = comentarios,
                        usuarioActual = usuarioActual
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

@Composable
private fun EncabezadoReceta(    receta: Receta,
                                 usuarioLogueado: Boolean,
                                 onBack: () -> Unit,
                                 destacarViewModel: DestacarRecetaViewModel) {
    Box(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = receta.imagenPortadaUrl,
            contentDescription = receta.nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
        )

        // 🔥 Filtro oscuro degradado para mejorar legibilidad
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        startY = 100f
                    )
                )
        )

        // Botón Volver
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopStart)
                .background(Color.White.copy(alpha = 0.75f), shape = CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        // Estrellita (si hay sesión)
        if (usuarioLogueado) {
            IconButton(
                onClick = { destacarViewModel.toggleDestacada(receta.id) },
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.White.copy(alpha = 0.75f), shape = CircleShape)
            ) {
                Icon(Icons.Default.StarBorder, contentDescription = "Guardar")
            }
        }

        // Detalles sobre imagen
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = receta.nombre,
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold // 💥 este es el cambio
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50), // verde Material
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tiempo de preparación: ${formatTiempo(receta.tiempo)}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "RECETA DE ${receta.autor.uppercase()}",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color.DarkGray // o MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .background(
                        color = Color.LightGray.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )

        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun IngredientesSection() {
    val ingredientes = listOf(
        Triple("1", "Kilo", "Carne"),
        Triple("4", "Unidad", "Tortilla"),
        Triple("1", "Unidad", "Pimiento"),
        Triple("5", "Unidad", "Limón"),
        Triple("1", "Rama", "Cilantro")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ingredientes",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(ingredientes) { (cantidad, unidad, nombre) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(Color(0xFFE8F5E9), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "$cantidad $unidad",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                    Text(
                        text = nombre,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección porciones (solo visual por ahora)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Porciones", modifier = Modifier.padding(end = 8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFFF1F1F1), RoundedCornerShape(6.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("-", modifier = Modifier.padding(horizontal = 4.dp))
                Text("5", modifier = Modifier.padding(horizontal = 4.dp))
                Text("+", modifier = Modifier.padding(horizontal = 4.dp))
            }
        }
    }
}

@Composable
fun PasoCard(paso: PasoReceta) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Multimedia (imagen o video)
        when {
            !paso.videoUrl.isNullOrBlank() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Video",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            !paso.imagenUrl.isNullOrBlank() -> {
                AsyncImage(
                    model = paso.imagenUrl,
                    contentDescription = "Imagen del paso ${paso.orden}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Paso N (título)
        Text(
            text = "Paso ${paso.orden}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Descripción del paso
        Text(
            text = paso.descripcion,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun PuntajeResumenSection(puntaje: Int, usuarioActual: String?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Reviews (100)", // 📌 Hardcode por ahora
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título centrado
            Text("Rating", style = MaterialTheme.typography.labelLarge)

            Spacer(modifier = Modifier.height(4.dp))

            // Puntaje en grande
            Text(
                text = "$puntaje/5",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )

            // Estrellas
            Row {
                repeat(5) { index ->
                    val filled = index < puntaje
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (filled) Color(0xFFFFD700) else Color.LightGray
                    )
                }
            }

            Text(
                "(100 reviews)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Barras dummy
            val dummyBars = listOf(
                "5 Star" to 20,
                "4 Star" to 20,
                "3 Star" to 20,
                "2 Star" to 20,
                "1 Star" to 20
            )

            dummyBars.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(label, modifier = Modifier.width(60.dp))
                    LinearProgressIndicator(
                        progress = value / 100f,
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .padding(horizontal = 8.dp),
                        color = Color(0xFF7E57C2),       // barra activa
                        trackColor = Color(0xFFEDE7F6)   // barra fondo
                    )
                    Text("$value", style = MaterialTheme.typography.bodySmall)
                }
            }

            // Botón condicional
            if (usuarioActual != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { /* Acción futura */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("¡Escribí tu review!")
                }
            }
        }
    }
}

@Composable
fun ComentariosSection(comentarios: List<Comentario>, usuarioActual: String?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Comentarios",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (comentarios.isEmpty()) {
            Text(
                text = "Aún no hay comentarios.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            comentarios.forEach { comentario ->
                ComentarioCard(
                    comentario = comentario
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun ComentarioCard(
    comentario: Comentario
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // 🖼 Avatar
        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = "Avatar del usuario",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 👉 Contenido
        Column(modifier = Modifier.weight(1f)) {



            // ⭐ Estrellas (si hay puntaje)
            comentario.puntaje?.let { puntaje ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < puntaje) Color(0xFFFFD700) else Color.LightGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // 💬 Contenido entrecomillado
            Text(
                text = "“${comentario.contenido}”",
                style = MaterialTheme.typography.bodyMedium
            )

            // Nombre + Fecha
            Column() {
                Text(
                    text = "Por: ${comentario.autor}",
                    color = Color.Black, // 💥 fuerza contraste
                    style = MaterialTheme.typography.bodyMedium
                )
                comentario.fechaRevision?.let {
                    Text(
                        text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it)),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}