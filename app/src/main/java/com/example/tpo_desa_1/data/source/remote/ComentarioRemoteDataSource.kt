package com.example.tpo_desa_1.data.source.remote

import com.example.tpo_desa_1.data.mapper.toModel
import com.example.tpo_desa_1.data.model.Comentario
import com.example.tpo_desa_1.data.model.request.CalificarRecetaRequest

class ComentarioRemoteDataSource(
    private val api: ApiService
) {

    suspend fun obtenerComentarios(recetaId: Int): List<Comentario> {
        val comentariosDto = api.getRatingsByRecipe(recetaId)
        return comentariosDto.map { it.toModel() }
    }


    suspend fun enviarComentario(token: String, recetaId: Int, contenido: String, puntaje: Int) {
        val req = CalificarRecetaRequest(
            recipeId = recetaId,
            puntaje = puntaje,
            comentario = contenido
        )
        api.calificarReceta("Bearer $token", req)
    }
}
