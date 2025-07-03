package com.example.tpo_desa_1.data.mapper

import com.example.tpo_desa_1.data.model.*
import com.example.tpo_desa_1.data.model.response.RecetaGuardadaDTO

fun RecetaGuardadaDTO.toModel(): Triple<Receta, List<PasoReceta>, List<Ingrediente>> {
    val receta = Receta(
        id = this.idRecipe,
        nombre = this.title,
        puntaje = 0, // aún no hay puntaje desde backend
        tiempo = this.duracion,
        estado = this.estado,
        fechaRevision = null,
        imagenPortadaUrl = this.imagePortada,
        autor = this.autor
    )

    val pasos = this.pasos.mapIndexed { index, paso ->
        PasoReceta(
            recetaId = this.idRecipe,
            orden = index,
            descripcion = paso.proceso,
            imagenUrl = paso.url,
            videoUrl = null
        )
    }

    val ingredientes = this.ingredientes.map {
        Ingrediente(
            recetaId = this.idRecipe,
            idIngrediente = it.idIngrediente,
            nombre = it.nombre,
            medida = it.medida,
            nombreMedida = it.nombreMedida
        )
    }

    return Triple(receta, pasos, ingredientes)
}
