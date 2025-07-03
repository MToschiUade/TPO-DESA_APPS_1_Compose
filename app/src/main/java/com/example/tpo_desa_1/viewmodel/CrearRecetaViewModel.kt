package com.example.tpo_desa_1.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import com.example.tpo_desa_1.data.mapper.mapViewModelToDto
import com.example.tpo_desa_1.data.model.PasoPreparacionUI
import com.example.tpo_desa_1.data.model.TipoMedia
import com.example.tpo_desa_1.data.model.IngredienteUI
import com.example.tpo_desa_1.data.model.UnidadMedida
import com.example.tpo_desa_1.data.persistence.UserPreferences
import com.example.tpo_desa_1.data.source.remote.ApiService
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.utils.uriToFile
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import android.net.Uri
import com.google.gson.Gson

// Enum que representa los pasos del formulario
enum class PasoFormularioReceta {
    PREPARACION,
    INGREDIENTES,
    PORTADA
}

class CrearRecetaViewModel(
    private val recetaRepository: RecetaRepository,
    private val userPreferences: UserPreferences,
    private val appContext: Context
) : ViewModel() {

    // Estado actual del paso del formulario
    private val _pasoActual = mutableStateOf(PasoFormularioReceta.PREPARACION)
    val pasoActual: State<PasoFormularioReceta> = _pasoActual

    // Lista dinámica de pasos de preparación
    private val _pasosPreparacion = mutableStateListOf(PasoPreparacionUI())
    val pasosPreparacion: List<PasoPreparacionUI> = _pasosPreparacion

    // Lista dinámica de ingredientes
    private val _ingredientes = mutableStateListOf(IngredienteUI())
    val ingredientes: List<IngredienteUI> = _ingredientes

    private val _porciones = mutableStateOf(1)
    val porciones: State<Int> = _porciones

    private val _imagenPortadaUri = mutableStateOf<String?>(null)
    val imagenPortadaUri: State<String?> = _imagenPortadaUri

    private val _tituloReceta = mutableStateOf("")
    val tituloReceta: State<String> = _tituloReceta

    private val _tiempoHoras = mutableStateOf(0)
    val tiempoHoras: State<Int> = _tiempoHoras

    private val _tiempoMinutos = mutableStateOf(0)
    val tiempoMinutos: State<Int> = _tiempoMinutos

    fun obtenerTiempoTotalEnMinutos(): Int {
        return (_tiempoHoras.value * 60) + _tiempoMinutos.value
    }

    fun actualizarImagenPortada(uri: String?) {
        _imagenPortadaUri.value = uri
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
    fun agregarIngrediente() {
        _ingredientes.add(IngredienteUI())
    }

    fun eliminarIngrediente(index: Int) {
        if (_ingredientes.size > 1) {
            _ingredientes.removeAt(index)
        }
    }

    fun actualizarIngrediente(index: Int, nuevo: IngredienteUI) {
        _ingredientes[index] = nuevo
    }

    fun incrementarPorciones() {
        _porciones.value += 1
    }

    fun decrementarPorciones() {
        if (_porciones.value > 1) _porciones.value -= 1
    }


    // Agregar un nuevo paso vacío
    fun agregarPaso() {
        _pasosPreparacion.add(PasoPreparacionUI())
    }

    // Actualizar solo la descripción de un paso
    fun actualizarDescripcionPaso(index: Int, descripcion: String) {
        _pasosPreparacion[index] = _pasosPreparacion[index].copy(descripcion = descripcion)
    }

    // Adjuntar o quitar media (imagen/video)
    fun actualizarMediaPaso(index: Int, uri: String?, tipo: TipoMedia?) {
        _pasosPreparacion[index] = _pasosPreparacion[index].copy(mediaUri = uri, tipoMedia = tipo)
    }


    // Función para avanzar al siguiente paso
    fun avanzarPaso() {
        _pasoActual.value = when (_pasoActual.value) {
            PasoFormularioReceta.PREPARACION -> PasoFormularioReceta.INGREDIENTES
            PasoFormularioReceta.INGREDIENTES -> PasoFormularioReceta.PORTADA
            PasoFormularioReceta.PORTADA -> PasoFormularioReceta.PORTADA // no avanza más allá
        }
    }

    // Función para retroceder al paso anterior
    fun retrocederPaso() {
        _pasoActual.value = when (_pasoActual.value) {
            PasoFormularioReceta.PORTADA -> PasoFormularioReceta.INGREDIENTES
            PasoFormularioReceta.INGREDIENTES -> PasoFormularioReceta.PREPARACION
            PasoFormularioReceta.PREPARACION -> PasoFormularioReceta.PREPARACION // no retrocede más allá
        }
    }

    // Si necesitás reiniciar el formulario
    fun reiniciarPasos() {
        _pasoActual.value = PasoFormularioReceta.PREPARACION
    }

    fun eliminarPaso(index: Int) {
        if (_pasosPreparacion.size > 1) {
            _pasosPreparacion.removeAt(index)
        }
    }

    suspend fun enviarReceta(): Boolean {
        val portadaUri = imagenPortadaUri.value ?: return false
        val titulo = tituloReceta.value
        val tiempoTotal = obtenerTiempoTotalEnMinutos()

        if (titulo.isBlank() || tiempoTotal <= 0) return false

        return try {
            println("🔐 Token obtenido correctamente")
            val token = userPreferences.getAccessToken().firstOrNull() ?: return false

            println("📁 Intentando subir imagen con URI: $portadaUri")
            val urlPortada = recetaRepository.subirImagen(appContext, Uri.parse(portadaUri), token)
                ?: return false
            println("✅ Imagen portada subida con éxito: $urlPortada")

            // 👉 Subir media de pasos si corresponde
            val pasosSubidos = pasosPreparacion.map { paso ->
                val uriMedia = paso.mediaUri
                val url = if (!uriMedia.isNullOrBlank() && uriMedia.startsWith("content://")) {
                    println("🎞️ Subiendo media del paso: ${paso.descripcion}")
                    recetaRepository.subirImagen(appContext, Uri.parse(uriMedia), token)
                } else {
                    uriMedia // ya es una URL o vacío
                }

                paso.copy(mediaUri = url) // reemplazamos el URI por la URL real
            }

            val recetaDTO = mapViewModelToDto(
                titulo = titulo,
                portadaUrl = urlPortada,
                tiempoEnMinutos = tiempoTotal,
                ingredientes = ingredientes,
                pasos = pasosSubidos
            )

            val gson = Gson()
            val jsonDebug = gson.toJson(recetaDTO)
            println("🧾 Receta a enviar:\n$jsonDebug")

            val resultado = recetaRepository.crearReceta(recetaDTO)
            println("📤 Resultado al enviar receta: $resultado")
            resultado
        } catch (e: Exception) {
            println("❌ Excepción en enviarReceta: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    fun reiniciarFormulario() {
        _pasoActual.value = PasoFormularioReceta.PREPARACION

        _ingredientes.clear()
        _ingredientes.add(IngredienteUI()) // agregamos uno por defecto

        _pasosPreparacion.clear()
        _pasosPreparacion.add(PasoPreparacionUI())

        _imagenPortadaUri.value = null
        _tituloReceta.value = ""
        _tiempoHoras.value = 0
        _tiempoMinutos.value = 0
        _porciones.value = 1
    }
}
