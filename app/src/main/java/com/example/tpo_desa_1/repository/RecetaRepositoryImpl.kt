package com.example.tpo_desa_1.repository

import android.content.Context
import android.net.Uri
import com.example.tpo_desa_1.config.AppConfig
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.source.local.RecetaLocalDataSource
import com.example.tpo_desa_1.data.source.remote.RecetaRemoteDataSource
import com.example.tpo_desa_1.data.mapper.toDto
import com.example.tpo_desa_1.data.model.RecetaDTO
import com.example.tpo_desa_1.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import com.example.tpo_desa_1.data.mapper.*
import com.example.tpo_desa_1.data.model.Comentario
import com.example.tpo_desa_1.data.source.remote.ComentarioRemoteDataSource

class RecetaRepositoryImpl(
    private val localDataSource: RecetaLocalDataSource,
    private val remoteDataSource: RecetaRemoteDataSource,
    private val comentarioRemoteDataSource: ComentarioRemoteDataSource
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

    override suspend fun crearReceta(receta: RecetaDTO): Boolean {
        return try {
            remoteDataSource.enviarReceta(receta)
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

    override suspend fun obtenerCantidadRecetas(token: String): Int {
        return try {
            val response = remoteDataSource.getCantidadRecetas(token)

            if (response.isSuccessful) {
                response.body() ?: 0
            } else {
                0
            }
        } catch (e: Exception) {

            0
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
            localDataSource.obtenerAprobadas()
        }
    }

    override suspend fun subirImagen(context: Context, uri: Uri, token: String): String? {
        return remoteDataSource.subirImagen(context, uri, token)
    }

    override suspend fun obtenerMisRecetas(token: String): List<Receta> {
        return try {
            val misRecetasDto = remoteDataSource.obtenerMisRecetas(token)
            val triples = misRecetasDto.map { it.toModel() }

            val recetas = triples.map { it.first }
            val pasos = triples.flatMap { it.second }
            val ingredientes = triples.flatMap { it.third }

            localDataSource.insertarRecetasConDependencias(recetas, pasos, ingredientes)

            recetas
        } catch (e: Exception) {
            localDataSource.obtenerPorUsuario("YO") // opcional: usar un alias especial, o dejar vacío
        }
    }

    override suspend fun toggleRecetaDestacada(recipeId: Int, token: String): Result<String> {
        return try {
            val response = remoteDataSource.toggleFeaturedRecipe(recipeId, token)
            if (response.isSuccessful) {
                Result.success(response.body() ?: "Sin mensaje")
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun obtenerRecetasGuardadas(token: String): List<Receta> {
        return try {
            val guardadasDto = remoteDataSource.obtenerRecetasGuardadas(token)
            val triples = guardadasDto.map { it.toModel() }

            val recetas = triples.map { it.first }
            val pasos = triples.flatMap { it.second }
            val ingredientes = triples.flatMap { it.third }

            localDataSource.insertarRecetasConDependencias(recetas, pasos, ingredientes)

            recetas
        } catch (e: Exception) {
            localDataSource.obtenerRecientesAprobadas() // fallback opcional
        }
    }

    override suspend fun obtenerComentariosDeReceta(recetaId: Int): List<Comentario> {
        return comentarioRemoteDataSource.obtenerComentarios(recetaId)
    }

    override suspend fun agregarComentarioAReceta(token: String, recetaId: Int, contenido: String, puntaje: Int) {
        comentarioRemoteDataSource.enviarComentario(token, recetaId, contenido, puntaje)
    }


}

