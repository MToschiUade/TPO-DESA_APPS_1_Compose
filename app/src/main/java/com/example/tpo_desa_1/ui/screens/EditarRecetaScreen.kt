package com.example.tpo_desa_1.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tpo_desa_1.viewmodel.*
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.data.persistence.UserPreferences
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import coil.compose.AsyncImage
import com.example.tpo_desa_1.data.model.PasoPreparacionUI
import com.example.tpo_desa_1.data.model.TipoMedia
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.tpo_desa_1.data.model.IngredienteUI
import com.example.tpo_desa_1.data.model.UnidadMedida
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import androidx.compose.material3.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarRecetaScreen(
    recetaId: Int,
    recetaRepository: RecetaRepository,
    userPreferences: UserPreferences,
    onRecetaEditada: () -> Unit,
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: EditarRecetaViewModel = viewModel(
        factory = EditarRecetaViewModelFactory(
            recetaId = recetaId,
            recetaRepository = recetaRepository,
            userPreferences = userPreferences,
            appContext = context.applicationContext
        )
    )

    val pasoActual by viewModel.pasoActual
    val titulo by viewModel.tituloReceta
    val horas by viewModel.tiempoHoras
    val minutos by viewModel.tiempoMinutos
    val ingredientes = viewModel.ingredientes
    val pasos = viewModel.pasosPreparacion
    val portada by viewModel.imagenPortadaUri

    var mostrarDialogoError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Receta") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (pasoActual) {
                PasoFormularioEdicion.PREPARACION -> PasoEdicionPreparacionEditar(
                    pasos = pasos,
                    onAgregarPaso = viewModel::agregarPaso,
                    onEliminarPaso = viewModel::eliminarPaso,
                    onActualizarDescripcion = viewModel::actualizarDescripcionPaso,
                    onActualizarMedia = viewModel::actualizarMediaPaso
                )

                PasoFormularioEdicion.INGREDIENTES -> PasoEdicionIngredientesEditar(
                    ingredientes = ingredientes,
                    onAgregarIngrediente = viewModel::agregarIngrediente,
                    onEliminarIngrediente = viewModel::eliminarIngrediente,
                    onActualizarIngrediente = viewModel::actualizarIngrediente
                )

                PasoFormularioEdicion.PORTADA -> PasoEdicionPortadaEditar(
                    imagenUri = portada,
                    onImagenSeleccionada = viewModel::actualizarImagenPortada,
                    titulo = titulo,
                    onTituloCambiado = viewModel::actualizarTitulo,
                    horas = horas,
                    minutos = minutos,
                    onHorasCambiadas = viewModel::actualizarHoras,
                    onMinutosCambiados = viewModel::actualizarMinutos,
                    onBuscarImagen = {
                        // TODO: Implementá si vas a abrir el selector de galería acá
                    }
                )

            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (pasoActual != PasoFormularioEdicion.PREPARACION) {
                    OutlinedButton(onClick = viewModel::retrocederPaso) {
                        Text("Anterior")
                    }
                }

                val coroutineScope = rememberCoroutineScope()

                Button(
                    onClick = {
                        if (pasoActual == PasoFormularioEdicion.PORTADA) {
                            // Confirmar edición
                            coroutineScope.launch {
                                val ok = viewModel.enviarEdicion()
                                if (ok) {
                                    Toast
                                        .makeText(context, "Receta editada correctamente", Toast.LENGTH_SHORT)
                                        .show()
                                    onRecetaEditada()
                                } else {
                                    mostrarDialogoError = true
                                }
                            }
                        } else {
                            viewModel.avanzarPaso()
                        }
                    }
                ) {
                    Text(if (pasoActual == PasoFormularioEdicion.PORTADA) "Guardar cambios" else "Siguiente")
                }

            }
        }
    }

    if (mostrarDialogoError) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoError = false },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoError = false }) {
                    Text("OK")
                }
            },
            title = { Text("Error") },
            text = { Text("No se pudo guardar la receta. Verificá que los campos obligatorios estén completos.") }
        )
    }
}

@Composable
fun PasoEdicionPreparacionEditar(
    pasos: List<PasoPreparacionUI>,
    onAgregarPaso: () -> Unit,
    onEliminarPaso: (Int) -> Unit,
    onActualizarDescripcion: (Int, String) -> Unit,
    onActualizarMedia: (Int, String?, TipoMedia?) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Pasos de preparación",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(pasos) { index, paso ->
                PasoItemEditar(
                    paso = paso,
                    index = index,
                    onEliminar = onEliminarPaso,
                    onActualizarDescripcion = onActualizarDescripcion,
                    onActualizarMedia = onActualizarMedia
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAgregarPaso,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Agregar paso")
        }
    }
}

@Composable
fun PasoItemEditar(
    paso: PasoPreparacionUI,
    index: Int,
    onEliminar: (Int) -> Unit,
    onActualizarDescripcion: (Int, String) -> Unit,
    onActualizarMedia: (Int, String?, TipoMedia?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Paso ${index + 1}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onEliminar(index) }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                    contentDescription = "Eliminar paso"
                )
            }
        }

        TextField(
            value = paso.descripcion,
            onValueChange = { onActualizarDescripcion(index, it) },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        if (!paso.mediaUri.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = paso.mediaUri,
                contentDescription = "Imagen del paso",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }

        // ⚠️ Si querés agregar lógica para reemplazar imagen o video, avisame y lo integramos acá también.
    }
}

@Composable
fun PasoEdicionIngredientesEditar(
    ingredientes: List<IngredienteUI>,
    onAgregarIngrediente: () -> Unit,
    onEliminarIngrediente: (Int) -> Unit,
    onActualizarIngrediente: (Int, IngredienteUI) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("Ingredientes", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        ingredientes.forEachIndexed { index, ingrediente ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Nombre
                    OutlinedTextField(
                        value = ingrediente.nombre,
                        onValueChange = {
                            onActualizarIngrediente(index, ingrediente.copy(nombre = it))
                        },
                        label = { Text("Ingrediente") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Cantidad y unidad
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Cantidad
                        OutlinedTextField(
                            value = ingrediente.cantidad,
                            onValueChange = {
                                if (it.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                    onActualizarIngrediente(index, ingrediente.copy(cantidad = it))
                                }
                            },
                            label = { Text("Cantidad") },
                            modifier = Modifier.width(100.dp),
                            singleLine = true
                        )

                        // Unidad
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            Button(onClick = { expanded = true }) {
                                Text(ingrediente.unidad.displayName)
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                UnidadMedida.values().forEach { unidad ->
                                    DropdownMenuItem(
                                        text = { Text(unidad.displayName) },
                                        onClick = {
                                            onActualizarIngrediente(index, ingrediente.copy(unidad = unidad))
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Eliminar
                        if (ingredientes.size > 1) {
                            IconButton(onClick = { onEliminarIngrediente(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar ingrediente")
                            }
                        } else {
                            Spacer(modifier = Modifier.size(48.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onAgregarIngrediente) {
            Text("+ Agregar otro ingrediente")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección visual de porciones (sin lógica)
        Text("¿Cuántas porciones rinde tu receta?", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { }) {
                Text("-")
            }
            Text("1", modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { }) {
                Text("+")
            }
        }
    }
}

@Composable
fun PasoEdicionPortadaEditar(
    imagenUri: String?,
    onImagenSeleccionada: (String?) -> Unit,
    titulo: String,
    onTituloCambiado: (String) -> Unit,
    horas: Int,
    minutos: Int,
    onHorasCambiadas: (Int) -> Unit,
    onMinutosCambiados: (Int) -> Unit,
    onBuscarImagen: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título
        OutlinedTextField(
            value = titulo,
            onValueChange = onTituloCambiado,
            label = { Text("Título de la receta") },
            modifier = Modifier.fillMaxWidth()
        )

        // Duración
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = horas.toString(),
                onValueChange = { onHorasCambiadas(it.toIntOrNull() ?: 0) },
                label = { Text("Horas") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = minutos.toString(),
                onValueChange = { onMinutosCambiados(it.toIntOrNull() ?: 0) },
                label = { Text("Minutos") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        // Imagen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .clickable { onBuscarImagen() },
            contentAlignment = Alignment.Center
        ) {
            if (imagenUri.isNullOrBlank()) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Seleccionar imagen",
                    modifier = Modifier.size(64.dp),
                    tint = Color.DarkGray
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(Uri.parse(imagenUri))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen de portada",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Text(
            text = "Tocá la imagen para cambiarla",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}