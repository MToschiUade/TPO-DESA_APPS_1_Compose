package com.example.tpo_desa_1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.repository.UsuarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SessionViewModel(
    application: Application,
    private val usuarioRepository: UsuarioRepository
) : AndroidViewModel(application) {

    // Estado interno de login (cargando, éxito, error…)
    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState: StateFlow<LoginResult> = _loginState.asStateFlow()

    // ❗ Exponemos el token persistido
    val accessToken: StateFlow<String?> =
        usuarioRepository
            .getAccessToken()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    // ❗ Exponemos el estado de sesión (true si hay token no vacío)
    val isLoggedIn: StateFlow<Boolean> =
        usuarioRepository
            .isLoggedIn()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                false
            )

    // ❗ Exponemos alias y email del usuario
    val alias: StateFlow<String?> =
        usuarioRepository
            .getAlias()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    val email: StateFlow<String?> =
        usuarioRepository
            .getEmail()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    /**
     * Ejecuta el login remoto y persiste datos en DataStore.
     * Actualiza _loginState para reflejar loading/success/error.
     */
    fun login(identificador: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _loginState.value = LoginResult.Loading
            val success = usuarioRepository.login(identificador, password)
            _loginState.value = if (success) LoginResult.Success("OK") else LoginResult.Error("Login fallido")
            onResult(success)
        }
    }

    /**
     * Limpia sesión en repositorio (borra DataStore).
     */
    fun logout() {
        viewModelScope.launch {
            println("👋 Ejecutando logout desde SessionViewModel…")
            usuarioRepository.logout()
        }
    }
}
