package com.proyecto.proyectointegradomra.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.authentication.AuthController
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.view.BottomNavigationBar
import com.proyecto.proyectointegradomra.view.FotoPerfil
import com.proyecto.proyectointegradomra.view.StandardButton

@Composable
fun ProfileView(
    authController: AuthController = viewModel(),
    navTo: NavHostController,
) {
    val nombreUsuario by authController.nombreUsuario.observeAsState()
    LaunchedEffect(Unit) {
        authController.obtenerNombreUsuario()
    }
    Scaffold(bottomBar = { BottomNavigationBar(navController = navTo) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(ColorDeFondo)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = nombreUsuario ?: "Nombre de usuario no encontrado")
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { /* Abrir diálogo o pantalla para editar el nombre */ }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Editar nombre")
                }
                FotoPerfil()
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.padding(90.dp, 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StandardButton(
                    text = "Cerrar Sesión",
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    onClick = {
                        authController.cerrarSesion()
                        navTo.navigate("StartScreen")
                    })
                StandardButton(
                    text = "Borrar Cuenta",
                    icon = Icons.Filled.DeleteForever,
                    onClick = {
                        authController.eliminarCuenta()
                        navTo.navigate("StartScreen")
                    })
            }
        }
    }
}
