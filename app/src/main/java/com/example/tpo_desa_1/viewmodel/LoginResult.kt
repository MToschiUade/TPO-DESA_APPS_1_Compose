package com.example.tpo_desa_1.viewmodel

sealed class LoginResult {
    object Idle : LoginResult()
    object Loading : LoginResult()
    data class Success(val token: String) : LoginResult()
    data class Error(val message: String) : LoginResult()
}
