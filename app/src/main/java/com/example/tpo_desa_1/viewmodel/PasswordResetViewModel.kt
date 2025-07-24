package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.repository.PasswordResetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PasswordResetViewModel(
    private val repository: PasswordResetRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun clearError() {
        _errorMessage.value = null
    }

    fun requestReset(email: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.requestReset(email)
            _loading.value = false

            if (result.isSuccess) {
                _errorMessage.value = null
                onSuccess()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Error al enviar el código"
            }
        }
    }

    fun verifyCode(email: String, totpCode: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.verifyCode(email, totpCode)
            _loading.value = false

            if (result.getOrDefault(false)) {
                _errorMessage.value = null
                onSuccess()
            } else {
                _errorMessage.value = "Código incorrecto"
            }
        }
    }

    fun changePassword(email: String, totpCode: String, newpass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.changePassword(email, totpCode, newpass)
            _loading.value = false

            if (result.isSuccess) {
                _errorMessage.value = null
                onSuccess()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Error al cambiar la contraseña"
            }
        }
    }
}
