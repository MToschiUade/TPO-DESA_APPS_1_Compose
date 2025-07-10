package com.example.tpo_desa_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.data.model.Comentario
import com.example.tpo_desa_1.repository.RecetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PuntajeViewModel(
    private val repository: RecetaRepository
) : ViewModel() {

    private val _mapaComentarios = MutableStateFlow<Map<Int, List<Comentario>>>(emptyMap())
    val mapaComentarios: StateFlow<Map<Int, List<Comentario>>> = _mapaComentarios

    fun cargarComentariosDe(recetaIds: List<Int>) {
        viewModelScope.launch {
            val resultados = mutableMapOf<Int, List<Comentario>>()
            recetaIds.forEach { recetaId ->
                try {
                    val comentarios = repository.obtenerComentariosDeReceta(recetaId)
                    println("📥 Receta $recetaId tiene ${comentarios.size} comentarios")
                    resultados[recetaId] = comentarios
                } catch (e: Exception) {
                    println("❌ Error al cargar comentarios de $recetaId: ${e.message}")
                    resultados[recetaId] = emptyList()
                }
            }
            _mapaComentarios.value = resultados
        }
    }


    fun getPromedioPara(recetaId: Int): Float {
        val comentarios = _mapaComentarios.value[recetaId] ?: return 0f
        val puntuaciones = comentarios.mapNotNull { it.puntaje }
        return if (puntuaciones.isNotEmpty()) puntuaciones.average().toFloat() else 0f
    }

    fun getCantidadReviews(recetaId: Int): Int {
        return _mapaComentarios.value[recetaId]?.size ?: 0
    }

    fun getDistribucionEstrellas(recetaId: Int): Map<Int, Int> {
        val comentarios = _mapaComentarios.value[recetaId] ?: return emptyMap()
        return (1..5).associateWith { estrellas ->
            comentarios.count { it.puntaje == estrellas }
        }
    }

    fun obtenerPromediosPorReceta(): Map<Int, Int> {
        return _mapaComentarios.value.mapValues { (_, comentarios) ->
            val puntuaciones = comentarios.mapNotNull { it.puntaje }
            if (puntuaciones.isNotEmpty()) puntuaciones.average().toInt() else 0
        }
    }
}
