package com.example.tpo_desa_1.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tpo_desa_1.data.model.PasoReceta

@Composable
fun PasoRecetaCard(paso: PasoReceta) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Paso ${paso.orden}",
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = paso.descripcion,
            style = MaterialTheme.typography.bodyMedium
        )

        paso.imagenUrl?.let {
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = it,
                contentDescription = "Imagen del paso ${paso.orden}: ${paso.descripcion}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        paso.videoUrl?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Video: $it",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp
            )
        }
    }
}
