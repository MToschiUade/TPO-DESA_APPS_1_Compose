package com.example.tpo_desa_1.repository

import com.example.tpo_desa_1.data.mapper.toModel
import com.example.tpo_desa_1.data.model.UsuarioDetalle
import com.example.tpo_desa_1.data.model.request.LoginRequest
import com.example.tpo_desa_1.data.persistence.UserPreferences
import com.example.tpo_desa_1.data.source.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

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

    suspend fun obtenerUsuarioDetalle(): UsuarioDetalle? {
        val token = userPreferences.getToken()
        val alias = getAlias().firstOrNull()

        println("🔐 TOKEN actual: $token")
        println("📛 ALIAS actual: $alias")

        if (token == null || alias == null) {
            println("❌ No se puede continuar. Faltan token o alias.")
            return null
        }

        return try {
            val response = apiService.getUsuarioDetalle("Bearer $token", alias)
            println("📡 Llamada a /users/alias => código HTTP: ${response.code()}")

            if (response.isSuccessful) {
                println("✅ Respuesta exitosa. Cuerpo: ${response.body()}")
                response.body()?.toModel()
            } else {
                println("❌ Fallo en respuesta. Código: ${response.code()}, Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            println("💥 Excepción al llamar a getUsuarioDetalle: ${e.message}")
            null
        }
    }




}