package com.example.tpo_desa_1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tpo_desa_1.data.model.Receta

@Composable
fun RecetasList(titulo: String, recetas: List<Receta>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = titulo, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        recetas.forEach { receta ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = receta.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Puntaje: ${receta.puntaje}")
                    Text(text = "Tiempo: ${receta.tiempo} minutos")
                }
            }
        }
    }
}
