package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tpo_desa_1.R
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import com.example.tpo_desa_1.viewmodel.ProfileViewModel
import com.example.tpo_desa_1.data.model.Usuario
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import com.example.tpo_desa_1.data.model.Receta
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.tpo_desa_1.data.db.AppDatabase
import com.example.tpo_desa_1.navigation.Screen
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.viewmodel.ProfileViewModelFactory

@Composable
fun ProfileScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    // ✅ INYECCIÓN MANUAL DEL VIEWMODEL CON FACTORY
    val context = LocalContext.current
    val recetaDao = AppDatabase.getDatabase(context).recetaDao()
    val recetaRepo = RecetaRepository(recetaDao)
    val factory = ProfileViewModelFactory(recetaRepo)
    val profileViewModel: ProfileViewModel = viewModel(factory = factory)

    // 📌 Obtener el usuario logueado
    val usuario = sessionViewModel.usuarioLogueado.value
    val recetasCreadas by profileViewModel.recetasCreadas.collectAsState()

    // 🔁 Cargar recetas cuando cambia el usuario
    LaunchedEffect(usuario) {
        usuario?.let { profileViewModel.cargarRecetasCreadas(it.alias) }
    }

    // 🖼️ Renderizar pantalla
    ScreenWithBottomBar(navController = navController) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            usuario?.let {
                Column {
                    UserProfileSection(it, recetasCreadas, navController, sessionViewModel)
                }
            } ?: Text("Iniciá sesión para ver el perfil")
        }
    }
}

@Composable
fun UserProfileSection(    usuario: Usuario,
                           recetasCreadas: List<Receta>,
                           navController: NavController,
                           sessionViewModel: SessionViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ProfileHeader(usuario)
        RecipeStats(recetasCreadas)
        AboutSection(usuario)

        Spacer(Modifier.weight(1f)) // empuja al fondo

        EndSessionButton(
            onConfirmLogout = {
                sessionViewModel.logout()
                navController.navigate(Screen.Splash.route) {
                    popUpTo(0) { inclusive = true } // limpia todo el backstack
                    launchSingleTop = true
                }
            }
        )

    }
}

@Composable
fun ProfileHeader(usuario: Usuario) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.burger), // Usá tu imagen real
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.height(8.dp))

        Text(usuario.alias, style = MaterialTheme.typography.titleMedium)
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
fun RecipeStats(recetasCreadas: List<Receta>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${recetasCreadas.size}", style = MaterialTheme.typography.titleLarge)
            Text("Recetas subidas")
        }
        //olumn(horizontalAlignment = Alignment.CenterHorizontally) {
          //  Text("10", style = MaterialTheme.typography.titleLarge)
            //Text("Recetas guardadas")
        //}
    }
}

@Composable
fun AboutSection(usuario: Usuario) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("About", style = MaterialTheme.typography.titleMedium)
        Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit...")

        InfoRow(Icons.Default.Place, "Argentina")
        InfoRow(Icons.Default.Email, usuario.email)
        InfoRow(Icons.Default.Work, "UX Designer at Ratatouille")
        InfoRow(Icons.Default.School, "Studying at UADE")
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


