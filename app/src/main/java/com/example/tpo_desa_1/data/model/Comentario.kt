package com.example.tpo_desa_1.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "comentarios",
    primaryKeys = ["recetaId", "autor"], // ❗ solo uno por receta y autor
    foreignKeys = [
        ForeignKey(
            entity = Receta::class,
            parentColumns = ["id"],
            childColumns = ["recetaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["recetaId"])]
)
data class Comentario(
    val recetaId: Int,
    val autor: String,
    val contenido: String,
    val estado: String, // "pendiente", "aprobado", "rechazado"
    val fecha: Long, // puede mantenerse como metadata
    val fechaRevision: Long? = null,
    val puntaje: Int? = null
)