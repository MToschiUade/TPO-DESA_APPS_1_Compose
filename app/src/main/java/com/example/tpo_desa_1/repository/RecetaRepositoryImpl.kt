package com.example.tpo_desa_1.repository

import com.example.tpo_desa_1.config.AppConfig
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.source.local.RecetaLocalDataSource
import com.example.tpo_desa_1.data.source.remote.RecetaRemoteDataSource
import com.example.tpo_desa_1.data.mapper.toDto

class RecetaRepositoryImpl(
    private val localDataSource: RecetaLocalDataSource,
    private val remoteDataSource: RecetaRemoteDataSource
) : RecetaRepository {

    private val usarApi = AppConfig.USE_REMOTE_DATA_SOURCE

    override suspend fun obtenerTodas(): List<Receta> {
        return when {
            AppConfig.USE_REMOTE_DATA_SOURCE -> {
                try {
                    val recetasRemotas = remoteDataSource.obtenerTodas()
                    if (!AppConfig.ENABLE_DEMO_SEEDING) {
                        localDataSource.insertarTodas(recetasRemotas)
                    }
                    recetasRemotas
                } catch (e: Exception) {
                    // Si falla la API, y está la demo habilitada
                    if (AppConfig.ENABLE_DEMO_SEEDING) {
                        localDataSource.obtenerTodasDemo()
                    } else {
                        // fallback a la última data local persistida
                        localDataSource.obtenerTodas()
                    }
                }
            }

            AppConfig.ENABLE_DEMO_SEEDING -> {
                localDataSource.obtenerTodasDemo()
            }

            else -> {
                localDataSource.obtenerTodas()
            }
        }
    }



    override suspend fun obtenerPorId(id: Int): Receta? {
        return if (usarApi) {
            try {
                remoteDataSource.obtenerPorId(id) ?: localDataSource.obtenerPorId(id)
            } catch (e: Exception) {
                localDataSource.obtenerPorId(id)
            }
        } else {
            localDataSource.obtenerPorId(id)
        }
    }

    override suspend fun crearReceta(receta: Receta): Boolean {
        return try {
            if (usarApi) {
                remoteDataSource.enviarReceta(receta.toDto()) // ✔️ usando tu mapper
            } else {
                localDataSource.insertar(receta)
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun obtenerRecetasPorUsuario(alias: String): List<Receta> {
        return if (usarApi) {
            try {
                val recetas = remoteDataSource.obtenerPorUsuario(alias)
                localDataSource.insertarTodas(recetas)
                recetas
            } catch (e: Exception) {
                localDataSource.obtenerPorUsuario(alias)
            }
        } else {
            localDataSource.obtenerPorUsuario(alias)
        }
    }

    override suspend fun obtenerRecetasAprobadasRecientes(): List<Receta> {
        return if (usarApi) {
            try {
                val recetas = remoteDataSource.obtenerRecientesAprobadas()
                localDataSource.insertarTodas(recetas)
                recetas
            } catch (e: Exception) {
                localDataSource.obtenerRecientesAprobadas()
            }
        } else {
            localDataSource.obtenerRecientesAprobadas()
        }
    }

    override suspend fun obtenerTodasAprobadas(): List<Receta> {
        return if (usarApi) {
            try {
                val recetas = remoteDataSource.obtenerAprobadas()
                localDataSource.insertarTodas(recetas)
                recetas
            } catch (e: Exception) {
                localDataSource.obtenerAprobadas()
            }
        } else {
            localDataSource.obtenerAprobadas()
        }
    }
}
