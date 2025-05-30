package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.repository.RecetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State
import com.example.tpo_desa_1.data.demo.demoRecetas


class RecetaViewModel(
    private val repository: RecetaRepository,
    loadDemo: Boolean = false
) : ViewModel() {

    private val _recetas = mutableStateOf<List<Receta>>(emptyList())
    private val _recetasAprobadasRecientes = mutableStateOf<List<Receta>>(emptyList())
    private val _recetasAprobadas = mutableStateOf<List<Receta>>(emptyList())

    val recetas: State<List<Receta>> = _recetas
    val recetasAprobadasRecientes: State<List<Receta>> = _recetasAprobadasRecientes
    val recetasAprobadas: State<List<Receta>> = _recetasAprobadas

    init {
        if (loadDemo) {
            cargarDemo()
        } else {
            cargarRecetas()
        }
    }

    private fun cargarDemo() {
        viewModelScope.launch {
            // Solo guarda si aún no están
            demoRecetas.forEach { receta ->
                repository.cargarRecetasEnBase(receta)
            }
            cargarRecetas()
        }
    }

    fun cargarRecetas() {
        viewModelScope.launch {
            _recetas.value = repository.obtenerTodas()
        }
    }

    fun cargarRecientesAprobadas() {
        viewModelScope.launch {
            _recetasAprobadasRecientes.value = repository.obtenerRecetasAprobadasRecientes()
        }
    }

    fun cargarRecetasAprobadas() {
        viewModelScope.launch {
            _recetasAprobadas.value = repository.obtenerTodasAprobadas()
        }
    }

}
