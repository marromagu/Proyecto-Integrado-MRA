package com.proyecto.proyectointegradomra.ui.createAd

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.data.model.Publicaciones
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

@Preview
@Composable
fun CreateAdView(
    createAdController: CreateAdViewModel = viewModel(),
//    navTo: NavHostController,
//    dataRepository: DataRepository
) {

    val title by createAdController.title.observeAsState("")
    val description by createAdController.description.observeAsState("")
    val fecha by createAdController.fecha.observeAsState("")
    val hora by createAdController.hora.observeAsState("")
    val plazas by createAdController.plazas.observeAsState(0)

    val miAd = Publicaciones()

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
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            StandardField(label = "Titulo",
                value = title,
                icon = Icons.Filled.AccountBalance,
                onValueChange = {
                    createAdController.updateTitle(it)
                })
        }

        Spacer(modifier = Modifier.weight(0.25f))
        // Texto
        TextArea(label = "Descripción", value = description, onValueChange = {
            createAdController.updateDescription(it)
        })

        Spacer(modifier = Modifier.weight(0.25f))
        // Fecha y hora
        Row(modifier = Modifier.fillMaxWidth()) {
            DatePickerField(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onDateSelected = { f -> createAdController.updateFecha(f) }
            )
            TimePickerField(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onDateSelected = { h -> createAdController.updateHora(h) }
            )
        }

        Spacer(modifier = Modifier.weight(0.25f))
        // Numero de personas
        Contador(plazas, onValueChange = {
            createAdController.updatePlazas(it)
        })

        Spacer(modifier = Modifier.weight(1f))
        // Botones
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f)) {
                StandardButton(text = "Cancelar", icon = Icons.Filled.Cancel, onClick = {
//                    navTo.navigate("HomeView")
                })
            }
            Box(modifier = Modifier.weight(1f)) {
                StandardButton(
                    text = "Crear",
                    icon = Icons.Filled.CheckCircle,
                    onClick = {
                        miAd.title = title
                        miAd.description = description
//                        miAd.userId = dataRepository.usuario.value?.uid ?: ""
                        miAd.plazas = plazas
                        miAd.date = combinarFechaYHora(fecha, hora)
//                        dataRepository.agregarDocumentoPublicacionesFirestore(miAd)
//                        navTo.navigate("HomeView")
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