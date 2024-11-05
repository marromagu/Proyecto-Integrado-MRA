package com.proyecto.proyectointegradomra.data

data class Ofertante(
    val usuario: Usuario = Usuario(),
    val actividades: List<String> = emptyList()  // Lista de IDs de actividades publicadas
)