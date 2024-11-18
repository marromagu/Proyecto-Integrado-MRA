package com.proyecto.proyectointegradomra.data.model

data class Publicacion(
    var uid: String = "",
    var ownerId: String = "",
    var title: String = "",
    var description: String = "",
    var date: Long = System.currentTimeMillis(),
    var size: Int = 0,
    var type: TipoPublicaciones = TipoPublicaciones.BUSQUEDA, // Tipo de publicación: "actividad" o "busqueda"
    var participantes: List<String> = emptyList() // Lista de IDs de los usuarios inscritos
)