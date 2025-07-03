package com.example.tpo_desa_1.data.source.remote

import com.example.tpo_desa_1.data.model.RecetaDTO
import com.example.tpo_desa_1.data.model.request.LoginRequest
import com.example.tpo_desa_1.data.model.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import com.example.tpo_desa_1.data.model.response.RecetaAprobadaDTO


interface ApiService {

    // ——————————————————————————————
    //   RECETAS PÚBLICAS
    // ——————————————————————————————

    /** Devuelve todas las recetas (públicas) */
    @GET("recipes")
    suspend fun getRecetas(): List<RecetaDTO>

    /** Devuelve la receta por su ID */
    @GET("recipes/{id}")
    suspend fun getRecetaPorId(
        @Path("id") id: Int
    ): RecetaDTO

    // ——————————————————————————————
    //   RECETAS DEL USUARIO
    // ——————————————————————————————

    /** Devuelve las recetas creadas por un usuario */
    @GET("recipes/user/{alias}")
    suspend fun getRecetasPorUsuario(
        @Path("alias") alias: String
    ): List<RecetaDTO>

    // ——————————————————————————————
    //   RECETAS APROBADAS
    // ——————————————————————————————

    @GET("recipes/aprobadas")
    suspend fun getRecetasAprobadasNew(): List<RecetaAprobadaDTO>

    @GET("recipes/status/aprobados/ultimas")
    suspend fun getRecetasAprobadasRecientesNew(): List<RecetaAprobadaDTO>

    // ——————————————————————————————
    //   CREACIÓN / SUBIDA
    // ——————————————————————————————

    /** Crea una nueva receta (requiere auth) */
    @POST("recipes")
    suspend fun postReceta(
        @Body receta: RecetaDTO
    )

    @Multipart
    @POST("/image/upload")
    suspend fun subirImagen(
        @Part imagen: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Response<ResponseBody>

    // ——————————————————————————————
    //   AUTENTICACIÓN
    // ——————————————————————————————

    /** Login: devuelve tokens y datos de usuario */
    @POST("auth/authenticate")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}
