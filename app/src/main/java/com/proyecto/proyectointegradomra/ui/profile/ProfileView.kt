package com.proyecto.proyectointegradomra.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.data.model.Publicacion
import com.proyecto.proyectointegradomra.data.model.TipoPublicaciones
import com.proyecto.proyectointegradomra.data.model.TipoUsuarios
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.common.*
import com.proyecto.proyectointegradomra.ui.theme.ColorDeLetras

@Composable
fun ProfileView(
    dataRepository: DataRepository,
    navTo: NavHostController,
) {
    // Obtener el usuario actual y mantener actualizada su información
    val miUsuario by dataRepository.obtenerUsuarioActual().observeAsState()

    // Variables de estado para publicaciones y diálogos
    var publicaciones by remember { mutableStateOf<List<Publicacion>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var actionConfirmed by remember { mutableStateOf({}) }
    var newName by remember { mutableStateOf("") }

    // Cargar información inicial al componer
    LaunchedEffect(Unit) {
        dataRepository.cargarUsuario()
        publicaciones = if (miUsuario?.type == TipoUsuarios.OFERTANTE) {
            dataRepository.obtenerPublicacionesParticipadas(
                TipoPublicaciones.BUSQUEDA, miUsuario?.uid!!
            )
        } else {
            dataRepository.obtenerPublicacionesParticipadas(
                TipoPublicaciones.ACTIVIDAD, miUsuario?.uid!!
            )
        }
    }

    // Estructura de diseño general
    Scaffold(
        bottomBar = { BarraDeNavegacion(navController = navTo) } // Barra de navegación inferior
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(ColorDeFondo)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado con la foto y el nombre editable
            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Foto()
                CampoDeTextoPorDefectoNoEditable(
                    value = miUsuario?.name ?: "Nombre no disponible",
                    label = "Nombre",
                    onValueChange = {},
                    modifier = Modifier.weight(1f) // Ocupa el espacio restante
                )
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar nombre",
                        tint = ColorDeLetras
                    )
                }
            }

            // Diálogo para editar el nombre
            DialogoEditarNombre(
                showDialog = showDialog,
                showDialogChanger = { showDialog = it },
                newName = newName,
                uid = miUsuario?.uid,
                newNameChanger = { newName = it },
                dataRepository = dataRepository,
                onDismiss = { navTo.navigate("ProfileView") }
            )

            Spacer(modifier = Modifier.weight(1f)) // Espaciador flexible

            // Lista de datos de usuario
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Información estática del usuario
                item {
                    CampoDeTextoPorDefectoNoEditable(
                        label = "UID",
                        value = miUsuario?.uid ?: "Error",
                        onValueChange = {},
                        modifier = Modifier
                    )
                }
                item {
                    CampoDeTextoPorDefectoNoEditable(
                        label = "Email",
                        value = miUsuario?.email ?: "Email no disponible",
                        onValueChange = {},
                        modifier = Modifier
                    )
                }
                item {
                    CampoDeTextoPorDefectoNoEditable(
                        label = "Tipo",
                        value = miUsuario?.type.toString().lowercase(),
                        onValueChange = {},
                        modifier = Modifier
                    )
                }

                // Publicaciones relacionadas con el usuario
                item {
                    LazyRow(modifier = Modifier.padding(4.dp)) {
                        items(publicaciones.size) { index ->
                            CardClickable(publicaciones[index], "remove", onItemClick = {
                                dataRepository.eliminarParticipante(
                                    miUsuario?.uid!!, publicaciones[index].uid
                                )
                                navTo.navigate("ProfileView") // Recargar vista
                            }) {
                                navTo.navigate("UpdateAdView")
                            }
                        }
                    }
                }

                // Botones de cerrar sesión y borrar cuenta
                item {
                    BotonPorDefecto(
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
                }
                item {
                    BotonPorDefecto(
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
            }

            // Diálogo de confirmación para acciones críticas
            DialogoAlertaBotones(
                showDialog = showAlert,
                message = alertMessage,
                onConfirm = actionConfirmed,
                onDismiss = { showAlert = false }
            )
        }
    }
}
