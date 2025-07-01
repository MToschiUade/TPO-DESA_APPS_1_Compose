package com.example.tpo_desa_1.data.mapper

import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.model.RecetaDTO

fun RecetaDTO.toModel(): Receta {
    return Receta(
        id = 0, // No viene del backend por ahora
        nombre = this.title,
        puntaje = 0, // No está en DTO, valor default
        tiempo = this.tiempoReceta,
        estado = "pendiente", // No viene en DTO
        fechaRevision = null, // No viene, y es String en DTO
        imagenPortadaUrl = this.image,
        autor = "desconocido" // No viene en DTO
    )
}

fun Receta.toDto(): RecetaDTO {
    return RecetaDTO(
        title = this.nombre,
        image = this.imagenPortadaUrl,
        ingredientes = emptyList(), // Más adelante podés mapearlos
        pasos = emptyList(),
        fecha = "", // Podés formatear la fecha si querés
        tiempoReceta = this.tiempo
    )
}
