package com.proyecto.proyectointegradomra.ui.createAd

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
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.*
import com.proyecto.proyectointegradomra.ui.common.*

@Composable
fun CreateAdView(
    createAdController: CreateAdViewModel = viewModel(),
    navTo: NavHostController,
    dataRepository: DataRepository
) {
    // Observa los estados de los campos del formulario
    val title by createAdController.title.observeAsState("")
    val description by createAdController.description.observeAsState("")
    val fecha by createAdController.fecha.observeAsState("")
    val hora by createAdController.hora.observeAsState("")
    val plazas by createAdController.plazas.observeAsState(0)

    // Lista de mensajes de error
    var errorMessages by remember { mutableStateOf<List<String>>(emptyList()) }

    // Usuario actual y objeto para la nueva publicación
    val miUsuario = dataRepository.obtenerUsuarioActualAuth().value
    val miAd = Publicacion()

    // Layout principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorDeFondo)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PublicacionIMG()

        Spacer(modifier = Modifier.weight(0.12f))

        // Campo de texto para el título
        Row(verticalAlignment = Alignment.CenterVertically) {
            CampoDeTextoPorDefectoEditable(
                label = "Título",
                value = title,
                icon = Icons.Filled.AccountBalance,
                onValueChange = createAdController::updateTitle
            )
        }

        Spacer(modifier = Modifier.weight(0.25f))

        // Campo de texto para la descripción
        CampoDeTextoEnArea(
            label = "Descripción",
            value = description,
            onValueChange = createAdController::updateDescription
        )

        Spacer(modifier = Modifier.weight(0.25f))

        // Campos para seleccionar fecha y hora
        Row(modifier = Modifier.fillMaxWidth()) {
            VentanaFecha(
                modifier = Modifier.weight(1f), onDateSelected = createAdController::updateFecha
            )
            Spacer(modifier = Modifier.width(16.dp))
            VentanaHora(
                modifier = Modifier.weight(1f), onDateSelected = createAdController::updateHora
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Campo para ingresar el número de plazas
        CampoNumeroDePlazas(
            value = plazas, onValueChange = createAdController::updatePlazas
        )

        Spacer(modifier = Modifier.weight(1f))

        // Mostrar mensajes de error si existen
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

        // Botones de acción (Cancelar y Crear)
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            // Botón de cancelar
            Box(modifier = Modifier.weight(1f)) {
                BotonPorDefecto(text = "Cancelar",
                    icon = Icons.Filled.Cancel,
                    onClick = { navTo.navigate("CreateView") })
            }

            // Botón de crear
            Box(modifier = Modifier.weight(1f)) {
                BotonPorDefecto(text = "Crear", icon = Icons.Filled.CheckCircle, onClick = {
                    val errores = validarCampos(title, description, fecha, hora, plazas, 0)

                    // Mostrar errores si existen
                    if (errores.isNotEmpty()) {
                        errorMessages = errores
                    } else {
                        // Crear la publicación y subirla a Firestore
                        miAd.apply {
                            ownerId = miUsuario?.uid ?: ""
                            this.title = title
                            this.description = description
                            this.size = plazas
                            this.date = combinarFechaYHora(fecha, hora) ?: 0L
                            this.type = if (miUsuario?.type == TipoUsuarios.CONSUMIDOR) {
                                TipoPublicaciones.BUSQUEDA
                            } else {
                                TipoPublicaciones.ACTIVIDAD
                            }
                        }

                        dataRepository.agregarDocumentoPublicacionesFirestore(miAd)
                        navTo.navigate("CreateView")
                    }
                })
            }
        }
    }
}