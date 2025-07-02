package com.example.tpo_desa_1.config

import com.example.tpo_desa_1.data.persistence.UserPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val userPreferences: UserPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { userPreferences.getToken() }

        val request = if (!token.isNullOrBlank()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
