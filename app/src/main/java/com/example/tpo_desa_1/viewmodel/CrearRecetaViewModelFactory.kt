package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_desa_1.repository.RecetaRepository

class CrearRecetaViewModelFactory(
    private val recetaRepository: RecetaRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrearRecetaViewModel::class.java)) {
            return CrearRecetaViewModel(recetaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
