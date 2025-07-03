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
import com.example.tpo_desa_1.utils.uriToFile
import okhttp3.RequestBody.Companion.asRequestBody

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

    suspend fun obtenerRecientesAprobadas(): List<Receta> = withContext(Dispatchers.IO) {
        try {
            api.getRecetasAprobadasRecientes().map { it.toModel() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun obtenerAprobadas(): List<Receta> = withContext(Dispatchers.IO) {
        try {
            api.getRecetasAprobadas().map { it.toModel() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}
