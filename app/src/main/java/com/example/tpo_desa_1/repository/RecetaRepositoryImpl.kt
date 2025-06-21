package com.example.tpo_desa_1.repository

import com.example.tpo_desa_1.config.AppConfig
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.source.local.RecetaLocalDataSource
import com.example.tpo_desa_1.data.source.remote.RecetaRemoteDataSource

class RecetaRepositoryImpl(
    private val localDataSource: RecetaLocalDataSource,
    private val remoteDataSource: RecetaRemoteDataSource
) : RecetaRepository {

    private val usarApi = AppConfig.USE_REMOTE_DATA_SOURCE

    override suspend fun obtenerTodas(): List<Receta> {
        return if (usarApi) {
            // TODO: implementar lógica de fetch desde API real
            emptyList()
        } else {
            localDataSource.obtenerTodas()
        }
    }

    override suspend fun obtenerPorId(id: Int): Receta? {
        return if (usarApi) {
            null
        } else {
            localDataSource.obtenerPorId(id)
        }
    }

    override suspend fun crearReceta(receta: Receta): Boolean {
        return try {
            if (usarApi) {
                // TODO: receta.toDto() cuando esté mapeo implementado
                // remoteDataSource.enviarReceta(receta.toDto())
                true
            } else {
                localDataSource.insertar(receta)
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun obtenerRecetasPorUsuario(alias: String): List<Receta> {
        return if (usarApi) emptyList()
        else localDataSource.obtenerPorUsuario(alias)
    }

    override suspend fun obtenerRecetasAprobadasRecientes(): List<Receta> {
        return if (usarApi) emptyList()
        else localDataSource.obtenerRecientesAprobadas()
    }

    override suspend fun obtenerTodasAprobadas(): List<Receta> {
        return if (usarApi) emptyList()
        else localDataSource.obtenerAprobadas()
    }
}
