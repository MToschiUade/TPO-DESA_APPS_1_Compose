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
import com.example.tpo_desa_1.data.model.response.Usuario
import com.example.tpo_desa_1.navigation.Screen
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar
import com.example.tpo_desa_1.viewmodel.ProfileViewModel
import com.example.tpo_desa_1.viewmodel.ProfileViewModelFactory
import com.example.tpo_desa_1.viewmodel.SessionViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val factory = ProfileViewModelFactory(context)
    val profileViewModel: ProfileViewModel = viewModel(factory = factory)

    val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState(initial = false)
    val aliasState = sessionViewModel.alias.collectAsState(initial = null)
    val alias = aliasState.value

    val usuario by sessionViewModel.usuario.collectAsState()
    val recetasCreadas by profileViewModel.recetasCreadas.collectAsState()
    val token = sessionViewModel.getAccessTokenActual()
    val cantidadRecetas by sessionViewModel.cantidadRecetasCreadas.collectAsState()

    // Cargar datos del usuario y sus recetas
    LaunchedEffect(alias) {
        alias?.let {
            sessionViewModel.cargarDatosUsuario()
            // Agregamos esta línea 👇
            sessionViewModel.cargarCantidadRecetasCreadas()
        }
    }

    ScreenWithBottomBar(navController = navController, sessionViewModel = sessionViewModel) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            if (isLoggedIn && alias != null && usuario != null) {
                UserProfileSection(
                    usuario = usuario!!,
                    recetasCreadas = recetasCreadas,
                    navController = navController,
                    sessionViewModel = sessionViewModel
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
    usuario: Usuario,
    recetasCreadas: List<Receta>,
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ProfileHeader(usuario.username)
        RecipeStats(recetasCreadas = recetasCreadas, cantidadRecetas = sessionViewModel.cantidadRecetasCreadas.value ?: 0)
        AboutSection(usuario)

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
fun ProfileHeader(alias: String) {
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
        Text("Chef Amateur", style = MaterialTheme.typography.bodySmall)

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(Icons.Default.Share, contentDescription = "Instagram")
            Icon(Icons.Default.Person, contentDescription = "LinkedIn")
            Icon(Icons.Default.Email, contentDescription = "Email")
        }
    }
}

@Composable
fun RecipeStats(recetasCreadas: List<Receta>, cantidadRecetas: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {



        Column(horizontalAlignment = Alignment.CenterHorizontally) {


            Text("${cantidadRecetas}", style = MaterialTheme.typography.titleLarge)
            Text("Recetas subidas")
        }

    }
}

@Composable
fun AboutSection(usuario: Usuario) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Acerca de", style = MaterialTheme.typography.titleMedium)
        InfoRow(Icons.Default.Place, usuario.ubicacion ?: "Argentina")
        InfoRow(Icons.Default.Email, usuario.email)
        InfoRow(Icons.Default.Person, "${usuario.name} ${usuario.lastName}")
        InfoRow(Icons.Default.Verified, if (usuario.status) "Cuenta activa" else "Cuenta inactiva")
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null)
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