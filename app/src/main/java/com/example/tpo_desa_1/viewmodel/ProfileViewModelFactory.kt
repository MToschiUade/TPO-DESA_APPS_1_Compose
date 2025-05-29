package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_desa_1.repository.RecetaRepository

class ProfileViewModelFactory(
    private val recetaRepository: RecetaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(recetaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}