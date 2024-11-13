package com.proyecto.proyectointegradomra.data.model

data class Publicaciones(
    var uid: String = "",
    var userId: String = "",
    var title: String = "",
    var description: String = "",
    var date: Long = System.currentTimeMillis(),
    var plazas: Int = 0,
    var tipo: TipoPublicaciones = TipoPublicaciones.BUSQUEDA, // Tipo de publicación: "actividad" o "busqueda"
    var participantes: List<String> = emptyList() // Lista de IDs de los usuarios inscritos
)