package com.example.tpo_desa_1.data.source.remote

import com.example.tpo_desa_1.config.AppConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceBuilder {

    // ⚠️ Si no usás AuthInterceptor en recuperación de contraseña, se puede evitar
    private val client = OkHttpClient.Builder().build()

    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(AppConfig.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}
