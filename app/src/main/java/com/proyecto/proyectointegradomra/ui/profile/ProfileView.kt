package com.proyecto.proyectointegradomra.ui.profile

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.common.BottomNavigationBar
import com.proyecto.proyectointegradomra.ui.common.FotoPerfil
import com.proyecto.proyectointegradomra.ui.common.StandardButton

@Composable
fun ProfileView(
    dataRepository: DataRepository,
    navTo: NavHostController,
) {
    val miUsuario by dataRepository.usuario.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        dataRepository.cargarUsuario()
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
                Text(text = miUsuario?.name ?: "Nombre no disponible")
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { showDialog = true }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Editar nombre")
                }
                FotoPerfil()
            }

            // Dialogo para editar el nombre
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Editar nombre") },
                    text = {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Nuevo nombre") }
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (newName.isNotBlank()) {
                                    dataRepository.actualizarNombreUsuario(newName)
                                    newName = ""
                                    showDialog = false
                                }
                            }
                        ) {
                            Text("Guardar")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.padding(90.dp, 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Nombre: ${miUsuario?.name ?: "Nombre no disponible"}") // Manejo de caso nulo
                Text(text = "UID: ${miUsuario?.uid ?: "UID no disponible"}") // Manejo de caso nulo
                Text(text = "Correo: ${miUsuario?.email ?: "Correo no disponible"}") // Manejo de caso nulo
                Text(text = "Tipo: ${miUsuario?.type?.name ?: "Tipo no disponible"}") // Manejo de caso nulo

                Spacer(modifier = Modifier.weight(1f))
                StandardButton(
                    text = "Cerrar Sesión",
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    onClick = {
                        dataRepository.cerrarSesion()
                        navTo.navigate("StartView")
                    })
                StandardButton(
                    text = "Borrar Cuenta",
                    icon = Icons.Filled.DeleteForever,
                    onClick = {
                        dataRepository.eliminarCuenta()
                        navTo.navigate("StartView")
                    })
            }
        }
    }
}
