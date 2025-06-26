package com.example.tpo_desa_1.data.source.remote

import com.example.tpo_desa_1.data.model.RecetaDTO
import com.example.tpo_desa_1.data.model.request.LoginRequest
import com.example.tpo_desa_1.data.model.response.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /*RECETAS*/

    @GET("recipes")
    suspend fun getRecetas(): List<RecetaDTO>

    @POST("recipes")
    suspend fun postReceta(
        @Body receta: RecetaDTO
    )

    @Multipart
    @POST("image/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): String

    /*LOGIN*/

    @POST("auth/authenticate")
    suspend fun loginUsuarioCloud(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    suspend fun getRecetaPorId(id: Int): RecetaDTO?
    suspend fun getRecetasPorUsuario(alias: String): List<RecetaDTO>
    suspend fun getRecetasAprobadasRecientes(): List<RecetaDTO>
    suspend fun getRecetasAprobadas(): List<RecetaDTO>

}
