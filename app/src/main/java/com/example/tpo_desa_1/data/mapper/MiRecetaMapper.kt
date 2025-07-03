package com.example.tpo_desa_1.data.mapper

import com.example.tpo_desa_1.data.model.*
import com.example.tpo_desa_1.data.model.response.MiRecetaDTO

fun MiRecetaDTO.toModel(): Triple<Receta, List<PasoReceta>, List<Ingrediente>> {
    val receta = Receta(
        id = idRecipe,
        nombre = title,
        puntaje = 0, // las propias no tienen puntaje
        tiempo = duracion,
        estado = estado,
        fechaRevision = null,
        imagenPortadaUrl = imagePortada ?: "",
        autor = autor
    )

    val pasos = pasos.mapIndexed { index, paso ->
        PasoReceta(
            recetaId = idRecipe,
            orden = index,
            descripcion = paso.proceso,
            imagenUrl = paso.url.ifBlank { null },
            videoUrl = null
        )
    }

    val ingredientes = ingredientes.map {
        Ingrediente(
            recetaId = idRecipe,
            idIngrediente = it.idIngrediente,
            nombre = it.nombre,
            medida = it.medida,
            nombreMedida = it.nombreMedida
        )
    }

    return Triple(receta, pasos, ingredientes)
}
