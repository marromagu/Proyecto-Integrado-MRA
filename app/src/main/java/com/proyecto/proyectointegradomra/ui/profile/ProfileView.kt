package com.proyecto.proyectointegradomra.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.proyecto.proyectointegradomra.data.model.Publicaciones
import com.proyecto.proyectointegradomra.data.model.TipoPublicaciones
import com.proyecto.proyectointegradomra.data.model.TipoUsuarios
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.common.BarraDeNavegacion
import com.proyecto.proyectointegradomra.ui.common.CardClickable
import com.proyecto.proyectointegradomra.ui.common.DialogoAlertaBotones
import com.proyecto.proyectointegradomra.ui.common.DialogoEditarNombre
import com.proyecto.proyectointegradomra.ui.common.Foto
import com.proyecto.proyectointegradomra.ui.common.BotonPorDefecto
import com.proyecto.proyectointegradomra.ui.common.CampoDeTextoPorDefectoNoEditable

@Composable
fun ProfileView(
    dataRepository: DataRepository,
    navTo: NavHostController,
) {
    val miUsuario by dataRepository.obtenerUsuarioActual().observeAsState()
    var publicaciones by remember { mutableStateOf<List<Publicaciones>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var actionConfirmed by remember { mutableStateOf({}) }
    var newName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        dataRepository.cargarUsuario()
        publicaciones = if (miUsuario?.type == TipoUsuarios.OFERTANTE) {
            dataRepository.obtenerPublicacionesParticipadas(
                TipoPublicaciones.BUSQUEDA,
                miUsuario?.uid!!
            )
        } else {
            dataRepository.obtenerPublicacionesParticipadas(
                TipoPublicaciones.ACTIVIDAD,
                miUsuario?.uid!!
            )
        }
    }

    Scaffold(bottomBar = { BarraDeNavegacion(navController = navTo) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(ColorDeFondo)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Foto()
                CampoDeTextoPorDefectoNoEditable(
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
                uid = miUsuario?.uid,
                newNameChanger = { newName = it },
                dataRepository = dataRepository,
                onDismiss = { navTo.navigate("ProfileView") }
            )

            Spacer(modifier = Modifier.weight(1f))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                        value = miUsuario?.type.toString(),
                        onValueChange = {},
                        modifier = Modifier
                    )
                }
                item {
                    Spacer(modifier = Modifier.weight(1f)) // Espacio flexible
                }
                item {
                    LazyRow(modifier = Modifier.padding(4.dp)) {
                        items(publicaciones.size) { index ->
                            CardClickable(
                                publicaciones[index], "remove", onItemClick = {
                                    dataRepository.eliminarParticipante(
                                        miUsuario?.uid!!,
                                        publicaciones[index].uid!!
                                    )
                                    navTo.navigate("ProfileView")
                                }
                            ) {
                                navTo.navigate("UpdateAdView")
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.weight(1f)) // Espacio flexible
                }
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

            // Diálogo de alerta para confirmar acciones
            DialogoAlertaBotones(
                showAlert,
                alertMessage,
                actionConfirmed,
                onDismiss = { showAlert = false })
        }
    }
}