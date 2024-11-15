package com.proyecto.proyectointegradomra.ui.updateAd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.data.model.Publicaciones
import com.proyecto.proyectointegradomra.data.model.TipoPublicaciones
import com.proyecto.proyectointegradomra.data.model.TipoUsuarios
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.common.Contador
import com.proyecto.proyectointegradomra.ui.common.CrearPublicacionIMG
import com.proyecto.proyectointegradomra.ui.common.DatePickerField
import com.proyecto.proyectointegradomra.ui.common.StandardButton
import com.proyecto.proyectointegradomra.ui.common.StandardField
import com.proyecto.proyectointegradomra.ui.common.TextArea
import com.proyecto.proyectointegradomra.ui.common.TimePickerField
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

    val miAd = Publicaciones()
    val miUsuario = dataRepository.obtenerUsuarioActual().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorDeFondo)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        CrearPublicacionIMG()

        Spacer(modifier = Modifier.weight(0.12f))
        // Titulo
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            StandardField(label = "Titulo",
                value = title,
                icon = Icons.Filled.AccountBalance,
                onValueChange = {
                    updateAdController.updateTitle(it)
                })
        }

        Spacer(modifier = Modifier.weight(0.25f))
        // Texto
        TextArea(label = "Descripción", value = description, onValueChange = {
            updateAdController.updateDescription(it)
        })

        Spacer(modifier = Modifier.weight(0.25f))
        // Fecha y hora
        Row(modifier = Modifier.fillMaxWidth()) {
            DatePickerField(
                modifier = Modifier
                    .weight(1f),
                onDateSelected = { f -> updateAdController.updateFecha(f) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            TimePickerField(
                modifier = Modifier
                    .weight(1f),
                onDateSelected = { h -> updateAdController.updateHora(h) }
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))
        // Numero de personas
        Contador(plazas, onValueChange = {
            updateAdController.updatePlazas(it)
        })

        Spacer(modifier = Modifier.weight(1f))
        // Botones
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f)) {
                StandardButton(text = "Cancelar", icon = Icons.Filled.Cancel, onClick = {
                    navTo.navigate("CreateView")
                })
            }
            Box(modifier = Modifier.weight(1f)) {
                StandardButton(
                    text = "Actualizar",
                    icon = Icons.Filled.CheckCircle,
                    onClick = {
                        miAd.userId = miUsuario?.uid ?: ""
                        if (miUsuario?.type == TipoUsuarios.CONSUMIDOR) {
                            miAd.tipo = TipoPublicaciones.BUSQUEDA
                        } else {
                            miAd.tipo = TipoPublicaciones.ACTIVIDAD
                        }
                        miAd.title = title
                        miAd.description = description
                        miAd.plazas = plazas
                        miAd.date = combinarFechaYHora(fecha, hora)
                        //dataRepository.agregarDocumentoPublicacionesFirestore(miAd)
                        navTo.navigate("CreateView")
                    },
                )
            }
        }
    }
}

fun combinarFechaYHora(fecha: String, hora: String): Long {
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
    val fechaCompleta = "$fecha $hora"
    return formatoFecha.parse(fechaCompleta)?.time ?: System.currentTimeMillis()
}