package com.example.tpo_desa_1.data.model.response

import com.google.gson.annotations.SerializedName

// data/model/response/MiRecetaDTO.kt
data class MiRecetaDTO(
    @SerializedName("idRecipe") val idRecipe: Int,
    @SerializedName("title") val title: String,
    @SerializedName("ingredientes") val ingredientes: List<IngredienteAprobadoDTO>,
    @SerializedName("pasos") val pasos: List<PasoAprobadoDTO>,
    @SerializedName("duracion") val duracion: Int,
    @SerializedName("imagePortada") val imagePortada: String?,
    @SerializedName("estado") val estado: String,
    @SerializedName("motivo") val motivo: String?,
    @SerializedName("autor") val autor: String
)
