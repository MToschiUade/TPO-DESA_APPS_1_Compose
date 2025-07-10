package com.example.tpo_desa_1.data.mapper

import com.example.tpo_desa_1.data.model.Comentario
import com.example.tpo_desa_1.data.model.response.ComentarioDTO

fun ComentarioDTO.toModel(): Comentario {
    return Comentario(
        recetaId = recetaId,
        autor = user ?: "[usuario desconocido]",
        contenido = contenido ?: "[sin contenido]",
        estado = estado ?: "aprobado", // ✅ asumimos todo aprobado
        fecha = fecha ?: System.currentTimeMillis(), // ✅ usamos fecha actual como fallback
        fechaRevision = fechaRevision, // puede quedar null
        puntaje = puntaje
    )
}
