package com.example.tpo_desa_1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recetas")
data class Receta(
    @PrimaryKey val nombre: String,
    val puntaje: Int,
    val tiempo: Int,
    val estado:  String, // "aprobada", "pendiente", "rechazada",
    val fechaRevision: Long?, // Epoch timestamp (nullable, porque puede no estar aprobada) TODO: revisar cuando se integre con el back conversión de el dato de fecha a timeStamp
    val imagenPortadaUrl: String
)
