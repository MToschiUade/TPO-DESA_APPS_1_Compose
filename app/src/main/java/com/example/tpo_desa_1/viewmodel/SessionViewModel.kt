package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.tpo_desa_1.data.model.Usuario
import com.example.tpo_desa_1.repository.UsuarioRepository
import kotlinx.coroutines.launch


class SessionViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _usuarioLogueado = mutableStateOf<Usuario?>(null)
    val usuarioLogueado: State<Usuario?> = _usuarioLogueado


    fun login(identificador: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val usuario = usuarioRepository.login(identificador, password)
            _usuarioLogueado.value = usuario
            onResult(usuario != null)
        }
    }

    fun logout() {
        _usuarioLogueado.value = null
    }

}
