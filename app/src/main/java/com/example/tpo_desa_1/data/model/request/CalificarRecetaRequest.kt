package com.example.tpo_desa_1.data.model.request

data class CalificarRecetaRequest(
    val recipeId: Int,
    val puntaje: Int,
    val comentario: String
)
