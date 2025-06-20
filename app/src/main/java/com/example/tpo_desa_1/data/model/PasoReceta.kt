package com.example.tpo_desa_1.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "pasos_receta",
    primaryKeys = ["recetaId", "orden"], // clave primaria compuesta
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
data class PasoReceta(
    val recetaId: Int,
    val orden: Int,
    val descripcion: String,
    val imagenUrl: String?,
    val videoUrl: String?
)

