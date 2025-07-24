package com.example.tpo_desa_1.repository

import com.example.tpo_desa_1.data.model.request.*
import com.example.tpo_desa_1.data.source.remote.ApiService
import retrofit2.HttpException
import java.io.IOException

class PasswordResetRepository(
    private val apiService: ApiService
) {
    suspend fun requestReset(email: String): Result<Unit> = try {
        println("📨 Enviando email como FormUrlEncoded: $email")
        val response = apiService.requestPasswordReset(email)

        if (response.isSuccessful) {
            println("✅ Reset solicitado con éxito")
            Result.success(Unit)
        } else {
            val errorBody = response.errorBody()?.string()
            println("❌ Error HTTP ${response.code()} - $errorBody")
            Result.failure(HttpException(response))
        }
    } catch (e: Exception) {
        println("🚨 Excepción al solicitar reset: ${e.message}")
        Result.failure(e)
    }

    suspend fun verifyCode(email: String, totpCode: String): Result<Boolean> = try {
        println("📤 Enviando: email=$email, totpCode=$totpCode")

        val response = apiService.verifyCode(email, totpCode)

        if (response.isSuccessful) {
            val result = response.body()?.success ?: false
            println("✅ Verificación exitosa: $result")
            Result.success(result)
        } else {
            val errorBody = response.errorBody()?.string()
            println("❌ Error HTTP ${response.code()} - $errorBody")
            Result.failure(HttpException(response))
        }
    } catch (e: Exception) {
        println("🚨 Excepción al verificar código: ${e.message}")
        Result.failure(e)
    }

    suspend fun changePassword(email: String, totpCode: String, newpass: String): Result<Unit> = try {
        println("📩 Enviando datos a changePassword → email=$email, totpCode=$totpCode, newpass=$newpass")
        val response = apiService.changePassword(email, totpCode, newpass)

        if (response.isSuccessful) {
            println("✅ Contraseña cambiada con éxito")
            Result.success(Unit)
        } else {
            val errorBody = response.errorBody()?.string()
            println("❌ Error HTTP ${response.code()} - $errorBody")
            Result.failure(HttpException(response))
        }
    } catch (e: IOException) {
        println("💥 Excepción de red: ${e.message}")
        Result.failure(e)
    } catch (e: Exception) {
        println("💥 Excepción inesperada: ${e.message}")
        Result.failure(e)
    }

}
