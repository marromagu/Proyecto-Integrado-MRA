package com.proyecto.proyectointegradomra.data.model


data class Publicaciones(
    var uid: String = "",
    var userId: String = "",
    var title: String = "",
    var description: String = "",
    val date: Long = System.currentTimeMillis(),
    var plazas: Int = 0
)