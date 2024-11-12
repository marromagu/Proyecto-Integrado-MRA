package com.proyecto.proyectointegradomra.ui.home


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.data.model.Publicaciones
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.common.BottomNavigationBar
import com.proyecto.proyectointegradomra.ui.common.Logo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun HomeView(
    dataRepository: DataRepository,
    navTo: NavHostController
) {
    var publicaciones by remember { mutableStateOf<List<Publicaciones>>(emptyList()) }
    val miUsuario by dataRepository.usuario.observeAsState()

    LaunchedEffect(miUsuario) {
        miUsuario?.uid?.let { userId ->
            publicaciones = dataRepository.obtenerPublicacionesPorUsuario(userId)
        }
    }

    val listState = rememberLazyListState() // Estado de desplazamiento del LazyColumn
    val coroutineScope = rememberCoroutineScope()

    // Variable para controlar la visibilidad del FAB basada en el desplazamiento
    val showFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 // Mostrar cuando estamos en la parte superior
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
                LazyColumn(
                    modifier = Modifier.padding(4.dp),
                    state = listState
                ) {
                    items(publicaciones.size) { index ->
                        ClickableElevatedCardSample(
                            publicaciones[index]
                        )
                    }
                }
            }

            // FloatingActionButton con animación de aparición
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                visible = showFab,
//                enter = slideInVertically(initialOffsetY = { it }),
//                exit = slideOutVertically(targetOffsetY = { it })
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()

                ) {
                FloatingActionButton(
                    onClick = {
                        navTo.navigate("CreateAdView")
                    }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar publicación")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    ClickableElevatedCardSample(
        Publicaciones(
            "",
            "",
            "Paseo por la playa",
            "Sevilla es un municipio y una ciudad de España, capital de la provincia homónima y de Andalucía.\u200B Contao con 684 164 habitantes en 2023, \u200B por lo que es la ciudad más poblada de Andalucía, la cuarta de España según los datos oficiales del INE.\u200B El municipio tiene una superficie de 141,4",
            1731521700000,
            10
        )
    )
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
                    text = miUsuarioAd.title,
                    modifier = Modifier.padding(8.dp)
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
                        text = fechaCompleta,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Plazas: ${miUsuarioAd.plazas}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}