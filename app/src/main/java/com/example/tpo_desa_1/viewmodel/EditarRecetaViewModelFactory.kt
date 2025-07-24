package com.example.tpo_desa_1.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_desa_1.data.persistence.UserPreferences
import com.example.tpo_desa_1.repository.RecetaRepository

class EditarRecetaViewModelFactory(
    private val recetaId: Int,
    private val recetaRepository: RecetaRepository,
    private val userPreferences: UserPreferences,
    private val appContext: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditarRecetaViewModel(
            recetaId = recetaId,
            recetaRepository = recetaRepository,
            userPreferences = userPreferences,
            appContext = appContext
        ) as T
    }
}
