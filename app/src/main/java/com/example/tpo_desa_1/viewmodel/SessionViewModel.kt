package com.example.tpo_desa_1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.repository.UsuarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.tpo_desa_1.data.model.UsuarioDetalle


class SessionViewModel(
    application: Application,
    private val usuarioRepository: UsuarioRepository
) : AndroidViewModel(application) {

    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState: StateFlow<LoginResult> = _loginState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken.asStateFlow()

    private val _alias = MutableStateFlow<String?>(null)
    val alias: StateFlow<String?> = _alias.asStateFlow()

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email.asStateFlow()

    private val _usuarioDetalle = MutableStateFlow<UsuarioDetalle?>(null)
    val usuarioDetalle: StateFlow<UsuarioDetalle?> = _usuarioDetalle.asStateFlow()

    init {
        // Escuchar cambios en DataStore
        viewModelScope.launch {
            usuarioRepository.getAccessToken().collect {
                _accessToken.value = it
                _isLoggedIn.value = !it.isNullOrBlank()
            }
        }

        viewModelScope.launch {
            usuarioRepository.getAlias().collect {
                _alias.value = it
            }
        }

        viewModelScope.launch {
            usuarioRepository.getEmail().collect {
                _email.value = it
            }
        }
    }

    fun login(identificador: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _loginState.value = LoginResult.Loading
            val success = usuarioRepository.login(identificador, password)
            if (success) {
                _loginState.value = LoginResult.Success("OK")
            } else {
                _loginState.value = LoginResult.Error("Login fallido")
            }
            onResult(success)
        }
    }

    fun logout() {
        viewModelScope.launch {
            println("👋 Ejecutando logout desde SessionViewModel")
            usuarioRepository.logout()

            // Limpiar estado local también
            _accessToken.value = null
            _isLoggedIn.value = false
            _alias.value = null
            _email.value = null
            _loginState.value = LoginResult.Idle
        }
    }

    fun loadUsuarioDetalle() {
        viewModelScope.launch {
            val detalle = usuarioRepository.obtenerUsuarioDetalle()
            if (detalle != null) {
                println("✅ UsuarioDetalle recibido: $detalle")
                _usuarioDetalle.value = detalle
            } else {
                println("⚠️ No se pudo obtener info detallada del usuario")
            }
        }
    }

}

