package com.example.tpo_desa_1.data.mapper

import com.example.tpo_desa_1.data.model.*

fun mapViewModelToDto(
    titulo: String,
    portadaUrl: String,
    tiempoEnMinutos: Int,
    ingredientes: List<IngredienteUI>,
    pasos: List<PasoPreparacionUI>
): RecetaDTO {
    return RecetaDTO(
        title = titulo,
        imagePortada = portadaUrl,
        tiempoReceta = tiempoEnMinutos,
        fecha = "", // no usado
        ingredientes = ingredientes.map {
            IngredienteDTO(
                nombre = it.nombre,
                medida = it.cantidad.toFloatOrNull() ?: 0f,
                nombreMedida = it.unidad.name.lowercase()
            )
        },
        pasos = pasos.map {
            PasoDTO(
                proceso = it.descripcion,
                url = it.mediaUri ?: ""
            )
        }
    )
}
