package com.proyecto.proyectointegradomra.ui.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.data.model.Publicaciones
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.common.BarraDeNavegacion
import com.proyecto.proyectointegradomra.ui.common.Logo
import com.proyecto.proyectointegradomra.data.model.TipoPublicaciones
import com.proyecto.proyectointegradomra.data.model.TipoUsuarios
import com.proyecto.proyectointegradomra.ui.common.CardClickable

@Composable
fun HomeView(dataRepository: DataRepository, navTo: NavHostController) {
    var publicaciones by remember { mutableStateOf<List<Publicaciones>>(emptyList()) }
    val miUsuario by dataRepository.obtenerUsuarioActual().observeAsState()

    LaunchedEffect(Unit) {
        dataRepository.cargarUsuario()

        miUsuario?.let { usuario ->
            publicaciones = if (usuario.type == TipoUsuarios.OFERTANTE) {
                dataRepository.obtenerPublicacionesPorTipoSinParticipar(
                    TipoPublicaciones.BUSQUEDA,
                    usuario.uid
                )
            } else {
                dataRepository.obtenerPublicacionesPorTipoSinParticipar(
                    TipoPublicaciones.ACTIVIDAD,
                    usuario.uid
                )
            }
        }
    }

    Scaffold(bottomBar = { BarraDeNavegacion(navController = navTo) }) { innerPadding ->
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
                        CardClickable(
                            publicaciones[index], "add", onItemClick = {
                                dataRepository.agregarParticipante(
                                    miUsuario?.uid!!,
                                    publicaciones[index].uid
                                )
                                navTo.navigate("HomeView")
                            }
                        ) {
                            navTo.navigate("UpdateAdView")
                        }
                    }
                }
            }
        }
    }
}