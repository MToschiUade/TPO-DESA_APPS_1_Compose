package com.example.tpo_desa_1.repository

import com.example.tpo_desa_1.data.model.RecetaDTO
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("image/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): String

    @POST("recipes")
    suspend fun postReceta(
        @Body receta: RecetaDTO
    )
}
