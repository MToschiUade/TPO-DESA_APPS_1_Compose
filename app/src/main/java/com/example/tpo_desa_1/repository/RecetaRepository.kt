package com.example.tpo_desa_1.repository

import com.example.tpo_desa_1.data.db.RecetaDao
import com.example.tpo_desa_1.data.model.Receta

class RecetaRepository(private val dao: RecetaDao) {

    suspend fun cargarRecetasEnBase(receta: Receta) {
        dao.insertar(receta)
    }

    suspend fun obtener(nombre: String): Receta? = dao.obtenerPorNombre(nombre)

    suspend fun obtenerTodas(): List<Receta> = dao.obtenerTodas()

    suspend fun obtenerRecetasAprobadasRecientes(): List<Receta> =
        dao.obtenerRecientesAprobadas()

    suspend fun obtenerTodasAprobadas(): List<Receta> = dao.obtenerAprobadas()

    suspend fun obtenerRecetasPorUsuario(alias: String): List<Receta> {
        return dao.obtenerRecetasPorUsuario(alias)
    }

    suspend fun obtenerPorId(id: Int): Receta? {
        return dao.obtenerPorId(id)
    }

}
