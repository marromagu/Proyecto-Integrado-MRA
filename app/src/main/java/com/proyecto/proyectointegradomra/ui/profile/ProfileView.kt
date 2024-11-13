package com.proyecto.proyectointegradomra.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.common.BottomNavigationBar
import com.proyecto.proyectointegradomra.ui.common.DialogoAlerta
import com.proyecto.proyectointegradomra.ui.common.DialogoEditarNombre
import com.proyecto.proyectointegradomra.ui.common.FotoPerfil
import com.proyecto.proyectointegradomra.ui.common.StandardButton
import com.proyecto.proyectointegradomra.ui.common.StandardFieldText

@Composable
fun ProfileView(
    dataRepository: DataRepository,
    navTo: NavHostController,
) {
    val miUsuario by dataRepository.usuario.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var actionConfirmed by remember { mutableStateOf({}) }
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
            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                FotoPerfil()
                StandardFieldText(
                    value = miUsuario?.name ?: "Email no disponible",
                    label = "Nombre",
                    onValueChange = {},
                    modifier = Modifier.weight(1f) // Restringe el ancho
                )
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar nombre",
                        tint = Color.DarkGray
                    )
                }
            }

            // Diálogo para editar el nombre
            DialogoEditarNombre(
                showDialog = showDialog,
                showDialogChanger = { showDialog = it },
                newName = newName,
                newNameChanger = { newName = it },
                dataRepository = dataRepository
            )

            Spacer(modifier = Modifier.weight(1f))

            // Column para los datos del usuario
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StandardFieldText(
                    label = "UID",
                    value = miUsuario?.uid ?: "Error",
                    onValueChange = {},
                    modifier = Modifier
                )
                StandardFieldText(
                    label = "Email",
                    value = miUsuario?.email ?: "Email no disponible",
                    onValueChange = {},
                    modifier = Modifier
                )
                StandardFieldText(
                    label = "Tipo",
                    value = miUsuario?.type.toString(),
                    onValueChange = {},
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.weight(1f))

                // Botón de Cerrar Sesión con alerta
                StandardButton(
                    text = "Cerrar Sesión",
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    onClick = {
                        alertMessage = "¿Estás seguro de que deseas cerrar sesión?"
                        actionConfirmed = {
                            dataRepository.cerrarSesion()
                            navTo.navigate("StartView")
                        }
                        showAlert = true
                    }
                )

                // Botón de Borrar Cuenta con alerta
                StandardButton(
                    text = "Borrar Cuenta",
                    icon = Icons.Filled.DeleteForever,
                    onClick = {
                        alertMessage = "¿Estás seguro de que deseas borrar tu cuenta?"
                        actionConfirmed = {
                            dataRepository.eliminarCuenta()
                            navTo.navigate("StartView")
                        }
                        showAlert = true
                    }
                )
            }

            // Diálogo de alerta para confirmar acciones
            DialogoAlerta(
                showAlert,
                alertMessage,
                actionConfirmed,
                onDismiss = { showAlert = false })
        }
    }
}