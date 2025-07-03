package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.data.persistence.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DestacarRecetaViewModel(
    private val recetaRepository: RecetaRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _toggleResult = MutableStateFlow<Result<String>?>(null)
    val toggleResult: StateFlow<Result<String>?> = _toggleResult

    fun toggleDestacada(recipeId: Int) {
        viewModelScope.launch {
            val token = userPreferences.getAccessToken().firstOrNull()
            if (token != null) {
                val result = recetaRepository.toggleRecetaDestacada(recipeId, token)
                _toggleResult.value = result
            } else {
                _toggleResult.value = Result.failure(Exception("Token no disponible"))
            }
        }
    }
}
