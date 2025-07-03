package com.example.tpo_desa_1.data.source.local

import com.example.tpo_desa_1.data.db.IngredienteDao
import com.example.tpo_desa_1.data.db.PasoRecetaDao
import com.example.tpo_desa_1.data.db.RecetaDao
import com.example.tpo_desa_1.data.model.Ingrediente
import com.example.tpo_desa_1.data.model.PasoReceta
import com.example.tpo_desa_1.data.model.Receta

class RecetaLocalDataSource(
    private val recetaDao: RecetaDao,
    private val pasoDao: PasoRecetaDao,
    private val ingredienteDao: IngredienteDao
) {

    suspend fun insertarRecetasConDependencias(
        recetas: List<Receta>,
        pasos: List<PasoReceta>,
        ingredientes: List<Ingrediente>
    ) {
        recetaDao.insertarTodas(recetas)
        pasoDao.insertar(pasos)
        ingredienteDao.insertar(ingredientes)
    }

    suspend fun obtenerTodas(): List<Receta> = recetaDao.obtenerTodas()

    suspend fun obtenerPorId(id: Int): Receta? = recetaDao.obtenerPorId(id)

    suspend fun insertar(receta: Receta) = recetaDao.insertar(receta)

    suspend fun insertarTodas(recetas: List<Receta>) {
        recetas.forEach { recetaDao.insertar(it) }
    }

    suspend fun obtenerPorUsuario(alias: String): List<Receta> =
        recetaDao.obtenerRecetasPorUsuario(alias)

    suspend fun obtenerRecientesAprobadas(): List<Receta> =
        recetaDao.obtenerRecientesAprobadas()

    suspend fun obtenerAprobadas(): List<Receta> = recetaDao.obtenerAprobadas()
}


