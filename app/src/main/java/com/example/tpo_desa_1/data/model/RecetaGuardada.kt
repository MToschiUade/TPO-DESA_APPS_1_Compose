package com.example.tpo_desa_1.data.model

import androidx.room.Entity

@Entity(
    tableName = "recetas_guardadas",
    primaryKeys = ["usuarioEmail", "recetaId"]
)
data class RecetaGuardada(
    val usuarioEmail: String,
    val recetaId: Int,
    val fechaGuardado: Long = System.currentTimeMillis()
)
