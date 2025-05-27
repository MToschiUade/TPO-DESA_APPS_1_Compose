package com.example.tpo_desa_1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recetas")
data class Receta(
    @PrimaryKey val nombre: String,
    val puntaje: Int,
    val tiempo: Int,
    val status: String
)
