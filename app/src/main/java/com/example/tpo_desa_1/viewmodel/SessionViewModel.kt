package com.example.tpo_desa_1.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.config.AppConfig
import com.example.tpo_desa_1.data.model.Usuario
import com.example.tpo_desa_1.data.model.request.LoginRequest
import com.example.tpo_desa_1.data.source.remote.ApiService
import com.example.tpo_desa_1.repository.UsuarioRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log


class SessionViewModel(
    application: Application,
    private val usuarioRepository: UsuarioRepository
) : AndroidViewModel(application) {

    private val _usuarioLogueado = mutableStateOf<Usuario?>(null)
    val usuarioLogueado: State<Usuario?> = _usuarioLogueado

    private val _loginState = mutableStateOf<LoginResult>(LoginResult.Idle)
    val loginState: State<LoginResult> = _loginState

    private val apiService: ApiService = Retrofit.Builder()
        .also {
            Log.d("API-BASE", "Retrofit se construye con BASE_URL: ${AppConfig.BASE_URL}")
        }
        .baseUrl(AppConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    fun login(identificador: String, password: String, onResult: (Boolean) -> Unit) {
        if (AppConfig.useCloudLogin) {
            loginWithBackend(identificador, password, onResult)
        } else {
            loginDemo(identificador, password, onResult)
        }
    }

    private fun loginDemo(identificador: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val usuario = usuarioRepository.login(identificador, password)
            _usuarioLogueado.value = usuario
            onResult(usuario != null)
        }
    }

    private fun loginWithBackend(username: String, password: String, onResult: (Boolean) -> Unit) {
        Log.d("LOGIN", "Intentando login con usuario: $username y BASE_URL: ${AppConfig.BASE_URL}")
        viewModelScope.launch {
            _loginState.value = LoginResult.Loading
            try {
                val response = apiService.loginUsuarioCloud(LoginRequest(username, password))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        saveAuthToken(body.accessToken)

                        _usuarioLogueado.value = Usuario(alias = username, email = "backend@user.com") // temporal
                        _loginState.value = LoginResult.Success(body.accessToken)
                        onResult(true)
                    } else {
                        _loginState.value = LoginResult.Error("Respuesta vacía")
                        onResult(false)
                    }
                    Log.d("LOGIN", "Respuesta exitosa: ${body?.accessToken}")
                } else {
                    _loginState.value = LoginResult.Error("Credenciales incorrectas")
                    onResult(false)
                }
            } catch (e: HttpException) {
                _loginState.value = LoginResult.Error("Error HTTP: ${e.message()}")
                onResult(false)
            } catch (e: Exception) {
                _loginState.value = LoginResult.Error("Error inesperado: ${e.message}")
                Log.e("LOGIN", "Excepción: ${e.message}", e)
                onResult(false)
            }
        }
    }

    fun logout() {
        _usuarioLogueado.value = null
        clearToken()
    }

    private fun saveAuthToken(token: String) {
        getApplication<Application>().getSharedPreferences("auth", Context.MODE_PRIVATE)
            .edit()
            .putString("access_token", token)
            .apply()
    }

    private fun clearToken() {
        getApplication<Application>().getSharedPreferences("auth", Context.MODE_PRIVATE)
            .edit()
            .remove("access_token")
            .apply()
    }

    fun getSavedToken(): String? {
        return getApplication<Application>().getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("access_token", null)
    }
}
