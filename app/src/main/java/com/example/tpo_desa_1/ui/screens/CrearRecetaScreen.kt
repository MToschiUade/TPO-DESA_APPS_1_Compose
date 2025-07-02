package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tpo_desa_1.viewmodel.CrearRecetaViewModel
import com.example.tpo_desa_1.viewmodel.PasoFormularioReceta
import androidx.compose.material.icons.filled.Delete
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tpo_desa_1.data.model.TipoMedia
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.example.tpo_desa_1.data.model.UnidadMedida
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import com.example.tpo_desa_1.data.source.remote.ApiService
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRecetaScreen(
    navController: NavController,
    viewModel: CrearRecetaViewModel,
    apiService: ApiService
) {
    val paso = viewModel.pasoActual.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Receta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 💡 Contenido principal del paso actual
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                when (paso) {
                    PasoFormularioReceta.PREPARACION -> PasoPreparacion(viewModel = viewModel)
                    PasoFormularioReceta.INGREDIENTES -> PasoIngredientes(viewModel = viewModel)
                    PasoFormularioReceta.PORTADA -> PasoPortada(
                        viewModel = viewModel,
                        apiService = apiService
                    )
                }
            }

            // 🔘 Botones de navegación
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (paso != PasoFormularioReceta.PREPARACION) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { viewModel.retrocederPaso() }) {
                            Text("Volver")
                        }
                        Button(onClick = { viewModel.avanzarPaso() }) {
                            Text("Continuar")
                        }
                    }
                } else {
                    Button(onClick = { viewModel.avanzarPaso() }) {
                        Text("Continuar")
                    }
                }
            }
        }
    }
}



@Composable
fun PasoPreparacion(viewModel: CrearRecetaViewModel = viewModel()) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text("Paso 1: Preparación", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        viewModel.pasosPreparacion.forEachIndexed { index, paso ->

            val launcherImagen = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->
                uri?.let {
                    viewModel.actualizarMediaPaso(index, it.toString(), TipoMedia.IMAGEN)
                }
            }

            val launcherVideo = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->
                uri?.let {
                    viewModel.actualizarMediaPaso(index, it.toString(), TipoMedia.VIDEO)
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Paso ${index + 1}", style = MaterialTheme.typography.titleMedium)

                        Box {
                            if (viewModel.pasosPreparacion.size > 1) {
                                IconButton(onClick = { viewModel.eliminarPaso(index) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar paso"
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(48.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = paso.descripcion,
                        onValueChange = { viewModel.actualizarDescripcionPaso(index, it) },
                        label = { Text("Descripción del paso") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(onClick = { launcherImagen.launch("image/*") }) {
                            Text("Agregar imagen")
                        }
                        Button(onClick = { launcherVideo.launch("video/*") }) {
                            Text("Agregar video")
                        }
                    }

                    paso.mediaUri?.let { uri ->
                        Spacer(modifier = Modifier.height(8.dp))
                        when (paso.tipoMedia) {
                            TipoMedia.IMAGEN -> {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Imagen del paso",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                )
                            }
                            TipoMedia.VIDEO -> {
                                val context = LocalContext.current

                                AndroidView(
                                    factory = {
                                        VideoView(context).apply {
                                            setVideoURI(Uri.parse(uri))
                                            setMediaController(MediaController(context).apply {
                                                setAnchorView(this@apply)
                                            })
                                            requestFocus()
                                            start()
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )
                            }

                            else -> {}
                        }
                    }
                }
            }
        }

        Button(
            onClick = { viewModel.agregarPaso() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("+ Agregar otro paso")
        }
    }
}

@Composable
fun PasoIngredientes(viewModel: CrearRecetaViewModel = viewModel()) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text("Paso 2: Ingredientes", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        viewModel.ingredientes.forEachIndexed { index, ingrediente ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

                    // Nombre del ingrediente
                    OutlinedTextField(
                        value = ingrediente.nombre,
                        onValueChange = {
                            viewModel.actualizarIngrediente(
                                index,
                                ingrediente.copy(nombre = it)
                            )
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
                        // Cantidad como texto editable
                        OutlinedTextField(
                            value = ingrediente.cantidad,
                            onValueChange = {
                                if (it.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                    viewModel.actualizarIngrediente(
                                        index,
                                        ingrediente.copy(cantidad = it)
                                    )
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
                                            viewModel.actualizarIngrediente(
                                                index,
                                                ingrediente.copy(unidad = unidad)
                                            )
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Eliminar ingrediente
                        if (viewModel.ingredientes.size > 1) {
                            IconButton(onClick = { viewModel.eliminarIngrediente(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar ingrediente")
                            }
                        } else {
                            Spacer(modifier = Modifier.size(48.dp))
                        }
                    }
                }
            }
        }

        // Agregar nuevo ingrediente
        TextButton(onClick = { viewModel.agregarIngrediente() }) {
            Text("+ Agregar otro ingrediente")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Porciones
        Text("¿Cuántas porciones rinde tu receta?", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.decrementarPorciones() }) {
                Text("-")
            }

            Text("${viewModel.porciones.value}", modifier = Modifier.padding(horizontal = 8.dp))

            IconButton(onClick = { viewModel.incrementarPorciones() }) {
                Text("+")
            }
        }
    }
}


@Composable
fun PasoPortada(
    viewModel: CrearRecetaViewModel,
    apiService: ApiService
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val imagenUri = viewModel.imagenPortadaUri.value
    val titulo = viewModel.tituloReceta.value
    val horas = viewModel.tiempoHoras.value
    val minutos = viewModel.tiempoMinutos.value

    val launcherImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.actualizarImagenPortada(it.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("Paso 3: Portada", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))

        // Imagen principal
        Text("Imagen principal *", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { launcherImagen.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (imagenUri != null) "Cambiar imagen" else "Seleccionar imagen")
        }

        imagenUri?.let {
            Spacer(modifier = Modifier.height(12.dp))
            AsyncImage(
                model = it,
                contentDescription = "Imagen de portada",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Título
        Text("Título de la receta *", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { viewModel.actualizarTitulo(it) },
            placeholder = { Text("Ej. Pastel de papa") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tiempo estimado
        Text("Tiempo estimado *", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // HORAS
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Horas", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (horas > 0) viewModel.actualizarHoras(horas - 1)
                        }
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Restar hora")
                    }

                    Box(
                        modifier = Modifier.width(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$horas", style = MaterialTheme.typography.titleLarge)
                    }

                    IconButton(
                        onClick = {
                            if (horas < 12) viewModel.actualizarHoras(horas + 1)
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Sumar hora")
                    }
                }
            }

            // MINUTOS
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Minutos", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            val nuevosMinutos = when {
                                minutos <= 0 -> 0
                                minutos <= 5 -> 0
                                else -> minutos - 5
                            }
                            viewModel.actualizarMinutos(nuevosMinutos)
                        }
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Restar minutos")
                    }

                    Box(
                        modifier = Modifier.width(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$minutos", style = MaterialTheme.typography.titleLarge)
                    }

                    IconButton(
                        onClick = {
                            val nuevosMinutos = when {
                                minutos >= 55 -> 55
                                else -> minutos + 5
                            }
                            viewModel.actualizarMinutos(nuevosMinutos)
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Sumar minutos")
                    }
                }
            }
        }
    }

    Button(
        onClick = {
            coroutineScope.launch {
                val resultado = viewModel.enviarReceta(apiService)
                if (resultado) {
                    println("🎉 Receta enviada exitosamente")
                    // Podés redirigir a otra pantalla si querés acá
                } else {
                    println("❌ Error al enviar la receta")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text("✅ Enviar receta")
    }
}


