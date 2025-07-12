package com.example.tpo_desa_1.data.model.response

import com.google.gson.annotations.SerializedName

data class ComentarioDTO(
    val recetaId: Int,
    val user: String?,
    @SerializedName("comentario") val contenido: String?, // ✅ fix clave
    val estado: String? = null,              // 🔁 opcional
    val fecha: Long? = null,                 // 🔁 opcional
    val fechaRevision: Long? = null,
    val puntaje: Int? = null
)


