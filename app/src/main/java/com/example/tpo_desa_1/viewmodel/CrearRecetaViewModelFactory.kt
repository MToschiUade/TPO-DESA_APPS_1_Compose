package com.example.tpo_desa_1.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_desa_1.data.persistence.UserPreferences
import com.example.tpo_desa_1.repository.RecetaRepository

class CrearRecetaViewModelFactory(
    private val recetaRepository: RecetaRepository,
    private val userPreferences: UserPreferences,
    private val appContext: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrearRecetaViewModel::class.java)) {
            return CrearRecetaViewModel(recetaRepository, userPreferences, appContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

