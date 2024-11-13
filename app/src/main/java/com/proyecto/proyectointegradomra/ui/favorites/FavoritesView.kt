package com.proyecto.proyectointegradomra.ui.favorites

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
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
import com.proyecto.proyectointegradomra.data.model.Publicaciones
import com.proyecto.proyectointegradomra.data.model.TipoPublicaciones
import com.proyecto.proyectointegradomra.data.model.TipoUsuarios
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.common.BottomNavigationBar
import com.proyecto.proyectointegradomra.ui.common.Logo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FavoritesView(
    dataRepository: DataRepository, navTo: NavHostController
) {
    var publicaciones by remember { mutableStateOf<List<Publicaciones>>(emptyList()) }
    val miUsuario by dataRepository.usuario.observeAsState()

    LaunchedEffect(miUsuario) {
        publicaciones = if (miUsuario?.type == TipoUsuarios.OFERTANTE) {
            dataRepository.obtenerPublicaciones(
                TipoPublicaciones.BUSQUEDA
            )
        } else {
            dataRepository.obtenerPublicaciones(
                TipoPublicaciones.ACTIVIDAD
            )
        }
    }

    Scaffold(bottomBar = { BottomNavigationBar(navController = navTo) }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(ColorDeFondo)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.align(Alignment.TopCenter)) {
                Logo()
                LazyColumn(modifier = Modifier.padding(4.dp)) {
                    items(publicaciones.size) { index ->
                        ClickableElevatedCardSample(
                            publicaciones[index]
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ClickableElevatedCardSample(miUsuarioAd: Publicaciones) {
    var expanded by remember { mutableStateOf(false) } // Controla si la tarjeta está expandida o no

    // Animación para cambiar el tamaño de la tarjeta de forma suave
    val cardSize by animateDpAsState(
        targetValue = if (expanded) 400.dp else 150.dp, // Tamaño expandido o contraído
        animationSpec = tween(durationMillis = 300), label = "" // Duración de la animación
    )

    ElevatedCard(
        onClick = { expanded = !expanded }, // Alterna entre expandido y contraído al hacer clic
        modifier = Modifier
            .size(width = 350.dp, height = cardSize) // Aplica el tamaño animado
            .padding(8.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Column {
                Text(
                    text = miUsuarioAd.title, modifier = Modifier.padding(8.dp)
                )

                // Muestra la descripción solo si la tarjeta está expandida
                if (expanded) {
                    Text(
                        text = miUsuarioAd.description,
                        modifier = Modifier.padding(16.dp, 16.dp, 8.dp, 4.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    // Formateo de la fecha
                    val formatoFecha = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
                    val fechaCompleta = formatoFecha.format(Date(miUsuarioAd.date))

                    Text(
                        text = fechaCompleta, modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Plazas: ${miUsuarioAd.plazas}", modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}