package com.example.tpo_desa_1.repository

import com.example.tpo_desa_1.data.model.Receta

interface RecetaRepository {
    suspend fun obtenerTodas(): List<Receta>
    suspend fun obtenerPorId(id: Int): Receta?
    suspend fun crearReceta(receta: Receta): Boolean
    suspend fun obtenerRecetasPorUsuario(alias: String): List<Receta>
    suspend fun obtenerRecetasAprobadasRecientes(): List<Receta>
    suspend fun obtenerTodasAprobadas(): List<Receta>
}
