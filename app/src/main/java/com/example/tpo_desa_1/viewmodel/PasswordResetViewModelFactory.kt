package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_desa_1.repository.PasswordResetRepository

class PasswordResetViewModelFactory(
    private val repository: PasswordResetRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PasswordResetViewModel(repository) as T
    }
}
