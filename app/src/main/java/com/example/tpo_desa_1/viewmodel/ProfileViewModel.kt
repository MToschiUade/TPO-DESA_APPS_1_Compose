package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.data.model.Receta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val recetaRepository: RecetaRepository
) : ViewModel() {

    private val _recetasCreadas = MutableStateFlow<List<Receta>>(emptyList())
    val recetasCreadas: StateFlow<List<Receta>> = _recetasCreadas.asStateFlow()

    fun cargarRecetasCreadas(aliasUsuario: String) {
        viewModelScope.launch {
            _recetasCreadas.value = recetaRepository.obtenerRecetasPorUsuario(aliasUsuario)
        }
    }
}
