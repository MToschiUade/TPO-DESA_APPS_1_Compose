package com.example.tpo_desa_1.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "comentarios",
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
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recetaId: Int,
    val autor: String,
    val contenido: String,
    val estado: String, // "pendiente", "aprobado", "rechazado"
    val fecha: Long,
    val fechaRevision: Long? = null
)
