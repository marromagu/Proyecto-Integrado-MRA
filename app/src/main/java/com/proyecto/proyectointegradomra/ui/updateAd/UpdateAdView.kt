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
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.common.CampoNumeroDePlazas
import com.proyecto.proyectointegradomra.ui.common.PublicacionIMG
import com.proyecto.proyectointegradomra.ui.common.VentanaFecha
import com.proyecto.proyectointegradomra.ui.common.BotonPorDefecto
import com.proyecto.proyectointegradomra.ui.common.CampoDeTextoPorDefectoEditable
import com.proyecto.proyectointegradomra.ui.common.CampoDeTextoEnArea
import com.proyecto.proyectointegradomra.ui.common.VentanaHora
import com.proyecto.proyectointegradomra.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun UpdateAdView(
    updateAdController: UpdateAdViewModel = viewModel(),
    navTo: NavHostController,
    dataRepository: DataRepository
) {

    val title by updateAdController.title.observeAsState("")
    val description by updateAdController.description.observeAsState("")
    val fecha by updateAdController.fecha.observeAsState("")
    val hora by updateAdController.hora.observeAsState("")
    val plazas by updateAdController.plazas.observeAsState(0)
    val miPublicacion by updateAdController.publicacion.observeAsState(Publicacion())

    var errorMessages by remember { mutableStateOf<List<String>>(emptyList()) }


    val miUsuario = dataRepository.obtenerUsuarioActual().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorDeFondo)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        PublicacionIMG()

        Spacer(modifier = Modifier.weight(0.12f))
        // Titulo
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            CampoDeTextoPorDefectoEditable(label = "Titulo",
                value = title,
                icon = Icons.Filled.AccountBalance,
                onValueChange = {
                    updateAdController.updateTitle(it)
                })
        }

        Spacer(modifier = Modifier.weight(0.25f))
        // Texto
        CampoDeTextoEnArea(label = "Descripción", value = description, onValueChange = {
            updateAdController.updateDescription(it)
        })

        Spacer(modifier = Modifier.weight(0.25f))
        // Fecha y hora
        Row(modifier = Modifier.fillMaxWidth()) {
            VentanaFecha(
                modifier = Modifier
                    .weight(1f),
                onDateSelected = { f -> updateAdController.updateFecha(f) },
                defaultDate = fecha
            )
            Spacer(modifier = Modifier.width(16.dp))
            VentanaHora(
                modifier = Modifier
                    .weight(1f),
                onDateSelected = { h -> updateAdController.updateHora(h) },
                defaultTime = hora
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))
        // Numero de personas
        CampoNumeroDePlazas(plazas, onValueChange = {
            updateAdController.updatePlazas(it)
        })

        Spacer(modifier = Modifier.weight(1f))
        // Mensaje de error
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


        // Botones
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f)) {
                BotonPorDefecto(text = "Cancelar", icon = Icons.Filled.Cancel, onClick = {
                    navTo.navigate("CreateView")
                })
            }
            Box(modifier = Modifier.weight(1f)) {
                BotonPorDefecto(
                    text = "Actualizar",
                    icon = Icons.Filled.CheckCircle,
                    onClick = {
                        // Lista para recopilar errores
                        val errores = mutableListOf<String>()

                        // Validar cada campo y agregar mensaje si es necesario
                        if (title.isBlank()) errores.add("El título no puede estar vacío.")
                        if (description.isBlank()) errores.add("La descripción no puede estar vacía.")
                        if (fecha.isBlank()) errores.add("Debes seleccionar una fecha.")
                        if (hora.isBlank()) errores.add("Debes seleccionar una hora.")
                        if (plazas <= 0) errores.add("Debes ingresar un número válido de plazas.")
                        if (miPublicacion.participantes.size > plazas) errores.add("No puedes tener menos numero de plazas que de participantes.")

                        val fechaCombinada = combinarFechaYHora(fecha, hora)
                        // Validar la fecha completa
                        if (fecha.isBlank() || hora.isBlank()) {
                            errores.add("Debes seleccionar una fecha y una hora.")

                        } else {
                            if (fechaCombinada == null) {
                                errores.add("La fecha y hora no son válidas.")
                            } else {
                                if (fechaCombinada < System.currentTimeMillis()) {
                                    errores.add("La fecha y hora no pueden ser anteriores a la actual.")
                                }
                            }
                        }

                        // Si hay errores, mostrar la lista de errores
                        if (errores.isNotEmpty()) {
                            errorMessages = errores // Actualizar estado
                        } else {
                            // Crear el objeto Publicaciones y subirlo a Firestore
                            miPublicacion.ownerId = miUsuario?.uid ?: ""
                            miPublicacion.title = title
                            miPublicacion.description = description
                            miPublicacion.size = plazas
                            if (fechaCombinada != null) {
                                miPublicacion.date = fechaCombinada
                            }
                            miPublicacion.type = if (miUsuario?.type == TipoUsuarios.CONSUMIDOR) {
                                TipoPublicaciones.BUSQUEDA
                            } else {
                                TipoPublicaciones.ACTIVIDAD
                            }

                            dataRepository.actualizarPublicacion(miPublicacion)
                            navTo.navigate("CreateView")
                        }
                    }
                )
            }
        }
    }
}

fun combinarFechaYHora(fecha: String, hora: String): Long? {
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
    val fechaCompleta = "$fecha $hora"
    return try {
        val fechaSeleccionada = formatoFecha.parse(fechaCompleta)?.time
        val fechaActual = System.currentTimeMillis()

        if (fechaSeleccionada != null && fechaSeleccionada >= fechaActual) {
            fechaSeleccionada
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}