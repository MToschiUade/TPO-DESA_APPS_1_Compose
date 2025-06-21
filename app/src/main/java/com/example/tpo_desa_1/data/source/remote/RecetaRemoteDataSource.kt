package com.example.tpo_desa_1.data.source.remote

import android.content.Context
import android.net.Uri
import com.example.tpo_desa_1.data.model.RecetaDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

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

    suspend fun enviarReceta(dto: RecetaDTO) = withContext(Dispatchers.IO) {
        api.postReceta(dto)
    }

    // Más adelante: funciones como obtenerRecetasDesdeApi() si tu backend lo permite.
}
