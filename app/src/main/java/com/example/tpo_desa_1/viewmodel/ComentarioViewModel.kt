package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.data.model.Comentario
import com.example.tpo_desa_1.data.persistence.UserPreferences
import com.example.tpo_desa_1.repository.RecetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ComentarioViewModel(
    private val repository: RecetaRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _comentarios = MutableStateFlow<List<Comentario>>(emptyList())
    val comentarios: StateFlow<List<Comentario>> = _comentarios

    var nuevoComentario = MutableStateFlow("")
    var nuevoPuntaje = MutableStateFlow(0)

    fun cargarComentarios(recetaId: Int) {
        viewModelScope.launch {
            val comentarios = repository.obtenerComentariosDeReceta(recetaId)
            _comentarios.value = comentarios
        }
    }

    fun enviarComentario(recetaId: Int, comentario: String, puntaje: Int?) {
        viewModelScope.launch {
            val token = userPreferences.getAccessToken().firstOrNull()
            if (!token.isNullOrEmpty() && puntaje != null) {
                repository.agregarComentarioAReceta(token, recetaId, comentario, puntaje)
                cargarComentarios(recetaId)
            }
        }
    }

}
