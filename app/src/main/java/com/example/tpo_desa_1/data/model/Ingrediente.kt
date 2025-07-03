package com.example.tpo_desa_1.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "ingredientes",
    primaryKeys = ["recetaId", "idIngrediente"], // Clave compuesta
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
data class Ingrediente(
    val recetaId: Int,
    val idIngrediente: Int,
    val nombre: String,
    val medida: Int,
    val nombreMedida: String
)
