package com.example.tpo_desa_1.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.repository.UsuarioRepository
import com.example.tpo_desa_1.viewmodel.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SessionViewModel(
    application: Application,
    private val usuarioRepository: UsuarioRepository
) : AndroidViewModel(application) {

    private val _loginState = mutableStateOf<LoginResult>(LoginResult.Idle)
    val loginState: State<LoginResult> = _loginState

    val isLoggedIn: Flow<Boolean> = usuarioRepository.isLoggedIn()
    val accessToken: Flow<String?> = usuarioRepository.getAccessToken()
    val alias: Flow<String?> = usuarioRepository.getAlias()
    val email: Flow<String?> = usuarioRepository.getEmail()

    fun login(identificador: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _loginState.value = LoginResult.Loading
            val success = usuarioRepository.login(identificador, password)
            _loginState.value = if (success) LoginResult.Success("OK") else LoginResult.Error("Login fallido")
            onResult(success)
        }
    }

    fun logout() {
        viewModelScope.launch {
            println("👋 Ejecutando logout desde SessionViewModel...")
            usuarioRepository.logout()
        }
    }

}
