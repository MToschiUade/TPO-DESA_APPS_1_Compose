package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.repository.RecetaRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class RecetaViewModel(
    private val repository: RecetaRepository
) : ViewModel() {

    private val _recetas = mutableStateOf<List<Receta>>(emptyList())
    private val _recetasAprobadasRecientes = mutableStateOf<List<Receta>>(emptyList())
    private val _recetasAprobadas = mutableStateOf<List<Receta>>(emptyList())

    val recetas: State<List<Receta>> = _recetas
    val recetasAprobadasRecientes: State<List<Receta>> = _recetasAprobadasRecientes
    val recetasAprobadas: State<List<Receta>> = _recetasAprobadas

    init {
        cargarRecetas()
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
