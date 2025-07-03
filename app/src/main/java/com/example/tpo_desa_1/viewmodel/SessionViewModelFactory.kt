package com.example.tpo_desa_1.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_desa_1.repository.UsuarioRepository

class SessionViewModelFactory(
    private val application: Application,
    private val usuarioRepository: UsuarioRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SessionViewModel(application, usuarioRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
