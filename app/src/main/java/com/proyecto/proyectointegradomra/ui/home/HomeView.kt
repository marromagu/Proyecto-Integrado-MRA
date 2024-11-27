package com.proyecto.proyectointegradomra.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.data.model.Publicacion
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.common.*
import com.proyecto.proyectointegradomra.data.model.TipoPublicaciones
import com.proyecto.proyectointegradomra.data.model.TipoUsuarios

@Composable
fun HomeView(dataRepository: DataRepository, navTo: NavHostController) {
    // Variables de estado para gestionar publicaciones y el diálogo de alerta
    var publicaciones by remember { mutableStateOf<List<Publicacion>>(emptyList()) }
    val miUsuario by dataRepository.obtenerUsuarioActualAuth().observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    var nombrePublicacion by remember { mutableStateOf("") }
    var expandedCardIndex by remember { mutableStateOf<Int?>(null) }

    // Cargar datos iniciales al componer
    LaunchedEffect(Unit) {
        dataRepository.cargarUsuario() // Cargar información del usuario actual
        miUsuario?.let { usuario ->
            publicaciones = if (usuario.type == TipoUsuarios.OFERTANTE) {
                // Obtener publicaciones tipo "Búsqueda" si el usuario es ofertante
                dataRepository.obtenerPublicacionesPorTipoSinParticipar(
                    TipoPublicaciones.BUSQUEDA, usuario.uid
                )
            } else {
                // Obtener publicaciones tipo "Actividad" si el usuario es consumidor
                dataRepository.obtenerPublicacionesPorTipoSinParticipar(
                    TipoPublicaciones.ACTIVIDAD, usuario.uid
                )
            }
        }
    }

    // Estructura principal de la vista
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
                // Mostrar el logo en la parte superior
                Logo()

                // Lista de publicaciones
                LazyColumn(modifier = Modifier.padding(4.dp)) {
                    items(publicaciones.size) { index ->
                        // Tarjeta clicable para cada publicación
                        CardClickable(
                            miPublicacion = publicaciones[index],
                            action = "add",
                            onItemClick = {
                                // Verificar disponibilidad de plazas antes de agregar al participante
                                if (publicaciones[index].size > publicaciones[index].participantes.size) {
                                    dataRepository.agregarParticipante(miUsuario?.uid!!,
                                        publicaciones[index],
                                        onSuccess = {
                                            navTo.navigate("HomeView")
                                        },
                                        onError = {
                                            showDialog = true
                                        })
                                } else {
                                    showDialog = true
                                }
                            },
                            onItemNombre = {
                                dataRepository.obtenerUsuarioPorUid(publicaciones[index].ownerId) { nombre ->
                                    nombrePublicacion = nombre ?: "No disponible."
                                }
                            },
                            nombre = nombrePublicacion,
                            index = index,
                            isExpanded = expandedCardIndex == index,
                            onExpandChange = { newIndex ->
                                expandedCardIndex =
                                    if (newIndex == expandedCardIndex) null else newIndex
                            }
                        ) {}
                    }
                }

                // Diálogo de alerta para notificar errores o limitaciones
                DialogoAlerta(showAlert = showDialog,
                    alertMessage = "El número de plazas disponibles ya está completo.",
                    actionConfirmed = {
                        navTo.navigate("HomeView")
                        showDialog = false
                    },
                    onDismiss = { })
            }
        }
    }
}