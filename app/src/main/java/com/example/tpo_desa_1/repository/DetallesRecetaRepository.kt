package com.example.tpo_desa_1.repository

import com.example.tpo_desa_1.data.db.ComentarioDao
import com.example.tpo_desa_1.data.db.IngredienteDao
import com.example.tpo_desa_1.data.db.PasoRecetaDao
import com.example.tpo_desa_1.data.model.Comentario
import com.example.tpo_desa_1.data.model.Ingrediente
import com.example.tpo_desa_1.data.model.PasoReceta

class DetallesRecetaRepository(
    private val comentarioDao: ComentarioDao,
    private val pasoDao: PasoRecetaDao,
    private val ingredienteDao: IngredienteDao // ✅ nuevo
) {

    suspend fun obtenerComentariosAprobados(recetaId: Int): List<Comentario> =
        comentarioDao.obtenerPorReceta(recetaId).filter { it.estado == "aprobado" }

    suspend fun obtenerPasos(recetaId: Int): List<PasoReceta> =
        pasoDao.obtenerPorReceta(recetaId)

    suspend fun eliminarComentarioSiAutor(recetaId: Int, autor: String, fecha: Long): Boolean =
        comentarioDao.eliminarSiEsDelAutor(recetaId, autor) > 0

    suspend fun obtenerIngredientes(recetaId: Int): List<Ingrediente> {
        return ingredienteDao.obtenerPorReceta(recetaId)
    }


}
