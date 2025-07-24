package com.example.tpo_desa_1.data.model.request

data class EditarRecetaRequest(
    val title: String,
    val ingredientes: List<IngredienteRequest>,
    val pasos: List<PasoRequest>,
    val duracion: Int,
    val imagePortada: String?
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
