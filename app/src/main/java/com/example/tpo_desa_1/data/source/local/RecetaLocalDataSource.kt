package com.example.tpo_desa_1.data.source.local

import com.example.tpo_desa_1.data.db.RecetaDao
import com.example.tpo_desa_1.data.model.Receta

class RecetaLocalDataSource(
    private val recetaDao: RecetaDao
) {
    suspend fun obtenerTodas(): List<Receta> = recetaDao.obtenerTodas()

    suspend fun obtenerPorId(id: Int): Receta? = recetaDao.obtenerPorId(id)

    suspend fun insertar(receta: Receta) = recetaDao.insertar(receta)

    suspend fun obtenerPorUsuario(alias: String): List<Receta> =
        recetaDao.obtenerRecetasPorUsuario(alias)

    suspend fun obtenerRecientesAprobadas(): List<Receta> =
        recetaDao.obtenerRecientesAprobadas()

    suspend fun obtenerAprobadas(): List<Receta> = recetaDao.obtenerAprobadas()
}
