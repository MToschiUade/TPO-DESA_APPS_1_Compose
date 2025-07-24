package com.example.tpo_desa_1.data.source.remote

import android.content.Context
import android.net.Uri
import com.example.tpo_desa_1.data.model.RecetaDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.mapper.toModel
import com.example.tpo_desa_1.data.model.request.EditarRecetaRequest
import com.example.tpo_desa_1.data.model.response.MiRecetaDTO
import com.example.tpo_desa_1.utils.uriToFile
import okhttp3.RequestBody.Companion.asRequestBody
import com.example.tpo_desa_1.data.model.response.RecetaAprobadaDTO
import com.example.tpo_desa_1.data.model.response.RecetaGuardadaDTO
import retrofit2.Response

class RecetaRemoteDataSource(
    private val api: ApiService
) {
    suspend fun subirImagen(context: Context, uri: Uri, token: String): String? = withContext(Dispatchers.IO) {
        println("📁 Intentando subir imagen con URI: $uri")

        val file = uriToFile(context, uri) ?: return@withContext null
        println("📂 Archivo generado: ${file.name}, tamaño: ${file.length()} bytes")

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

        return@withContext try {
            val response = api.subirImagen(imagePart, "Bearer $token")

            if (response.isSuccessful) {
                val url = response.body()?.string()?.trim()
                println("🌐 URL recibida: $url")
                url
            } else {
                println("⚠️ Error HTTP: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            println("❌ Excepción al subir imagen: ${e.message}")
            null
        }
    }

    suspend fun enviarReceta(dto: RecetaDTO): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            api.postReceta(dto)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun obtenerRecetas(): List<RecetaDTO> = withContext(Dispatchers.IO) {
        return@withContext try {
            api.getRecetas()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun obtenerTodas(): List<Receta> = withContext(Dispatchers.IO) {
        try {
            api.getRecetas().map { it.toModel() } // Necesitás el mapper DTO → Receta
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun obtenerPorId(id: Int): Receta? = withContext(Dispatchers.IO) {
        try {
            api.getRecetaPorId(id)?.toModel()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun obtenerPorUsuario(alias: String): List<Receta> = withContext(Dispatchers.IO) {
        try {
            api.getRecetasPorUsuario(alias).map { it.toModel() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun obtenerAprobadasAprobadasDTO(): List<RecetaAprobadaDTO> = withContext(Dispatchers.IO) {
        try {
            api.getRecetasAprobadasNew()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun obtenerRecientesAprobadasDTO(): List<RecetaAprobadaDTO> = withContext(Dispatchers.IO) {
        try {
            api.getRecetasAprobadasRecientesNew()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun obtenerMisRecetas(token: String): List<MiRecetaDTO> = withContext(Dispatchers.IO) {
        return@withContext try {
            api.getMisRecetas("Bearer $token")
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun toggleFeaturedRecipe(recipeId: Int, token: String): Response<String> {
        return api.toggleFeaturedRecipe("Bearer $token", recipeId)
    }

    suspend fun obtenerRecetasGuardadas(token: String): List<RecetaGuardadaDTO> = withContext(Dispatchers.IO) {
        return@withContext try {
            api.getRecetasGuardadas("Bearer $token")
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getCantidadRecetas(token: String): Response<Int> {
        return api.getCantidadRecetas("Bearer $token")
    }

    suspend fun editarReceta(id: Int, request: EditarRecetaRequest, token: String): Response<Void> {
        return api.editarReceta(id, request, "Bearer $token")
    }

    suspend fun obtenerMiRecetaPorId(id: Int, token: String): Response<MiRecetaDTO> {
        return api.obtenerMiRecetaPorId(id, "Bearer $token")
    }


}
