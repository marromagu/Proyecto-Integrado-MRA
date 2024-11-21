package com.proyecto.proyectointegradomra.ui.updateAd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.data.model.Publicacion
import com.proyecto.proyectointegradomra.data.model.TipoPublicaciones
import com.proyecto.proyectointegradomra.data.model.TipoUsuarios
import com.proyecto.proyectointegradomra.data.model.Usuario
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.common.CampoNumeroDePlazas
import com.proyecto.proyectointegradomra.ui.common.PublicacionIMG
import com.proyecto.proyectointegradomra.ui.common.VentanaFecha
import com.proyecto.proyectointegradomra.ui.common.BotonPorDefecto
import com.proyecto.proyectointegradomra.ui.common.CampoDeTextoPorDefectoEditable
import com.proyecto.proyectointegradomra.ui.common.CampoDeTextoEnArea
import com.proyecto.proyectointegradomra.ui.common.VentanaHora
import com.proyecto.proyectointegradomra.ui.common.actualizarPublicacion
import com.proyecto.proyectointegradomra.ui.common.validarCampos
import com.proyecto.proyectointegradomra.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun UpdateAdView(
    updateAdController: UpdateAdViewModel = viewModel(),
    navTo: NavHostController,
    dataRepository: DataRepository
) {
    // Observables que representan el estado de cada campo
    val title by updateAdController.title.observeAsState("")
    val description by updateAdController.description.observeAsState("")
    val fecha by updateAdController.fecha.observeAsState("")
    val hora by updateAdController.hora.observeAsState("")
    val plazas by updateAdController.plazas.observeAsState(0)
    val miPublicacion by updateAdController.publicacion.observeAsState(Publicacion())
    var errorMessages by remember { mutableStateOf<List<String>>(emptyList()) }

    // Usuario actual obtenido del repositorio de datos
    val miUsuario = dataRepository.obtenerUsuarioActual().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorDeFondo)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen destacada de la publicación
        PublicacionIMG()

        Spacer(modifier = Modifier.weight(0.12f))

        // Campo para editar el título
        Row(verticalAlignment = Alignment.CenterVertically) {
            CampoDeTextoPorDefectoEditable(
                label = "Título",
                value = title,
                icon = Icons.Filled.AccountBalance,
                onValueChange = updateAdController::updateTitle
            )
        }

        Spacer(modifier = Modifier.weight(0.25f))

        // Campo para editar la descripción
        CampoDeTextoEnArea(
            label = "Descripción",
            value = description,
            onValueChange = updateAdController::updateDescription
        )

        Spacer(modifier = Modifier.weight(0.25f))

        // Selección de fecha y hora
        Row(modifier = Modifier.fillMaxWidth()) {
            VentanaFecha(
                modifier = Modifier.weight(1f),
                onDateSelected = updateAdController::updateFecha,
                defaultDate = fecha
            )
            Spacer(modifier = Modifier.width(16.dp))
            VentanaHora(
                modifier = Modifier.weight(1f),
                onDateSelected = updateAdController::updateHora,
                defaultTime = hora
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Campo para número de plazas
        CampoNumeroDePlazas(
            value = plazas, onValueChange = updateAdController::updatePlazas
        )

        Spacer(modifier = Modifier.weight(1f))

        // Mensajes de error
        if (errorMessages.isNotEmpty()) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                errorMessages.forEach { mensaje ->
                    Text(
                        text = mensaje,
                        color = ColorEliminar,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }

        // Botones de acción
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            // Botón para cancelar y volver
            Box(modifier = Modifier.weight(1f)) {
                BotonPorDefecto(text = "Cancelar",
                    icon = Icons.Filled.Cancel,
                    onClick = { navTo.navigate("CreateView") })
            }

            // Botón para actualizar la publicación
            Box(modifier = Modifier.weight(1f)) {
                BotonPorDefecto(text = "Actualizar", icon = Icons.Filled.CheckCircle, onClick = {
                    val errores = validarCampos(
                        title = title,
                        description = description,
                        fecha = fecha,
                        hora = hora,
                        plazas = plazas,
                        participantes = miPublicacion.participantes.size
                    )

                    if (errores.isNotEmpty()) {
                        errorMessages = errores
                    } else {
                        actualizarPublicacion(
                            miPublicacion = miPublicacion,
                            miUsuario = miUsuario,
                            title = title,
                            description = description,
                            plazas = plazas,
                            fecha = fecha,
                            hora = hora,
                            dataRepository = dataRepository,
                            navTo = navTo
                        )
                    }
                })
            }
        }
    }
}