package com.example.tpo_desa_1.data.model.response

import com.google.gson.annotations.SerializedName

data class RecetaAprobadaDTO(
    @SerializedName("idRecipe")
    val idRecipe: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("ingredientes")
    val ingredientes: List<IngredienteAprobadoDTO>,

    @SerializedName("pasos")
    val pasos: List<PasoAprobadoDTO>,

    @SerializedName("duracion")
    val duracion: Int,

    @SerializedName("imagePortada")
    val imagePortada: String,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("motivo")
    val motivo: String?, // puede venir null

    @SerializedName("autor")
    val autor: String
)

data class IngredienteAprobadoDTO(
    @SerializedName("idIngrediente")
    val idIngrediente: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("medida")
    val medida: Int,

    @SerializedName("nombreMedida")
    val nombreMedida: String
)

data class PasoAprobadoDTO(
    @SerializedName("idPaso")
    val idPaso: Int,

    @SerializedName("url")
    val url: String,

    @SerializedName("proceso")
    val proceso: String
)
