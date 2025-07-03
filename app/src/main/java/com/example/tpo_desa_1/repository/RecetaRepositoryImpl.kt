package com.example.tpo_desa_1.repository

import android.util.Log
import com.example.tpo_desa_1.config.AppConfig
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.source.local.RecetaLocalDataSource
import com.example.tpo_desa_1.data.source.remote.RecetaRemoteDataSource
import com.example.tpo_desa_1.data.mapper.toDto
import com.example.tpo_desa_1.data.mapper.toModel

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

    override suspend fun crearReceta(receta: Receta): Boolean {
        return try {
            remoteDataSource.enviarReceta(receta.toDto())
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
            val recetasDto = remoteDataSource.obtenerRecientesAprobadasDTO()

            val triples = recetasDto.map { it.toModel() }
            val recetas = triples.map { it.first }
            val pasos = triples.flatMap { it.second }
            val ingredientes = triples.flatMap { it.third }

            localDataSource.insertarRecetasConDependencias(recetas, pasos, ingredientes)

            recetas
        } catch (e: Exception) {
            localDataSource.obtenerRecientesAprobadas()
        }
    }


    override suspend fun obtenerTodasAprobadas(): List<Receta> {
        return try {
            val recetasDto = remoteDataSource.obtenerAprobadasAprobadasDTO()

            val triples = recetasDto.map { it.toModel() }
            val recetas = triples.map { it.first }
            val pasos = triples.flatMap { it.second }
            val ingredientes = triples.flatMap { it.third }

            localDataSource.insertarRecetasConDependencias(recetas, pasos, ingredientes)

            recetas
        } catch (e: Exception) {
            val recetasLocales = localDataSource.obtenerAprobadas()
            Log.d("RoomFallback", "Se usaron ${recetasLocales.size} recetas desde Room (BE falló)")
            recetasLocales
        }
    }

}

