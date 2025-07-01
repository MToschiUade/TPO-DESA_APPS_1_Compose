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
            // 🔒 El backend aún no soporta login por email
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
                println("✅ Login exitoso: token guardado")
                true
            } else {
                println("❌ Error login: código HTTP ${response.code()}")
                false
            }
        } catch (e: Exception) {
            println("💥 Excepción en login: ${e.message}")
            false
        }
    }

    suspend fun logout() {
        println("👋 Cerrando sesión y limpiando preferencias")
        userPreferences.clear()
    }

    // Exponer flujos que consume SessionViewModel
    fun isLoggedIn(): Flow<Boolean> = userPreferences.isLoggedIn()
    fun getAccessToken(): Flow<String?> = userPreferences.getAccessToken()
    fun getRefreshToken(): Flow<String?> = userPreferences.getRefreshToken()
    fun getAlias(): Flow<String?> = userPreferences.getAlias()
    fun getEmail(): Flow<String?> = userPreferences.getEmail()
}