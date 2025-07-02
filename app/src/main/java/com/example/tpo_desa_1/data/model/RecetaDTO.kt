package com.example.tpo_desa_1.data.model;

data class RecetaDTO(
        val title: String,
        val image: String,
        val ingredientes: List<IngredienteDTO>,
        val pasos: List<PasoDTO>,
        val fecha: String,
        val tiempoReceta: Int
)

data class IngredienteDTO(
        val nombre: String,
        val medida: Float,
        val nombreMedida: String
)

data class PasoDTO(
        val url: String,
        val proceso: String
)

