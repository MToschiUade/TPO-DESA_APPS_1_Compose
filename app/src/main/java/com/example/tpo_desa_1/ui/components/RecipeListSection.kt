package com.example.tpo_desa_1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.example.tpo_desa_1.R
import com.example.tpo_desa_1.R.drawable.recipe_ribs
import com.example.tpo_desa_1.data.model.Receta


import coil.compose.AsyncImage

@Composable
fun RecipeListSection(
    recetas: List<Receta>,
    titulo: String = "Más recetas para vos",
    modifier: Modifier = Modifier,
    heightModifier: Modifier = Modifier,
    mostrarEstado: Boolean = false,
    mostrarPuntaje: Boolean = false,
    mostrarAutor: Boolean = false,
    maxItems: Int? = null,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(heightModifier)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = titulo,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(recetas.take(maxItems ?: recetas.size)) { receta ->
                Row(    modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("detalle_receta/${receta.id}") }) {
                    AsyncImage(
                        model = receta.imagenPortadaUrl,
                        contentDescription = receta.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        Column {
                            // Título y puntaje alineado a derecha
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = receta.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                                if (mostrarPuntaje) {
                                    Row {
                                        repeat(receta.puntaje.coerceIn(0, 5)) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Estrella",
                                                tint = Color(0xFFFFD700),
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${receta.puntaje}/5",
                                            fontSize = 12.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Tiempo
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = "Tiempo",
                                    tint = Color(0xFF00A86B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = formatTiempo(receta.tiempo),
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                            }

                            // Estado (opcional)
                            if (mostrarEstado) {
                                Spacer(modifier = Modifier.height(4.dp))
                                val estadoColor = when (receta.estado.lowercase()) {
                                    //TODO cambiar los status del BE para que coincidan con el FE y terminen en A el objeto es receta!
                                    "aprobado" -> Color(0xFF34A853)
                                    "pendiente" -> Color(0xFFFFA000)
                                    "rechazado" -> Color(0xFFE53935)
                                    else -> Color.Gray
                                }

                                Text(
                                    text = "Receta ${receta.estado.lowercase()}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .background(estadoColor, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            // Autor (última línea)
                            if (mostrarAutor) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "por ${receta.autor}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 👉 Función utilitaria para mostrar tiempos más legibles
fun formatTiempo(minutos: Int): String {
    return when {
        minutos < 60 -> "$minutos Mins"
        minutos % 60 == 0 -> "${minutos / 60} Hours"
        else -> "${minutos / 60}.${(minutos % 60) * 10 / 60} Hours"
    }
}

