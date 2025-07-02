package com.example.tpo_desa_1.repository

import com.example.tpo_desa_1.config.AppConfig
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.source.local.RecetaLocalDataSource
import com.example.tpo_desa_1.data.source.remote.RecetaRemoteDataSource
import com.example.tpo_desa_1.data.mapper.toDto
import com.example.tpo_desa_1.data.model.RecetaDTO

class RecetaRepositoryImpl(
    private val localDataSource: RecetaLocalDataSource,
    private val remoteDataSource: RecetaRemoteDataSource
) : RecetaRepository {

    override suspend fun obtenerTodas(): List<Receta> {
        return try {
            val recetasRemotas = remoteDataSource.obtenerTodas()
            localDataSource.insertarTodas(recetasRemotas)
            recetasRemotas
        } catch (e: Exception) {
            localDataSource.obtenerTodas()
        }
    }

    override suspend fun obtenerPorId(id: Int): Receta? {
        return try {
            remoteDataSource.obtenerPorId(id) ?: localDataSource.obtenerPorId(id)
        } catch (e: Exception) {
            localDataSource.obtenerPorId(id)
        }
    }

    override suspend fun crearReceta(receta: Receta): Boolean { // Meli: no lo estoy usando pq usa Mapper viejo: "RecetaMapper"
        return try {
            remoteDataSource.enviarReceta(receta.toDto())
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun crearRecetaDesdeFormulario(dto: RecetaDTO): Boolean {
        return try {
            remoteDataSource.enviarReceta(dto) // usa directamente el DTO generado desde CrearRecetaViewModel
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun obtenerRecetasPorUsuario(alias: String): List<Receta> {
        return try {
            val recetas = remoteDataSource.obtenerPorUsuario(alias)
            localDataSource.insertarTodas(recetas)
            recetas
        } catch (e: Exception) {
            localDataSource.obtenerPorUsuario(alias)
        }
    }

    override suspend fun obtenerRecetasAprobadasRecientes(): List<Receta> {
        return try {
            val recetas = remoteDataSource.obtenerRecientesAprobadas()
            localDataSource.insertarTodas(recetas)
            recetas
        } catch (e: Exception) {
            localDataSource.obtenerRecientesAprobadas()
        }
    }

    override suspend fun obtenerTodasAprobadas(): List<Receta> {
        return try {
            val recetas = remoteDataSource.obtenerAprobadas()
            localDataSource.insertarTodas(recetas)
            recetas
        } catch (e: Exception) {
            localDataSource.obtenerAprobadas()
        }
    }

}

