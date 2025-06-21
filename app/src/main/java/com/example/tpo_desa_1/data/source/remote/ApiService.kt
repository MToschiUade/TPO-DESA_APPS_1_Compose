package com.example.tpo_desa_1.data.source.remote

import com.example.tpo_desa_1.data.model.RecetaDTO
import com.example.tpo_desa_1.data.model.request.LoginRequest
import com.example.tpo_desa_1.data.model.response.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Response
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

    @POST("auth/authenticate")
    suspend fun loginUsuarioCloud(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}
