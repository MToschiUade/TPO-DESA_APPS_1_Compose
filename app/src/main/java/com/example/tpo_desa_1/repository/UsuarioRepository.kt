package com.example.tpo_desa_1.repository

import com.example.tpo_desa_1.data.model.request.LoginRequest
import com.example.tpo_desa_1.data.persistence.UserPreferences
import com.example.tpo_desa_1.data.source.remote.ApiService
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {

    suspend fun login(identificador: String, password: String): Boolean {
        val isEmail = identificador.contains("@") && identificador.contains(".")
        val alias = if (isEmail) {
            // 🔒 Por ahora el BE no acepta login por email
            return false
        } else identificador

        return try {
            val response = apiService.login(LoginRequest(username = alias, password = password))
            if (response.isSuccessful) {
                val data = response.body()!!
                userPreferences.saveLoginData(
                    alias = alias,
                    email = if (isEmail) identificador else "",
                    accessToken = data.accessToken,
                    refreshToken = data.token
                )
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun logout() {
        println("🧹 Limpiando UserPreferences...")
        userPreferences.clear()
    }


    fun isLoggedIn(): Flow<Boolean> = userPreferences.isLoggedIn
    fun getAccessToken(): Flow<String?> = userPreferences.accessTokenFlow
    fun getRefreshToken(): Flow<String?> = userPreferences.refreshTokenFlow
    fun getAlias(): Flow<String?> = userPreferences.aliasFlow
    fun getEmail(): Flow<String?> = userPreferences.emailFlow
}
