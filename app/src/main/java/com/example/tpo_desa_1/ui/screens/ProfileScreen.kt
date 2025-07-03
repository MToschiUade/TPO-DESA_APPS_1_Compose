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
    val emailState = sessionViewModel.email.collectAsState(initial = null)
    val email = emailState.value

    val recetasCreadas by profileViewModel.recetasCreadas.collectAsState()

    // Cargar recetas del usuario si hay alias
    LaunchedEffect(alias) {
        alias?.let { profileViewModel.cargarRecetasCreadas(it) }
    }

    ScreenWithBottomBar(navController = navController, sessionViewModel = sessionViewModel) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            if (isLoggedIn && alias != null && email != null) {
                UserProfileSection(
                    alias = alias,
                    email = email,
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
    alias: String,
    email: String,
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
        ProfileHeader(alias)
        RecipeStats(recetasCreadas)
        AboutSection(email)

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

        //Text(usuario.alias, style = MaterialTheme.typography.titleMedium)
        // Todo UPDATE USER PREFERENCE


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
    }

}

@Composable

/*fun AboutSection(usuario: Usuario) {*/
/*TODO Revisar cambiar la firma para en lugar de usar el dataclass usuario por "user preference" que es la clase que ahora persiste la data del user*/
    
fun AboutSection(email: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Acerca de: ", style = MaterialTheme.typography.titleMedium)

/*        Spacer(Modifier.height(10.dp))
        InfoRow(Icons.Default.Place, usuario.pais)
        InfoRow(Icons.Default.Email, usuario.email)
        InfoRow(Icons.Default.Person, "${usuario.nombre} ${usuario.apellido}")
        InfoRow(Icons.Default.Verified, "Cuenta ${usuario.status}")*/
        InfoRow(Icons.Default.Place, "Argentina")
        InfoRow(Icons.Default.Email, email)
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
