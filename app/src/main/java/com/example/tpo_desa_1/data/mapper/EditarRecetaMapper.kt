package com.example.tpo_desa_1.data.mapper

import com.example.tpo_desa_1.data.model.request.EditarRecetaRequest
import com.example.tpo_desa_1.data.model.request.IngredienteRequest
import com.example.tpo_desa_1.data.model.request.PasoRequest
import com.example.tpo_desa_1.data.model.response.MiRecetaDTO

fun MiRecetaDTO.toEditarRecetaRequest(): EditarRecetaRequest {
    return EditarRecetaRequest(
        title = this.title,
        ingredientes = this.ingredientes.map {
            IngredienteRequest(
                nombre = it.nombre,
                medida = it.medida
            )
        },
        pasos = this.pasos.map {
            PasoRequest(
                idPaso = it.idPaso,
                proceso = it.proceso,
                url = it.url
            )
        },
        duracion = this.duracion,
        imagePortada = this.imagePortada!!
        // TODO Parche para build, revisar después
    )
}
