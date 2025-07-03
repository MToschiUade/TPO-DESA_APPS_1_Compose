package com.example.tpo_desa_1.data.model.response

import com.google.gson.annotations.SerializedName

data class RecetaGuardadaDTO(
    @SerializedName("idRecipe")
    val idRecipe: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("ingredientes")
    val ingredientes: List<IngredienteAprobadoDTO>, // mismo tipo

    @SerializedName("pasos")
    val pasos: List<PasoAprobadoDTO>, // mismo tipo

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