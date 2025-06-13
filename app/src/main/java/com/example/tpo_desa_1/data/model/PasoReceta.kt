package com.example.tpo_desa_1.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pasos_receta",
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
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recetaId: Int,
    val orden: Int,
    val descripcion: String,
    val imagenUrl: String? = null,
    val videoUrl: String? = null
)
