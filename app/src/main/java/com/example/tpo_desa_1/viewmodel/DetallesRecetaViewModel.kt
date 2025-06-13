package com.example.tpo_desa_1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.data.model.Comentario
import com.example.tpo_desa_1.data.model.PasoReceta
import com.example.tpo_desa_1.repository.DetallesRecetaRepository
import kotlinx.coroutines.launch

class DetallesRecetaViewModel(
    private val repository: DetallesRecetaRepository
) : ViewModel() {

    var comentarios by mutableStateOf<List<Comentario>>(emptyList())
        private set

    var pasos by mutableStateOf<List<PasoReceta>>(emptyList())
        private set

    fun cargarDatos(recetaId: Int) {
        viewModelScope.launch {
            comentarios = repository.obtenerComentariosAprobados(recetaId)
            pasos = repository.obtenerPasos(recetaId)
        }
    }

    fun eliminarComentario(id: Int, autor: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val eliminado = repository.eliminarComentarioSiAutor(id, autor)
            if (eliminado) {
                comentarios = comentarios.filterNot { it.id == id }
                onSuccess()
            }
        }
    }
}
