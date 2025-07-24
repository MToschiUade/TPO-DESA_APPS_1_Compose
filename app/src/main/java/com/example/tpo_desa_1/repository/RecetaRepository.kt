package com.example.tpo_desa_1.repository

import android.content.Context
import android.net.Uri
import com.example.tpo_desa_1.data.model.Comentario
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.model.RecetaDTO
import com.example.tpo_desa_1.data.model.request.EditarRecetaRequest
import com.example.tpo_desa_1.data.model.response.MiRecetaDTO

interface RecetaRepository {
    suspend fun obtenerTodas(): List<Receta>
    suspend fun obtenerPorId(id: Int): Receta?
    suspend fun crearReceta(receta: RecetaDTO): Boolean
    suspend fun obtenerRecetasPorUsuario(alias: String): List<Receta>
    suspend fun obtenerRecetasAprobadasRecientes(): List<Receta>
    suspend fun obtenerTodasAprobadas(): List<Receta>
    suspend fun crearRecetaDesdeFormulario(dto: RecetaDTO): Boolean
    suspend fun subirImagen(context: Context, uri: Uri, token: String): String?
    suspend fun obtenerMisRecetas(token: String): List<Receta>
    suspend fun toggleRecetaDestacada(recipeId: Int, token: String): Result<String>
    suspend fun obtenerRecetasGuardadas(token: String): List<Receta>
    suspend fun obtenerCantidadRecetas(token: String): Int
    suspend fun obtenerComentariosDeReceta(recetaId: Int): List<Comentario>
    suspend fun agregarComentarioAReceta(token: String, recetaId: Int, contenido: String, puntaje: Int)
    suspend fun editarReceta(recipeId: Int, request: EditarRecetaRequest, token: String): Boolean
    suspend fun obtenerMiRecetaPorId(id: Int, token: String): MiRecetaDTO?

}
