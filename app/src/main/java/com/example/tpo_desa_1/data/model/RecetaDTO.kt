package com.example.tpo_desa_1.data.model;

import com.google.gson.annotations.SerializedName

data class RecetaDTO(
        val title: String,
        val imagePortada: String,
        val ingredientes: List<IngredienteDTO>,
        val pasos: List<PasoDTO>,
        val fecha: String,
        @SerializedName("duracion")
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

