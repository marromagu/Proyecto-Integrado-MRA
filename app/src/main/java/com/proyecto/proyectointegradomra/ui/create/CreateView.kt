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
    // Estado para almacenar las publicaciones del usuario
    var publicaciones by remember { mutableStateOf<List<Publicacion>>(emptyList()) }
    // Estado para obtener los datos del usuario actual
    val miUsuario by dataRepository.obtenerUsuarioActualAuth().observeAsState()

    // Cargar las publicaciones del usuario
    LaunchedEffect(Unit) {
        miUsuario?.uid?.let { userId ->
            // Obtiene las publicaciones asociadas al usuario actual desde el repositorio
            publicaciones = dataRepository.obtenerPublicacionesPorUsuario(userId)
        }
    }

    // Estado para manejar el estado del LazyColumn y la visibilidad del FAB
    val listState = rememberLazyListState()

    // Mostrar el FAB solo cuando el usuario esté en la parte superior de la lista
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
            // Contenedor principal de la vista
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CrearIMG()
                LazyColumn(
                    modifier = Modifier.padding(4.dp), state = listState
                ) {
                    // Itera a través de las publicaciones y muestra cada una
                    items(publicaciones.size) { index ->
                        // Card clickable para cada publicación
                        CardClickable(publicaciones[index], "update", onItemClick = {
                            // Serializa la publicación a JSON y navega a la vista de actualización
                            val publicacionJson = Uri.encode(Gson().toJson(publicaciones[index]))
                            navTo.navigate("UpdateAdView/$publicacionJson")
                        }, onItemClickDelete = {
                            // Elimina la publicación y recarga la vista
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
                visible = showFab, // Solo visible si la lista está en la parte superior
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                FloatingActionButton(
                    onClick = {
                        navTo.navigate("CreateAdView")
                    }, containerColor = Blanco, contentColor = ColorDeBotones
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar publicación")
                }
            }
        }
    }
}