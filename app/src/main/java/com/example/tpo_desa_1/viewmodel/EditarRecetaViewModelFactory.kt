package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_desa_1.data.persistence.UserPreferences
import com.example.tpo_desa_1.repository.RecetaRepository

class EditarRecetaViewModelFactory(
    private val recetaRepository: RecetaRepository,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditarRecetaViewModel(recetaRepository, userPreferences) as T
    }
}
