package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpo_desa_1.R
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.model.UsuarioDetalle
import com.example.tpo_desa_1.navigation.Screen
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar
import com.example.tpo_desa_1.viewmodel.ProfileViewModel
import com.example.tpo_desa_1.viewmodel.ProfileViewModelFactory
import com.example.tpo_desa_1.viewmodel.RecetaViewModel
import com.example.tpo_desa_1.viewmodel.RecetaViewModelFactory
import com.example.tpo_desa_1.viewmodel.SessionViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // ViewModels
    val factory = ProfileViewModelFactory(context)
    val profileViewModel: ProfileViewModel = viewModel(factory = factory)

    val recetaViewModel: RecetaViewModel = viewModel(
        factory = RecetaViewModelFactory(context)
    )

    // Observables
    val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState(initial = false)
    val aliasState = sessionViewModel.alias.collectAsState(initial = null)
    val alias = aliasState.value
    val usuarioDetalle by sessionViewModel.usuarioDetalle.collectAsState()
    val recetasCreadas by profileViewModel.recetasCreadas.collectAsState()
    val token by sessionViewModel.accessToken.collectAsState(initial = null)
    val cantidadRecetas by recetaViewModel.cantidadRecetas

    // Cargar datos al iniciar
    LaunchedEffect(alias, token) {
        println("🧪 alias: $alias")
        println("🧪 token: $token")

        alias?.let {
            profileViewModel.cargarRecetasCreadas(it)
            sessionViewModel.loadUsuarioDetalle()

            token?.let {
                println("✅ Ejecutando cantidadRecetas con token: $it")
                recetaViewModel.cantidadRecetas(it)
            }
        }
    }

    ScreenWithBottomBar(navController = navController, sessionViewModel = sessionViewModel) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            if (isLoggedIn && alias != null) {
                UserProfileSection(
                    alias = alias,
                    recetasCreadas = recetasCreadas,
                    usuarioDetalle = usuarioDetalle,
                    navController = navController,
                    sessionViewModel = sessionViewModel,
                    cantidadRecetas = cantidadRecetas
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Iniciá sesión para ver el perfil")
                }
            }
        }
    }
}

@Composable
fun UserProfileSection(
    alias: String,
    recetasCreadas: List<Receta>,
    usuarioDetalle: UsuarioDetalle?,
    navController: NavController,
    sessionViewModel: SessionViewModel,
    cantidadRecetas: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ProfileHeader(alias = alias, descripcion = usuarioDetalle?.descripcion)
        RecipeStats(cantidadRecetas)
        AboutSection(usuarioDetalle = usuarioDetalle)

        Spacer(Modifier.weight(1f))

        EndSessionButton(
            onConfirmLogout = {
                sessionViewModel.logout()
                navController.navigate(Screen.Splash.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}

@Composable
fun ProfileHeader(alias: String, descripcion: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.burger),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.height(8.dp))

        Text(alias, style = MaterialTheme.typography.titleMedium)

        descripcion?.let {
            Spacer(Modifier.height(4.dp))
            Text(it, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(Icons.Default.Share, contentDescription = "Instagram")
            Icon(Icons.Default.Person, contentDescription = "LinkedIn")
            Icon(Icons.Default.Email, contentDescription = "Email")
        }
    }
    Spacer(Modifier.height(15.dp))
}


@Composable
fun RecipeStats(cantidadRecetas: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$cantidadRecetas", style = MaterialTheme.typography.titleLarge)
            Text("Recetas subidas")
        }
    }
}

@Composable
fun AboutSection(usuarioDetalle: UsuarioDetalle?) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Acerca de:", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        InfoRow(
            icon = Icons.Default.Place,
            text = usuarioDetalle?.ubicacion ?: "Ubicación desconocida"
        )
        InfoRow(
            icon = Icons.Default.Email,
            text = usuarioDetalle?.email ?: "Email no disponible"
        )
        InfoRow(
            icon = Icons.Default.Person,
            text = "${usuarioDetalle?.nombre ?: "Nombre"} ${usuarioDetalle?.apellido ?: "Apellido"}"
        )
        InfoRow(
            icon = Icons.Default.Description,
            text = usuarioDetalle?.descripcion ?: "Sin descripción"
        )
        InfoRow(
            icon = Icons.Default.VerifiedUser,
            text = if (usuarioDetalle?.status == true) "Cuenta activa" else "Cuenta inactiva"
        )
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}



@Composable
fun EndSessionButton(onConfirmLogout: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Divider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.3f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true }
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Cerrar sesión",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cerrar sesión",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("¿Cerrar sesión?") },
            text = { Text("¿Estás seguro que querés cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onConfirmLogout()
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
