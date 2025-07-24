package com.example.tpo_desa_1.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpo_desa_1.data.model.IngredienteUI
import com.example.tpo_desa_1.data.model.PasoPreparacionUI
import com.example.tpo_desa_1.data.model.TipoMedia
import com.example.tpo_desa_1.data.model.UnidadMedida
import com.example.tpo_desa_1.data.model.request.EditarRecetaRequest
import com.example.tpo_desa_1.data.model.request.IngredienteRequest
import com.example.tpo_desa_1.data.model.request.PasoRequest
import com.example.tpo_desa_1.data.persistence.UserPreferences
import com.example.tpo_desa_1.repository.RecetaRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

enum class PasoFormularioEdicion {
    PREPARACION,
    INGREDIENTES,
    PORTADA
}

class EditarRecetaViewModel(
    private val recetaId: Int,
    private val recetaRepository: RecetaRepository,
    private val userPreferences: UserPreferences,
    private val appContext: Context
) : ViewModel() {

    private val _pasoActual = mutableStateOf(PasoFormularioEdicion.PREPARACION)
    val pasoActual: State<PasoFormularioEdicion> = _pasoActual

    private val _pasosPreparacion = mutableStateListOf<PasoPreparacionUI>()
    val pasosPreparacion: List<PasoPreparacionUI> = _pasosPreparacion

    private val _ingredientes = mutableStateListOf<IngredienteUI>()
    val ingredientes: List<IngredienteUI> = _ingredientes

    private val _imagenPortadaUri = mutableStateOf<String?>(null)
    val imagenPortadaUri: State<String?> = _imagenPortadaUri

    private val _tituloReceta = mutableStateOf("")
    val tituloReceta: State<String> = _tituloReceta

    private val _tiempoHoras = mutableStateOf(0)
    val tiempoHoras: State<Int> = _tiempoHoras

    private val _tiempoMinutos = mutableStateOf(0)
    val tiempoMinutos: State<Int> = _tiempoMinutos

    init {
        cargarDatosIniciales()
    }

    private fun cargarDatosIniciales() {
        viewModelScope.launch {
            val token = userPreferences.getAccessToken().firstOrNull() ?: return@launch
            val receta = recetaRepository.obtenerMiRecetaPorId(recetaId, token) ?: return@launch

            println("🧠 Receta cargada: ${receta.title}")
            println("📸 Imagen: ${receta.imagePortada}")
            println("🕒 Duración: ${receta.duracion} minutos")
            println("📋 Ingredientes: ${receta.ingredientes.size}")
            println("👨‍🍳 Pasos: ${receta.pasos.size}")
            receta.pasos.forEachIndexed { i, paso ->
                println("Paso $i → proceso: ${paso.proceso}, url: ${paso.url}")
            }

            _tituloReceta.value = receta.title
            _imagenPortadaUri.value = receta.imagePortada

            val duracion = receta.duracion
            _tiempoHoras.value = duracion / 60
            _tiempoMinutos.value = duracion % 60

            _ingredientes.clear()
            _ingredientes.addAll(receta.ingredientes.map {
                IngredienteUI(
                    nombre = it.nombre,
                    cantidad = it.medida.toString(),
                    unidad = it.nombreMedida.toUnidadEnum()
                )
            })

            _pasosPreparacion.clear()
            _pasosPreparacion.addAll(receta.pasos.map {
                PasoPreparacionUI(
                    descripcion = it.proceso,
                    mediaUri = it.url,
                    tipoMedia = it.url.toTipoMedia()
                )
            })
        }
    }

    fun avanzarPaso() {
        _pasoActual.value = when (_pasoActual.value) {
            PasoFormularioEdicion.PREPARACION -> PasoFormularioEdicion.INGREDIENTES
            PasoFormularioEdicion.INGREDIENTES -> PasoFormularioEdicion.PORTADA
            PasoFormularioEdicion.PORTADA -> PasoFormularioEdicion.PORTADA
        }
    }

    fun retrocederPaso() {
        _pasoActual.value = when (_pasoActual.value) {
            PasoFormularioEdicion.PORTADA -> PasoFormularioEdicion.INGREDIENTES
            PasoFormularioEdicion.INGREDIENTES -> PasoFormularioEdicion.PREPARACION
            PasoFormularioEdicion.PREPARACION -> PasoFormularioEdicion.PREPARACION
        }
    }

    fun actualizarTitulo(titulo: String) {
        _tituloReceta.value = titulo
    }

    fun actualizarHoras(horas: Int) {
        _tiempoHoras.value = horas
    }

    fun actualizarMinutos(minutos: Int) {
        _tiempoMinutos.value = minutos
    }

    fun actualizarImagenPortada(uri: String?) {
        _imagenPortadaUri.value = uri
    }

    fun agregarIngrediente() {
        _ingredientes.add(IngredienteUI())
    }

    fun eliminarIngrediente(index: Int) {
        if (_ingredientes.size > 1) _ingredientes.removeAt(index)
    }

    fun actualizarIngrediente(index: Int, nuevo: IngredienteUI) {
        _ingredientes[index] = nuevo
    }

    fun agregarPaso() {
        _pasosPreparacion.add(PasoPreparacionUI())
    }

    fun eliminarPaso(index: Int) {
        if (_pasosPreparacion.size > 1) _pasosPreparacion.removeAt(index)
    }

    fun actualizarDescripcionPaso(index: Int, descripcion: String) {
        _pasosPreparacion[index] = _pasosPreparacion[index].copy(descripcion = descripcion)
    }

    fun actualizarMediaPaso(index: Int, uri: String?, tipo: TipoMedia?) {
        _pasosPreparacion[index] = _pasosPreparacion[index].copy(mediaUri = uri, tipoMedia = tipo)
    }

    suspend fun enviarEdicion(): Boolean {
        val uriPortada = imagenPortadaUri.value ?: return false
        val token = userPreferences.getAccessToken().firstOrNull() ?: return false
        val titulo = tituloReceta.value
        val tiempoTotal = (_tiempoHoras.value * 60) + _tiempoMinutos.value

        if (titulo.isBlank() || tiempoTotal <= 0) return false

        return try {
            val urlPortada = if (uriPortada.startsWith("content://")) {
                recetaRepository.subirImagen(appContext, Uri.parse(uriPortada), token)
            } else uriPortada

            val pasosSubidos = pasosPreparacion.map { paso ->
                val uriMedia = paso.mediaUri
                val url = if (!uriMedia.isNullOrBlank() && uriMedia.startsWith("content://")) {
                    recetaRepository.subirImagen(appContext, Uri.parse(uriMedia), token)
                } else uriMedia
                paso.copy(mediaUri = url)
            }

            // 🍽️ INGREDIENTES
            val ingredientesList = ingredientes.map {
                IngredienteRequest(
                    nombre = it.nombre,
                    medida = it.cantidad.toIntOrNull() ?: 1
                )
            }

            // 👨‍🍳 PASOS
            val pasosList = pasosSubidos.mapIndexed { index, paso ->
                PasoRequest(
                    idPaso = index + 1,
                    proceso = paso.descripcion,
                    url = paso.mediaUri ?: ""
                )
            }

            val recetaEditada = EditarRecetaRequest(
                title = titulo,
                imagePortada = urlPortada ?: "",
                duracion = tiempoTotal,
                ingredientes = ingredientesList,
                pasos = pasosList
            )

            recetaRepository.editarReceta(recetaId, recetaEditada, token)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

fun String.toUnidadEnum(): UnidadMedida {
    return UnidadMedida.values().find { it.displayName.equals(this, ignoreCase = true) }
        ?: UnidadMedida.UNIDADES
}

fun String.toTipoMedia(): TipoMedia? {
    return when {
        endsWith(".jpg", true) || endsWith(".jpeg", true) || endsWith(".png", true) -> TipoMedia.IMAGEN
        endsWith(".mp4", true) || endsWith(".avi", true) -> TipoMedia.VIDEO
        else -> null
    }
}
