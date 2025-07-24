package com.example.tpo_desa_1.data.model.request

import com.google.gson.annotations.SerializedName

data class EditarRecetaRequest(
    val title: String,
    val imagePortada: String,
    val ingredientes: List<IngredienteRequest>,
    val pasos: List<PasoRequest>,
    @SerializedName("duracion")
    val duracion: Int,
)

data class IngredienteRequest(
    val nombre: String,
    val medida: Int
)

data class PasoRequest(
    val idPaso: Int, // ⬅️ necesario si el backend respeta este orden
    val proceso: String,
    val url: String
)
