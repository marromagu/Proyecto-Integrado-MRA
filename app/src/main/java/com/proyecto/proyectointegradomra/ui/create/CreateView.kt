package com.proyecto.proyectointegradomra.ui.create

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.proyecto.proyectointegradomra.data.model.Publicacion
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.common.BarraDeNavegacion
import com.proyecto.proyectointegradomra.ui.common.CardClickable
import com.proyecto.proyectointegradomra.ui.common.CrearIMG
import com.proyecto.proyectointegradomra.ui.theme.Blanco
import com.proyecto.proyectointegradomra.ui.theme.ColorDeBotones

@Composable
fun CreateView(dataRepository: DataRepository, navTo: NavHostController) {
    var publicaciones by remember { mutableStateOf<List<Publicacion>>(emptyList()) }
    val miUsuario by dataRepository.obtenerUsuarioActual().observeAsState()

    LaunchedEffect(miUsuario) {
        miUsuario?.uid?.let { userId ->
            publicaciones = dataRepository.obtenerPublicacionesPorUsuario(userId)
        }
    }

    val listState = rememberLazyListState()

    val showFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    Scaffold(bottomBar = { BarraDeNavegacion(navController = navTo) }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(ColorDeFondo)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CrearIMG()
                LazyColumn(
                    modifier = Modifier.padding(4.dp), state = listState
                ) {
                    items(publicaciones.size) { index ->
                        CardClickable(publicaciones[index], "update", onItemClick = {
                            // Serializar el objeto Publicacion como JSON
                            val publicacionJson = Uri.encode(Gson().toJson(publicaciones[index]))
                            navTo.navigate("UpdateAdView/$publicacionJson")
                        }, onItemClickDelete = {
                            dataRepository.eliminarPublicacion(publicaciones[index].uid)
                            navTo.navigate("CreateView")
                        })
                    }
                }

            }

            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                visible = showFab,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
//              enter = fadeIn() + scaleIn(), exit = fadeOut() + scaleOut()

            ) {
                FloatingActionButton(
                    onClick = {
                        navTo.navigate("CreateAdView")
                    },
                    containerColor = Blanco,
                    contentColor = ColorDeBotones
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar publicación")
                }

            }
        }
    }
}