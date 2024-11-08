package com.proyecto.proyectointegradomra.ui.createAd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.common.Contador
import com.proyecto.proyectointegradomra.ui.common.CrearPublicacionIMG
import com.proyecto.proyectointegradomra.ui.common.DatePickerField
import com.proyecto.proyectointegradomra.ui.common.StandardButton
import com.proyecto.proyectointegradomra.ui.common.StandardField
import com.proyecto.proyectointegradomra.ui.common.TextArea
import com.proyecto.proyectointegradomra.ui.common.TimePickerField
import com.proyecto.proyectointegradomra.ui.theme.*

@Composable
fun CreateAdView(navTo: NavHostController, dataRepository: DataRepository) {
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
                value = "Titulo de la publicacion",
                icon = Icons.Filled.AccountBalance,
                onValueChange = { })
        }

        Spacer(modifier = Modifier.weight(0.25f))
        // Texto
        TextArea(label = "Titulo", value = "Titulo de la publicacion", onValueChange = { })

        Spacer(modifier = Modifier.weight(0.25f))
        // Fecha y hora
        Row(modifier = Modifier.fillMaxWidth()) {
            DatePickerField(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            TimePickerField(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(0.25f))
        // Numero de personas
        Contador()

        Spacer(modifier = Modifier.weight(1f))
        // Botones
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f)) {
                StandardButton(
                    text = "Crear",
                    icon = Icons.Filled.CheckCircle,
                    onClick = {
                        //dataRepository.crearPublicacion()
                    },
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                StandardButton(text = "Cancelar", icon = Icons.Filled.Cancel, onClick = { })
            }
        }
    }
}