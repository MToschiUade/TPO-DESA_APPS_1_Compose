package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.data.mapper.toEditarRecetaRequest
import com.example.tpo_desa_1.data.model.response.MiRecetaDTO
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.data.persistence.UserPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EditarRecetaViewModel(
    private val recetaRepository: RecetaRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _receta = MutableStateFlow<MiRecetaDTO?>(null)
    val receta: StateFlow<MiRecetaDTO?> = _receta.asStateFlow()

    fun cargarReceta(recetaId: Int) {
        viewModelScope.launch {
            val token = userPreferences.getAccessToken().firstOrNull()
            if (token.isNullOrEmpty()) {
                _receta.value = null
                return@launch
            }

            val recetaLocal = recetaRepository.obtenerMiRecetaPorId(recetaId, token)
            _receta.value = recetaLocal
        }
    }

    fun editarReceta(
        recetaId: Int,
        recetaActualizada: MiRecetaDTO,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val token = userPreferences.getAccessToken().firstOrNull()
            if (token.isNullOrEmpty()) {
                onError("No se pudo obtener el token de acceso.")
                return@launch
            }

            try {
                val request = recetaActualizada.toEditarRecetaRequest()
                recetaRepository.editarReceta(
                    recipeId = recetaId,
                    request = request,
                    token = token
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error al editar la receta")
            }
        }
    }

}
