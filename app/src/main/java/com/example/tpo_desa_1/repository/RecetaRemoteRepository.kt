package com.example.tpo_desa_1.repository

import android.content.Context
import android.net.Uri
import com.example.tpo_desa_1.config.AppConfig
import com.example.tpo_desa_1.data.model.*
import com.example.tpo_desa_1.data.source.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecetaRemoteRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl(AppConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

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
}
