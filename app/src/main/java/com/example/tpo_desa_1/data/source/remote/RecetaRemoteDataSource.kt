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
import com.example.tpo_desa_1.data.model.response.RecetaAprobadaDTO

class RecetaRemoteDataSource(
    private val api: ApiService
) {

    suspend fun subirImagen(context: Context, uri: Uri): String = withContext(Dispatchers.IO) {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("No se pudo abrir la imagen")

        val bytes = inputStream.readBytes()
        val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData(
            name = "image",
            filename = "receta.jpg",
            body = requestBody
        )

        api.uploadImage(imagePart)
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


}
