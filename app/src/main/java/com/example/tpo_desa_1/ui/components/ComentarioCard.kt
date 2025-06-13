package com.example.tpo_desa_1.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tpo_desa_1.data.model.Comentario
import java.util.Date
import androidx.compose.material.icons.filled.Delete

@Composable
fun ComentarioCard(
    comentario: Comentario,
    usuarioActual: String?, // ← aceptamos nullable
    onEliminar: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = comentario.autor,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 12.sp
            )
            Text(
                text = comentario.contenido,
                style = MaterialTheme.typography.bodyMedium
            )
            comentario.fechaRevision?.let {
                Text(
                    "Revisado: ${Date(it)}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp
                )
            }
        }

        if (usuarioActual != null && comentario.autor == usuarioActual) {
            IconButton(onClick = { onEliminar(comentario.id) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar comentario",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

    }
}
