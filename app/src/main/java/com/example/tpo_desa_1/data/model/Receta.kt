package com.example.tpo_desa_1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "recetas",
    indices = [Index(value = ["autor", "nombre"], unique = true)]
)
data class Receta(
    @PrimaryKey val id: Int, // ya no se autogenera
    val nombre: String,
    val puntaje: Int,
    val tiempo: Int,
    val estado: String,
    val fechaRevision: Long?,
    val imagenPortadaUrl: String,
    val autor: String,
    val motivoRechazo: String? = null // 👈 NUEVO
)